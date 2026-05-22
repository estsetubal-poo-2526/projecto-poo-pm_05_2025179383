package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.io.FileHandler;

public class StartScreen {

    private final Stage stage;
    private final GameSession session;

    private TextField player1NameField;
    private TextField player2NameField;
    private Label messageLabel;

    public StartScreen(Stage stage, GameSession session) {
        this.stage = stage;
        this.session = session;
    }

    public Scene createScene() {
        Label title = new Label("Jogo de Estratégia");

        player1NameField = new TextField();
        player1NameField.setPromptText("Nome do Jogador 1");
        player1NameField.setMaxWidth(250);

        player2NameField = new TextField();
        player2NameField.setPromptText("Nome do Jogador 2");
        player2NameField.setMaxWidth(250);

        Button newGameButton = new Button("Novo Jogo");
        Button loadGameButton = new Button("Carregar Jogo");
        Button exitButton = new Button("Sair");

        messageLabel = new Label();

        newGameButton.setOnAction(event -> startNewGame());
        loadGameButton.setOnAction(event -> loadSavedGame());
        exitButton.setOnAction(event -> stage.close());

        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(
                title,
                player1NameField,
                player2NameField,
                newGameButton,
                loadGameButton,
                exitButton,
                messageLabel
        );

        return new Scene(vBox, 600, 400);
    }

    private void startNewGame() {
        String player1Name = player1NameField.getText();
        String player2Name = player2NameField.getText();

        if (player1Name == null || player1Name.isBlank()) {
            messageLabel.setText("Erro: Introduz o nome do Jogador 1.");
            return;
        }

        if (player2Name == null || player2Name.isBlank()) {
            messageLabel.setText("Erro: Introduz o nome do Jogador 2.");
            return;
        }

        session.startNewGame(player1Name, player2Name);

        MainGameScreen mainGameScreen = new MainGameScreen(stage, session);
        stage.setScene(mainGameScreen.createScene());
    }

    private void loadSavedGame() {
        try {
            FileHandler.SaveData data = FileHandler.loadGame();

            session.loadGame(
                    data.getMap(),
                    data.getP1(),
                    data.getP2(),
                    data.getDay()
            );

            MainGameScreen mainGameScreen = new MainGameScreen(stage, session);
            stage.setScene(mainGameScreen.createScene());

        } catch (GameException e) {
            messageLabel.setText("Erro ao carregar jogo: " + e.getMessage());
        }
    }
}