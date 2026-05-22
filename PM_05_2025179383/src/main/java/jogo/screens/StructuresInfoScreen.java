package jogo.screens;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.models.Structures.CreateStructure;
import jogo.models.Structures.Structures;
import jogo.models.Structures.StructuresType;

public class StructuresInfoScreen {

    private GameSession session;
    private Stage stage;

    public StructuresInfoScreen(GameSession session, Stage stage) {
        this.stage = stage;
        this.session = session;
    }

    public Scene createScreen(Structures type){

        Button button = new Button("Voltar");

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(new Label("Imagem"));
        borderPane.setCenter(center(type));
        borderPane.setBottom(button);

        button.setOnAction(event -> {
            MainGameScreen mainGameScreen = new MainGameScreen(stage,session);
        });

        return new Scene(borderPane);

    }

    public VBox center(Structures type){

        Label name = new Label("Nome: " + type.getName());
        Label structureCost = new Label("Custo de Manutenção: " + type.getExpense() + " " + type.getCostType());
        Label production = new Label("Producão Diaria: " + type.getProfit() + " " + type.getProduction());

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(name,structureCost,production);

        return vBox;



    }
}
