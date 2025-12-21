package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import motorola6809.integration.SimulatorBridge;

public class EditeurWindow {
    
    private Stage stage;
    private TextArea textArea;
    private Button execBtn;
    private Button pasAPasBtn;
    private Button newBtn;
    private SimulatorBridge bridge;
    
    public EditeurWindow() {
        stage = new Stage();
        stage.setTitle("EDITEUR");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        
        VBox root = new VBox(5);
        root.setPadding(new Insets(5));
        root.setPrefSize(250, 300);
        
        HBox buttonBox1 = new HBox(5);
        newBtn = new Button("New");
        newBtn.setPrefWidth(240);
        newBtn.setOnAction(e -> clearAll());
        buttonBox1.getChildren().add(newBtn);
        
        HBox buttonBox2 = new HBox(5);
        pasAPasBtn = new Button("Pas à Pas");
        pasAPasBtn.setPrefWidth(115);
        pasAPasBtn.setOnAction(e -> executeStepByStep());
        
        execBtn = new Button("Exécuter");
        execBtn.setPrefWidth(115);
        execBtn.setOnAction(e -> executeProgram());
        
        buttonBox2.getChildren().addAll(pasAPasBtn, execBtn);
        
        textArea = new TextArea();
        textArea.setFont(javafx.scene.text.Font.font("Monospaced", 12));
        textArea.setPrefHeight(230);
        
        // Exemple de code par défaut
        textArea.setText("; Programme exemple\nORG $1400\n\nSTART:\n    LDA #$05\n    LDB #$03\n    MUL\n    NOP\nEND");
        
        root.getChildren().addAll(buttonBox1, buttonBox2, textArea);
        
        Scene scene = new Scene(root, 250, 300);
        stage.setScene(scene);
        stage.setX(1000);
        stage.setY(120);
        
        // Initialisation du bridge
        this.bridge = new SimulatorBridge(this);
    }
    
    private void clearAll() {
        textArea.clear();
        execBtn.setText("Exécuter");
        execBtn.setDisable(false);
        pasAPasBtn.setDisable(false);
        ArchitectureWindow.resetValues();
        ProgrammeWindow.clear();
        bridge.reset();
    }
    
    private void executeStepByStep() {
        try {
            // Compile si nécessaire
            if (!bridge.isProgramLoaded()) {
                String code = textArea.getText();
                if (code.trim().isEmpty()) {
                    showAlert("Erreur", "Le code est vide !");
                    return;
                }
                
                System.out.println("=== COMPILATION ===");
                System.out.println(code);
                
                boolean success = bridge.compileAndLoad();
                if (!success) {
                    showAlert("Erreur de compilation", "Vérifiez votre code assembleur");
                    return;
                }
            }
            
            // Exécute une instruction
            System.out.println("=== EXECUTION PAS A PAS ===");
            boolean executed = bridge.executeStep();
            
            if (!executed) {
                showAlert("Fin du programme", "Le programme est terminé (SWI)");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }
    
    private void executeProgram() {
        try {
            // Si pas encore chargé, compile
            if (!bridge.isProgramLoaded()) {
                String code = textArea.getText();
                if (code.trim().isEmpty()) {
                    showAlert("Erreur", "Le code est vide !");
                    return;
                }
                
                boolean success = bridge.compileAndLoad();
                if (!success) {
                    showAlert("Erreur de compilation", "Vérifiez votre code assembleur");
                    return;
                }
            }
            
            // Basculer entre exécution et arrêt
            if (bridge.isRunning()) {
                bridge.stop();
                execBtn.setText("Exécuter");
            } else {
                bridge.executeAll();
                execBtn.setText("Arrêter");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    
    public String getText() {
        return textArea.getText();
    }
}