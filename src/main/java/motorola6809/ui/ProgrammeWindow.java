package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class ProgrammeWindow {
    
    private Stage stage;
    private static TextArea textArea;
    // Supprimé lineCount car non utilisé actuellement
    
    public ProgrammeWindow() {
        stage = new Stage();
        stage.setTitle("Programme");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        
        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(javafx.scene.text.Font.font("Monospaced", 12));
        textArea.setPrefSize(230, 250);
        
        root.getChildren().add(textArea);
        
        Scene scene = new Scene(root, 250, 300);
        stage.setScene(scene);
        stage.setX(280);
        stage.setY(120);
    }
    
    public static void displayInstructions(int address, String instruction) {
        String addrStr = String.format("%04X", address);
        textArea.appendText(" " + addrStr + "\t" + instruction + "\n");
    }
    
    public static void displayInstructions(String instruction) {
        textArea.appendText("     \t" + instruction + "\n");
    }
    
    public static void clear() {
        textArea.clear();
    }
    
    public void show() {
        stage.show();
    }
    
    public void hide() {
        stage.hide();
    }
    
    public boolean isShowing() {
        return stage.isShowing();
    }
}
