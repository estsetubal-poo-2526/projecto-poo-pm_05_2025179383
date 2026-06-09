package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameEngine;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.models.ResourceType;

import java.util.Objects;

/**
 * Controller and view class responsible for rendering the resource gathering interface.
 * Provides interactive cards for players to extract basic materials from the environment
 * while dynamically updating their active action potential limits.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class SearchResourcesScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private Label resourceFoundLabel;
    private Label actionPointsLabel;

    /**
     * Constructs a new SearchResourcesScreen viewport context.
     *
     * @param stage   The primary Stage window architecture container.
     * @param session The current active {@link GameSession} core engine instance.
     */
    public SearchResourcesScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    /**
     * Instantiates the core layout nodes, maps structural subsections,
     * appends styles, and wraps the gathering catalog inside a definitive Scene.
     *
     * @return A fully compiled JavaFX Scene instance ready for stage rendering.
     */
    public Scene searchResourcesMenu() {
        StackPane root = new StackPane();
        root.getStyleClass().add("search-root");

        BorderPane container = new BorderPane();
        container.getStyleClass().add("search-container");

        container.setTop(top());
        container.setCenter(center());
        container.setBottom(bottom());

        root.getChildren().add(container);

        Scene scene = new Scene(root, 1200, 800);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/jogo/style.css")).toExternalForm());
        } catch (Exception ignore) {
        }

        return scene;
    }

    /**
     * Assembles the top header layout banner displaying the section's instructive header title and icons.
     *
     * @return A VBox container node forming the upper boundary layer.
     */
    public VBox top() {
        ImageView searchIcon = new ImageView();
        try {
            searchIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/magnifying-glass.png"))));
            searchIcon.setFitWidth(50);
            searchIcon.setFitHeight(50);
            searchIcon.setPreserveRatio(true);
        } catch (Exception ignore) {

        }

        Label label = new Label("Qual recurso deseja procurar?");
        label.getStyleClass().add("search-title");

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        topBox.getChildren().addAll(searchIcon, label);

        return topBox;
    }

    /**
     * Assembles the central interactive catalog layer hosting selection cards and live gathering result text boxes.
     */
    private BorderPane center() {
        Button woodButton = createResourceCard("MADEIRA", "/icons/log.png", "card-wood", "label-wood", ResourceType.WOOD);
        Button stoneButton = createResourceCard("PEDRA", "/icons/granite.png", "card-stone", "label-stone", ResourceType.STONE);
        Button foodButton = createResourceCard("COMIDA", "/icons/beef.png", "card-food", "label-food", ResourceType.FOOD);

        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(woodButton, stoneButton, foodButton);

        HBox resultBar = new HBox(10);
        resultBar.getStyleClass().add("result-bar");
        resultBar.setAlignment(Pos.CENTER_LEFT);
        resultBar.setMaxWidth(860);

        try {
            ImageView chestIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/treasure-chest.png"))));
            chestIcon.setFitWidth(30);
            chestIcon.setFitHeight(30);
            resultBar.getChildren().add(chestIcon);
        } catch (Exception ignore) {}

        Label prefixLabel = new Label("Resultado: ");
        prefixLabel.getStyleClass().add("result-prefix");

        resourceFoundLabel = new Label("Escolha um recurso para procurar.");
        resourceFoundLabel.getStyleClass().add("result-text");

        resultBar.getChildren().addAll(prefixLabel, resourceFoundLabel);

        BorderPane centerPane = new BorderPane();
        centerPane.setCenter(buttonsBox);
        centerPane.setBottom(resultBar);
        BorderPane.setAlignment(resultBar, Pos.CENTER);
        BorderPane.setMargin(resultBar, new Insets(20, 0, 20, 0));

        return centerPane;
    }

    /**
     * Helper factory method that compiles a stylized JavaFX Button acting as an individual resource selection card.
     *
     * @param text      The descriptive text label for the resource asset.
     * @param imgPath   The package path pointing toward the graphic asset location.
     * @param cardStyle The custom CSS class styling the button wrapper element.
     * @param lblStyle  The custom CSS class styling the layout inner text labels.
     * @param type      The structural {@link ResourceType} associated with the action trigger card.
     * @return A configured JavaFX Button node styled as a choice card.
     */
    private Button createResourceCard(String text, String imgPath, String cardStyle, String lblStyle, ResourceType type) {
        Button btn = new Button();
        btn.getStyleClass().addAll("resource-card", cardStyle);

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setMouseTransparent(true);

        try {
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imgPath))));
            iv.setFitWidth(80);
            iv.setFitHeight(80);
            iv.setPreserveRatio(true);
            content.getChildren().add(iv);
        } catch (Exception ignore) {
        }

        Label lbl = new Label(text);
        lbl.getStyleClass().add(lblStyle);
        content.getChildren().add(lbl);

        btn.setGraphic(content);
        btn.setOnAction(event -> handleSearchResource(type));

        return btn;
    }

    /**
     * Assembles the lower dashboard action bar containing return pathways and current turn action point metrics.
     *
     * @return A BorderPane container node forming the bottom layer boundary.
     */
    public BorderPane bottom() {
        Button backButton = new Button("←  Voltar");
        backButton.getStyleClass().add("btn-back");
        backButton.setOnAction(event -> {
            MainGameScreen gameMenuScreen = new MainGameScreen(STAGE, SESSION);
            STAGE.setScene(gameMenuScreen.createScene());
        });

        HBox apBox = new HBox(5);
        apBox.getStyleClass().add("ap-box");
        apBox.setAlignment(Pos.CENTER);

        try {
            ImageView rayIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/lighting.png"))));
            rayIcon.setFitWidth(20);
            rayIcon.setFitHeight(20);
            apBox.getChildren().add(rayIcon);
        } catch (Exception ignore) {}

        Label apText = new Label("AP Atual: ");
        apText.getStyleClass().add("ap-text");

        actionPointsLabel = new Label(String.valueOf(getCurrentActionPoints()));
        actionPointsLabel.getStyleClass().add("ap-number");

        apBox.getChildren().addAll(apText, actionPointsLabel);

        BorderPane bottomPane = new BorderPane();
        bottomPane.setPadding(new Insets(20, 10, 10, 10));
        bottomPane.setLeft(backButton);
        bottomPane.setRight(apBox);

        return bottomPane;
    }

    /**
     * Delegates the extraction logic query directly toward the core {@link GameEngine} layer,
     * trapping action violations or updating visual metrics accordingly.
     *
     * @param type The targeted type of environmental resource to query for extraction.
     */
    private void handleSearchResource(ResourceType type) {
        try {
            int result = GameEngine.searchResources(SESSION.getActualPlayer(), type);
            resourceFoundLabel.setText("Encontraste " + result + " de " + type.name() + "!");
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
        updateActionPointsLabel();
    }

    /**
     * Synchronizes the lower textual indicators with the player's remaining action points count.
     */
    private void updateActionPointsLabel() {
        actionPointsLabel.setText(String.valueOf(getCurrentActionPoints()));
    }

    /**
     * Accesses the active session's participant to extract their current action potential balance.
     *
     * @return The integer amount of action points remaining for the current active player.
     */
    private int getCurrentActionPoints() {
        return SESSION.getActualPlayer().getActionPoints();
    }
}