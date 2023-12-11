package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer implements Collideable {
    private Circle energizer;
    private SmartSquare smartSquare;
    public Energizer(double centerX, double centerY, Pane gamePane) {
        this.energizer = new Circle(centerX, centerY, 8, Color.WHITE);
        gamePane.getChildren().add(this.energizer);
    }

    /**
     * Gets row.
     * @return
     */
    @Override
    public int getRow() {
        return (int) this.energizer.getCenterY() / Constants.SQUARE_WIDTH;
    }

    /**
     * Gets column.
     * @return
     */
    @Override
    public int getCol() {
        return (int) this.energizer.getCenterX() / Constants.SQUARE_WIDTH;
    }

    /**
     * Removes graphically and logically.
     * @param gamePane
     */
    @Override
    public void removeFromGame(Pane gamePane) {
        gamePane.getChildren().remove(this.energizer);
        if (this.smartSquare != null) {
            this.smartSquare.removeFromCollideables(this);
        }
    }

    /**
     * Adds 100 to score.
     * @param sidebar
     */
    @Override
    public void addToScore(Sidebar sidebar) {
        sidebar.addToScore(100);
    }

    /**
     * Handles collision by calling on helper method, and sets each ghost to frightened.
     * @param gamePane
     * @param sidebar
     * @param ghosts
     */
    @Override
    public void handleCollision(Pane gamePane, Sidebar sidebar, Ghost[] ghosts) {
        this.removeFromGame(gamePane);
        this.addToScore(sidebar);
        for (Ghost ghost : ghosts) {
            ghost.switchToFrightenedMode();
        }
    }

    public void setSmartSquare(SmartSquare smartSquare) {
        this.smartSquare = smartSquare;
    }

    /**
     * Cannot be returned to the pen.
     * @return
     */
    @Override
    public boolean canBeReturnedToPen() {
        return false;
    }

    /**
     * Used to check for game over, is an energizer so it returns true.
     * @return
     */
    @Override
    public boolean isDotOrEnergizer() {
        return true;
    }

}
