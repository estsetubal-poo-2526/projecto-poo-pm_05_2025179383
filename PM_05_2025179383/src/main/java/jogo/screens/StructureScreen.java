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

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    public StructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    public Scene createScene() {
        Label title = new Label("Informação da posição " + X + ", " + Y);
        Label infoLabel = new Label();

        try {
            Structures structure = SESSION.getMap().getStructure(X, Y);

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
            ErrorPopUp.show(e.getMessage());
        }

        Button backButton = new Button("Voltar");
        Button closeButton = new Button("Fechar");

        backButton.setOnAction(event -> {
            MapCellScreen screen = new MapCellScreen(
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
                infoLabel,
                backButton,
                closeButton
        );

        return new Scene(vbox, 600, 400);
    }
}