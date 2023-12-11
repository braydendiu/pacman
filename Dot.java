package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collideable {
    private Circle dot;
    private SmartSquare smartSquare;
    private Pane pane;
    public Dot(double centerX, double centerY, Pane gamePane) {
        this.dot = new Circle(centerX, centerY, 3, Color.WHITE);
        this.pane = gamePane;
        this.pane.getChildren().add(this.dot);
    }

    /**
     * Gets row.
     * @return
     */
    @Override
    public int getRow() {
        return (int) this.dot.getCenterY() / Constants.SQUARE_WIDTH;
    }

    /**
     * Gets column.
     * @return
     */

    @Override
    public int getCol() {
        return (int) this.dot.getCenterX() / Constants.SQUARE_WIDTH;
    }

    /**
     * Removes dot graphically and logically.
     * @param gamePane
     */
    @Override
    public void removeFromGame(Pane gamePane) {
        gamePane.getChildren().remove(this.dot);
        if (this.smartSquare != null) {
            this.smartSquare.removeFromCollideables(this);
        }
    }

    /**
     * Adds 10 to the score.
     * @param sidebar
     */
    @Override
    public void addToScore(Sidebar sidebar) {
        sidebar.addToScore(10);
    }

    /**
     * Handles collision logic by calling on helper methods.
     * @param gamePane
     * @param sidebar
     * @param ghosts
     */
    @Override
    public void handleCollision(Pane gamePane, Sidebar sidebar, Ghost[] ghosts) {
        this.removeFromGame(gamePane);
        this.addToScore(sidebar);
    }
    public void setSmartSquare(SmartSquare smartSquare) {
        this.smartSquare = smartSquare;
    }

    /**
     * Cannot be returned to pen.
     * @return
     */
    @Override
    public boolean canBeReturnedToPen() {
        return false;
    }

    /**
     * Used to check for game won, this is a dot or energizer.
     * @return
     */
    @Override
    public boolean isDotOrEnergizer() {
        return true;
    }
}
