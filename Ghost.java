package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ghost {
    private Rectangle ghost;
    //private Rectangle blueGhost;
    //private Rectangle purpleGhost;
    //private Rectangle orangeGhost;
    public Ghost (int x, int y, Pane mapPane, Color color) {
        this.ghost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.ghost.setFill(color);
        mapPane.getChildren().add(this.ghost);
        this.ghost.toFront();
        //this.blueGhost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        //this.purpleGhost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        //this.orangeGhost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
    }
}
