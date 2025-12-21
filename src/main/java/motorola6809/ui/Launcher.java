package motorola6809.ui;

public class Launcher {
    public static void main(String[] args) {
        // Désactiver le lancement direct de JavaFX
        System.setProperty("javafx.preloader", "none");
        MainApp.main(args);
    }
}