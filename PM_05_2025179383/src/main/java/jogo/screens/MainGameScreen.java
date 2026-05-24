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

    private final Stage STAGE;
    private final GameSession SESSION;

    private Stage currentPopupStage;

    private Label topInfoLabel;
    private Label eventLabel;
    private Label inventoryLabel;
    private Label messageLabel;

    private String eventMessage;

    public MainGameScreen(Stage stage, GameSession session) {
        this.STAGE = stage;
        this.SESSION = session;
        this.eventMessage = "";
    }

    public MainGameScreen(Stage stage, GameSession session, String eventMessage) {
        this.STAGE = stage;
        this.SESSION = session;
        this.eventMessage = eventMessage;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();

        root.setTop(createTopArea());
        root.setLeft(createLeftMenu());
        root.setCenter(createMapArea());

        messageLabel = new Label();

        VBox bottomWithMessage = new VBox(10);
        bottomWithMessage.setAlignment(Pos.CENTER);
        bottomWithMessage.setPadding(new Insets(10));
        bottomWithMessage.getChildren().addAll(createBottomArea(), messageLabel);

        root.setBottom(bottomWithMessage);

        updateInfo();

        return new Scene(root, 900, 650);
    }

    private VBox createTopArea() {
        topInfoLabel = new Label();
        eventLabel = new Label();

        VBox topBox = new VBox(8);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(15));

        topBox.getChildren().addAll(topInfoLabel, eventLabel);

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
            SearchResourcesScreen searchResourcesScreen = new SearchResourcesScreen(STAGE, SESSION);
            STAGE.setScene(searchResourcesScreen.searchResourcesMenu());
        });

        saveButton.setOnAction(event -> saveGame());

        endTurnButton.setOnAction(event -> {
            String newEventMessage = SESSION.endTurn();

            if (SESSION.isGameOver()) {
                EndScreen endScreen = new EndScreen(SESSION);
                STAGE.setScene(endScreen.createScene());
                return;
            }

            MainGameScreen updatedScreen = new MainGameScreen(
                    STAGE,
                    SESSION,
                    newEventMessage
            );

            STAGE.setScene(updatedScreen.createScene());
        });

        exitButton.setOnAction(event -> {
            StartScreen startScreen = new StartScreen(STAGE, SESSION);
            STAGE.setScene(startScreen.createScene());
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

        int columns = SESSION.getMap().getCOLUMN_SIZE();
        int rows = SESSION.getMap().getLINE_SIZE();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Button cellButton = new Button();
                cellButton.setPrefSize(70, 70);

                try {
                    Structures structure = SESSION.getMap().getStructure(x, y);

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
                    if (currentPopupStage != null && currentPopupStage.isShowing()) {
                        currentPopupStage.close();
                    }

                    currentPopupStage = new Stage();

                    MapCellScreen mapCellScreen = new MapCellScreen(
                            currentPopupStage,
                            SESSION,
                            finalX,
                            finalY,
                            () -> {
                                MainGameScreen updatedScreen = new MainGameScreen(
                                        STAGE,
                                        SESSION,
                                        eventMessage
                                );

                                STAGE.setScene(updatedScreen.createScene());
                            }
                    );

                    currentPopupStage.setTitle("Ações na posição " + finalX + ", " + finalY);
                    currentPopupStage.setScene(mapCellScreen.createScene());
                    currentPopupStage.show();
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
                "Dia: " + SESSION.getDay()
                        + " | Jogador atual: " + SESSION.getActualPlayer().getName()
                        + " | AP: " + SESSION.getActualPlayer().getActionPoints()
        );

        if (eventMessage == null || eventMessage.isBlank()) {
            eventLabel.setText("");
        } else {
            eventLabel.setText("Evento: " + eventMessage);
        }

        inventoryLabel.setText(
                "Inventário: " + SESSION.getActualPlayer().getInventory()
        );
    }

    private void saveGame() {
        try {
            FileHandler.saveFullGame(
                    SESSION.getMap(),
                    SESSION.getPlayer1(),
                    SESSION.getPlayer2(),
                    SESSION.getDay()
            );

            messageLabel.setText("Jogo guardado com sucesso!");

        } catch (Exception e) {
            ErrorPopUp.show(e.getMessage());
        }
    }
}