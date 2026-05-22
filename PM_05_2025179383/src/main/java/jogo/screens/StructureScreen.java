package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.models.Structures.Structures;

public class StructureScreen {

    private final Stage popupStage;
    private final GameSession session;
    private final int x;
    private final int y;
    private final Runnable onUpdateMap;

    public StructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.popupStage = popupStage;
        this.session = session;
        this.x = x;
        this.y = y;
        this.onUpdateMap = onUpdateMap;
    }

    public Scene createScene() {
        Label title = new Label("Informação da posição " + x + ", " + y);
        Label infoLabel = new Label();

        try {
            Structures structure = session.getMap().getStructure(x, y);

            if (structure == null) {
                infoLabel.setText("Não existe estrutura nesta posição.");
            } else {
                infoLabel.setText(
                        "Estrutura: " + structure +
                                "\nDono: " + structure.getOwner().getName() +
                                "\nNível: " + structure.getLevel()
                );
            }

        } catch (GameException e) {
            infoLabel.setText("Erro: " + e.getMessage());
        }

        Button backButton = new Button("Voltar");
        Button closeButton = new Button("Fechar");

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

        closeButton.setOnAction(event -> {
            popupStage.close();
        });

        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(
                title,
                infoLabel,
                backButton,
                closeButton
        );

        return new Scene(vbox, 600, 400);
    }
}