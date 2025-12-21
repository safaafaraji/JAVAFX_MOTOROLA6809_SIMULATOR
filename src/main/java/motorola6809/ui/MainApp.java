package motorola6809.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Créer et afficher le menu principal
        MenuWindow menuWindow = new MenuWindow();
        menuWindow.show();
        
        // Créer et afficher l'architecture interne
        ArchitectureWindow archWindow = new ArchitectureWindow();
        archWindow.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
