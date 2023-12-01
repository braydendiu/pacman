package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ghost implements Collideable {
    private Rectangle ghost;
    private int row;
    private int column;
    public Ghost (int x, int y, Pane mapPane, Color color) {
        this.ghost = new Rectangle(x, y, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.ghost.setFill(color);
        mapPane.getChildren().add(this.ghost);
        this.ghost.toFront();
    }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return column;
    }
    @Override
    public void removeFromPane(Pane gamePane) {
        gamePane.getChildren().remove(ghost);
    }
}
