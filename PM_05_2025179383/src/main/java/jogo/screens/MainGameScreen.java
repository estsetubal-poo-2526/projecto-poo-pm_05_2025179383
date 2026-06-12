package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.io.FileHandler;
import jogo.models.ResourceType;
import jogo.models.Structures.Structures;

/**
 * The primary gameplay interface controller and view dashboard.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class MainGameScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private Stage currentPopupStage;
    private GridPane mapGrid;

    private Label dayLabel;
    private Label playerLabel;
    private Label apLabel;
    private Label eventLabel;
    private HBox eventBar;

    private Label woodLabel;
    private Label stoneLabel;
    private Label foodLabel;

    private Label endDayWoodLabel;
    private Label endDayStoneLabel;
    private Label endDayFoodLabel;

    private Label messageLabel;

    private String eventMessage;

    public MainGameScreen(Stage stage, GameSession session) {
        this(stage, session, "");
    }

    public MainGameScreen(Stage stage, GameSession session, String eventMessage) {
        this.STAGE = stage;
        this.SESSION = session;
        this.eventMessage = eventMessage;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("main-game-root");

        root.setLeft(createLeftMenu());
        root.setCenter(createCenterArea());
        root.setBottom(createBottomArea());

        refreshDashboardState();

        Scene scene = new Scene(root, 1300, 850);
        loadStyle(scene);

        return scene;
    }

    private VBox createLeftMenu() {
        VBox menu = new VBox(26);
        menu.getStyleClass().add("sidebar-menu");
        menu.setPrefWidth(300);
        menu.setPadding(new Insets(34, 28, 34, 28));
        menu.setAlignment(Pos.TOP_CENTER);

        Label menuTitle = new Label("Menu");
        menuTitle.getStyleClass().add("main-menu-title");

        Label divider = new Label("──────── ◆ ────────");
        divider.getStyleClass().add("menu-divider");

        Button searchResourceButton = createMenuButton(
                "/icons/magnifying-glass.png",
                "Procurar Recurso",
                "menu-btn-green"
        );

        Button saveButton = createMenuButton(
                "/icons/load.png",
                "Guardar Jogo",
                "menu-btn-blue"
        );

        Button endTurnButton = createMenuButton(
                "/icons/check.png",
                "Terminar Turno",
                "menu-btn-orange"
        );

        Button exitButton = createMenuButton(
                "/icons/back.png",
                "Sair",
                "menu-btn-red"
        );

        searchResourceButton.setOnAction(event -> {
            SearchResourcesScreen searchResourcesScreen = new SearchResourcesScreen(STAGE, SESSION);
            STAGE.setScene(searchResourcesScreen.searchResourcesMenu());
        });

        saveButton.setOnAction(event -> saveGame());

        endTurnButton.setOnAction(event -> {
            String newEventMessage = SESSION.endTurn();

            if (newEventMessage != null && !newEventMessage.isBlank()) {
                this.eventMessage = newEventMessage;
            }

            if (SESSION.isGameOver()) {
                EndScreen endScreen = new EndScreen(SESSION);
                STAGE.setScene(endScreen.createScene());
                return;
            }

            refreshDashboardState();
        });

        exitButton.setOnAction(event -> {
            StartScreen startScreen = new StartScreen(STAGE, SESSION);
            STAGE.setScene(startScreen.createScene());
        });

        menu.getChildren().addAll(
                menuTitle,
                divider,
                searchResourceButton,
                saveButton,
                endTurnButton,
                exitButton
        );

        return menu;
    }

    private Button createMenuButton(String imagePath, String text, String extraClass) {
        ImageView iconView = createIcon(imagePath, 32, 32);

        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("menu-btn-text");

        HBox content = new HBox(18, iconView, textLabel);
        content.setAlignment(Pos.CENTER_LEFT);

        Button button = new Button();
        button.setGraphic(content);
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().addAll("main-menu-btn", extraClass);

        return button;
    }

    private ImageView createIcon(String imagePath, double width, double height) {
        ImageView imageView = new ImageView();

        try {
            var stream = getClass().getResourceAsStream(imagePath);

            if (stream != null) {
                Image image = new Image(stream);
                imageView.setImage(image);
            } else {
                System.err.println("Imagem não encontrada: " + imagePath);
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + imagePath);
        }

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private VBox createCenterArea() {
        VBox centerContainer = new VBox(16);
        centerContainer.setPadding(new Insets(24, 28, 14, 28));
        centerContainer.setAlignment(Pos.TOP_CENTER);

        StackPane mapPanel = createMapPanel();

        centerContainer.getChildren().addAll(
                createTopArea(),
                mapPanel
        );

        VBox.setVgrow(mapPanel, Priority.ALWAYS);

        return centerContainer;
    }

    private VBox createTopArea() {
        VBox topContainer = new VBox(10);
        topContainer.setAlignment(Pos.CENTER);

        HBox infoBar = new HBox(80);
        infoBar.getStyleClass().add("top-info-box");
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setPadding(new Insets(14, 40, 14, 40));

        dayLabel = new Label();
        playerLabel = new Label();
        apLabel = new Label();

        dayLabel.getStyleClass().add("top-info-text");
        playerLabel.getStyleClass().add("top-info-text");
        apLabel.getStyleClass().add("top-info-text");

        infoBar.getChildren().addAll(
                createTopInfoItem("/icons/calendar.png", dayLabel),
                createTopInfoItem("/icons/person.png", playerLabel),
                createTopInfoItem("/icons/lighting.png", apLabel)
        );

        eventBar = new HBox(12);
        eventBar.getStyleClass().add("event-bar");
        eventBar.setAlignment(Pos.CENTER_LEFT);
        eventBar.setPadding(new Insets(10, 28, 10, 28));

        ImageView eventIcon = createIcon("/icons/confetti.png", 26, 26);

        eventLabel = new Label();
        eventLabel.getStyleClass().add("event-text");

        eventBar.getChildren().addAll(eventIcon, eventLabel);

        topContainer.getChildren().addAll(infoBar, eventBar);

        return topContainer;
    }

    private HBox createTopInfoItem(String imagePath, Label textLabel) {
        ImageView iconView = createIcon(imagePath, 26, 26);

        HBox box = new HBox(12, iconView, textLabel);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private StackPane createMapPanel() {
        StackPane mapPanel = new StackPane();
        mapPanel.getStyleClass().add("map-panel");
        mapPanel.setPadding(new Insets(6));

        mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setHgap(8);
        mapGrid.setVgap(8);
        mapGrid.setPadding(new Insets(10));

        mapPanel.getChildren().add(mapGrid);

        return mapPanel;
    }

    private void populateMapGrid() {
        mapGrid.getChildren().clear();

        int columns = SESSION.getMap().getCOLUMN_SIZE();
        int rows = SESSION.getMap().getLINE_SIZE();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Button cellButton = createMapCell(x, y);
                mapGrid.add(cellButton, y, x);
            }
        }
    }

    private Button createMapCell(int x, int y) {
        Button cellButton = new Button();

        cellButton.setPrefSize(60, 60);
        cellButton.setMinSize(60, 60);
        cellButton.setMaxSize(60, 60);
        cellButton.getStyleClass().add("map-cell");

        StackPane cellStack = new StackPane();

        try {
            ImageView terrainView = createIcon("/icons/grassland.png", 54, 54);
            terrainView.setOpacity(0.45);
            cellStack.getChildren().add(terrainView);
        } catch (Exception ignore) {
        }

        try {
            Structures structure = SESSION.getMap().getStructure(x, y);

            if (structure != null) {
                String typeName = structure.getClass().getSimpleName().toLowerCase();
                ImageView structureIcon = createIcon("/icons/" + typeName + ".png", 44, 44);

                cellStack.getChildren().add(structureIcon);

                if (structure.getOwner().equals(SESSION.getPlayer1())) {
                    cellButton.getStyleClass().add("cell-owner-p1");
                } else if (structure.getOwner().equals(SESSION.getPlayer2())) {
                    cellButton.getStyleClass().add("cell-owner-p2");
                }
            }

        } catch (Exception ignored) {
        }

        cellButton.setGraphic(cellStack);
        cellButton.setOnAction(event -> openMapCellPopup(x, y));

        return cellButton;
    }

    private void openMapCellPopup(int x, int y) {
        if (currentPopupStage != null && currentPopupStage.isShowing()) {
            currentPopupStage.close();
        }

        currentPopupStage = new Stage();

        MapCellScreen mapCellScreen = new MapCellScreen(
                currentPopupStage,
                SESSION,
                x,
                y,
                this::refreshDashboardState
        );

        currentPopupStage.setTitle("Ações na posição " + x + ", " + y);
        currentPopupStage.setScene(mapCellScreen.createScene());
        currentPopupStage.show();
    }

    private VBox createBottomArea() {
        VBox bottomContainer = new VBox(8);
        bottomContainer.setPadding(new Insets(0, 28, 12, 28));

        HBox inventoryBox = createInventoryBox();
        HBox endDayCostsBox = createEndDayCostsBox();

        HBox resourcesRow = new HBox(14);
        resourcesRow.setAlignment(Pos.CENTER);
        resourcesRow.getChildren().addAll(inventoryBox, endDayCostsBox);

        HBox.setHgrow(inventoryBox, Priority.ALWAYS);
        HBox.setHgrow(endDayCostsBox, Priority.ALWAYS);

        HBox statusBar = new HBox(14);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(10, 28, 10, 28));

        ImageView checkIcon = createIcon("/icons/check.png", 28, 28);

        messageLabel = new Label("Pronto para o próximo turno!");
        messageLabel.getStyleClass().add("status-message");

        statusBar.getChildren().addAll(checkIcon, messageLabel);

        bottomContainer.getChildren().addAll(
                resourcesRow,
                statusBar
        );

        return bottomContainer;
    }

    private HBox createInventoryBox() {
        HBox inventoryBox = new HBox(22);
        inventoryBox.getStyleClass().add("inventory-box");
        inventoryBox.setAlignment(Pos.CENTER_LEFT);
        inventoryBox.setPadding(new Insets(12, 20, 12, 20));
        inventoryBox.setMaxWidth(Double.MAX_VALUE);

        ImageView inventoryIcon = createIcon("/icons/backpack.png", 30, 30);

        Label invTitle = new Label("Inventário:");
        invTitle.getStyleClass().add("inventory-title");

        woodLabel = createInventoryLabel();
        stoneLabel = createInventoryLabel();
        foodLabel = createInventoryLabel();

        inventoryBox.getChildren().addAll(
                inventoryIcon,
                invTitle,
                createInventoryItem("/icons/log.png", woodLabel),
                createInventoryItem("/icons/granite.png", stoneLabel),
                createInventoryItem("/icons/beef.png", foodLabel)
        );

        return inventoryBox;
    }

    private HBox createEndDayCostsBox() {
        HBox endDayCostsBox = new HBox(22);
        endDayCostsBox.getStyleClass().add("end-day-costs-box");
        endDayCostsBox.setAlignment(Pos.CENTER_LEFT);
        endDayCostsBox.setPadding(new Insets(12, 20, 12, 20));
        endDayCostsBox.setMaxWidth(Double.MAX_VALUE);

        ImageView costIcon = createIcon("/icons/check.png", 26, 26);

        Label title = new Label("Custos:");
        title.getStyleClass().add("end-day-costs-title");

        endDayWoodLabel = createEndDayCostLabel();
        endDayStoneLabel = createEndDayCostLabel();
        endDayFoodLabel = createEndDayCostLabel();

        endDayCostsBox.getChildren().addAll(
                costIcon,
                title,
                createInventoryItem("/icons/log.png", endDayWoodLabel),
                createInventoryItem("/icons/granite.png", endDayStoneLabel),
                createInventoryItem("/icons/beef.png", endDayFoodLabel)
        );

        return endDayCostsBox;
    }

    private Label createInventoryLabel() {
        Label label = new Label();
        label.getStyleClass().add("inventory-item-text");
        return label;
    }

    private Label createEndDayCostLabel() {
        Label label = new Label();
        label.getStyleClass().add("end-day-costs-text");
        return label;
    }

    private HBox createInventoryItem(String imagePath, Label textLabel) {
        ImageView iconView = createIcon(imagePath, 24, 24);

        HBox box = new HBox(8, iconView, textLabel);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private void refreshDashboardState() {
        dayLabel.setText("Dia: " + SESSION.getDay());
        playerLabel.setText("Jogador atual: " + SESSION.getActualPlayer().getName());
        apLabel.setText("AP: " + SESSION.getActualPlayer().getActionPoints());

        updateEventBar();
        updateInventory();
        updateEndDayCosts();
        populateMapGrid();
    }

    private void updateEventBar() {
        String messageToShow = eventMessage;

        if (messageToShow == null || messageToShow.isBlank()) {
            messageToShow = SESSION.getCurrentEventMessage();
        }

        if (messageToShow == null || messageToShow.isBlank()) {
            eventBar.setVisible(false);
            eventBar.setManaged(false);
            return;
        }

        eventBar.setVisible(true);
        eventBar.setManaged(true);
        eventLabel.setText("Evento: " + messageToShow);
    }

    private void updateInventory() {
        try {
            var player = SESSION.getActualPlayer();

            woodLabel.setText("Madeira: " + player.getInventory().getOrDefault(ResourceType.WOOD, 0));
            stoneLabel.setText("Pedra: " + player.getInventory().getOrDefault(ResourceType.STONE, 0));
            foodLabel.setText("Comida: " + player.getInventory().getOrDefault(ResourceType.FOOD, 0));

        } catch (Exception e) {
            woodLabel.setText("Madeira: 0");
            stoneLabel.setText("Pedra: 0");
            foodLabel.setText("Comida: 0");
        }
    }

    private void updateEndDayCosts() {
        try {
            var costs = SESSION.getActualPlayerEndDayCosts();

            endDayWoodLabel.setText("Madeira: -" + costs.getOrDefault(ResourceType.WOOD, 0));
            endDayStoneLabel.setText("Pedra: -" + costs.getOrDefault(ResourceType.STONE, 0));
            endDayFoodLabel.setText("Comida: -" + costs.getOrDefault(ResourceType.FOOD, 0));

        } catch (Exception e) {
            endDayWoodLabel.setText("Madeira: -0");
            endDayStoneLabel.setText("Pedra: -0");
            endDayFoodLabel.setText("Comida: -0");

            System.out.println("Erro ao atualizar custos no ecrã: " + e.getMessage());
        }
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

    private void loadStyle(Scene scene) {
        try {
            scene.getStylesheets().clear();

            var css = getClass().getResource("/jogo/style.css");

            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
            } else {
                String userDir = System.getProperty("user.dir");
                java.io.File cssFile = new java.io.File(
                        userDir + "/PM_05_2025179383/src/main/resources/jogo/style.css"
                );

                if (cssFile.exists()) {
                    scene.getStylesheets().add(cssFile.toURI().toString());
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao carregar CSS: " + e.getMessage());
        }
    }
}