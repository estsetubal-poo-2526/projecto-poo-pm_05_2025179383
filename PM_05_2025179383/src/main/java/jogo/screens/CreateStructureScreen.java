package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameEngine;
import jogo.engine.GameSession;
import jogo.models.Structures.StructuresType;

public class CreateStructureScreen {

    private final Stage popupStage;
    private final GameSession session;
    private final int x;
    private final int y;
    private final Runnable onUpdateMap;

    public CreateStructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.popupStage = popupStage;
        this.session = session;
        this.x = x;
        this.y = y;
        this.onUpdateMap = onUpdateMap;
    }

    public Scene createScene() {
        Label title = new Label("Criar estrutura em " + x + ", " + y);

        Button forestButton = new Button("Floresta");
        Button mineButton = new Button("Mina");
        Button ranchButton = new Button("Rancho");
        Button cityButton = new Button("Cidade");
        Button backButton = new Button("Voltar");

        forestButton.setOnAction(event -> buildStructure(StructuresType.FOREST));
        mineButton.setOnAction(event -> buildStructure(StructuresType.MINE));
        ranchButton.setOnAction(event -> buildStructure(StructuresType.RANCH));
        cityButton.setOnAction(event -> buildStructure(StructuresType.CITY));

        backButton.setOnAction(event -> {
            MapCellScreen screen = new MapCellScreen(
                    popupStage,
                    session,
                    x,
                    y,
                    onUpdateMap
            );

            popupStage.setScene(screen.createScene());
        });

        HBox structuresBox = new HBox(15);
        structuresBox.setAlignment(Pos.CENTER);
        structuresBox.getChildren().addAll(
                forestButton,
                mineButton,
                ranchButton,
                cityButton
        );

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                title,
                structuresBox,
                backButton
        );

        return new Scene(vbox, 600, 400);
    }

    private void buildStructure(StructuresType type) {
        GameEngine.createStructure(
                session.getMap(),
                session.getActualPlayer(),
                type,
                x,
                y,
                session.getScoreModifier()
        );

        onUpdateMap.run();
        popupStage.close();
    }
}