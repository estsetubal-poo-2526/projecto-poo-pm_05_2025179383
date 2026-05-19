

import javafx.application.Application;
import javafx.stage.Stage;
import jogo.engine.GameSession;
import jogo.screens.StartScreen;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        GameSession session = new GameSession();

        StartScreen startScreen = new StartScreen(stage, session);

        stage.setTitle("Jogo");
        stage.setScene(startScreen.createScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}