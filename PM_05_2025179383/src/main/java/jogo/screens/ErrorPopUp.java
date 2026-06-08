package jogo.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class ErrorPopUp {

    private ErrorPopUp() {
    }

    public static void show(String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Erro");


        ImageView errorIcon = new ImageView();
        try {
            Image img = new Image(ErrorPopUp.class.getResourceAsStream("/icons/delete.png"));
            errorIcon.setImage(img);
            errorIcon.setFitWidth(100);
            errorIcon.setFitHeight(100);
            errorIcon.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Imagem de erro não encontrada.");
        }


        Label titleLabel = new Label("Ocorreu um erro");
        titleLabel.getStyleClass().add("popup-title");

        Label messageLabel = new Label(message + "\nTente novamente.");
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.getStyleClass().add("error-message-text");

        VBox messageBox = new VBox(messageLabel);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(15, 20, 15, 20));
        messageBox.setMaxWidth(380);
        messageBox.getStyleClass().add("error-message-box");

        Button closeButton = new Button("Fechar");
        closeButton.getStyleClass().add("btn-close");

        try {
            Image xImg = new Image(ErrorPopUp.class.getResourceAsStream("/icons/close.png"));
            ImageView xIcon = new ImageView(xImg);
            xIcon.setFitWidth(14);
            xIcon.setFitHeight(14);
            closeButton.setGraphic(xIcon);
            closeButton.setGraphicTextGap(8);
        } catch (Exception e) {
            closeButton.setText("X  Fechar");
        }

        closeButton.setOnAction(event -> popupStage.close());

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("popup-root");
        root.getChildren().addAll(errorIcon, titleLabel, messageBox, closeButton);

        Scene scene = new Scene(root, 480, 420);

        try {
            String cssPath = ErrorPopUp.class.getResource("/jogo/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.out.println("Parabéns, erraste no caminho do CSS: /css/styles.css");
        }

        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }
}