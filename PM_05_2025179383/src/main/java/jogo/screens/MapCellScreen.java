package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

public class MapCellScreen {

    private final Stage popupStage;
    private final GameSession session;
    private final int x;
    private final int y;
    private final Runnable onUpdateMap;

    public MapCellScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.popupStage = popupStage;
        this.session = session;
        this.x = x;
        this.y = y;
        this.onUpdateMap = onUpdateMap;
    }

    public Scene createScene() {
        Label title = new Label("Ações na posição " + x + ", " + y);

        Button constructStructure = new Button("Construir Estrutura");
        Button getInfoStructure = new Button("Obter Informações da Estrutura");
        Button closeButton = new Button("Fechar");

        constructStructure.setOnAction(event -> {
            CreateStructureScreen screen = new CreateStructureScreen(
                    popupStage,
                    session,
                    x,
                    y,
                    onUpdateMap
            );

            popupStage.setScene(screen.createScene());
        });

        getInfoStructure.setOnAction(event -> {
            StructureScreen screen = new StructureScreen(
                    popupStage,
                    session,
                    x,
                    y,
                    onUpdateMap
            );

            popupStage.setScene(screen.createScene());
        });

        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                title,
                constructStructure,
                getInfoStructure,
                closeButton
        );

        return new Scene(vbox, 600, 400);
    }
}