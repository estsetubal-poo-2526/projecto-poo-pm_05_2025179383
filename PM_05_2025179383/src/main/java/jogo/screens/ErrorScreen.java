package jogo.screens;

import javafx.scene.control.Alert;

public final class ErrorScreen {

    private ErrorScreen() {
    }

    public static void show(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(exception.getMessage());

        alert.showAndWait();
    }

    public static void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(message);

        alert.showAndWait();
    }
}