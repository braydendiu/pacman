package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SmartSquare {
    private Rectangle square;
    private SmartSquare [][] mapArray;

    private Color color;
    public SmartSquare(int row, int col, Pane mapPane, Color color) {
        this.square = new Rectangle(col, row, 30, 30);
        this.square.setFill(color);
        mapPane.getChildren().add(this.square);
        this.mapArray = new SmartSquare[23][23];
    }
    public void squareColor(Color color) {
        this.square.setFill(color);
        this.color = color;
    }

}
