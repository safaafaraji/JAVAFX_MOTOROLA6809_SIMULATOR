package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import motorola6809.core.SimulatorBackend;
import motorola6809.assembler.Assembler;
import javafx.scene.text.Font;

public class EditeurWindow {
    
    private Stage stage;
    private TextArea textArea;
    private Button execBtn;
    private Button pasAPasBtn;
    private Button newBtn;
    private Button assembleBtn;
    private SimulatorBackend backend;
    
    public EditeurWindow() {
        stage = new Stage();
        stage.setTitle("Éditeur Assembleur 6809");
        stage.setResizable(true);
        this.backend = SimulatorBackend.getInstance();

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setPrefSize(600, 500);
        
        // Titre
        Label title = new Label("Éditeur de Code Assembleur Motorola 6809");
        title.setFont(Font.font("Arial", 16));
        
        // Barre d'outils
        HBox toolbar = new HBox(10);
        
        newBtn = createToolbarButton("Nouveau", "#3498db");
        newBtn.setOnAction(e -> newFile());
        
        assembleBtn = createToolbarButton("Assembler", "#2ecc71");
        assembleBtn.setOnAction(e -> assembleCode());
        
        pasAPasBtn = createToolbarButton("Pas à pas", "#f39c12");
        pasAPasBtn.setOnAction(e -> stepExecution());
        
        execBtn = createToolbarButton("Exécuter", "#e74c3c");
        execBtn.setOnAction(e -> toggleExecution());
        
        Button resetBtn = createToolbarButton("Reset", "#9b59b6");
        resetBtn.setOnAction(e -> resetSimulator());
        
        toolbar.getChildren().addAll(newBtn, assembleBtn, pasAPasBtn, execBtn, resetBtn);
        
        // Zone de code
        textArea = new TextArea();
        textArea.setFont(Font.font("Monospaced", 13));
        textArea.setPrefHeight(400);
        
        // Exemple minimal
        textArea.setText(
            "; =======================================\n" +
            "; Programme Motorola 6809\n" +
            "; =======================================\n" +
            "\n" +
            "        ORG $1000           ; Adresse de départ\n" +
            "\n" +
            "START   LDA #$05           ; Charger 5 dans A\n" +
            "        LDB #$03           ; Charger 3 dans B\n" +
            "        MUL                ; Multiplier A * B -> D\n" +
            "        STA $2000          ; Stocker résultat\n" +
            "        SWI                ; Retour au système\n" +
            "\n" +
            "        END                ; Fin du programme\n"
        );
        
        // Status bar
        HBox statusBar = new HBox();
        Label statusLabel = new Label("Prêt");
        statusLabel.setFont(Font.font("Arial", 11));
        statusBar.getChildren().add(statusLabel);
        
        root.getChildren().addAll(title, toolbar, textArea, statusBar);
        
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        
        // Initialisation du backend
        this.backend = SimulatorBackend.getInstance();
        
        // Positionnement
        stage.setX(100);
        stage.setY(100);
    }
    
    private Button createToolbarButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                    "-fx-background-radius: 4; -fx-padding: 8 15 8 15;");
        btn.setFont(Font.font("Arial", 12));
        return btn;
    }
    
    private void newFile() {
        textArea.clear();
        showAlert("Nouveau fichier", "Prêt pour un nouveau programme.");
    }
    
    private void assembleCode() {
        String code = textArea.getText();
        
        if (code.trim().isEmpty()) {
            showAlert("Erreur", "Le code source est vide.");
            return;
        }
        
        try {
            boolean success = backend.assemble(code);
            if (success) {
                showAlert("Succès", "Programme assemblé avec succès.\n" +
                                  "Taille: " + backend.getCurrentProgram().length + " octets");
            } else {
                showAlert("Erreur", "Échec de l'assemblage.");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Exception: " + e.getMessage());
        }
    }
    
    private void stepExecution() {
        if (backend.getCurrentProgram() == null) {
            showAlert("Erreur", "Aucun programme assemblé. Utilisez 'Assembler' d'abord.");
            return;
        }
        
        backend.step();
        showAlert("Pas à pas", "Instruction exécutée.\nPC: $" + 
                  SimulatorBackend.formatHex16(backend.getPC()));
    }
    
    private void toggleExecution() {
        if (backend.getCurrentProgram() == null) {
            showAlert("Erreur", "Aucun programme assemblé.");
            return;
        }
        
        if (backend.isRunning()) {
            backend.stop();
            execBtn.setText("Exécuter");
        } else {
            backend.start();
            execBtn.setText("Arrêter");
        }
    }
    
    private void resetSimulator() {
        backend.reset();
        execBtn.setText("Exécuter");
        showAlert("Reset", "Simulateur réinitialisé.");
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