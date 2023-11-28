package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot {
    private Circle dot;
    public Dot(double centerX, double centerY, Pane mapPane) {
        this.dot = new Circle(centerX, centerY, 3, Color.WHITE);
        mapPane.getChildren().add(this.dot);
    }
}
