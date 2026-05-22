package jogo.screens;

import jogo.models.Structures.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.io.FileHandler;

public class MainGameScreen {

    private Stage stage;
    private GameSession session;

    private Label topInfoLabel;
    private Label inventoryLabel;
    private Label messageLabel;

    public MainGameScreen(Stage stage, GameSession session) {
        this.stage = stage;
        this.session = session;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();

        root.setTop(createTopArea());
        root.setLeft(createLeftMenu());
        root.setCenter(createMapArea());
        root.setBottom(createBottomArea());

        messageLabel = new Label();

        VBox bottomWithMessage = new VBox(10);
        bottomWithMessage.setAlignment(Pos.CENTER);
        bottomWithMessage.setPadding(new Insets(10));
        bottomWithMessage.getChildren().addAll(createBottomArea(), messageLabel);

        root.setBottom(bottomWithMessage);

        updateInfo();

        return new Scene(root, 900, 650);
    }

    private HBox createTopArea() {
        topInfoLabel = new Label();

        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(15));
        topBox.getChildren().add(topInfoLabel);

        return topBox;
    }

    private VBox createLeftMenu() {
        Button searchResourceButton = new Button("Procurar Recurso");
        Button saveButton = new Button("Guardar Jogo");
        Button exitButton = new Button("Sair");
        Button endTurnButton = new Button("Terminar Turno");

        searchResourceButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxWidth(Double.MAX_VALUE);
        endTurnButton.setMaxWidth(Double.MAX_VALUE);

        searchResourceButton.setOnAction(event -> {
            SearchResourcesScreen searchResourcesScreen = new SearchResourcesScreen(stage,session);

            stage.setScene(searchResourcesScreen.searchResourcesMenu());

        });

        saveButton.setOnAction(event -> {
            try {
                FileHandler.saveFullGame(
                        session.getMap(),
                        session.getPlayer1(),
                        session.getPlayer2()
                );

                messageLabel.setText("Jogo guardado com sucesso!");

            } catch (Exception e) {
                messageLabel.setText("Erro ao guardar jogo: " + e.getMessage());
            }
        });

        endTurnButton.setOnAction(event -> {
            session.endTurn();

            if (session.isGameOver()) {
                messageLabel.setText("Fim de jogo!");
                return;
            }

            MainGameScreen updatedScreen = new MainGameScreen(stage, session);
            stage.setScene(updatedScreen.createScene());
        });

        exitButton.setOnAction(event -> {
            StartScreen startScreen = new StartScreen(stage, session);
            stage.setScene(startScreen.createScene());
        });

        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setPrefWidth(180);

        menu.getChildren().addAll(
                searchResourceButton,
                saveButton,
                endTurnButton,
                exitButton
        );

        return menu;
    }

    private GridPane createMapArea() {
        GridPane mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setHgap(5);
        mapGrid.setVgap(5);

        int columns = session.getMap().getCOLUMN_SIZE();
        int rows = session.getMap().getLINE_SIZE();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Button cellButton = new Button();

                cellButton.setPrefSize(70, 70);

                try {
                    Structures structure = session.getMap().getStructure(x, y);

                    if (structure == null) {
                        cellButton.setText("");
                    } else {
                        cellButton.setText(structure.getClass().getSimpleName());
                    }

                } catch (Exception e) {
                    cellButton.setText("Erro");
                }

                int finalX = x;
                int finalY = y;

                cellButton.setOnAction(event -> {
                    Stage popupStage = new Stage();

                    MapCellScreen mapCellScreen = new MapCellScreen(
                            popupStage,
                            session,
                            finalX,
                            finalY,
                            () -> {
                                MainGameScreen updatedScreen = new MainGameScreen(stage, session);
                                stage.setScene(updatedScreen.createScene());
                            }
                    );

                    popupStage.setTitle("Ações na posição " + finalX + ", " + finalY);
                    popupStage.setScene(mapCellScreen.createScene());
                    popupStage.show();
                });

                mapGrid.add(cellButton, y, x);
            }
        }

        return mapGrid;
    }

    private VBox createBottomArea() {
        inventoryLabel = new Label();

        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));
        bottomBox.getChildren().add(inventoryLabel);

        return bottomBox;
    }

    private void updateInfo() {
        topInfoLabel.setText(
                "Dia: " + session.getDay()
                        + " | Jogador atual: " + session.getActualPlayer().getName()
                        + " | AP: " + session.getActualPlayer().getActionPoints()
        );

        inventoryLabel.setText(
                "Inventário: " + session.getActualPlayer().getInventory()
        );
    }
}