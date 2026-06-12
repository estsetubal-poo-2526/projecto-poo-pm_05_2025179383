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
import jogo.exceptions.GameException;
import jogo.io.FileHandler;

import java.util.Objects;

/**
 * The primary entry-point viewport and main menu controller.
 * Renders the introductory branding assets and establishes navigation routes
 * to bootstrap brand new match sessions, deserialize local game saves, or close the stage context.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class StartScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    /**
     * Constructs a baseline StartScreen container matching the main application context window.
     *
     * @param stage   The primary Stage window manager wrapper.
     * @param session The persistent {@link GameSession} tracker state machine.
     */
    public StartScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    /**
     * Initializes structural menu container nodes, binds selection actions, anchors
     * decorative graphic iconography, and packs the central card within a target Scene layout.
     *
     * @return A fully compiled JavaFX Scene representing the main gateway menu.
     */
    public Scene createScene() {
        VBox rootBackground = new VBox();
        rootBackground.setAlignment(Pos.CENTER);
        rootBackground.getStyleClass().add("start-screen-root");

        VBox menuCard = new VBox(25);
        menuCard.setAlignment(Pos.CENTER);
        menuCard.getStyleClass().add("menu-card-container");

        try {
            ImageView logoView = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/shield.png")))
            );

            logoView.setFitWidth(180);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);

            VBox logoWrapper = new VBox(logoView);
            logoWrapper.setAlignment(Pos.CENTER);
            logoWrapper.setPrefWidth(380);
            logoWrapper.setMaxWidth(380);
            logoWrapper.getStyleClass().add("menu-logo-view");

            menuCard.getChildren().add(logoWrapper);
        } catch (Exception ignore) {

        }

        Label title = new Label("JOGO DE ESTRATÉGIA");
        title.getStyleClass().add("menu-title");
        menuCard.getChildren().add(title);

        Button newGameButton = new Button("NOVO JOGO");
        newGameButton.getStyleClass().add("btn-menu-primary");

        try {
            ImageView icon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/swords.png")))
            );
            icon.setFitWidth(22);
            icon.setFitHeight(22);
            icon.setPreserveRatio(true);
            newGameButton.setGraphic(icon);
            newGameButton.setGraphicTextGap(10);
        } catch (Exception ignore) {

        }

        newGameButton.setOnAction(event -> {
            SelectPlayersNameScreen inputScreen = new SelectPlayersNameScreen(STAGE, SESSION);
            STAGE.setScene(inputScreen.createScene());
        });

        Button loadGameButton = new Button("CARREGAR JOGO");
        loadGameButton.getStyleClass().add("btn-menu-secondary");

        try {
            ImageView icon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/load.png")))
            );
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            icon.setPreserveRatio(true);
            loadGameButton.setGraphic(icon);
            loadGameButton.setGraphicTextGap(10);
        } catch (Exception ignore) {

        }

        loadGameButton.setOnAction(event -> loadSavedGame());

        Button exitButton = new Button("SAIR");
        exitButton.getStyleClass().add("btn-menu-neutral");

        try {
            ImageView icon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/back.png")))
            );
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            icon.setPreserveRatio(true);
            exitButton.setGraphic(icon);
            exitButton.setGraphicTextGap(10);
        } catch (Exception ignore) {

        }

        exitButton.setOnAction(event -> STAGE.close());

        menuCard.getChildren().addAll(newGameButton, loadGameButton, exitButton);

        HBox infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.getStyleClass().add("result-bar");
        infoBar.setPrefWidth(380);
        infoBar.setMaxWidth(380);

        try {
            ImageView infoIcon = new ImageView(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/information.png")))
            );
            infoIcon.setFitWidth(20);
            infoIcon.setFitHeight(20);
            infoIcon.setPreserveRatio(true);
            infoBar.getChildren().add(infoIcon);
        } catch (Exception ignore) {

        }

        Label infoText = new Label("Escolha uma opção para começar.");
        infoText.getStyleClass().add("result-text");
        infoBar.getChildren().add(infoText);

        menuCard.getChildren().add(infoBar);
        rootBackground.getChildren().add(menuCard);

        Scene scene = new Scene(rootBackground, 1200, 800);
        loadStyle(scene);

        return scene;
    }

    /**
     * Reaches persistent file directories via IO modules to reconstruct historical
     * map data grids, player metrics, and session indices. Renders main display loops on success.
     */
    private void loadSavedGame() {
        try {
            FileHandler.SaveData data = FileHandler.loadGame();

            SESSION.loadGame(
                    data.getMap(),
                    data.getP1(),
                    data.getP2(),
                    data.getDay()
            );

            MainGameScreen mainGameScreen = new MainGameScreen(STAGE, SESSION);
            STAGE.setScene(mainGameScreen.createScene());

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }

    /**
     * Pulls cascading stylesheet metrics from root resources to style core panel controls.
     *
     * @param scene The operational Scene workspace receiving stylesheet injections.
     */
    private void loadStyle(Scene scene) {
        try {
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(
                    userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css"
            );

            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            }
        } catch (Exception ignore) {

        }
    }
}