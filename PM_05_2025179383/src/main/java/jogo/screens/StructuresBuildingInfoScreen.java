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
import jogo.models.ResourceType;
import jogo.models.Structures.*;

public class StructuresBuildingInfoScreen {

    private final GameSession SESSION;
    private final Stage STAGE;
    private final Runnable ON_UPDATE_MAP;

    public StructuresBuildingInfoScreen(GameSession session, Stage stage, Runnable onUpdateMap) {
        this.SESSION = session;
        this.STAGE = stage;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    public Scene createScreen(StructuresType type, int x, int y) {

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(new Label("Imagem"));
        borderPane.setCenter(center(type));
        borderPane.setBottom(bottom(type, x, y));

        return new Scene(borderPane, 600, 400);
    }

    public VBox bottom(StructuresType type, int x, int y) {
        Button backButton = new Button("Voltar");
        Button constructButton = new Button("Construir");

        backButton.setOnAction(event -> {
            CreateStructureScreen createStructureScreen = new CreateStructureScreen(
                    STAGE,
                    SESSION,
                    x,
                    y,
                    ON_UPDATE_MAP
            );

            STAGE.setScene(createStructureScreen.createScene());
        });

        constructButton.setOnAction(event -> buildStructure(type, x, y));

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(constructButton, backButton);

        return vBox;
    }

    public VBox center(StructuresType type) {

        Label name = new Label("Nome: " + getStructureName(type));

        Label structureCost = new Label(
                "Custo de Manutenção: " +
                        getExpenseStructure(type) + " " +
                        getCostTypeStructure(type)
        );

        Label production = new Label(
                "Produção Diária: " +
                        getProfitStructure(type) + " " +
                        getProductionStructure(type)
        );

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(name, structureCost, production);

        return vBox;
    }

    private String getStructureName(StructuresType type) {
        switch (type) {
            case FOREST:
                return "Floresta";
            case MINE:
                return "Mina";
            case CITY:
                return "Cidade";
            case RANCH:
                return "Rancho";
            default:
                return "Desconhecida";
        }
    }

    private int getExpenseStructure(StructuresType type) {
        switch (type) {
            case FOREST:
                return Forest.getEXPENSE();

            case MINE:
                return Mine.getEXPENSE();

            case CITY:
                return City.getEXPENSE();

            case RANCH:
                return Ranch.getEXPENSE();

            default:
                return 0;
        }
    }

    private String getCostTypeStructure(StructuresType type) {
        switch (type) {
            case FOREST:
                return String.valueOf(Forest.getCOST_TYPE());

            case MINE:
                return String.valueOf(Mine.getCOST_TYPE());

            case CITY:
                return String.valueOf(City.getCOST_TYPE());

            case RANCH:
                return String.valueOf(Ranch.getCOST_TYPE());

            default:
                return "NONE";
        }
    }

    private String getProductionStructure(StructuresType type) {
        switch (type) {
            case FOREST:
                return String.valueOf(Forest.getPRODUCTION_TYPE());

            case MINE:
                return String.valueOf(Mine.getPRODUCTION_TYPE());

            case CITY:
                return String.valueOf(ResourceType.NONE);

            case RANCH:
                return String.valueOf(Ranch.getPRODUCTION_TYPE());

            default:
                return "NONE";
        }
    }

    private int getProfitStructure(StructuresType type) {
        switch (type) {
            case FOREST:
                return Forest.getPROFIT();

            case MINE:
                return Mine.getPROFIT();

            case CITY:
                return City.getPROFIT();

            case RANCH:
                return Ranch.getPROFIT();

            default:
                return 0;
        }
    }

    private void buildStructure(StructuresType type, int x, int y) {
        try {
            GameEngine.createStructure(
                    SESSION.getMap(),
                    SESSION.getActualPlayer(),
                    type,
                    x,
                    y,
                    SESSION.getScoreModifier()
            );

            ON_UPDATE_MAP.run();
            STAGE.close();

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }
}