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

    public void addToCollidable(Collideable collideable) {
        this.collideables.add(collideable);
    }

    /* public void removeFromCollidable(Collideable collideable) {
        this.collideables.remove(collideable);
    } */
    /* public void addToArrayList(Collideable collideable) {
        this.addToCollidable(collideable);
    } */
    public Color getColor() {
        return color;
    }

}
