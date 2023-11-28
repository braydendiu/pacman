package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import cs15.fnl.pacmanSupport.CS15SupportMap;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Map {
    private SmartSquare [][] mapArray;
    //private CS15SquareType [][] supportArray;
    private Pane mapPane;
    public Map(Pane mapPane) {
        CS15SupportMap.getSupportMap();
        this.mapArray = new SmartSquare[23][23];
        //this.supportArray = new CS15SquareType[23][23];
        this.mapPane = mapPane;
        this.setUpMap();
        //CS15SquareType [][] = new
    }
    private void setUpMap() {
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                switch (supportArray[row][col]) {
                    case WALL:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.mapPane, Color.BLUE);
//                        this.mapArray[row][col].squareColor(Color.BLUE);
                        break;
                    case FREE:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.mapPane, Color.BLACK);
//                        this.mapArray[row][col].squareColor(Color.BLACK);
                        break;
                    case DOT:
                        Dot dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.mapPane);
                        break;
                    case ENERGIZER:
                        Energizer energizer = new Energizer(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.mapPane);
                        break;
                    case GHOST_START_LOCATION:
                        Ghost redGhost = new Ghost(col * Constants.SQUARE_WIDTH, (row - 2) * Constants.SQUARE_WIDTH, this.mapPane, Color.RED);
//                        System.out.println("Creating ghosts at: (" + col + ", " + row + ")");
                        Ghost pinkGhost = new Ghost(col * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.PINK);
//                        System.out.println("Creating ghosts at: (" + col + ", " + row + ")");
                        Ghost blueGhost = new Ghost((col -2) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.CYAN);
//                        System.out.println("Creating ghosts at: (" + col + ", " + row + ")");
                        Ghost orangeGhost = new Ghost((col - 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.mapPane, Color.ORANGE);
//                        System.out.println("Creating ghosts at: (" + col + ", " + row + ")");
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
    public Pane getMapPane() {
        return this.mapPane;
    }
}
