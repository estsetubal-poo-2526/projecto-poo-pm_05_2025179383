package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jogo.engine.GameSession;

/**
 * Controller and view class responsible for rendering the game over screen.
 * Displays the final match results, identifying the victorious player along with
 * their final score rewards, or declaring a draw scenario.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class EndScreen {

    private final GameSession SESSION;

    /**
     * Constructs a new EndScreen bound to the finalized session data.
     *
     * @param session The finished {@link GameSession} instance containing the final standings.
     */
    public EndScreen(GameSession session) {
        this.SESSION = session;
    }

    /**
     * Assembles the graphical layout nodes, binds victory icons, parses metrics,
     * and wraps the final summary presentation within a configured Scene.
     *
     * @return The fully built JavaFX Scene instance representing the scoreboard dashboard.
     */
    public Scene createScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("end-screen-root");

        try {
            ImageView trophyView = new ImageView(new Image(getClass().getResourceAsStream("/icons/throphy.png")));
            trophyView.setFitWidth(160);
            trophyView.setFitHeight(160);
            trophyView.setPreserveRatio(true);
            root.getChildren().add(trophyView);
        } catch (Exception e) {
            System.err.println("Ícone do troféu não encontrado.");
        }

        Label title = new Label("Vencedor");
        title.getStyleClass().add("end-title");
        root.getChildren().add(title);

        VBox winnerBox = new VBox();
        winnerBox.setAlignment(Pos.CENTER);
        winnerBox.getStyleClass().add("winner-box-container");

        Label winnerName = new Label(getWinnerName());
        winnerName.getStyleClass().add("winner-name-text");
        winnerBox.getChildren().add(winnerName);
        root.getChildren().add(winnerBox);

        HBox scoreBox = new HBox(12);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.getStyleClass().add("score-box-container");

        try {
            ImageView starIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/star.png")));
            starIcon.setFitWidth(26);
            starIcon.setFitHeight(26);
            starIcon.setPreserveRatio(true);
            scoreBox.getChildren().add(starIcon);
        } catch (Exception ignore) {}

        Label score = new Label("Pontuação: " + getWinnerScore());
        score.getStyleClass().add("score-text");
        scoreBox.getChildren().add(score);
        root.getChildren().add(scoreBox);

        Scene scene = new Scene(root, 1200, 800);
        loadStyle(scene);

        return scene;
    }

    /**
     * Resolves the identity of the winning player from the session data.
     *
     * @return The name string of the winner, or a local draw message if no player won.
     */
    public String getWinnerName() {
        if (SESSION.getWinner() == null) {
            return "Empate!";
        }

        return SESSION.getWinner().getName();
    }

    /**
     * Retrieves the high score value associated with the match outcome.
     *
     * @return The winner's total accumulated score points, or the baseline player's points in a draw.
     */
    public int getWinnerScore() {
        if (SESSION.getWinner() == null) {
            return SESSION.getPlayer1().getScore();
        }

        return SESSION.getWinner().getScore();
    }

    /**
     * Resolves and injects the project's centralized style sheet rules from disk
     * to shape the final results dashboard elements.
     *
     * @param scene The current active Scene whose styling rules will be modified.
     */
    private void loadStyle(Scene scene) {
        try {
            scene.getStylesheets().clear();
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");

            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar CSS na EndScreen: " + e.getMessage());
        }
    }
}