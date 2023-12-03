package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {
    private Circle pacman;
    private Direction direction;
    private SmartSquare[][] mapArray;
    public Pacman(double centerX, double centerY, Pane gamePane, SmartSquare[][] mapArray) {
        this.pacman = new Circle(centerX, centerY, 15, Color.YELLOW);
        gamePane.getChildren().add(this.pacman);
        this.direction = Direction.UP;
        this.mapArray = mapArray;
    }
    public void movePacman() {
        double changeInX = this.direction.newX(0);
        double changeInY = this.direction.newY(0);
        double nextX = this.pacman.getCenterX() + changeInX;
        double nextY = this.pacman.getCenterY() + changeInY;

        //System.out.println("Current direction: " + this.direction);
        //System.out.println("Next X: " + nextX / 30 + ", Next Y: " + nextY / 30);
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

    /* public void setDirection(Direction direction) {
        this.direction = direction;
    } */
    public void changeDirection(Direction newDirection) {
        double changeInX = newDirection.newX(0);
        double changeInY = newDirection.newY(0);
        double nextX = this.pacman.getCenterX() + changeInX;
        double nextY = this.pacman.getCenterY() + changeInY;

        if (this.checkValidity(nextY, nextX)) {
            this.direction = newDirection;
        }
    }
    public boolean checkValidity(double nextY, double nextX) {
        int nextRow = (int) (nextY / Constants.SQUARE_WIDTH);
        int nextCol = (int) (nextX / Constants.SQUARE_WIDTH);

        if (nextCol < 0) {
            nextCol = this.mapArray[0].length - 1;
        } else if (nextCol >= this.mapArray[0].length) {
            nextCol = 0;
        }
        return this.mapArray[nextRow][nextCol].getColor().equals(Color.BLACK);
    }
    public int getPacmanRow() {
        return (int) this.pacman.getCenterY() / Constants.SQUARE_WIDTH;
    }
    public int getPacmanCol() {
        return (int) this.pacman.getCenterX() / Constants.SQUARE_WIDTH;
    }
}
