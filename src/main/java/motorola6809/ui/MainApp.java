package motorola6809.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Créer et afficher la fenêtre principale
        MenuWindow menuWindow = new MenuWindow();
        menuWindow.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

