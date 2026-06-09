package jogo.screens;

import javafx.geometry.Insets;
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

public class StartScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    public StartScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    public Scene createScene() {
        VBox rootBackground = new VBox();
        rootBackground.setAlignment(Pos.CENTER);
        rootBackground.getStyleClass().add("start-screen-root");

        VBox menuCard = new VBox(25);
        menuCard.setAlignment(Pos.CENTER);
        menuCard.getStyleClass().add("menu-card-container");

        
        try {
            ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/icons/shield.png")));
            logoView.setFitWidth(180);
            logoView.setFitHeight(180);
            logoView.setPreserveRatio(true);

            VBox logoWrapper = new VBox(logoView);
            logoWrapper.getStyleClass().add("menu-logo-view");
            menuCard.getChildren().add(logoWrapper);
        } catch (Exception e) {}

        
        Label title = new Label("JOGO DE ESTRATÉGIA");
        title.getStyleClass().add("menu-title");
        menuCard.getChildren().add(title);

        
        Button newGameButton = new Button("NOVO JOGO");
        newGameButton.getStyleClass().add("btn-menu-primary");
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/swords.png")));
            icon.setFitWidth(22);
            icon.setFitHeight(22);
            newGameButton.setGraphic(icon);
            newGameButton.setGraphicTextGap(10);
        } catch (Exception e) {}
        newGameButton.setOnAction(event -> {
            SelectPlayersNameScreen inputScreen = new SelectPlayersNameScreen(STAGE, SESSION);
            STAGE.setScene(inputScreen.createScene());
        });

        
        Button loadGameButton = new Button("CARREGAR JOGO");
        loadGameButton.getStyleClass().add("btn-menu-secondary");
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/load.png")));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            loadGameButton.setGraphic(icon);
            loadGameButton.setGraphicTextGap(10);
        } catch (Exception e) {}
        loadGameButton.setOnAction(event -> loadSavedGame());

        
        Button exitButton = new Button("SAIR");
        exitButton.getStyleClass().add("btn-menu-neutral");
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/icons/back.png")));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            exitButton.setGraphic(icon);
            exitButton.setGraphicTextGap(10);
        } catch (Exception e) {}
        exitButton.setOnAction(event -> STAGE.close());

        menuCard.getChildren().addAll(newGameButton, loadGameButton, exitButton);

        
        HBox infoBar = new HBox(10);
        infoBar.setAlignment(Pos.CENTER_LEFT);
        infoBar.getStyleClass().add("result-bar");
        infoBar.setPrefWidth(380);
        infoBar.setMaxWidth(380);

        try {
            ImageView infoIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/information.png")));
            infoIcon.setFitWidth(20);
            infoIcon.setFitHeight(20);
            infoBar.getChildren().add(infoIcon);
        } catch (Exception e) {}

        Label infoText = new Label("Escolha uma opção para começar.");
        infoText.getStyleClass().add("result-text");
        infoBar.getChildren().add(infoText);
        menuCard.getChildren().add(infoBar);

        rootBackground.getChildren().add(menuCard);
        Scene scene = new Scene(rootBackground, 1200, 800);
        loadStyle(scene);
        return scene;
    }

    private void loadSavedGame() {
        try {
            FileHandler.SaveData data = FileHandler.loadGame();
            SESSION.loadGame(data.getMap(), data.getP1(), data.getP2(), data.getDay());
            MainGameScreen mainGameScreen = new MainGameScreen(STAGE, SESSION);
            STAGE.setScene(mainGameScreen.createScene());
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }

    private void loadStyle(Scene scene) {
        try {
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");
            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            }
        } catch (Exception e) {}
    }
}