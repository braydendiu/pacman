package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SmartSquare {
    private Rectangle square;
    //private SmartSquare [][] mapArray;
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

    /* public void squareColor(Color color) {
        this.square.setFill(color);
        this.color = color;
    } */
    public void addToCollidable(Collideable collideable) {
        this.collideables.add(collideable);
        System.out.println("Adding to arraylist. Current size: " + this.collideables.size());
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

    /* public void checkPacmanCollisions(Pacman pacman, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        for (Collideable collideable : collideables) {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                // Handle collision logic here
                // You might want to remove collideable from the list and update the GUI
                collideables.remove(collideable);
                System.out.println("removing from arraylist");
                gamePane.getChildren().remove(collideable); // assuming getNode() returns the JavaFX Node
            }
        }
    } */
    /* public void checkPacmanCollisions(Pacman pacman, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        this.collideables.removeIf(collideable -> {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                //System.out.println("Pacman and collideable at (" + pacmanRow + ", " + pacmanColumn + ") collided.");
                //System.out.println("before size of array: " + collideables.size());
                System.out.println("Checking if gamePane contains collideable before removal: " + gamePane.getChildren().contains(collideable));
                gamePane.getChildren().remove(collideable);
                //System.out.println("after size of array: " + collideables.size());
                System.out.println("Checking if gamePane contains collideable after removal: " + gamePane.getChildren().contains(collideable));
                return true; // removes
            }
            return false; // keeps in list
        });
    } */
}
