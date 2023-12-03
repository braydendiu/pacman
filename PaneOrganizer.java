package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root;
    private Sidebar sidebar;

    public PaneOrganizer() {
        this.root = new BorderPane();
        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);

        this.sidebar = new Sidebar();
        this.root.setBottom(this.sidebar);

        new Game(gamePane, this.sidebar);
    }

    public BorderPane getRoot() {
        return this.root;
    }
}