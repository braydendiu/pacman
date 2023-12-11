package pacman;
import cs15.fnl.pacmanSupport.CS15SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
public class Game {
    private SmartSquare[][] mapArray;
    private Pane gamePane;
    private Timeline timeline;
    private Pacman pacman;
    private boolean gameStarted = false;
    private Sidebar sidebar;
    private BoardCoordinate pacmanLocation;
    private Ghost redGhost;
    private Ghost pinkGhost;
    private Ghost blueGhost;
    private Ghost orangeGhost;
    private Game game;
    private Queue<Ghost> ghostPenQueue;
    private int releaseCounter;
    private boolean restart;
    private boolean gameOver = false;
    private boolean runKeyPresses = true;
    public Game(Pane gamePane, Sidebar sidebar) {
        this.gamePane = gamePane;
        this.mapArray = new SmartSquare[23][23];
        this.setUpMap();
        this.sidebar = sidebar;
        this.game = game;
        this.releaseCounter = 0;
        this.ghostPenQueue = new LinkedList<>();
        this.addToPenQueue();
        this.restart = false;
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent event) -> this.handleKeyPress(event.getCode()));
        this.setUpTimeline();
    }

    /**
     * Sets up the map using the provided support map and switch cases within a
     * double for-loop.
     */
    private void setUpMap() {
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                if (Objects.requireNonNull(supportArray[row][col]) == CS15SquareType.FREE) {
                    this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                }
            }
        }
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                switch (supportArray[row][col]) {
                    case WALL:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLUE);
                        break;
                    case DOT:
                        Dot dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(dot);
                        dot.setSmartSquare(this.mapArray[row][col]);  // Set the associated SmartSquare
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(energizer);
                        energizer.setSmartSquare(this.mapArray[row][col]);  // Set the associated SmartSquare
                        break;
                    case GHOST_START_LOCATION:
                        this.redGhost = new Ghost(col * Constants.SQUARE_WIDTH, (row - 2) * Constants.SQUARE_WIDTH, this.gamePane, Ghost.GhostColor.RED.RED, this.mapArray, this.pacman, this.sidebar, this);
                        this.mapArray[row][col].addToCollidable(this.redGhost);
                        this.redGhost.moveGhost(this.pacmanLocation);

                        this.pinkGhost = new Ghost(col * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Ghost.GhostColor.PINK.PINK, this.mapArray, this.pacman, this.sidebar, this);
                        this.mapArray[row][col].addToCollidable(this.pinkGhost);
                        this.pinkGhost.moveGhost(this.pacmanLocation);

                        this.blueGhost = new Ghost((col + 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Ghost.GhostColor.CYAN.CYAN, this.mapArray, this.pacman, this.sidebar, this);
                        this.mapArray[row][col].addToCollidable(this.blueGhost);
                        this.blueGhost.moveGhost(this.pacmanLocation);

                        this.orangeGhost = new Ghost((col - 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Ghost.GhostColor.ORANGE.ORANGE, this.mapArray, this.pacman, this.sidebar, this);
                        this.mapArray[row][col].addToCollidable(this.orangeGhost);
                        this.orangeGhost.moveGhost(this.pacmanLocation);
                        break;
                    case PACMAN_START_LOCATION:
                        this.pacman = new Pacman(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane, this.mapArray);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Creates the timeline that always calls updateGame.
     */
    private void setUpTimeline() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(300), this::updateGame);
        this.timeline = new Timeline(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Moves the ghosts by giving them a target location, either Pacman or a corner
     * of the board.
     */
    private void updateGhostPositions() {
        this.redGhost.moveGhost(new BoardCoordinate(this.pacman.getPacmanRow(), this.pacman.getPacmanCol(), true));
        this.pinkGhost.moveGhost(new BoardCoordinate(this.pacman.getPacmanRow() + 1, this.pacman.getPacmanCol() - 3, true));
        this.blueGhost.moveGhost(new BoardCoordinate(this.pacman.getPacmanRow(), this.pacman.getPacmanCol() + 2, true));
        this.orangeGhost.moveGhost(new BoardCoordinate(this.pacman.getPacmanRow() - 4, this.pacman.getPacmanCol(), true));
    }

    /**
     * Updates the game state at each timeline event. Checks for collisions, releases
     * ghosts from the pen, and handles game-over or victory conditions.
     * @param event
     */
    private void updateGame(ActionEvent event) {
        if (this.gameStarted && !this.gameOver) {
            boolean ghostsInPen = this.areGhostsInPen();

            if (ghostsInPen) {
                this.releaseCounter++;
            }
            this.pacman.movePacman();
            this.checkCollisions(this.pacman, this.sidebar, this.gamePane);
            this.updateGhostPositions();
            this.checkCollisions(this.pacman, this.sidebar, this.gamePane);
            if (this.releaseCounter >= 20 && ghostsInPen) {
                this.releaseGhostFromPen();
                this.releaseCounter = 0;
            }
            if (this.sidebar.getLives() == 0) {
                this.gameOver();
            }
            if (this.checkForWinner()) {
                this.gameWon();
            }
        }
    }
    /**
     * Adds the ghosts to the ghostPenQueue for controlled release during the game.
     */
    public void addToPenQueue() {
        this.ghostPenQueue.add(this.pinkGhost);
        this.ghostPenQueue.add(this.blueGhost);
        this.ghostPenQueue.add(this.orangeGhost);
    }

    /**
     * Releases a ghost from the pen, resetting its position to the initial position.
     */
    private void releaseGhostFromPen() {
        if (!this.ghostPenQueue.isEmpty()) {
            Ghost releasedGhost = this.ghostPenQueue.poll();
            releasedGhost.resetToInitialPosition(8, 11);
        }
    }

    /**
     * Adds a ghost to the penQueue and resets its position to the specified pen location.
     * @param ghost
     * @param penRow
     * @param penColumn
     */
    private void returnGhostToPen(Ghost ghost, int penRow, int penColumn) {
        this.ghostPenQueue.add(ghost);
        ghost.resetToPenPosition(penRow, penColumn);
    }

    /**
     * Checks if the pen queue is empty so that the release counter will only increase
     * with ghosts in it.
     * @return
     */
    private boolean areGhostsInPen() {
        return !this.ghostPenQueue.isEmpty();
    }

    /**
     * Handles key presses so that the game can start upon key input.
     * @param code
     */
    public void handleKeyPress(KeyCode code) {
        if (!this.runKeyPresses) {
            return;
        }
        Direction newDirection = Direction.fromKeyCode(code);
        if (newDirection != null) {
            this.pacman.changeDirection(newDirection);
            if (!this.gameStarted) {
                this.timeline.play();
                this.gameStarted = true;
            }
        }
    }

    /**
     * Uses an ArrayList of collideables and checks if Pacman is on one of the squares
     * with a collideable. If it is, then the object's handleCollision method will be
     * called and it will be removed from the game graphically and logically.
     * @param pacman
     * @param sidebar
     * @param gamePane
     */
    private void checkCollisions(Pacman pacman, Sidebar sidebar, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();
        SmartSquare currentSquare = this.mapArray[pacmanRow][pacmanColumn];
        ArrayList<Collideable> collideables = new ArrayList<>(currentSquare.getCollideables());

        for (Collideable collideable : collideables) {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                collideable.handleCollision(gamePane, sidebar, new Ghost[]{this.redGhost, this.pinkGhost, this.blueGhost, this.orangeGhost});
                if (collideable.canBeReturnedToPen()) {
                    this.returnGhostToPen((Ghost) collideable, 10, 12);
                }
                currentSquare.removeFromCollideables(collideable);
            }
        }
    }

    /**
     * Called when Pacman collides with a ghost in chase or scatter. Game resets
     * to how it was in the beginning. Helper method for ghost class.
     */
    public void resetRound() {
        this.pacman.resetPacmanPosition();
        this.ghostPenQueue.clear();
        this.redGhost.resetGhostPosition();
        this.pinkGhost.resetGhostPosition();
        this.blueGhost.resetGhostPosition();
        this.orangeGhost.resetGhostPosition();

        this.ghostPenQueue.add(this.pinkGhost);
        this.ghostPenQueue.add(this.blueGhost);
        this.ghostPenQueue.add(this.orangeGhost);
        this.timeline.stop();
        this.gameStarted = false;
        this.releaseCounter = 0;
        this.gameOver = false;

        this.returnGhostToPen(this.pinkGhost, 10, 12);
        this.returnGhostToPen(this.blueGhost, 10, 12);
        this.returnGhostToPen(this.orangeGhost, 10, 12);
    }

    /**
     * Checks if the collideables ArrayList contains any more dots or energizers
     * to see if the game has been won.
     * @return
     */
    private boolean checkForWinner() {
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                SmartSquare currentSquare = this.mapArray[row][col];
                ArrayList<Collideable> collideables = new ArrayList<>(currentSquare.getCollideables());

                for (Collideable collideable : collideables) {
                    if (collideable.isDotOrEnergizer()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Stops the game and does not let the user play anymore if all lives
     * are lost.
     */
    public void gameOver() {
        this.timeline.stop();
        this.gameOver = true;
        this.pacman.resetPacmanPosition();

        this.redGhost.resetToInitialPosition(8, 11);
        this.pinkGhost.resetToInitialPosition(10, 12);
        this.blueGhost.resetToInitialPosition(10, 13);
        this.orangeGhost.resetToInitialPosition(10, 11);
        this.stopKeyPresses();
        Label label = new Label();
        label.toFront();
        label.setLayoutX(310);
        label.setLayoutY(430);
        label.setTextFill(Color.YELLOW);
        label.setText("Game Over!");
        this.gamePane.getChildren().add(label);
    }

    /**
     * If checkForWinner returns true, the game will stop and a label with
     * Winner! will appear.
     */
    private void gameWon() {
        this.timeline.stop();
        this.gameOver = true;
        this.pacman.resetPacmanPosition();

        this.redGhost.resetToInitialPosition(8, 11);
        this.pinkGhost.resetToInitialPosition(10, 11);
        this.blueGhost.resetToInitialPosition(10, 12);
        this.orangeGhost.resetToInitialPosition(10, 10);
        this.stopKeyPresses();

        Label label = new Label();
        label.toFront();
        label.setLayoutX(325);
        label.setLayoutY(430);
        label.setTextFill(Color.YELLOW);
        label.setText("Winner!");
        this.gamePane.getChildren().add(label);
    }

    /**
     * Stops key input from being recognized.
     */
    public void stopKeyPresses() {
        this.runKeyPresses = false;
    }
}