package jogo;

import javafx.application.Application;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.screens.StartScreen;

/**
 * The main entry point for the JavaFX application.
 * Initializes the core engine and bootstraps the primary
 * stage with the initial game launch screen.
 *
 * @author Fabio Cruz
 * @author Tiago Silva
 */
public class Main extends Application {

    /**
     * Confirms the primary JavaFX stage boundaries, hooks the initial game session state,
     * and renders the startup environment scene.
     *
     * @param stage The primary Stage container provided by the JavaFX runtime environment.
     */
    @Override
    public void start(Stage stage) {
        GameSession session = new GameSession();

        StartScreen startScreen = new StartScreen(stage, session);

        stage.setTitle("Jogo");
        stage.setScene(startScreen.createScene());
        stage.show();
    }

    /**
     * Standard Java main method used as a fallback mechanism to trigger
     * the underlying JavaFX runtime launch sequence.
     *
     * @param args The command-line arguments passed during application execution.
     */
    public static void main(String[] args) {
        launch(args);
    }
}