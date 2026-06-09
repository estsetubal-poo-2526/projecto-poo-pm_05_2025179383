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
 * Assembles core workspace matrices including navigation sidebars, active player
 * inventory asset monitors, and live operational updates within an in-place refresh layout.
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
    private Label messageLabel;

    private String eventMessage;

    /**
     * Constructs a baseline MainGameScreen sequence without any pending event logs.
     *
     * @param stage   The primary Stage window architecture container.
     * @param session The current active {@link GameSession} core engine state tracker.
     */
    public MainGameScreen(Stage stage, GameSession session) {
        this(stage, session, "");
    }

    /**
     * Constructs an operational MainGameScreen viewport carrying an active event notification banner.
     *
     * @param stage        The primary Stage window architecture container.
     * @param session      The current active {@link GameSession} core engine state tracker.
     * @param eventMessage A log summary text detailing structural updates or match events from the turn.
     */
    public MainGameScreen(Stage stage, GameSession session, String eventMessage) {
        this.STAGE = stage;
        this.SESSION = session;
        this.eventMessage = eventMessage;
    }

    /**
     * Instantiates primary structural viewport nodes, binds sub-menus,
     * triggers data population passes, and wraps everything inside the primary Scene layout.
     *
     * @return A compiled and fully populated JavaFX Scene instance ready for rendering.
     */
    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("main-game-root");

        root.setLeft(createLeftMenu());
        root.setCenter(createCenterArea());
        root.setBottom(createBottomArea());

        refreshDashboardState();

        Scene scene = new Scene(root, 1200, 800);
        loadStyle(scene);

        return scene;
    }

    /**
     * Assembles the left sidebar menu layout filled with structural buttons for actions,
     * serialization triggers, turn finalization, and scene exits.
     */
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
            this.eventMessage = SESSION.endTurn();

            if (SESSION.isGameOver()) {
                EndScreen endScreen = new EndScreen(SESSION);
                STAGE.setScene(endScreen.createScene());
                return;
            }

            // ATUALIZAÇÃO IN-PLACE: Nada de criar ecrãs novos! Atualizamos o que já existe.
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

    /**
     * Factory utility method to assemble structured navigation buttons fitted with custom graphic asset markers.
     */
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

    /**
     * Streamlines streaming input pipelines to safely extract internal icon images from application packages.
     */
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

    /**
     * Compiles central layouts including header information bars and structural maps.
     */
    private VBox createCenterArea() {
        VBox centerContainer = new VBox(20);
        centerContainer.setPadding(new Insets(30, 28, 20, 28));
        centerContainer.setAlignment(Pos.TOP_CENTER);

        StackPane mapPanel = createMapPanel();

        centerContainer.getChildren().addAll(
                createTopArea(),
                mapPanel
        );

        VBox.setVgrow(mapPanel, Priority.ALWAYS);

        return centerContainer;
    }

    /**
     * Assembles status rows mapping match progression indexes, active player identifiers, and action potential thresholds.
     */
    private VBox createTopArea() {
        VBox topContainer = new VBox(12);
        topContainer.setAlignment(Pos.CENTER);

        HBox infoBar = new HBox(80);
        infoBar.getStyleClass().add("top-info-box");
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setPadding(new Insets(18, 40, 18, 40));

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
        eventBar.setPadding(new Insets(14, 28, 14, 28));

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

    /**
     * Parses current matrix configurations to regenerate tile vectors in-place.
     */
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

    /**
     * Assembles interactive location controls, attaching low-opacity underlying
     * structural images to improve background visibility.
     */
    private Button createMapCell(int x, int y) {
        Button cellButton = new Button();

        cellButton.setPrefSize(60, 60);
        cellButton.setMinSize(60, 60);
        cellButton.setMaxSize(60, 60);
        cellButton.getStyleClass().add("map-cell");

        // CAMADA DE OPACIDADE: Criamos uma StackPane interna para empilhar o terreno desbotado e o edifício por cima
        StackPane cellStack = new StackPane();

        try {
            ImageView terrainView = createIcon("/icons/grassland.png", 54, 54);
            // AJUSTE SOLICITADO: Define a opacidade do fundo da célula para 45% para não ofuscar o resto
            terrainView.setOpacity(0.45);
            cellStack.getChildren().add(terrainView);
        } catch (Exception ignore) {}

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
        } catch (Exception ignored) {}

        cellButton.setGraphic(cellStack);
        cellButton.setOnAction(event -> openMapCellPopup(x, y));

        return cellButton;
    }

    /**
     * Opens a modal popup window detailing contextual management choices for a targeted grid cell.
     */
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
                this::refreshDashboardState // Executa a atualização in-place mal o popup feche com ações!
        );

        currentPopupStage.setTitle("Ações na posição " + x + ", " + y);
        currentPopupStage.setScene(mapCellScreen.createScene());
        currentPopupStage.show();
    }

    /**
     * Builds base interface layouts dedicated to active inventory changes.
     */
    private VBox createBottomArea() {
        VBox bottomContainer = new VBox(14);
        bottomContainer.setPadding(new Insets(0, 28, 24, 28));

        HBox inventoryBox = new HBox(34);
        inventoryBox.getStyleClass().add("inventory-box");
        inventoryBox.setAlignment(Pos.CENTER_LEFT);
        inventoryBox.setPadding(new Insets(18, 28, 18, 28));

        ImageView inventoryIcon = createIcon("/icons/backpack.png", 34, 34);

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

        HBox statusBar = new HBox(14);
        statusBar.getStyleClass().add("status-bar");
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(14, 28, 14, 28));

        ImageView checkIcon = createIcon("/icons/check.png", 28, 28);

        messageLabel = new Label("Pronto para o próximo turno!");
        messageLabel.getStyleClass().add("status-message");

        statusBar.getChildren().addAll(checkIcon, messageLabel);

        bottomContainer.getChildren().addAll(inventoryBox, statusBar);

        return bottomContainer;
    }

    private Label createInventoryLabel() {
        Label label = new Label();
        label.getStyleClass().add("inventory-item-text");
        return label;
    }

    private HBox createInventoryItem(String imagePath, Label textLabel) {
        ImageView iconView = createIcon(imagePath, 28, 28);

        HBox box = new HBox(10, iconView, textLabel);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    /**
     * Master synchronization workflow that updates global label matrices and reconstructs
     * grid elements in-place without stage scene flashing.
     */
    private void refreshDashboardState() {
        dayLabel.setText("Dia: " + SESSION.getDay());
        playerLabel.setText("Jogador atual: " + SESSION.getActualPlayer().getName());
        apLabel.setText("AP: " + SESSION.getActualPlayer().getActionPoints());

        updateEventBar();
        updateInventory();
        populateMapGrid(); // Reconstrói apenas os botões do mapa!
    }

    private void updateEventBar() {
        if (eventMessage == null || eventMessage.isBlank()) {
            eventBar.setVisible(false);
            eventBar.setManaged(false);
            return;
        }

        eventBar.setVisible(true);
        eventBar.setManaged(true);
        eventLabel.setText("Evento: " + eventMessage);
    }

    /**
     * Extracts values from the active player's resource map to safely populate inventory indicators.
     */
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

    /**
     * Dispatches current structural boards, player metrics, and session markers to persistent storage files.
     */
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

    /**
     * Resolves the primary stylesheet, using absolute disk path resolution as a fallback if necessary.
     */
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