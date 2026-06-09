package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameEngine;
import jogo.engine.GameSession;
import jogo.exceptions.GameException;
import jogo.exceptions.StructureDontExistException;
import jogo.models.Structures.Structures;

import java.util.Objects;

/**
 * Controller and view class responsible for rendering structural upgrade paths and statistics comparisons.
 * Displays dynamic data matrix grids comparing current properties with prospective tier outputs,
 * and handles in-place visual updates upon successful backend engine upgrades.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class UpgradeStructureScreen {

    private final Stage STAGE;
    private final GameSession SESSION;
    private final Runnable ON_UPDATE_MAP;

    private BorderPane borderPane;
    private Label messageLabel;
    private HBox successNotificationBar;
    private VBox bottomLayout;

    /**
     * Constructs an operational structure upgrade panel framework view.
     *
     * @param stage       The auxiliary Stage context window framework.
     * @param session     The master engine {@link GameSession} instance tracking global variables.
     * @param onUpdateMap A callback payload layout task triggered to re-render the parent grid interface.
     */
    public UpgradeStructureScreen(Stage stage, GameSession session, Runnable onUpdateMap) {
        this.SESSION = session;
        this.STAGE = stage;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    /**
     * Builds and structures baseline sub-panels components, handles runtime query exceptions safely,
     * and maps node elements inside an optimized viewport Scene bounds.
     *
     * @param x The target horizontal matrix map tile alignment coordinate.
     * @param y The target vertical matrix map tile alignment coordinate.
     * @return A compiled JavaFX Scene representing the upgrade comparative terminal dashboard.
     */
    public Scene createScene(int x, int y) {
        borderPane = new BorderPane();
        borderPane.getStyleClass().add("upgrade-container");

        messageLabel = new Label();

        try {
            borderPane.setLeft(left(x, y));
            borderPane.setCenter(center(x, y));
            borderPane.setBottom(bottom(x, y));
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
            closeAndRefresh();
            return new Scene(new BorderPane(), 1, 1);
        }

        Scene scene = new Scene(borderPane, 1200, 800);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/jogo/style.css")).toExternalForm());
        } catch (Exception ignore) {
        }
        return scene;
    }

    /**
     * Assembles the left side panel housing a stylized graphical snapshot bounding the target structural building.
     */
    private VBox left(int x, int y) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 40, 0, 10));

        try {
            String structureName = getStructureMap(x, y).getClass().getSimpleName().toLowerCase();
            Image buildingImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/" + structureName + ".png")));
            ImageView iv = new ImageView(buildingImg);
            iv.setFitWidth(320);
            iv.setFitHeight(320);
            iv.setPreserveRatio(true);

            VBox imgWrapper = new VBox(iv);
            imgWrapper.getStyleClass().add("building-preview");
            vBox.getChildren().add(imgWrapper);
        } catch (Exception e) {
            Label label = new Label("[Imagem da Estrutura]");
            vBox.getChildren().add(label);
        }

        return vBox;
    }

    /**
     * Compiles historical and future forecast statistics side-by-side using tabular matrix rows.
     */
    private VBox center(int x, int y) throws GameException {
        Label title = new Label("Melhorar Estrutura");
        title.getStyleClass().add("upgrade-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(30, 0, 30, 0));

        addGridRow(grid, 0, "/icons/star.png", "Nível atual:",
                String.valueOf(getLevelStructure(x, y)), String.valueOf(getLevelStructure(x, y) + 1), false, false);

        addGridRow(grid, 1, "/icons/dollar.png", "Despesa atual:",
                getActualExpense(x, y), getFutureExpense(x, y), false, false);

        addGridRow(grid, 2, "/icons/forest.png", "Produção atual:",
                getActualProduction(x, y), getFutureProduction(x, y), false, false);

        addGridRow(grid, 3, "/icons/tool.png", "Custo de melhoria:",
                getCostToUpgrade(x, y), "", true, true);

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER_LEFT);
        centerBox.setPadding(new Insets(20));
        centerBox.getChildren().addAll(title, grid);

        return centerBox;
    }

    /**
     * Intermediary matrix inline grid line builder that handles vector symbols,
     * comparative indicators, and value style transformations.
     */
    private void addGridRow(GridPane grid, int row, String iconPath, String labelText, String oldVal, String newVal, boolean isCost, boolean isSingleValue) {
        try {
            ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            iv.setFitWidth(24); iv.setFitHeight(24); iv.setPreserveRatio(true);
            grid.add(iv, 0, row);
        } catch (Exception ignore) {
        }

        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("attr-label");
        grid.add(lbl, 1, row);

        Label lblOld = new Label(oldVal);
        if (isCost) {
            lblOld.getStyleClass().add("value-cost");
        } else {
            lblOld.getStyleClass().add("value-old");
        }
        grid.add(lblOld, 2, row);

        if (!isSingleValue) {
            Label arrow = new Label("→");
            arrow.getStyleClass().add("value-old");
            arrow.setStyle("-fx-text-fill: #94a3b8;");
            grid.add(arrow, 3, row);

            Label lblNew = new Label(newVal);
            lblNew.getStyleClass().add("value-new");
            grid.add(lblNew, 4, row);
        }
    }

    /**
     * Renders base configurations including functional validation controls and transactional success indicators.
     */
    private VBox bottom(int x, int y) {
        bottomLayout = new VBox(20);
        bottomLayout.setAlignment(Pos.CENTER);
        bottomLayout.setPadding(new Insets(20, 0, 0, 0));

        Button upgrade = new Button("Melhorar");
        upgrade.getStyleClass().add("btn-upgrade");
        try {
            ImageView upIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/up-arrow.png"))));
            upIcon.setFitWidth(18); upIcon.setFitHeight(18);
            upgrade.setGraphic(upIcon);
            upgrade.setGraphicTextGap(8);
        } catch (Exception ignore) {
        }

        Button goBack = new Button("Voltar");
        goBack.getStyleClass().add("btn-back");

        upgrade.setOnAction(event -> upgradeStructure(x, y));
        goBack.setOnAction(event -> closeAndRefresh());

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(upgrade, goBack);

        bottomLayout.getChildren().add(buttonsBox);

        successNotificationBar = new HBox(10);
        successNotificationBar.getStyleClass().add("success-bar");
        successNotificationBar.setAlignment(Pos.CENTER);
        try {
            ImageView checkIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/log.png"))));
            checkIcon.setFitWidth(20); checkIcon.setFitHeight(20);
            successNotificationBar.getChildren().add(checkIcon);
        } catch (Exception ignore) {
        }

        messageLabel.getStyleClass().add("success-text");
        successNotificationBar.getChildren().add(messageLabel);

        if (messageLabel.getText() != null && !messageLabel.getText().isEmpty()) {
            bottomLayout.getChildren().add(successNotificationBar);
        }

        return bottomLayout;
    }

    /**
     * Dispatches evolutionary data parameters onto backend calculation layers.
     * Updates the current view layout in-place with refreshed structural specifications
     * instead of re-instantiating structural view controllers.
     */
    private void upgradeStructure(int x, int y) {
        try {
            GameEngine.upgradeStructure(
                    SESSION.getMap(),
                    SESSION.getActualPlayer(),
                    x,
                    y,
                    SESSION.getScoreModifier()
            );

            borderPane.setCenter(center(x, y));

            messageLabel.setText("Estrutura melhorada com sucesso!");
            if (!bottomLayout.getChildren().contains(successNotificationBar)) {
                bottomLayout.getChildren().add(successNotificationBar);
            }

            if (ON_UPDATE_MAP != null) {
                ON_UPDATE_MAP.run();
            }

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }

    /**
     * Executes external graphical state sync payloads before disposing of active display containers.
     */
    private void closeAndRefresh() {
        if (ON_UPDATE_MAP != null) {
            ON_UPDATE_MAP.run();
        }
        STAGE.close();
    }

    private Structures getStructureMap(int x, int y) throws GameException {
        Structures structure = SESSION.getMap().getStructure(x, y);
        if (structure == null) {
            throw new StructureDontExistException();
        }
        return structure;
    }

    public int getLevelStructure(int x, int y) throws GameException {
        return getStructureMap(x, y).getLevel();
    }

    public String getActualExpense(int x, int y) throws GameException {
        return getStructureMap(x, y).getExpense() + " " + getStructureMap(x, y).getCostType();
    }

    public String getFutureExpense(int x, int y) throws GameException {
        return getStructureMap(x, y).getFutureExpense() + " " + getStructureMap(x, y).getCostType();
    }

    public String getActualProduction(int x, int y) throws GameException {
        return getStructureMap(x, y).getProfit() + " " + getStructureMap(x, y).getProduction();
    }

    public String getFutureProduction(int x, int y) throws GameException {
        return getStructureMap(x, y).getFutureProfit() + " " + getStructureMap(x, y).getProduction();
    }

    public String getCostToUpgrade(int x, int y) throws GameException {
        return getStructureMap(x, y).getUpgradeCost() + " " + getStructureMap(x, y).getUpgradeMaterial();
    }
}