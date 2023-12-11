package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
public class Ghost implements Collideable {
    private Rectangle ghost;
    private int row;
    private int column;
    private SmartSquare[][] mapArray;
    private Direction[][] directionMap = new Direction[23][23];
    private Direction currentDirection;
    private GhostMode currentMode;
    private double scatterModeDuration;
    private double chaseModeDuration;
    private double modeSwitchCounter;
    private GhostColor color;
    private boolean frightened;
    private double frightenedCounter;
    private int previousRow;
    private int previousColumn;
    private Pacman pacman;
    private Sidebar sidebar;
    private static boolean updateScatterTargets = true;
    private boolean scatterTargetsUpdated;
    private boolean shouldUpdateScatterTargets;
    private BoardCoordinate scatterTarget;
    private double globalModeSwitchCounter = 0;
    private Game game;

    public Ghost(int x, int y, Pane mapPane, GhostColor color, SmartSquare[][] mapArray, Pacman pacman, Sidebar sidebar, Game game) {
        this.ghost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.ghost.setFill(this.getColor(color));
        mapPane.getChildren().add(this.ghost);
        this.ghost.toFront();
        this.mapArray = mapArray;
        this.row = (int) (y / Constants.SQUARE_WIDTH);
        this.column = (int) (x / Constants.SQUARE_WIDTH);
        this.currentDirection = Direction.LEFT;
        this.game = game;
        this.color = color;
        this.currentMode = GhostMode.CHASE;
        this.scatterModeDuration = 30;
        this.chaseModeDuration = 67;
        this.modeSwitchCounter = 0;
        this.frightened = false;
        this.frightenedCounter = 0;
        this.updateScatterTargets = false;
        this.scatterTargetsUpdated = false;
        this.shouldUpdateScatterTargets = false;
        this.pacman = pacman;
        this.sidebar = sidebar;
        this.updateScatterTargets();
    }

    /**
     * Enum for the different modes that the Ghost can switch to.
     */
    public enum GhostMode {
        CHASE, SCATTER, FRIGHTENED
    }

    /**
     * Enum for the four ghost colors.
     */
    public enum GhostColor {
        RED, PINK, CYAN, ORANGE
    }

    /**
     * Returns the color for each ghost.
     * @param ghostColor
     * @return
     */
    private javafx.scene.paint.Color getColor(GhostColor ghostColor) {
        switch (ghostColor) {
            case RED:
                return javafx.scene.paint.Color.RED;
            case PINK:
                return javafx.scene.paint.Color.PINK;
            case CYAN:
                return javafx.scene.paint.Color.CYAN;
            case ORANGE:
                return javafx.scene.paint.Color.ORANGE;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Performs a BFS seach to return the closest direction that the ghost
     * can turn based on its target. Does this by using a queue and taking in
     * valid neighbors, or squares, that are around it.
     * @param target
     * @return
     */
    private Direction BFS(BoardCoordinate target) {
        Queue<BoardCoordinate> queue = new LinkedList<>();
        boolean[][] visited = new boolean[23][23];
        Direction[][] directionMap = new Direction[23][23];
        BoardCoordinate closestSquare = new BoardCoordinate(this.getRow(), this.getCol(), false);
        Direction startDirection = this.currentDirection;
        for (Direction initialDirection : Direction.values()) {
            if (initialDirection != this.currentDirection.opposite()) {
                BoardCoordinate neighbor = this.getNextSquare(this.getRow(), this.getCol(), initialDirection);
                if (this.isValidNeighbor(neighbor, visited, this.mapArray)) {
                    queue.add(neighbor);
                    visited[neighbor.getRow()][neighbor.getColumn()] = true;
                    directionMap[neighbor.getRow()][neighbor.getColumn()] = initialDirection;
                }
            }
        }
        while (!queue.isEmpty()) {
            BoardCoordinate curr = queue.remove();
            double currentDistance = this.calculateDistance(curr, target);
            double closestDistance = this.calculateDistance(closestSquare, target);

            if (currentDistance < closestDistance) {
                closestSquare = curr;
            }

            for (Direction neighborDirection : Direction.values()) {
                BoardCoordinate neighbor = this.getNextSquare(curr.getRow(), curr.getColumn(), neighborDirection);
                if (this.isValidNeighbor(neighbor, visited, this.mapArray) && directionMap[neighbor.getRow()][neighbor.getColumn()] == null) {
                    queue.add(neighbor);
                    visited[neighbor.getRow()][neighbor.getColumn()] = true;
                    directionMap[neighbor.getRow()][neighbor.getColumn()] = directionMap[curr.getRow()][curr.getColumn()];
                }
            }
        }
        return directionMap[closestSquare.getRow()][closestSquare.getColumn()];
    }

    /**
     * Returns whether or not the squares around the ghost are valid moves based
     * on color checking and if its already been visited.
     * @param neighbor
     * @param visited
     * @param mapArray
     * @return
     */
    private boolean isValidNeighbor(BoardCoordinate neighbor, boolean[][] visited, SmartSquare[][] mapArray) {
        int row = neighbor.getRow();
        int col = neighbor.getColumn();
        int numRows = mapArray.length;
        int numCols = mapArray[0].length;
        col = (col + numCols) % numCols;
        return row >= 0 && row < numRows && mapArray[row][col].getColor().equals(Color.BLACK) && !visited[row][col];
    }

    /**
     * Finds the next BoardCoordinate when moving in a certain direction.
     * @param row
     * @param col
     * @param direction
     * @return
     */
    private BoardCoordinate getNextSquare(int row, int col, Direction direction) {
        int numRows = this.mapArray.length;
        int numCols = this.mapArray[0].length;
        int nextRow = (int) (row + direction.newY(0) / Constants.SQUARE_WIDTH + numRows) % numRows;
        int nextCol = (int) (col + direction.newX(0) / Constants.SQUARE_WIDTH + numCols) % numCols;
        return new BoardCoordinate(nextRow, nextCol, true);
    }

    /**
     * Calculates distance between two possible BoardCoordinates.
     * @param other
     * @param target
     * @return
     */
    private double calculateDistance(BoardCoordinate other, BoardCoordinate target) {
        if (other == null || target == null) {
            return Double.POSITIVE_INFINITY;
        }
        double deltaX = other.getColumn() - target.getColumn();
        double deltaY = other.getRow() - target.getRow();
        return Math.hypot(deltaX, deltaY);
    }

    /**
     * Moves the ghost by giving it a target to move towards.Uses switch cases to switch
     * the target of the ghost, and also adds and removes the ghost to each squares arraylist
     * as it moves.
     * @param pacmanLocation
     */
    public void moveGhost(BoardCoordinate pacmanLocation) {
        this.previousRow = this.row;
        this.previousColumn = this.column;
        this.updateMode();
        BoardCoordinate targetLocation;
        switch (this.currentMode) {
            case CHASE:
                targetLocation = pacmanLocation;
                break;
            case SCATTER:
                this.updateScatterTargets();
                targetLocation = this.calculateScatterTarget();
                break;
            case FRIGHTENED:
                targetLocation = this.getFrightenedTarget();
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (this.frightened) {
            this.frightenedCounter--;

            if (this.frightenedCounter <= 0) {
                this.setMode(GhostMode.CHASE);
            }
        }
        Direction nextDirection = this.BFS(targetLocation);
        if (nextDirection != null) {
            int nextRow = (int) (this.getRow() + nextDirection.newY(0) / Constants.SQUARE_WIDTH);
            int nextCol = (int) (this.getCol() + nextDirection.newX(0) / Constants.SQUARE_WIDTH);
            this.row = nextRow;
            this.column = nextCol;
            this.currentDirection = nextDirection;
            this.wrapAround();
            double newX = this.column * Constants.SQUARE_WIDTH;
            double newY = this.row * Constants.SQUARE_WIDTH;

            this.ghost.setX(newX);
            this.ghost.setY(newY);
            this.ghost.toFront();
            SmartSquare newSquare = this.mapArray[this.row][this.column];
            SmartSquare previousSquare = this.mapArray[this.previousRow][this.previousColumn];
            newSquare.addToCollidable(this);
            previousSquare.removeFromCollideables(this);
        }
    }

    /**
     * Based on what color the ghost is, the target will be a different
     * corner of the board for scatter mode.
     * @return
     */
    private BoardCoordinate calculateScatterTarget() {
        int numRows = this.mapArray.length;
        int numCols = this.mapArray[0].length;
        int targetRow, targetCol;

        if (this.color == GhostColor.RED) {
            targetRow = 0;
            targetCol = numCols - 1;
        } else if (this.color == GhostColor.PINK) {
            targetRow = 0;
            targetCol = 0;
        } else if (this.color == GhostColor.CYAN) {
            targetRow = numRows - 1;
            targetCol = 0;
        } else if (this.color == GhostColor.ORANGE) {
            targetRow = numRows - 1;
            targetCol = numCols - 1;
        } else {
            throw new IllegalArgumentException();
        }

        return new BoardCoordinate(targetRow, targetCol, true);
    }

    /**
     * Sets the target to the corner of the board by setting the target
     * equal to what is returned by calculateScatterTarget.
     */
    private void updateScatterTargets() {
        if (updateScatterTargets) {
            this.scatterTarget = this.calculateScatterTarget();
            updateScatterTargets = false;
        }
    }

    /**
     * Returns random BoardCoordinates for the ghost to move to in frightened mode.
     * @return
     */
    private BoardCoordinate getFrightenedTarget() {
        Random random = new Random();
        int randomRow = random.nextInt(this.mapArray.length);
        int randomCol = random.nextInt(this.mapArray[0].length);
        return new BoardCoordinate(randomRow, randomCol, false);
    }

    /**
     * Updates the ghost's mode based on the global mode switch counter and handles transitions between modes.
     */
    private void updateMode() {
        if (this.frightened) {
            this.frightenedCounter--;
            if (this.frightenedCounter <= 0) {
                this.setMode(GhostMode.CHASE);
                this.frightened = false;
                this.frightenedCounter = 0;
            }
        } else {
            this.globalModeSwitchCounter++;
            if (this.globalModeSwitchCounter >= this.chaseModeDuration) {
                this.currentMode = GhostMode.SCATTER;
                this.globalModeSwitchCounter = 0;
                this.frightened = false;
                this.ghost.setFill(this.getColor(this.color));
            } else if (this.globalModeSwitchCounter >= this.scatterModeDuration && this.currentMode == GhostMode.SCATTER) {
                this.currentMode = GhostMode.CHASE;
                this.globalModeSwitchCounter = 0;
                this.frightened = false;
                this.ghost.setFill(this.getColor(this.color));
            }
        }
    }

    /**
     * Sets the ghost's mode to the specified new mode.
     * @param newMode
     */
    public void setMode(GhostMode newMode) {
        this.currentMode = newMode;
        this.modeSwitchCounter = 0;
        if (newMode == GhostMode.FRIGHTENED) {
            this.frightened = true;
            this.frightenedCounter = 30;
            if (!this.ghost.getFill().equals(Color.SKYBLUE)) {
                this.ghost.setFill(Color.SKYBLUE);
            }
        } else {
            this.ghost.setFill(this.getColor(this.color));
        }
    }

    /**
     * Switches ghost to frightened mode for approximately 7 seconds.
     */
    public void switchToFrightenedMode() {
        if (!this.frightened) {
            this.frightened = true;
            this.frightenedCounter = 30;
            this.ghost.setFill(Color.SKYBLUE);
        }
    }

    /**
     * Allows the ghost to wrap through the tunnels.
     */
    private void wrapAround() {
        double newX = this.column * Constants.SQUARE_WIDTH;

        if (newX >= 690) {
            this.column = 0;
        } else if (newX < 0) {
            this.column = (int) (690 / Constants.SQUARE_WIDTH) - 1;
        }
    }

    /**
     * Resets the ghosts to their initial position based on their color.
     */
    public void resetGhostPosition() {
        switch (this.color) {
            case RED:
                this.resetToInitialPosition(8, 11);
                break;
            case PINK:
                this.resetToInitialPosition(10, 12);
                break;
            case CYAN:
                this.resetToInitialPosition(10, 13);
                break;
            case ORANGE:
                this.resetToInitialPosition(10, 11);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Resets the ghost's position to the specified initial position.
     * @param initialRow
     * @param initialColumn
     */
    public void resetToInitialPosition(int initialRow, int initialColumn) {
        this.row = initialRow;
        this.column = initialColumn;

        double newX = this.column * Constants.SQUARE_WIDTH;
        double newY = this.row * Constants.SQUARE_WIDTH;
        this.ghost.setX(newX);
        this.ghost.setY(newY);
    }

    /**
     * Resets the ghost's position to the original pen position.
     * @param penRow
     * @param penColumn
     */
    public void resetToPenPosition(int penRow, int penColumn) {
        this.row = penRow;
        this.column = penColumn;
        double newX = this.column * Constants.SQUARE_WIDTH;
        double newY = this.row * Constants.SQUARE_WIDTH;
        this.ghost.setX(newX);
        this.ghost.setY(newY);
    }

    /**
     * Resets round by calling on the method in Game class.
     */
    private void resetRound() {
        if (this.game != null) {
            this.game.resetRound();
            this.modeSwitchCounter = 0;
        }
    }

    /**
     * Returns the row.
     * @return
     */
    @Override
    public int getRow() {
        return this.row;
    }
    /**
     * Returns the column.
     * @return
     */
    @Override
    public int getCol() {
        return this.column;
    }

    /**
     * Removes ghost graphically.
     * @param gamePane
     */
    @Override
    public void removeFromGame(Pane gamePane) {
        gamePane.getChildren().remove(this.ghost);
    }

    /**
     * Adds 200 to the score.
     * @param sidebar
     */
    @Override
    public void addToScore(Sidebar sidebar) {
        sidebar.addToScore(200);
    }

    /**
     * If there is ghost collision, the lives will decrease and round will reset. If
     * it is in frightened, it will be returned to pen.
     * @param gamePane
     * @param sidebar
     * @param ghosts
     */
    @Override
    public void handleCollision(Pane gamePane, Sidebar sidebar, Ghost[] ghosts) {
        this.addToScore(sidebar);
        if ((this.currentMode == GhostMode.CHASE || this.currentMode == GhostMode.SCATTER) && !this.frightened) {
            sidebar.updateLives(sidebar.getLives() - 1);
            if (sidebar.getLives() > 0) {
                this.resetRound();
                this.modeSwitchCounter = 0;
            }
        } else if (this.currentMode == GhostMode.FRIGHTENED && this.canBeReturnedToPen()) {
            this.returnToPen();
        }
    }

    /**
     * Returns ghost to pen.
     */
    private void returnToPen() {
        if (this.currentMode == GhostMode.FRIGHTENED) {
            this.ghost.setFill(Color.SKYBLUE);
        } else {
            this.setMode(GhostMode.CHASE);
            this.resetToPenPosition(10, 12);
        }
    }

    /**
     * Only ghosts in frightened mode can be returned to the pen.
     * @return
     */
    @Override
    public boolean canBeReturnedToPen() {
        return this.frightened;
    }

    /**
     * Used to check for game won, ghost is not a dot or energizer.
     * @return
     */
    @Override
    public boolean isDotOrEnergizer() {
        return false;
    }
}