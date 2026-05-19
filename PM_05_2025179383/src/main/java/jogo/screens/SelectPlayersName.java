package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

public class SelectPlayersName {

    private Stage stage;
    private GameSession session;

    public SelectPlayersName(Stage stage, GameSession session) {
        this.stage = stage;
        this.session = session;
    }

    public Scene createScene() {
        Label label1 = new Label("Insira o Nome dos Jogadores!");
        Label label2 = new Label();

        TextField player1Name = new TextField();
        TextField player2Name = new TextField();

        player1Name.setPromptText("Nome do Jogador 1");
        player2Name.setPromptText("Nome do Jogador 2");

        Button startGame = new Button("Iniciar Jogo");

        startGame.setOnAction(event -> {
            String player1 = player1Name.getText();
            String player2 = player2Name.getText();

            if (player1.isBlank() || player2.isBlank()) {
                label2.setText("Preencha o nome dos dois jogadores!");
                return;
            }

            session.startNewGame(player1, player2);

            MainGameScreen mainGameScreen = new MainGameScreen(stage, session);
            stage.setScene(mainGameScreen.createScene());
        });

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                label1,
                player1Name,
                player2Name,
                startGame,
                label2
        );

        return new Scene(vbox, 500, 500);
    }
}