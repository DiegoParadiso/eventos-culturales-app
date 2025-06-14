package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("App JavaFX funcionando!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
