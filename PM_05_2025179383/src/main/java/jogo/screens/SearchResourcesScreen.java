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
import jogo.models.ResourceType;

public class SearchResourcesScreen {

    private Stage stage;
    private GameSession session;

    private Label resourceFoundLabel;
    private Label actionPointsLabel;

    public SearchResourcesScreen(Stage stage, GameSession session) {
        this.stage = stage;
        this.session = session;
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
        Button food = new Button("FOOD");
        Button stone = new Button("STONE");
        Button wood = new Button("WOOD");

        resourceFoundLabel = new Label();

        food.setOnAction(event -> searchResource(ResourceType.FOOD));
        stone.setOnAction(event -> searchResource(ResourceType.STONE));
        wood.setOnAction(event -> searchResource(ResourceType.WOOD));

        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(wood, stone, food);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(buttonsBox);
        borderPane.setBottom(resourceFoundLabel);
        BorderPane.setAlignment(resourceFoundLabel, Pos.CENTER);

        return borderPane;
    }

    public VBox down() {
        actionPointsLabel = new Label("AP Atual: " + updateInfo());

        Button backButton = new Button("Voltar");

        backButton.setOnAction(event -> {
            MainGameScreen gameMenuScreen = new MainGameScreen(stage, session);
            stage.setScene(gameMenuScreen.createScene());
        });

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(backButton, actionPointsLabel);

        return vBox;
    }

    private void searchResource(ResourceType type) {
        String result = obtainResources(type);

        resourceFoundLabel.setText(result);
        actionPointsLabel.setText("AP Atual: " + updateInfo());
    }

    private String obtainResources(ResourceType type) {
        return GameEngine.searchResources(session.getActualPlayer(), type);
    }

    private int updateInfo() {
        return session.getActualPlayer().getActionPoints();
    }
}