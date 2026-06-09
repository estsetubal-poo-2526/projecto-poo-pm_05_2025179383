package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameEngine;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.models.Structures.*;

import java.util.Objects;

/**
 * Controller and view class responsible for rendering structural construction blueprint specifications.
 * Delegates data mapping to the domain factory layer, decoupling user interface components
 * from baseline simulation rules.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class StructuresBuildingInfoScreen {

    private final GameSession SESSION;
    private final Stage STAGE;
    private final Runnable ON_UPDATE_MAP;

    /**
     * Constructs an operational blueprint information context.
     *
     * @param session     The current active {@link GameSession} model state tracker.
     * @param stage       The auxiliary Stage popup window container architecture.
     * @param onUpdateMap A callback task used to refresh the active world grid upon deployment.
     */
    public StructuresBuildingInfoScreen(GameSession session, Stage stage, Runnable onUpdateMap) {
        this.SESSION = session;
        this.STAGE = stage;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    /**
     * Compiles sub-panels layout layers, binds building parameters dynamically,
     * and maps boundaries inside a dedicated container Scene.
     *
     * @param type The targeted structural {@link StructuresType} to evaluate.
     * @param x    The target horizontal index location coordinate on the map.
     * @param y    The target vertical index location coordinate on the map.
     * @return A fully populated JavaFX Scene instance ready for viewport presentation.
     */
    public Scene createScreen(StructuresType type, int x, int y) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("building-info-container");

        borderPane.setLeft(left(type));
        borderPane.setCenter(center(type));
        borderPane.setBottom(bottom(type, x, y));

        Scene scene = new Scene(borderPane, 1200, 800);
        loadStyle(scene);
        return scene;
    }

    /**
     * Assembles the left side panel containing a stylized preview box of the chosen structure asset.
     */
    private VBox left(StructuresType type) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 40, 0, 10));

        try {
            String structureName = type.name().toLowerCase();
            Image buildingImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + structureName + ".png")));
            ImageView iv = new ImageView(buildingImg);
            iv.setFitWidth(320);
            iv.setFitHeight(320);
            iv.setPreserveRatio(true);

            VBox imgWrapper = new VBox(iv);
            imgWrapper.getStyleClass().add("building-preview");
            vBox.getChildren().add(imgWrapper);
        } catch (Exception e) {
            Label label = new Label("[Imagem da Estrutura]");
            vBox.getChildren().add(label);
        }

        return vBox;
    }

    /**
     * Compiles the central details specifications panel using tabular matrix rows fed by the factory.
     */
    public VBox center(StructuresType type) {
        Label title = new Label("Detalhes da Estrutura");
        title.getStyleClass().add("building-info-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(30, 0, 30, 0));

        // Consome os dados traduzidos e formatados diretamente da Factory do domínio
        addGridRow(grid, 0, "/icons/table-saw.png", "Estrutura:", CreateStructure.getStructureName(type), "value-old");

        String expenseText = CreateStructure.getMaterialType(type) + " " + CreateStructure.getMaterialCost(type);
        addGridRow(grid, 1, "/icons/dollar.png", "Manutenção:", expenseText, "value-cost");

        String productionText = CreateStructure.getProductionInfo(type);
        addGridRow(grid, 2, "/icons/cube.png", "Produção Diária:", productionText, "value-new");

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER_LEFT);
        centerBox.setPadding(new Insets(20));
        centerBox.getChildren().addAll(title, grid);

        return centerBox;
    }

    /**
     * Helper grid manipulation utility that injects unified graphic cells safely into layout structures.
     */
    private void addGridRow(GridPane grid, int row, String iconPath, String labelText, String valueText, String valueStyleClass) {
        try {
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            iv.setFitWidth(24);
            iv.setFitHeight(24);
            iv.setPreserveRatio(true);
            grid.add(iv, 0, row);
        } catch (Exception ignore) {
        }

        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("attr-label");
        grid.add(lbl, 1, row);

        Label val = new Label(valueText);
        val.getStyleClass().add(valueStyleClass);
        grid.add(val, 2, row);
    }

    /**
     * Generates lower action configurations including return controls and construction operational triggers.
     */
    public VBox bottom(StructuresType type, int x, int y) {
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        Button constructButton = new Button("Construir");
        constructButton.getStyleClass().add("btn-construct");
        try {
            ImageView hammerIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/tool.png"))));
            hammerIcon.setFitWidth(18);
            hammerIcon.setFitHeight(18);
            constructButton.setGraphic(hammerIcon);
            constructButton.setGraphicTextGap(8);
        } catch (Exception ignore) {
        }

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("btn-back");
        try {
            ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/back.png"))));
            backIcon.setFitWidth(16);
            backIcon.setFitHeight(16);
            backButton.setGraphic(backIcon);
            backButton.setGraphicTextGap(8);
        } catch (Exception ignore) {
            // Suppress navigation icon faults
        }

        backButton.setOnAction(event -> {
            CreateStructureScreen createStructureScreen = new CreateStructureScreen(
                    STAGE, SESSION, x, y, ON_UPDATE_MAP
            );
            STAGE.setScene(createStructureScreen.createScene());
        });

        constructButton.setOnAction(event -> buildStructure(type, x, y));

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(constructButton, backButton);

        vBox.getChildren().add(buttonsBox);
        return vBox;
    }

    /**
     * Contacts core processing engines to claim map coordinate properties,
     * validating tile states before confirming execution.
     */
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

    /**
     * Re-initializes stylesheet allocations from root disk trees to re-inject uniform element styling.
     */
    private void loadStyle(Scene scene) {
        try {
            scene.getStylesheets().clear();
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");

            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            }
        } catch (Exception ignore) {

        }
    }
}