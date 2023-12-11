package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {
    private Circle pacman;
    private Direction direction;
    private SmartSquare[][] mapArray;
    private int row;
    private int column;
    private int lives;
    public Pacman(double centerX, double centerY, Pane gamePane, SmartSquare[][] mapArray) {
        this.pacman = new Circle(345, 525, 15, Color.YELLOW);
        gamePane.getChildren().add(this.pacman);
        this.direction = Direction.UP;
        this.mapArray = mapArray;
        this.lives = 3;
    }

    /**
     * Moves Pacman by calculating the new x and y coordinates, also wraps Pacman.
     */
    public void movePacman() {
        double changeInX = this.direction.newX(0);
        double changeInY = this.direction.newY(0);
        double nextX = this.pacman.getCenterX() + changeInX;
        double nextY = this.pacman.getCenterY() + changeInY;
        if (this.checkValidity(nextY, nextX)) {
            this.pacman.setCenterX(nextX);
            this.pacman.setCenterY(nextY);
            this.pacman.toFront();

            if (nextX >= 690) {
                this.pacman.setCenterX(15);
            } else if (nextX < 0) {
                this.pacman.setCenterX(675);
            }
        }
    }

    /**
     * Changes direction of the Pacmans movement if it is a valid turn.
     * @param newDirection
     */
    public void changeDirection(Direction newDirection) {
        double changeInX = newDirection.newX(0);
        double changeInY = newDirection.newY(0);
        double nextX = this.pacman.getCenterX() + changeInX;
        double nextY = this.pacman.getCenterY() + changeInY;

        if (this.checkValidity(nextY, nextX)) {
            this.direction = newDirection;
        }
    }

    /**
     * Checks if moves are valid by color checking squares around it.
     * @param nextY
     * @param nextX
     * @return
     */
    private boolean checkValidity(double nextY, double nextX) {
        int nextRow = (int) (nextY / Constants.SQUARE_WIDTH);
        int nextCol = (int) (nextX / Constants.SQUARE_WIDTH);

        if (nextCol < 0) {
            nextCol = this.mapArray[0].length - 1;
        } else if (nextCol >= this.mapArray[0].length) {
            nextCol = 0;
        }
        return this.mapArray[nextRow][nextCol].getColor().equals(Color.BLACK);
    }

    /**
     * Resets Pacman position when game ends.
     */
    public void resetPacmanPosition() {
        this.row = 17;
        this.column = 11;

        double newX = this.column * Constants.SQUARE_WIDTH + 15;
        double newY = this.row * Constants.SQUARE_WIDTH + 15;
        this.pacman.setCenterX(newX);
        this.pacman.setCenterY(newY);
    }

    /**
     * Returns row.
     * @return
     */
    public int getPacmanRow() {
        return (int) this.pacman.getCenterY() / Constants.SQUARE_WIDTH;
    }

    /**
     * Returns column.
     * @return
     */
    public int getPacmanCol() {
        return (int) this.pacman.getCenterX() / Constants.SQUARE_WIDTH;
    }

    /**
     * Gets Pacmans location, used for BFS.
     * @return
     */
    public BoardCoordinate getLocation() {
        return new BoardCoordinate(this.getPacmanRow(), this.getPacmanCol(), true);
    }
}
