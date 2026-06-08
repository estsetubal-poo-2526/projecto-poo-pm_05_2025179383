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

public class SearchResourcesScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private Label resourceFoundLabel;
    private Label actionPointsLabel;

    public SearchResourcesScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
    }

    public Scene searchResourcesMenu() {
        StackPane root = new StackPane();
        root.getStyleClass().add("search-root");

        BorderPane container = new BorderPane();
        container.getStyleClass().add("search-container");

        container.setTop(top());
        container.setCenter(center());
        container.setBottom(down());

        root.getChildren().add(container);

        Scene scene = new Scene(root, 1200, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("/jogo/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Erro ao carregar o CSS.");
        }

        return scene;
    }

    public VBox top() {
        ImageView searchIcon = new ImageView();
        try {
            searchIcon.setImage(new Image(getClass().getResourceAsStream("/icons/magnifying-glass.png")));
            searchIcon.setFitWidth(50);
            searchIcon.setFitHeight(50);
            searchIcon.setPreserveRatio(true);
        } catch (Exception e) { /* Fail-safe */ }

        Label label = new Label("Qual recurso deseja procurar?");
        label.getStyleClass().add("search-title");

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 0, 20, 0));
        topBox.getChildren().addAll(searchIcon, label);

        return topBox;
    }

    private BorderPane center() {
        // Criar os três cards estilizados
        Button woodButton = criarCardRecurso("MADEIRA", "/icons/log.png", "card-wood", "label-wood", ResourceType.WOOD);
        Button stoneButton = criarCardRecurso("PEDRA", "/icons/granite.png", "card-stone", "label-stone", ResourceType.STONE);
        Button foodButton = criarCardRecurso("COMIDA", "/icons/beef.png", "card-food", "label-food", ResourceType.FOOD);

        HBox buttonsBox = new HBox(30);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(woodButton, stoneButton, foodButton);

        // Barra de Resultados (O retângulo inferior com o baú)
        HBox resultBar = new HBox(10);
        resultBar.getStyleClass().add("result-bar");
        resultBar.setAlignment(Pos.CENTER_LEFT);
        resultBar.setMaxWidth(860);

        try {
            ImageView chestIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/treasure-chest.png")));
            chestIcon.setFitWidth(30);
            chestIcon.setFitHeight(30);
            resultBar.getChildren().add(chestIcon);
        } catch (Exception e) {}

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

    private Button criarCardRecurso(String texto, String imgPath, String cardStyle, String labelStyle, ResourceType type) {
        Button btn = new Button();
        btn.getStyleClass().addAll("resource-card", cardStyle);

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setMouseTransparent(true);

        try {
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(imgPath)));
            iv.setFitWidth(80);
            iv.setFitHeight(80);
            iv.setPreserveRatio(true);
            content.getChildren().add(iv);
        } catch (Exception e) {
            System.out.println("Falta imagem: " + imgPath);
        }

        Label lbl = new Label(texto);
        lbl.getStyleClass().add(labelStyle);
        content.getChildren().add(lbl);

        btn.setGraphic(content);
        btn.setOnAction(event -> handleSearchResource(type));

        return btn;
    }

    public BorderPane down() {
        // Botão voltar à esquerda
        Button backButton = new Button("←  Voltar");
        backButton.getStyleClass().add("btn-back");
        backButton.setOnAction(event -> {
            MainGameScreen gameMenuScreen = new MainGameScreen(STAGE, SESSION);
            STAGE.setScene(gameMenuScreen.createScene());
        });

        // Caixa de AP à direita
        HBox apBox = new HBox(5);
        apBox.getStyleClass().add("ap-box");
        apBox.setAlignment(Pos.CENTER);

        try {
            ImageView rayIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/lighting.png")));
            rayIcon.setFitWidth(20);
            rayIcon.setFitHeight(20);
            apBox.getChildren().add(rayIcon);
        } catch (Exception e) {}

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

    private void handleSearchResource(ResourceType type) {
        try {
            int result = GameEngine.searchResources(SESSION.getActualPlayer(), type);
            resourceFoundLabel.setText("Encontraste " + result + " de " + type.name() + "!");
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
        updateActionPointsLabel();
    }

    private void updateActionPointsLabel() {
        actionPointsLabel.setText(String.valueOf(getCurrentActionPoints()));
    }

    private int getCurrentActionPoints() {
        return SESSION.getActualPlayer().getActionPoints();
    }
}