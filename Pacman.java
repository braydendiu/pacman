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
        this.direction = Direction.RIGHT;
        this.mapArray = mapArray;
    }
    public void movePacman() {
        double changeInX = this.direction.newX(0);
        double changeInY = this.direction.newY(0);
        double nextX = this.pacman.getCenterX() + changeInX;
        double nextY = this.pacman.getCenterY() + changeInY;
        //this.pacman.setCenterY(nextY);
        //this.pacman.setCenterX(nextX);

        if (this.checkValidity(direction) == true) {
            this.pacman.setCenterX(nextX);
            this.pacman.setCenterY(nextY);
            this.pacman.toFront();
            if (nextX >= 690) {
                this.pacman.setCenterX(15);
            }
            else if (nextX < 0) {
                this.pacman.setCenterX(675);
            } else {
                this.pacman.setCenterX(nextX);
            }
        }
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    public boolean checkValidity(Direction direction) {
        double nextX = direction.newX(this.pacman.getCenterX());
        double nextY = direction.newY(this.pacman.getCenterY());
        int nextRow = (int) (nextY / Constants.SQUARE_WIDTH);
        int nextCol = (int) (nextX / Constants.SQUARE_WIDTH);
        //int nextRow = (int) (nextY / Constants.SQUARE_WIDTH);
        //int nextCol = (int) (nextX / Constants.SQUARE_WIDTH);
        //System.out.println("nextRow: " + nextRow + ", nextCol: " + nextCol);

        if (nextCol < 0) {
            nextCol = this.mapArray[0].length - 1;
            //System.out.println("position: row=" + this.pacman.getCenterX() / 30 + ", col=" + this.pacman.getCenterY() / 30);
        } else if (nextCol >= this.mapArray[0].length) {
            nextCol = 0;
            //System.out.println("position: row=" + this.pacman.getCenterX() / 30 + ", col=" + this.pacman.getCenterY() / 30);
        }

        return this.mapArray[nextRow][nextCol].getColor().equals(Color.BLACK);
    }
    public int getPacmanRow() {
        return (int) this.pacman.getCenterX() / Constants.SQUARE_WIDTH;
    }
    public int getPacmanCol() {
        return (int) this.pacman.getCenterY() / Constants.SQUARE_WIDTH;
    }

}
