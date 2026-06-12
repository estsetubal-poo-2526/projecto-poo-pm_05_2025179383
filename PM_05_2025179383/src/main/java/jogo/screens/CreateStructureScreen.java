package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.models.Structures.StructuresType;

import java.util.Objects;

/**
 * Controller and view class responsible for rendering the structure selection menu.
 * Displays available building options as stylized cards and handles transitions
 * toward detailed construction requirement screens or back to cell details.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class CreateStructureScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    /**
     * Constructs a new CreateStructureScreen bound to a specific map grid coordinate.
     *
     * @param popupStage  The Stage window context hosting the current screen view.
     * @param session     The active {@link GameSession} instance containing player and world data.
     * @param x           The horizontal X-axis coordinate of the targeted map cell.
     * @param y           The vertical Y-axis coordinate of the targeted map cell.
     * @param onUpdateMap A Runnable callback used to refresh the main visual map grid after alterations.
     */
    public CreateStructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    /**
     * Helper factory method that generates a stylized JavaFX Button acting as a structure card.
     * Embeds the building's icon name, title, and attaches an event handler targeting detailed information.
     *
     * @param text               The descriptive label string to present on the card.
     * @param iconName           The file name of the icon asset located within the resources catalog.
     * @param specificCardClass  The specific CSS style class targeting the button card container.
     * @param specificLabelClass The specific CSS style class targeting the card text layout.
     * @param type               The associated {@link StructuresType} identifier representing the building.
     * @return A fully assembled JavaFX Button node styled as a choice card.
     */
    private Button createStructureCard(String text, String iconName, String specificCardClass, String specificLabelClass, StructuresType type) {
        Button cardButton = new Button();
        cardButton.getStyleClass().addAll("structure-card", specificCardClass);

        VBox cardContent = new VBox(15);
        cardContent.setAlignment(Pos.CENTER);

        try {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + iconName))));
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            cardContent.getChildren().add(imageView);
        } catch (Exception ignore) {

        }

        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add(specificLabelClass);
        cardContent.getChildren().add(label);

        cardButton.setGraphic(cardContent);
        cardButton.setOnAction(event -> showStructureInfo(type));

        return cardButton;
    }

    /**
     * Assembles the main user interface components, binds layout nodes,
     * applies stylesheet configurations, and wraps the tree layout inside a definitive Scene.
     *
     * @return The fully built JavaFX Scene instance ready for rendering on the active Stage.
     */
    public Scene createScene() {
        VBox rootBackground = new VBox();
        rootBackground.setAlignment(Pos.CENTER);
        rootBackground.getStyleClass().add("create-structure-root");

        VBox centerContainer = new VBox(30);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getStyleClass().add("create-structure-container");

        Label title = new Label("Que estrutura deseja criar em (" + X + ", " + Y + ")?");
        title.getStyleClass().add("create-structure-title");

        Button forestButton = createStructureCard("Floresta", "forest.png", "card-forest", "label-forest", StructuresType.FOREST);
        Button mineButton = createStructureCard("Mina", "mine.png", "card-mine", "label-mine", StructuresType.MINE);
        Button ranchButton = createStructureCard("Rancho", "ranch.png", "card-ranch", "label-ranch", StructuresType.RANCH);
        Button cityButton = createStructureCard("Cidade", "city.png", "card-city", "label-city", StructuresType.CITY);

        HBox structuresBox = new HBox(20);
        structuresBox.setAlignment(Pos.CENTER);
        structuresBox.getChildren().addAll(forestButton, mineButton, ranchButton, cityButton);

        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("btn-back");

        try {
            ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/back.png"))));
            backIcon.setFitWidth(16);
            backIcon.setFitHeight(16);
            backButton.setGraphic(backIcon);
            backButton.setGraphicTextGap(8);
        } catch (Exception ignore) {}

        backButton.setOnAction(event -> {
            MapCellScreen screen = new MapCellScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.getStyleClass().add("bottom-actions-box");
        bottomBox.getChildren().add(backButton);

        centerContainer.getChildren().addAll(title, structuresBox, bottomBox);
        rootBackground.getChildren().add(centerContainer);

        Scene scene = new Scene(rootBackground, 1200, 800);

        loadStyle(scene);

        return scene;
    }

    /**
     * Redirects the workflow towards the detailed build validation screen for a selected architecture.
     *
     * @param type The specific type of structure selected for deployment query.
     */
    private void showStructureInfo(StructuresType type) {
        StructuresBuildingInfoScreen structuresInfoScreen = new StructuresBuildingInfoScreen(
                SESSION,
                POPUP_STAGE,
                ON_UPDATE_MAP
        );

        POPUP_STAGE.setScene(structuresInfoScreen.createScreen(type, X, Y));
    }

    /**
     * Attempts to resolve and inject the external stylesheet definitions
     * from the targeted project path into the active Scene layout.
     *
     * @param scene The current active JavaFX Scene whose stylesheet stack will be manipulated.
     */
    private void loadStyle(Scene scene) {
        try {
            scene.getStylesheets().clear();
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");

            if (cssFile.exists()) {
                String cssUrl = cssFile.toURI().toString();
                scene.getStylesheets().add(cssUrl);
            }
        } catch (Exception ignore) {
            // Exceptions are intentionally ignored to avoid breaking runtime rendering when disk paths shift
        }
    }
}