package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

public class MapCellScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    public MapCellScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    public Scene createScene() {
        Label title = new Label("Ações na posição " + X + ", " + Y);

        Button constructStructure = new Button("Construir Estrutura");
        Button upgradeStructure = new Button("Melhorar Estrutura");
        Button getInfoStructure = new Button("Obter Informações da Estrutura");
        Button closeButton = new Button("Fechar");

        constructStructure.setOnAction(event -> {
            CreateStructureScreen screen = new CreateStructureScreen(
                    POPUP_STAGE,
                    SESSION,
                    X,
                    Y,
                    ON_UPDATE_MAP
            );

            POPUP_STAGE.setScene(screen.createScene());
        });

        upgradeStructure.setOnAction(event -> {
            UpgradeStructureScreen upgradeStructureScreen = new UpgradeStructureScreen(POPUP_STAGE, SESSION);
            POPUP_STAGE.setScene(upgradeStructureScreen.createScene(X, Y));
        });

        getInfoStructure.setOnAction(event -> {
            StructureScreen screen = new StructureScreen(
                    POPUP_STAGE,
                    SESSION,
                    X,
                    Y,
                    ON_UPDATE_MAP
            );

            POPUP_STAGE.setScene(screen.createScene());
        });

        closeButton.setOnAction(event -> {
            POPUP_STAGE.close();
        });

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                title,
                constructStructure,
                upgradeStructure,
                getInfoStructure,
                closeButton
        );

        return new Scene(vbox, 600, 400);
    }
}