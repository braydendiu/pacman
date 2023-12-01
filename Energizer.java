package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer implements Collideable {
    private Circle energizer;
    private int row;
    private int column;
    public Energizer(double centerX, double centerY, Pane gamePane) {
        this.energizer = new Circle(centerX, centerY, 8, Color.WHITE);
        gamePane.getChildren().add(this.energizer);
    }
    @Override
    public int getRow() {
        return (int) this.energizer.getCenterY() / Constants.SQUARE_WIDTH;
    }

    @Override
    public int getCol() {
        return (int) this.energizer.getCenterX() / Constants.SQUARE_WIDTH;
    }
    @Override
    public void removeFromPane(Pane gamePane) {
        gamePane.getChildren().remove(energizer);
    }
}
