package pacman;

import cs15.fnl.pacmanSupport.CS15SquareType;
import cs15.fnl.pacmanSupport.CS15SupportMap;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
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

    public Game(Pane gamePane) {
        this.gamePane = gamePane;
        //Map map = new Map(gamePane);
        CS15SupportMap.getSupportMap();
        this.mapArray = new SmartSquare[23][23];
        //this.pacman = new Pacman(15, 15, this.gamePane, this.mapArray);
        //this.mapPane = mapPane;
        //Game game = new Game(gamePane);
        this.setUpMap();
        gamePane.setFocusTraversable(true);
        gamePane.setOnKeyPressed((event) -> this.handleKeyPress(event.getCode()));
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
                        /*this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        Collideable dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(dot);
                        break;*/
                        Collideable dot = new Dot(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(dot);
                        System.out.println("Dot added to SmartSquare at (" + row + ", " + col + ")");
                        break;
                    case ENERGIZER:
                        //this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        Collideable energizer = new Energizer(col * Constants.SQUARE_WIDTH + 15, row * Constants.SQUARE_WIDTH + 15, this.gamePane);
                        this.mapArray[row][col].addToCollidable(energizer);
                        break;
                    case GHOST_START_LOCATION:
                        //this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        Ghost redGhost = new Ghost(col * Constants.SQUARE_WIDTH, (row - 2) * Constants.SQUARE_WIDTH, this.gamePane, Color.RED);
                        this.mapArray[row][col].addToCollidable(redGhost);

                        //this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        Ghost pinkGhost = new Ghost(col * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Color.PINK);
                        this.mapArray[row][col].addToCollidable(pinkGhost);

                        //this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
                        Ghost blueGhost = new Ghost((col + 1) * Constants.SQUARE_WIDTH, row * Constants.SQUARE_WIDTH, this.gamePane, Color.CYAN);
                        this.mapArray[row][col].addToCollidable(blueGhost);

                        //this.mapArray[row][col] = new SmartSquare(row * Constants.SQUARE_WIDTH, col * Constants.SQUARE_WIDTH, this.gamePane, Color.BLACK);
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
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), this::update);
        this.timeline = new Timeline(keyFrame);
        this.timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.play();
    }

    private void update(ActionEvent event) {
        if (this.gameStarted) {
            this.pacman.movePacman();
            this.checkCollisions(pacman, gamePane);
        }
    }


    public void handleKeyPress(KeyCode code) {
        Direction newDirection = Direction.fromKeyCode(code);
        if (newDirection != null) {
            this.pacman.setDirection(newDirection);
            if (!this.gameStarted) {
                this.timeline.play();
                this.gameStarted = true;
            }
        }
    }
    /* private void checkCollisions(Pacman pacman, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        SmartSquare currentSquare = this.mapArray[pacmanRow][pacmanColumn];
        ArrayList<Collideable> collideables = currentSquare.getCollideables();

        for (int i = 0; i < collideables.size(); i++) {
            Collideable collideable = collideables.get(i);
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                //System.out.println("Pacman and collideable at (" + pacmanRow + ", " + pacmanColumn + ") collided.");
                System.out.println("gamePane contains collideable before removal: " + gamePane.getChildren().contains(collideable));
                gamePane.getChildren().remove(collideable);
                System.out.println("gamePane contains collideable after removal: " + gamePane.getChildren().contains(collideable));

                i--;
            }
        }
    } */
    /* WORKS? private void checkCollisions(Pacman pacman, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        SmartSquare currentSquare = this.mapArray[pacmanRow][pacmanColumn];
        ArrayList<Collideable> collideables = currentSquare.getCollideables();

        // goes through collideables and check for collisions
        collideables.removeIf(collideable -> {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                //System.out.println("Pacman and collideable at (" + pacmanRow + ", " + pacmanColumn + ") collided.");
                System.out.println("Checking if gamePane contains collideable before removal: " + gamePane.getChildren().contains(collideable));
                gamePane.getChildren().remove(collideable);
                System.out.println("Checking if gamePane contains collideable after removal: " + gamePane.getChildren().contains(collideable));
                return true; // removes
            }

            return false; // keeps in list
        });
    } */
    private void checkCollisions(Pacman pacman, Pane gamePane) {
        int pacmanRow = pacman.getPacmanRow();
        int pacmanColumn = pacman.getPacmanCol();

        SmartSquare currentSquare = this.mapArray[pacmanRow][pacmanColumn];
        ArrayList<Collideable> collideables = currentSquare.getCollideables();
        ArrayList<Collideable> toRemove = new ArrayList<>();

        for (Collideable collideable : collideables) {
            int collideableRow = collideable.getRow();
            int collideableColumn = collideable.getCol();

            if (pacmanRow == collideableRow && pacmanColumn == collideableColumn) {
                System.out.println("gamePane contains collideable before removal: " + this.gamePane.getChildren().contains(collideable));
                toRemove.add(collideable);
                this.gamePane.getChildren().removeAll(toRemove);
                //this.gamePane.getChildren().remove(collideable);
                System.out.println("gamePane contains collideable after removal: " + this.gamePane.getChildren().contains(collideable));
            }
        }
//        collideables.removeAll(toRemove);
        collideables.clear();
//        gamePane.getChildren().removeAll(toRemove);
    }
}
