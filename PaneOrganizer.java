package pacman;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PaneOrganizer {
    private BorderPane root;
    //private Pane gamePane;
    public PaneOrganizer() {
        this.root = new BorderPane();
        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);
//        Map map = new Map(gamePane);
         new Game(gamePane);
    }
    /**
     * Returns root.
     * @return
     */
    public BorderPane getRoot() {
        return this.root;
    }
}
