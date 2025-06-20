package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("Gestor de Eventos");
        primaryStage.setScene(scene);
        
        // ✅ Pantalla completa
        primaryStage.setMaximized(true); // Recomendado
        // primaryStage.setFullScreen(true); // Alternativa si querés ocultar hasta la barra del sistema

        primaryStage.show();
    }

    public static void main(String[] args) {
        
        launch(args);
    }
}
