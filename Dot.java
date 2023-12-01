package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collideable {
    private Circle dot;
    private int row;
    private int column;

    private Pane pane;
    public Dot(double centerX, double centerY, Pane gamePane) {
        this.dot = new Circle(centerX, centerY, 3, Color.WHITE);
        this.pane = gamePane;
        this.pane.getChildren().add(this.dot);
    }
    @Override
    public int getRow() {
        return (int) this.dot.getCenterY() / Constants.SQUARE_WIDTH;
    }

    @Override
    public int getCol() {
        return (int) this.dot.getCenterX() / Constants.SQUARE_WIDTH;
    }
    @Override
    public void removeFromPane(Pane gamePane) {
        gamePane.getChildren().remove(dot);
    }
}
