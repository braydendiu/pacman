package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {
    private Circle pacman;
    public Pacman(double centerX, double centerY, Pane mapPane) {
        this.pacman = new Circle(centerX, centerY, 15, Color.YELLOW);
        mapPane.getChildren().add(this.pacman);
    }
}
