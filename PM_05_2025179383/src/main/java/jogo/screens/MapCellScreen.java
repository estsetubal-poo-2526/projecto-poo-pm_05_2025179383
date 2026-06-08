package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;

public class MapCellScreen {

    private final Stage POPUP_STAGE;
    private final GameSession SESSION;
    private final int X;
    private final int Y;
    private final Runnable ON_UPDATE_MAP;

    public MapCellScreen(Stage popupStage, GameSession session, int x, int y, Runnable onUpdateMap) {
        this.POPUP_STAGE = popupStage;
        this.SESSION = session;
        this.X = x;
        this.Y = y;
        this.ON_UPDATE_MAP = onUpdateMap;
    }

    // Método mágico para estruturar o botão exatamente como na imagem
    private Button criarBotaoMenu(String texto, String caminhoIcone, boolean isFechar) {
        Button btn = new Button();
        btn.getStyleClass().add("cell-action-btn");

        // Criar o esqueleto de dentro do botão (Esquerda: Ícone, Centro: Texto, Direita: Seta)
        BorderPane conteudoInner = new BorderPane();
        conteudoInner.setMouseTransparent(true); // Evita que cliques nos labels quebrem o botão

        // 1. Ícone da esquerda
        try {
            Image img = new Image(getClass().getResourceAsStream(caminhoIcone));
            ImageView iv = new ImageView(img);
            iv.setFitWidth(32);
            iv.setFitHeight(32);
            iv.setPreserveRatio(true);
            BorderPane.setAlignment(iv, Pos.CENTER);
            BorderPane.setMargin(iv, new Insets(0, 15, 0, 0));
            conteudoInner.setLeft(iv);
        } catch (Exception e) {
            System.out.println("Ícone em falta: " + caminhoIcone);
        }

        // 2. Texto do meio
        Label labelTexto = new Label(texto);
        if (isFechar) {
            labelTexto.getStyleClass().add("cell-close-text");
        } else {
            labelTexto.getStyleClass().add("cell-action-text");
        }
        BorderPane.setAlignment(labelTexto, Pos.CENTER_LEFT);
        conteudoInner.setCenter(labelTexto);

        // 3. Seta da direita (só se não for o botão de fechar)
        if (!isFechar) {
            Label labelSeta = new Label(">");
            labelSeta.getStyleClass().add("cell-action-text");
            labelSeta.setStyle("-fx-text-fill: #9c9284;"); // Seta num tom mais claro cinzento
            BorderPane.setAlignment(labelSeta, Pos.CENTER_RIGHT);
            conteudoInner.setRight(labelSeta);
        }

        // Encaixar o BorderPane todo dentro do botão
        btn.setGraphic(conteudoInner);
        return btn;
    }

    public Scene createScene() {
        // Layout Root em StackPane para aguentar a imagem de fundo atrás de tudo
        StackPane root = new StackPane();
        root.getStyleClass().add("map-cell-root");

        // Ícone de Localização do Topo (aquele pin verde com terra)
        ImageView pinIcon = new ImageView();
        try {
            pinIcon.setImage(new Image(getClass().getResourceAsStream("/icons/circle.png")));
            pinIcon.setFitWidth(60);
            pinIcon.setFitHeight(60);
            pinIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Falta o pin do topo.");
        }

        Label title = new Label("Ações na posição (" + X + ", " + Y + ")");
        title.getStyleClass().add("menu-title");

        // Criar os botões com o novo método artolas
        Button constructStructure = criarBotaoMenu("Construir Estrutura", "/icons/tool.png", false);
        Button upgradeStructure = criarBotaoMenu("Melhorar Estrutura", "/icons/up-arrow.png", false);
        Button getInfoStructure = criarBotaoMenu("Obter Informações da Estrutura", "/icons/information.png", false);
        Button closeButton = criarBotaoMenu("Fechar", "/icons/close.png", true);

        // Ações dos botões (inalteradas para não estragar a tua lógica)
        constructStructure.setOnAction(event -> {
            CreateStructureScreen screen = new CreateStructureScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        upgradeStructure.setOnAction(event -> {
            UpgradeStructureScreen upgradeStructureScreen = new UpgradeStructureScreen(POPUP_STAGE, SESSION);
            POPUP_STAGE.setScene(upgradeStructureScreen.createScene(X, Y));
        });

        getInfoStructure.setOnAction(event -> {
            StructureScreen screen = new StructureScreen(POPUP_STAGE, SESSION, X, Y, ON_UPDATE_MAP);
            POPUP_STAGE.setScene(screen.createScene());
        });

        closeButton.setOnAction(event -> POPUP_STAGE.close());

        // Alinhamento vertical de tudo
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(40));
        menuBox.getStyleClass().add("menu-container"); // Aplica o painel semi-transparente se quiseres

        menuBox.getChildren().addAll(
                pinIcon,
                title,
                constructStructure,
                upgradeStructure,
                getInfoStructure,
                closeButton
        );

        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, 1000, 650); // Ajustei ligeiramente para bater melhor com a proporção da imagem

        try {
            scene.getStylesheets().add(getClass().getResource("/jogo/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Estás a falhar no caminho do CSS outra vez.");
        }

        return scene;
    }
}