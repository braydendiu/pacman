package pacman;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Sidebar extends HBox {
    private HBox quitButtonBox;
    private HBox labelsBox;
    private int score;
    private int lives;
    private Label livesLabel;
    private Game game;
    private Label gameOverLabel;

    public Sidebar(Game game) {
        this.setPrefSize(690, 30);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: blue;");
        this.setSpacing(150);
        this.lives = 3;
        this.quitButtonBox = new HBox();
        this.quitButtonBox.setAlignment(Pos.BOTTOM_LEFT);
        this.labelsBox = new HBox();
        this.labelsBox.setAlignment(Pos.BOTTOM_RIGHT);
        this.getChildren().addAll(this.quitButtonBox, this.labelsBox);
        this.setUpQuitButton();
        this.setUpLabels();
        this.game = game;
    }

    /**
     * Sets up Quit button.
     */
    private void setUpQuitButton() {
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));
        quitButton.setFocusTraversable(false);
        this.quitButtonBox.getChildren().add(quitButton);
    }

    /**
     *
     */
    private void setUpLabels() {
        Label scoreLabel = new Label("Score: " + this.score);
        this.livesLabel = new Label("Lives: " + this.lives);
        scoreLabel.setStyle("-fx-text-fill: yellow;");
        this.livesLabel.setStyle("-fx-text-fill: yellow;");
        HBox.setMargin(scoreLabel, new Insets(0, 150, 0, 0));
        this.labelsBox.getChildren().addAll(scoreLabel, this.livesLabel);
    }

    /**
     * Adds points to the score total.
     * @param points
     */
    public void addToScore(int points) {
        this.score += points;
        this.updateScoreLabel();
    }

    /**
     * updates the label for the score.
     */
    private void updateScoreLabel() {
        Label scoreLabel = (Label) this.labelsBox.getChildren().get(0);
        scoreLabel.setText("Score: " + this.score);
    }

    /**
     * Updates lives.
     * @param lives
     */
    public void updateLives(int lives) {
        this.lives = lives;
        this.updateLivesLabel();
    }

    /**
     * Returns lives.
     * @return
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Updates the lives label.
     */
    private void updateLivesLabel() {
        this.livesLabel.setText("Lives: " + this.lives);
    }
}