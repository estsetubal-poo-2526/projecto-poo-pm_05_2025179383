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

public class CreateStructureScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    public CreateStructureScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    private Button createStructureCard(String text, String iconName, String specificCardClass, String specificLabelClass, StructuresType type) {
        Button cardButton = new Button();
        cardButton.getStyleClass().addAll("structure-card", specificCardClass);

        VBox cardContent = new VBox(15);
        cardContent.setAlignment(Pos.CENTER);

        try {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/icons/" + iconName)));
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            cardContent.getChildren().add(imageView);
        } catch (Exception e) {
            System.err.println("Não foi possível carregar o ícone: " + iconName);
        }

        Label label = new Label(text.toUpperCase());
        label.getStyleClass().add(specificLabelClass);
        cardContent.getChildren().add(label);

        cardButton.setGraphic(cardContent);
        cardButton.setOnAction(event -> showStructureInfo(type));

        return cardButton;
    }

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
        Button mineButton = createStructureCard("Mina", "granite.png", "card-mine", "label-mine", StructuresType.MINE);
        Button ranchButton = createStructureCard("Rancho", "ranch.png", "card-ranch", "label-ranch", StructuresType.RANCH);
        Button cityButton = createStructureCard("Cidade", "city.png", "card-city", "label-city", StructuresType.CITY);

        HBox structuresBox = new HBox(20);
        structuresBox.setAlignment(Pos.CENTER);
        structuresBox.getChildren().addAll(forestButton, mineButton, ranchButton, cityButton);


        Button backButton = new Button("Voltar");
        backButton.getStyleClass().add("btn-back");

        try {
            ImageView backIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/back.png"))); // Ícone de seta para a esquerda
            backIcon.setFitWidth(16);
            backIcon.setFitHeight(16);
            backButton.setGraphic(backIcon);
            backButton.setGraphicTextGap(8);
        } catch (Exception e) {}

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

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.F5) {
                loadStyle(scene);
                System.out.println("CSS Recarregado (F5) em: " + X + "," + Y);
            }
        });

        return scene;
    }


    private void showStructureInfo(StructuresType type) {
        StructuresBuildingInfoScreen structuresInfoScreen = new StructuresBuildingInfoScreen(
                SESSION,
                POPUP_STAGE,
                ON_UPDATE_MAP
        );

        POPUP_STAGE.setScene(structuresInfoScreen.createScreen(type, X, Y));
    }

    private void loadStyle(Scene scene) {
        try {
            scene.getStylesheets().clear();
            String userDir = System.getProperty("user.dir");
            java.io.File cssFile = new java.io.File(userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css");

            if (cssFile.exists()) {
                String cssUrl = cssFile.toURI().toString();
                scene.getStylesheets().add(cssUrl);
                System.out.println("[SUCESSO INTERNACIONAL] CSS injetado via URI limpa!");
            } else {
                System.err.println("[ERRO] O ficheiro sumiu.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar CSS: " + e.getMessage());
        }
    }
}