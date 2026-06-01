package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        BorderPane borderPane = new BorderPane();

        borderPane.setTop(top());
        borderPane.setCenter(center());
        borderPane.setBottom(down());

        return new Scene(borderPane, 600, 400);
    }

    public HBox top() {
        Label label = new Label("Qual Recurso?");

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(label);

        return hBox;
    }

    private BorderPane center() {
        Button foodButton = new Button("FOOD");
        Button stoneButton = new Button("STONE");
        Button woodButton = new Button("WOOD");

        resourceFoundLabel = new Label();

        foodButton.setOnAction(event -> handleSearchResource(ResourceType.FOOD));
        stoneButton.setOnAction(event -> handleSearchResource(ResourceType.STONE));
        woodButton.setOnAction(event -> handleSearchResource(ResourceType.WOOD));

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(woodButton, stoneButton, foodButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(buttonsBox);
        borderPane.setBottom(resourceFoundLabel);
        BorderPane.setAlignment(resourceFoundLabel, Pos.CENTER);

        return borderPane;
    }

    public VBox down() {
        actionPointsLabel = new Label("AP Atual: " + getCurrentActionPoints());

        Button backButton = new Button("Voltar");

        backButton.setOnAction(event -> {
            MainGameScreen gameMenuScreen = new MainGameScreen(STAGE, SESSION);
            STAGE.setScene(gameMenuScreen.createScene());
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(backButton, actionPointsLabel);

        return vBox;
    }

    private void handleSearchResource(ResourceType type) {
        try {
            int result = GameEngine.searchResources(
                    SESSION.getActualPlayer(),
                    type
            );

            resourceFoundLabel.setText("Encontraste " + result + " de " + type.name());

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }

        updateActionPointsLabel();
    }

    private void updateActionPointsLabel() {
        actionPointsLabel.setText("AP Atual: " + getCurrentActionPoints());
    }

    private int getCurrentActionPoints() {
        return SESSION.getActualPlayer().getActionPoints();
    }
}