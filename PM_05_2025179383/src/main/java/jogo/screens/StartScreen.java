package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.io.FileHandler;

public class StartScreen {

    private Stage stage;
    private GameSession session;

    public StartScreen(Stage stage, GameSession session) {
        this.stage = stage;
        this.session = session;
    }

    public Scene createScene() {
        Label title = new Label("Menu Principal");

        Button newGameButton = new Button("Novo Jogo");
        Button loadGameButton = new Button("Carregar Jogo");
        Button exitButton = new Button("Sair");
        Label errorLabel = new Label();

        newGameButton.setOnAction(event -> {
            SelectPlayersName loginScreen = new SelectPlayersName(stage, session);
            stage.setScene(loginScreen.createScene());
        });

        loadGameButton.setOnAction(event -> {
            try {
                FileHandler.SaveData data = FileHandler.loadGame();

                session.setMap(data.getMap());
                session.setPlayer1(data.getP1());
                session.setPlayer2(data.getP2());

                session.prepareLoadedGame();

                MainGameScreen mainGameScreen = new MainGameScreen(stage, session);
                stage.setScene(mainGameScreen.createScene());

            } catch (Exception e) {
                errorLabel.setText(e.getMessage());
            }
        });

        exitButton.setOnAction(event -> {
            stage.close();
        });

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                title,
                newGameButton,
                loadGameButton,
                exitButton,
                errorLabel

        );

        return new Scene(vbox, 600, 400);
    }
}