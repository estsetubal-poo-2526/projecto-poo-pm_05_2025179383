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
import jogo.models.Structures.Structures;

public class UpgradeStructureScreen {

    private final Stage STAGE;
    private final GameSession SESSION;

    private Label messageLabel;
    private HBox successNotificationBar;
    private VBox bottomLayout;

    public UpgradeStructureScreen(Stage stage, GameSession session) {
        this.SESSION = session;
        this.STAGE = stage;
    }

    public Scene createScene(int x, int y) {
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("upgrade-container");

        messageLabel = new Label();

        try {
            // Desenha os componentes principais
            borderPane.setLeft(left(x, y));
            borderPane.setCenter(center(x, y));
            borderPane.setBottom(bottom(x, y));
        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
            Button goBack = new Button("Voltar");
            goBack.getStyleClass().add("btn-back");
            goBack.setOnAction(event -> goBackToMainGameScreen());

            BorderPane errorPane = new BorderPane(goBack);
            errorPane.setPadding(new Insets(50));
            return new Scene(errorPane, 400, 300);
        }

        Scene scene = new Scene(borderPane, 1200, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("/jogo/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS não encontrado.");
        }
        return scene;
    }

    private VBox left(int x, int y) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0, 40, 0, 10));

        // Tenta carregar a imagem dinamicamente baseada no nome da estrutura
        try {
            String structureName = getStructureMap(x, y).getClass().getSimpleName().toLowerCase();
            Image buildingImg = new Image(getClass().getResourceAsStream("/icons/" + structureName + ".png"));
            ImageView iv = new ImageView(buildingImg);
            iv.setFitWidth(320);
            iv.setFitHeight(320);
            iv.setPreserveRatio(true);

            // Container para dar a borda arredondada à imagem
            VBox imgWrapper = new VBox(iv);
            imgWrapper.getStyleClass().add("building-preview");
            vBox.getChildren().add(imgWrapper);
        } catch (Exception e) {
            // Imagem genérica se falhar
            Label label = new Label("[Imagem da Estrutura]");
            vBox.getChildren().add(label);
        }

        return vBox;
    }

    private VBox center(int x, int y) throws GameException {
        Label title = new Label("Melhorar Estrutura");
        title.getStyleClass().add("upgrade-title");

        // Criar a tabela de atributos usando um GridPane
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(30, 0, 30, 0));

        // Linha 1: Nível
        adicionarLinhaGrid(grid, 0, "/icons/star.png", "Nível atual:",
                String.valueOf(getLevelStructure(x, y)), String.valueOf(getLevelStructure(x, y) + 1), false, false);

        // Linha 2: Despesa
        adicionarLinhaGrid(grid, 1, "/icons/dollar.png", "Despesa atual:",
                getActualExpense(x, y), getFutureExpense(x, y), false, false);

        // Linha 3: Produção
        adicionarLinhaGrid(grid, 2, "/icons/forest.png", "Produção atual:",
                getActualProduction(x, y), getFutureProduction(x, y), false, false);

        // Linha 4: Custo de Melhoria (Aqui passamos como custo/vermelho)
        adicionarLinhaGrid(grid, 3, "/icons/tool.png", "Custo de melhoria:",
                getCostToUpgrade(x, y), "", true, true);

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER_LEFT);
        centerBox.setPadding(new Insets(20));
        centerBox.getChildren().addAll(title, grid);

        return centerBox;
    }

    private void adicionarLinhaGrid(GridPane grid, int row, String iconPath, String labelText, String oldVal, String newVal, boolean isCost, boolean isSingleValue) {
        // Coluna 0: Ícone do atributo
        try {
            ImageView iv = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            iv.setFitWidth(24); iv.setFitHeight(24); iv.setPreserveRatio(true);
            grid.add(iv, 0, row);
        } catch (Exception e) {}

        // Coluna 1: Nome do Atributo
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("attr-label");
        grid.add(lbl, 1, row);

        // Coluna 2: Valor Antigo / Atual
        Label lblOld = new Label(oldVal);
        if (isCost) {
            lblOld.getStyleClass().add("value-cost");
        } else {
            lblOld.getStyleClass().add("value-old");
        }
        grid.add(lblOld, 2, row);

        // Se for uma linha com "Antes -> Depois", adiciona a seta e o valor novo
        if (!isSingleValue) {
            // Coluna 3: Seta
            Label arrow = new Label("→");
            arrow.getStyleClass().add("value-old");
            arrow.setStyle("-fx-text-fill: #94a3b8;");
            grid.add(arrow, 3, row);

            // Coluna 4: Valor Novo
            Label lblNew = new Label(newVal);
            lblNew.getStyleClass().add("value-new");
            grid.add(lblNew, 4, row);
        }
    }

    private VBox bottom(int x, int y) {
        bottomLayout = new VBox(20);
        bottomLayout.setAlignment(Pos.CENTER);
        bottomLayout.setPadding(new Insets(20, 0, 0, 0));

        Button upgrade = new Button("Melhorar");
        upgrade.getStyleClass().add("btn-upgrade");
        try {
            ImageView upIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/up-arrow.png")));
            upIcon.setFitWidth(18); upIcon.setFitHeight(18);
            upgrade.setGraphic(upIcon);
            upgrade.setGraphicTextGap(8);
        } catch (Exception e) {}

        Button goBack = new Button("Voltar");
        goBack.getStyleClass().add("btn-back");

        upgrade.setOnAction(event -> upgradeStructure(x, y));
        goBack.setOnAction(event -> goBackToMainGameScreen());

        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(upgrade, goBack);

        bottomLayout.getChildren().add(buttonsBox);

        // Preparar a barra de notificação de sucesso oculta
        successNotificationBar = new HBox(10);
        successNotificationBar.getStyleClass().add("success-bar");
        successNotificationBar.setAlignment(Pos.CENTER);
        try {
            ImageView checkIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/log.png")));
            checkIcon.setFitWidth(20); checkIcon.setFitHeight(20);
            successNotificationBar.getChildren().add(checkIcon);
        } catch (Exception e) {}

        messageLabel.getStyleClass().add("success-text");
        successNotificationBar.getChildren().add(messageLabel);

        // Se a mensagem já tiver conteúdo (devido ao refresh), exibe a barra
        if (messageLabel.getText() != null && !messageLabel.getText().isEmpty()) {
            bottomLayout.getChildren().add(successNotificationBar);
        }

        return bottomLayout;
    }

    private void upgradeStructure(int x, int y) {
        try {
            GameEngine.upgradeStructure(
                    SESSION.getMap(),
                    SESSION.getActualPlayer(),
                    x,
                    y,
                    SESSION.getScoreModifier()
            );

            // Recarrega o ecrã, mas injeta a mensagem de sucesso
            UpgradeStructureScreen nextScreen = new UpgradeStructureScreen(STAGE, SESSION);
            Scene nextScene = nextScreen.createScene(x, y);
            nextScreen.messageLabel.setText("Pronto para melhorar a estrutura!");

            STAGE.setScene(nextScene);

        } catch (GameException e) {
            ErrorPopUp.show(e.getMessage());
        }
    }

    private void goBackToMainGameScreen() {
        MainGameScreen mainGameScreen = new MainGameScreen(STAGE, SESSION);
        STAGE.setScene(mainGameScreen.createScene());
    }

    private Structures getStructureMap(int x, int y) throws GameException {
        Structures structure = SESSION.getMap().getStructure(x, y);
        if (structure == null) {
            throw new GameException("Não existe nenhuma estrutura nesta posição.");
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