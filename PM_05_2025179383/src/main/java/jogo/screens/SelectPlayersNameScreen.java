package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

public class SelectPlayersNameScreen{

    private final Stage STAGE;
    private final GameSession SESSION;

    private TextField player1NameField;
    private TextField player2NameField;
    private VBox errorBarContainer;
    private Label errorLabel;

    public SelectPlayersNameScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    private HBox createCustomInputField(String prompt, String iconPath, String boxClass) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().addAll("custom-input-box", boxClass);

        try {
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            iv.setFitWidth(20);
            iv.setFitHeight(20);
            box.getChildren().add(iv);
        } catch (Exception e) {}

        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("custom-text-field");
        tf.setPrefWidth(320);

        if (prompt.contains("1")) {
            this.player1NameField = tf;
        } else {
            this.player2NameField = tf;
        }

        box.getChildren().add(tf);
        return box;
    }

    public Scene createScene() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("input-screen-root");

        // Ícone do Escudo Central
        try {
            ImageView shieldView = new ImageView(new Image(getClass().getResourceAsStream("/icons/shield.png")));
            shieldView.setFitWidth(130);
            shieldView.setFitHeight(130);
            shieldView.setPreserveRatio(true);
            root.getChildren().add(shieldView);
        } catch (Exception e) {}

        Label title = new Label("Insira o Nome\ndos Jogadores!");
        title.getStyleClass().add("players-title");
        title.setAlignment(Pos.CENTER);
        root.getChildren().add(title);

        // Contentores Customizados dos Inputs
        HBox p1Box = createCustomInputField("Nome do Jogador 1", "/icons/peoplegreen.png", "input-box-p1");
        HBox p2Box = createCustomInputField("Nome do Jogador 2", "/icons/peopleblue.png", "input-box-p2");
        root.getChildren().addAll(p1Box, p2Box);

        // Botão Iniciar Jogo
        Button startButton = new Button("Iniciar Jogo");
        startButton.getStyleClass().add("btn-start-game");
        try {
            ImageView startIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/swords.png")));
            startIcon.setFitWidth(18);
            startIcon.setFitHeight(18);
            startButton.setGraphic(startIcon);
            startButton.setGraphicTextGap(10);
        } catch (Exception e) {}
        startButton.setOnAction(event -> validateAndStart());
        root.getChildren().add(startButton);

        // Contentor da Barra de Erros (Invisível por omissão)
        errorBarContainer = new VBox();
        errorBarContainer.setAlignment(Pos.CENTER);
        errorBarContainer.setManaged(false);
        errorBarContainer.setVisible(false);

        HBox errorBar = new HBox(12);
        errorBar.setAlignment(Pos.CENTER_LEFT);
        errorBar.getStyleClass().add("validation-error-bar");

        try {
            ImageView errIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/information.png")));
            errIcon.setFitWidth(18);
            errIcon.setFitHeight(18);
            errorBar.getChildren().add(errIcon);
        } catch (Exception e) {}

        errorLabel = new Label();
        errorLabel.getStyleClass().add("validation-error-text");
        errorBar.getChildren().add(errorLabel);
        errorBarContainer.getChildren().add(errorBar);
        root.getChildren().add(errorBarContainer);

        Scene scene = new Scene(root, 1200, 800);
        loadStyle(scene);
        return scene;
    }

    private void validateAndStart() {
        String p1 = player1NameField.getText();
        String p2 = player2NameField.getText();

        if (p1 == null || p1.isBlank() || p2 == null || p2.isBlank()) {
            errorLabel.setText("Preencha o nome dos dois jogadores!");
            errorBarContainer.setManaged(true);
            errorBarContainer.setVisible(true);
            return;
        }

        SESSION.startNewGame(p1, p2);
        String eventText = SESSION.startDayEvent();

        MainGameScreen mainGameScreen = new MainGameScreen(STAGE, SESSION, eventText);
        STAGE.setScene(mainGameScreen.createScene());
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