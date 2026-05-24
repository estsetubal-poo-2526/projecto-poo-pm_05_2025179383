package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameEngine;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.models.Structures.*;

public class UpgradeStructureScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private Label messageLabel;

    public UpgradeStructureScreen(Stage stage, GameSession session){
        this.SESSION = session;
        this.STAGE = stage;
    }

    public Scene createScene(int x, int y) {
        BorderPane borderPane = new BorderPane();

        messageLabel = new Label();

        try {
            borderPane.setCenter(center(x, y));
            borderPane.setBottom(bottom(x, y));
            borderPane.setLeft(left());
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
            Button goBack = new Button("Voltar");

            goBack.setOnAction(event -> {
                goBackToMainGameScreen();
            });

            return new Scene(new BorderPane(goBack),300,300);
        }
        return new Scene(borderPane, 600, 400);
    }

    private VBox left() {
        Label label = new Label("Imagem");

        VBox vBox = new VBox();
        vBox.getChildren().add(label);

        return vBox;
    }

    private VBox center(int x, int y) throws GameException {

        Label title = new Label("Melhorar Estrutura");

        Label actualLevel = new Label("Nivel atual: " + getLevelStructure(x, y));
        Label futureLevel = new Label("Proximo Nivel: " + (getLevelStructure(x, y) + 1));

        Label actualExpense = new Label("Despesa atual: " + getActualExpense(x, y));
        Label futureExpense = new Label("Despesa Após Melhoria: " + getFutureExpense(x, y));

        Label actualProduction = new Label("Produção Atual: " + getActualProduction(x, y));
        Label futureProduction = new Label("Producao Após Melhoria: " + getFutureProduction(x, y));

        Label costToUpgrade = new Label("Custo de Melhoria: " + getCostToUpgrade(x,y));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(title,
                actualLevel,
                futureLevel,
                actualExpense,
                actualProduction,
                futureExpense,
                futureProduction,
                costToUpgrade);

        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    private VBox bottom(int x, int y) {
        Button upgrade = new Button("Melhorar");
        Button goBack = new Button("Voltar");

        upgrade.setOnAction(event -> {
            upgradeStructure(x, y);
        });

        goBack.setOnAction(event  -> {
            goBackToMainGameScreen();
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(upgrade, goBack, messageLabel);

        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }

    private void upgradeStructure(int x, int y) {
        try {
            GameEngine.upgradeStructure(
                    SESSION.getMap(),
                    SESSION.getActualPlayer(),
                    x,
                    y,
                    SESSION.getScoreModifier()
            );

            messageLabel.setText("Estrutura melhorada com sucesso!");

            STAGE.setScene(createScene(x, y));

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }

    private void goBackToMainGameScreen() {
        MainGameScreen mainGameScreen = new MainGameScreen(STAGE, SESSION);
        STAGE.setScene(mainGameScreen.createScene());
    }

    private Structures getStructureMap(int x, int y) throws GameException {
        Structures structure = SESSION.getMap().getStructure(x, y);

        if (structure == null) {
            throw new GameException("Não existe nenhuma estrutura nesta posição.");
        }

        return structure;
    }

    public int getLevelStructure(int x, int y) throws GameException {
        return getStructureMap(x,y).getLevel();
    }

    public String getActualExpense(int x, int y) throws GameException {
        return getStructureMap(x,y).getExpense() + " " + getStructureMap(x,y).getCostType();
    }

    public String getFutureExpense(int x, int y) throws GameException {
        return getStructureMap(x,y).getFutureExpense() + " " + getStructureMap(x,y).getCostType();
    }

    public String getActualProduction(int x, int y) throws GameException {
        return getStructureMap(x,y).getProfit() + " " + getStructureMap(x,y).getProduction();
    }

    public String getFutureProduction(int x, int y) throws GameException {
        return getStructureMap(x,y).getFutureProfit() + " " + getStructureMap(x,y).getProduction();
    }

    public String getCostToUpgrade(int x, int y) throws GameException {
        return getStructureMap(x,y).getUpgradeCost() + " " + getStructureMap(x,y).getUpgradeMaterial();
    }

}
