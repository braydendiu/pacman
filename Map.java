/*package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import cs15.fnl.pacmanSupport.CS15SupportMap;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Map {
    private SmartSquare [][] mapArray;
    private Pane mapPane;
    public Map(Pane mapPane) {
        CS15SupportMap.getSupportMap();
        this.mapArray = new SmartSquare[23][23];
        this.mapPane = mapPane;
        this.setUpMap();
    }
    private void setUpMap() {
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                switch (supportArray[row][col]) {
                    case FREE:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        break;
                    default:
                        break;
                }
            }
        }
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                switch (supportArray[row][col]) {
                    case WALL:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.mapPane, Color.BLUE);
                        break;
                    case DOT:
                        Dot dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.mapPane);
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.mapPane);
                        break;
                    case GHOST_START_LOCATION:
                        Ghost redGhost = new Ghost(col * Constants.SQUARE_WIDTH, (row - 2) * Constants.SQUARE_WIDTH, this.mapPane, Color.RED);
                        Ghost pinkGhost = new Ghost(col * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.PINK);
                        Ghost blueGhost = new Ghost((col + 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.CYAN);
                        Ghost orangeGhost = new Ghost((col - 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.ORANGE);
                        break;
                    case PACMAN_START_LOCATION:
                        Pacman pacman = new Pacman(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.mapPane);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    /**
     * Returns the mapPane.
     * @return
     */
    /*public Pane getMapPane() {
        return this.mapPane;
    }
} */
