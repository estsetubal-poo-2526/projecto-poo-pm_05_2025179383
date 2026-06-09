package jogo.screens;

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

import java.util.Objects;

/**
 * Controller and view class responsible for handling player identity registration.
 * Collects custom player names, runs input text validations, handles inline error feedback,
 * and bootstraps the persistent model state inside the GameSession before map generation.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class SelectPlayersNameScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private TextField player1NameField;
    private TextField player2NameField;
    private VBox errorBarContainer;
    private Label errorLabel;

    /**
     * Constructs a new SelectPlayersNameScreen configuration view.
     *
     * @param stage   The primary Stage window context framework.
     * @param session The core engine {@link GameSession} instance mapping active match data.
     */
    public SelectPlayersNameScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    /**
     * Factory utility method that constructs a stylized input field box matched with a graphical icon indicator.
     * Conditionally assigns the internal reference targets by assessing the prompt text criteria.
     *
     * @param prompt   The descriptive layout placeholder message text inside the input element.
     * @param iconPath The source resource URL path targeting the required descriptive icon.
     * @param boxClass The custom CSS style class mapping the external wrapper border rules.
     * @return A compiled and bound JavaFX HBox container containing input nodes.
     */
    private HBox createCustomInputField(String prompt, String iconPath, String boxClass) {
        HBox box = new HBox(12);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().addAll("custom-input-box", boxClass);

        try {
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            iv.setFitWidth(20);
            iv.setFitHeight(20);
            box.getChildren().add(iv);
        } catch (Exception ignore) {
        }

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

    /**
     * Compiles the central interactive configuration catalog nodes, hooks form submission logic,
     * appends verification feedback components, and locks styling references inside a target Scene wrapper.
     *
     * @return A fully populated JavaFX Scene instance ready for Stage visualization.
     */
    public Scene createScene() {
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("input-screen-root");

        try {
            ImageView shieldView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/shield.png"))));
            shieldView.setFitWidth(130);
            shieldView.setFitHeight(130);
            shieldView.setPreserveRatio(true);
            root.getChildren().add(shieldView);
        } catch (Exception ignore) {
        }

        Label title = new Label("Insira o Nome\ndos Jogadores!");
        title.getStyleClass().add("players-title");
        title.setAlignment(Pos.CENTER);
        root.getChildren().add(title);

        HBox p1Box = createCustomInputField("Nome do Jogador 1", "/icons/peoplegreen.png", "input-box-p1");
        HBox p2Box = createCustomInputField("Nome do Jogador 2", "/icons/peopleblue.png", "input-box-p2");
        root.getChildren().addAll(p1Box, p2Box);

        Button startButton = new Button("Iniciar Jogo");
        startButton.getStyleClass().add("btn-start-game");
        try {
            ImageView startIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/swords.png"))));
            startIcon.setFitWidth(18);
            startIcon.setFitHeight(18);
            startButton.setGraphic(startIcon);
            startButton.setGraphicTextGap(10);
        } catch (Exception ignore) {
        }
        startButton.setOnAction(event -> validateAndStart());
        root.getChildren().add(startButton);

        errorBarContainer = new VBox();
        errorBarContainer.setAlignment(Pos.CENTER);
        errorBarContainer.setManaged(false);
        errorBarContainer.setVisible(false);

        HBox errorBar = new HBox(12);
        errorBar.setAlignment(Pos.CENTER_LEFT);
        errorBar.getStyleClass().add("validation-error-bar");

        try {
            ImageView errIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/information.png"))));
            errIcon.setFitWidth(18);
            errIcon.setFitHeight(18);
            errorBar.getChildren().add(errIcon);
        } catch (Exception ignore) {
        }

        errorLabel = new Label();
        errorLabel.getStyleClass().add("validation-error-text");
        errorBar.getChildren().add(errorLabel);
        errorBarContainer.getChildren().add(errorBar);
        root.getChildren().add(errorBarContainer);

        Scene scene = new Scene(root, 1200, 800);
        loadStyle(scene);
        return scene;
    }

    /**
     * Inspects textual string sequences inside form components, generating runtime error warnings
     * or routing context execution blocks toward game world initializations upon validation.
     */
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

    /**
     * Resolves layout structure rules from persistent directory files to normalize dashboard rendering properties.
     *
     * @param scene The active target JavaFX Scene instance whose stylesheets stack will adapt.
     */
    private void loadStyle(Scene scene) {
        try {
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");
            if (cssFile.exists()) {
                scene.getStylesheets().add(cssFile.toURI().toString());
            }
        } catch (Exception ignore) {
        }
    }
}