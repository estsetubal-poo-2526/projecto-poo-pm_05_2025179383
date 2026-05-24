package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jogo.engine.GameSession;

public class EndScreen {

    private final GameSession SESSION;

    public EndScreen(GameSession session) {
        this.SESSION = session;
    }

    public Scene createScene(){

        Label title = new Label("Vencedor");
        Label winnerName = new Label(getWinnerName());
        Label score = new Label("Pontuação: "  + getWinnerScore());

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(title,winnerName,score);

        return new Scene(vBox,600,600);

    }

    public String getWinnerName() {
        if (SESSION.getWinner() == null) {
            return "Empate!";
        }

        return SESSION.getWinner().getName();
    }

    public int getWinnerScore() {
        if (SESSION.getWinner() == null) {
            return SESSION.getPlayer1().getScore();
        }

        return SESSION.getWinner().getScore();
    }


}
