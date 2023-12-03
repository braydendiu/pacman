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

    public Sidebar() {
        this.setPrefSize(690, 30);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: blue;");
        this.setSpacing(150);
        this.quitButtonBox = new HBox();
        this.quitButtonBox.setAlignment(Pos.BOTTOM_LEFT);
        this.labelsBox = new HBox();
        this.labelsBox.setAlignment(Pos.BOTTOM_RIGHT);
        this.getChildren().addAll(this.quitButtonBox, this.labelsBox);
        this.setUpQuitButton();
        this.setUpLabels();
    }

    private void setUpQuitButton() {
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));
        this.quitButtonBox.getChildren().add(quitButton);
    }

    private void setUpLabels() {
        Label scoreLabel = new Label("Score: " + this.score);
        Label livesLabel = new Label("Lives: 3");
        scoreLabel.setStyle("-fx-text-fill: yellow;");
        livesLabel.setStyle("-fx-text-fill: yellow;");
        HBox.setMargin(scoreLabel, new Insets(0, 150, 0, 0));
        this.labelsBox.getChildren().addAll(scoreLabel, livesLabel);
    }
    public void addToScore(int points) {
        this.score += points;
        this.updateScoreLabel();
    }
    private void updateScoreLabel() {
        Label scoreLabel = (Label) this.labelsBox.getChildren().get(0);
        scoreLabel.setText("Score: " + this.score);
    }
}