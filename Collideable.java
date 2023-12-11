package pacman;

import javafx.scene.layout.Pane;

public interface Collideable {
    int getRow();
    int getCol();
    void removeFromGame(Pane gamePane);
    void addToScore(Sidebar sidebar);
    void handleCollision(Pane gamePane, Sidebar sidebar, Ghost[] ghosts);
    boolean canBeReturnedToPen();
    boolean isDotOrEnergizer();
}
