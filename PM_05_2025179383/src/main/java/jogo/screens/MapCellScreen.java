package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

import java.util.Objects;

/**
 * Controller and view class that handles the contextual action menu for a selected map tile.
 * Serves as an operational router allowing players to construct, upgrade, or inspect
 * structures at a specific grid coordinate.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class MapCellScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    /**
     * Constructs a new MapCellScreen context bound to a target grid tile location.
     *
     * @param popupStage  The auxiliary Stage container displaying this contextual menu.
     * @param session     The current active {@link GameSession} tracking the world engine state.
     * @param x           The horizontal position index of the targeted cell.
     * @param y           The vertical position index of the targeted cell.
     * @param onUpdateMap A callback task used to refresh the main grid viewport upon completion of cell actions.
     */
    public MapCellScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    /**
     * Factory helper method that builds a structured JavaFX Button formatted as a list action row.
     * Employs internal layout wrapping to embed structural graphics, title labels, and navigation arrows.
     *
     * @param text       The main description label to display inside the action row.
     * @param iconPath   The package resource path pointing toward the required graphic icon.
     * @param isCloseBtn A boolean flag dictating if this row represents a safe window termination choice.
     * @return A fully stylized and composite JavaFX Button cell row.
     */
    private Button createMenuButton(String text, String iconPath, boolean isCloseBtn) {
        Button btn = new Button();
        btn.getStyleClass().add("cell-action-btn");

        BorderPane conteudoInner = new BorderPane();
        conteudoInner.setMouseTransparent(true);

        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            iv.setPreserveRatio(true);
            BorderPane.setAlignment(iv, Pos.CENTER);
            BorderPane.setMargin(iv, new Insets(0, 15, 0, 0));
            conteudoInner.setLeft(iv);
        } catch (Exception e) {
            System.out.println("Ícone em falta: " + iconPath);
        }

        Label labelTexto = new Label(text);
        if (isCloseBtn) {
            labelTexto.getStyleClass().add("cell-close-text");
        } else {
            labelTexto.getStyleClass().add("cell-action-text");
        }
        BorderPane.setAlignment(labelTexto, Pos.CENTER_LEFT);
        conteudoInner.setCenter(labelTexto);

        if (!isCloseBtn) {
            Label labelSeta = new Label(">");
            labelSeta.getStyleClass().add("cell-action-text");
            labelSeta.setStyle("-fx-text-fill: #9c9284;");
            BorderPane.setAlignment(labelSeta, Pos.CENTER_RIGHT);
            conteudoInner.setRight(labelSeta);
        }

        btn.setGraphic(conteudoInner);
        return btn;
    }

    /**
     * Assembles the structural container layout tree, initializes contextual routing buttons,
     * appends event triggers, and loads external stylesheets.
     *
     * @return A compiled and fully localized JavaFX Scene instance representing the cell actions.
     */
    public Scene createScene() {
        StackPane root = new StackPane();
        root.getStyleClass().add("map-cell-root");

        ImageView pinIcon = new ImageView();
        try {
            pinIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/circle.png"))));
            pinIcon.setFitWidth(60);
            pinIcon.setFitHeight(60);
            pinIcon.setPreserveRatio(true);
        } catch (Exception ignore) {
        }

        Label title = new Label("Ações na posição (" + X + ", " + Y + ")");
        title.getStyleClass().add("menu-title");

        Button constructStructure = createMenuButton("Construir Estrutura", "/icons/tool.png", false);
        Button upgradeStructure = createMenuButton("Melhorar Estrutura", "/icons/up-arrow.png", false);
        Button getInfoStructure = createMenuButton("Obter Informações da Estrutura", "/icons/information.png", false);
        Button closeButton = createMenuButton("Fechar", "/icons/close.png", true);

        constructStructure.setOnAction(event -> {
            CreateStructureScreen screen = new CreateStructureScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        upgradeStructure.setOnAction(event -> {
            UpgradeStructureScreen upgradeStructureScreen = new UpgradeStructureScreen(POPUP_STAGE, SESSION, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(upgradeStructureScreen.createScene(X, Y));
        });

        getInfoStructure.setOnAction(event -> {
            StructureScreen screen = new StructureScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        closeButton.setOnAction(event -> POPUP_STAGE.close());

        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(40));
        menuBox.getStyleClass().add("menu-container");

        menuBox.getChildren().addAll(
                pinIcon,
                title,
                constructStructure,
                upgradeStructure,
                getInfoStructure,
                closeButton
        );

        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, 1000, 650);

        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/jogo/style.css")).toExternalForm());
        } catch (Exception ignore) {
        }

        return scene;
    }
}