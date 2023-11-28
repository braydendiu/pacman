package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer {
    private Circle energizer;
    public Energizer(double centerX, double centerY, Pane mapPane) {
        this.energizer = new Circle(centerX, centerY, 8, Color.WHITE);
        mapPane.getChildren().add(this.energizer);
    }
}
