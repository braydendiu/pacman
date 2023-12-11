package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SmartSquare {
    private Rectangle square;
    private ArrayList<Collideable> collideables;

    private Color color;

    public SmartSquare(int row, int col, Pane gamePane, Color color) {
        this.square = new Rectangle(col, row, 30, 30);
        this.square.setFill(color);
        gamePane.getChildren().add(this.square);
        //this.mapArray = new SmartSquare[23][23];
        this.collideables = new ArrayList<>();
        this.color = color;
    }
    public ArrayList<Collideable> getCollideables() {
        return this.collideables;
    }

    /**
     * Adds collideable objects to the ArrayList.
     * @param collideable
     */
    public void addToCollidable(Collideable collideable) {
        this.collideables.add(collideable);
    }

    /**
     * Removes collideables from ArrayList upon collision.
     * @param collideable
     */
    public void removeFromCollideables(Collideable collideable) {
        this.collideables.remove(collideable);
    }

    /**
     * Returns the color.
     * @return
     */
    public Color getColor() {
        return color;
    }

}
