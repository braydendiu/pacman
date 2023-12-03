package pacman;

import javafx.scene.layout.Pane;

public interface Collideable {
    int getRow();
    int getCol();
    void removeFromPane(Pane gamePane);
    void addToScore(Sidebar sidebar);
}
