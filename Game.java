package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import cs15.fnl.pacmanSupport.CS15SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Objects;

public class Game {
    private SmartSquare[][] mapArray;
    private Pane gamePane;
    private Timeline timeline;
    private Pacman pacman;
    private boolean gameStarted = false;
    private Sidebar sidebar;

    public Game(Pane gamePane, Sidebar sidebar) {
        this.gamePane = gamePane;
        CS15SupportMap.getSupportMap();
        this.mapArray = new SmartSquare[23][23];
        this.setUpMap();
        this.sidebar = sidebar;
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent event) -> this.handleKeyPress(event.getCode()));
        this.setUpTimeline();
    }

    private void setUpMap() {
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                if (Objects.requireNonNull(supportArray[row][col]) == CS15SquareType.FREE) {
                    this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                }
            }
        }
        for (int row = 0; row < 23; row++) {
            for (int col = 0; col < 23; col++) {
                cs15.fnl.pacmanSupport.CS15SquareType[][] supportArray = cs15.fnl.pacmanSupport.CS15SupportMap.getSupportMap();
                switch (supportArray[row][col]) {
                    case WALL:
                        this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLUE);
                        break;
                    case DOT:
                        Collideable dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(dot);
                        break;
                    case ENERGIZER:
                        Collideable energizer = new Energizer(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(energizer);
                        break;
                    case GHOST_START_LOCATION:
                        Ghost redGhost = new Ghost(col * Constants.SQUARE_WIDTH, (row - 2) * Constants.SQUARE_WIDTH, this.gamePane, Color.RED);
                        this.mapArray[row][col].addToCollidable(redGhost);

                        Ghost pinkGhost = new Ghost(col * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Color.PINK);
                        this.mapArray[row][col].addToCollidable(pinkGhost);

                        Ghost blueGhost = new Ghost((col + 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Color.CYAN);
                        this.mapArray[row][col].addToCollidable(blueGhost);

                        Ghost orangeGhost = new Ghost((col - 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Color.ORANGE);
                        this.mapArray[row][col].addToCollidable(orangeGhost);
                        break;
                    case PACMAN_START_LOCATION:
                        this.pacman = new Pacman(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane, this.mapArray);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setUpTimeline() {
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), this::update);
        this.timeline = new Timeline(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void update(ActionEvent event) {
        if (this.gameStarted) {
            this.pacman.movePacman();
            this.checkCollisions(this.pacman, this.sidebar, this.gamePane);
        }
    }

    public void handleKeyPress(KeyCode code) {
        System.out.println("calling keypress");

        Direction newDirection = Direction.fromKeyCode(code);
        if (newDirection != null) {
            System.out.println("Changing direction to: " + newDirection);
            this.pacman.changeDirection(newDirection);
            if (!this.gameStarted) {
                this.timeline.play();
                this.gameStarted = true;
            }
        }
        //System.out.println("calling keypress");
    }
    private void checkCollisions(Pacman pacman, Sidebar sidebar, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        SmartSquare currentSquare = this.mapArray[pacmanRow][pacmanColumn];
        ArrayList<Collideable> collideables = currentSquare.getCollideables();

        for (Collideable collideable : collideables) {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                collideable.removeFromPane(gamePane);
                collideable.addToScore(sidebar);
                collideables.remove(collideable);  // Remove from the current square's collideables
                break;
            }
        }
    }
}
