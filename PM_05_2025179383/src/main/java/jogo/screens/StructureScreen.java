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
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.models.Structures.Structures;

import java.util.Objects;

/**
 * Controller and view class responsible for displaying the inspection properties
 * of an existing building structure deployed on the map grid.
 * Presents level metrics, ownership definitions, and contextual status indicators.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class StructureScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    /**
     * Constructs a new StructureScreen inspection context window.
     *
     * @param popupStage  The auxiliary Stage popup window architecture container.
     * @param session     The current active {@link GameSession} engine instance tracking world states.
     * @param x           The horizontal index coordinate of the selected grid tile.
     * @param y           The vertical index coordinate of the selected grid tile.
     * @param onUpdateMap A callback task payload used to refresh the parent viewport grid layout upon closure.
     */
    public StructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    /**
     * Queries structural data from the grid, populates descriptive layout containers,
     * appends action handlers, and builds a comprehensive target inspection Scene.
     *
     * @return A compiled JavaFX Scene instance fully populated with contextual indicators.
     */
    public Scene createScene() {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("info-container");

        Structures structure = null;
        try {
            structure = SESSION.getMap().getStructure(X, Y);
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setPadding(new Insets(0, 40, 0, 10));

        try {
            String structureName = (structure != null) ? structure.getClass().getSimpleName().toLowerCase() : "default";
            Image buildingImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + structureName + ".png")));
            ImageView iv = new ImageView(buildingImg);
            iv.setFitWidth(280);
            iv.setFitHeight(280);
            iv.setPreserveRatio(true);

            VBox imgWrapper = new VBox(iv);
            imgWrapper.getStyleClass().add("building-preview");
            leftBox.getChildren().add(imgWrapper);
        } catch (Exception e) {
            Label placeholder = new Label("[Sem Imagem]");
            leftBox.getChildren().add(placeholder);
        }
        borderPane.setLeft(leftBox);

        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER_LEFT);
        centerBox.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(20);
        grid.getStyleClass().add("info-grid");
        grid.setAlignment(Pos.CENTER_LEFT);

        if (structure == null) {
            Label noStructureLabel = new Label("Não existe estrutura nesta posição.");
            noStructureLabel.getStyleClass().add("info-label-type");
            centerBox.getChildren().add(noStructureLabel);
        } else {
            addInfoRow(grid, 0, "/images/construct.png", "Estrutura:", structure.toString());
            addInfoRow(grid, 1, "/images/user_azul.png", "Dono:", structure.getOwner().getName());
            addInfoRow(grid, 2, "/images/icon_estreia.png", "Nível:", String.valueOf(structure.getLevel()));

            centerBox.getChildren().add(grid);

            HBox successBar = new HBox(12);
            successBar.setAlignment(Pos.CENTER_LEFT);
            successBar.getStyleClass().add("success-bar");

            try {
                ImageView checkIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon_check.png"))));
                checkIcon.setFitWidth(20);
                checkIcon.setFitHeight(20);
                successBar.getChildren().add(checkIcon);
            } catch (Exception ignore) {
            }

            Label successText = new Label("Esta posição contém uma estrutura.");
            successText.getStyleClass().add("success-text");
            successBar.getChildren().add(successText);

            centerBox.getChildren().add(successBar);
        }
        borderPane.setCenter(centerBox);

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("btn-back");
        try {
            ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon_voltar.png"))));
            backIcon.setFitWidth(16);
            backIcon.setFitHeight(16);
            backButton.setGraphic(backIcon);
            backButton.setGraphicTextGap(8);
        } catch (Exception ignore) {
        }

        backButton.setOnAction(event -> {
            MapCellScreen screen = new MapCellScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        Button closeButton = new Button("Fechar");
        closeButton.getStyleClass().add("btn-close");
        try {
            ImageView closeIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/x_branco.png"))));
            closeIcon.setFitWidth(14);
            closeIcon.setFitHeight(14);
            closeButton.setGraphic(closeIcon);
            closeButton.setGraphicTextGap(8);
        } catch (Exception ignore) {
        }

        closeButton.setOnAction(event -> POPUP_STAGE.close());

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));
        buttonsBox.getChildren().addAll(backButton, closeButton);
        borderPane.setBottom(buttonsBox);

        Scene scene = new Scene(borderPane, 1200, 800);
        loadStyle(scene);
        return scene;
    }

    /**
     * Intermediary matrix cell generator that appends graphical symbols,
     * static headers, and descriptive structural entity properties inside an inline format.
     */
    private void addInfoRow(GridPane grid, int row, String iconPath, String labelText, String valueText) {
        try {
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            iv.setFitWidth(28);
            iv.setFitHeight(28);
            iv.setPreserveRatio(true);
            grid.add(iv, 0, row);
        } catch (Exception ignore) {
        }

        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("info-label-type");
        grid.add(lbl, 1, row);

        Label val = new Label(valueText);
        val.getStyleClass().add("info-value-text");
        grid.add(val, 2, row);
    }

    /**
     * Resolves layout structure rules from local directory paths to apply external styling.
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