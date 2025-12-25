package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import motorola6809.core.SimulatorBackend;
import javafx.geometry.*;

public class DebugWindow {
    
    private Stage stage;
    private SimulatorBackend backend;
    private ListView<String> breakpointList;
    private TextField breakpointField;
    
    public DebugWindow(SimulatorBackend backend) {
        this.backend = backend;
        this.stage = new Stage();
        stage.setTitle("Débogueur 6809");
        
        createUI();
    }
    
    private void createUI() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        // Breakpoints
        Label bpLabel = new Label("Breakpoints");
        bpLabel.setStyle("-fx-font-weight: bold;");
        
        breakpointList = new ListView<>();
        breakpointList.setPrefHeight(150);
        
        HBox bpControls = new HBox(10);
        breakpointField = new TextField();
        breakpointField.setPromptText("Adresse hex (ex: 1000)");
        
        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(e -> {
            try {
                String text = breakpointField.getText().trim();
                if (text.startsWith("$")) text = text.substring(1);
                int address = Integer.parseInt(text, 16);
                backend.addBreakpoint(address);
                updateBreakpointList();
                breakpointField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Adresse invalide. Utilisez hex (ex: 1000 ou $1000)");
            }
        });
        
        Button removeBtn = new Button("Retirer");
        removeBtn.setOnAction(e -> {
            String selected = breakpointList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String addrStr = selected.split(" ")[0].substring(1); // "$1000" -> "1000"
                int address = Integer.parseInt(addrStr, 16);
                backend.removeBreakpoint(address);
                updateBreakpointList();
            }
        });
        
        Button clearBtn = new Button("Tout effacer");
        clearBtn.setOnAction(e -> {
            backend.clearBreakpoints();
            updateBreakpointList();
        });
        
        bpControls.getChildren().addAll(breakpointField, addBtn, removeBtn, clearBtn);
        
        // Registres rapides
        Label regLabel = new Label("État CPU");
        regLabel.setStyle("-fx-font-weight: bold;");
        
        GridPane regGrid = new GridPane();
        regGrid.setHgap(10);
        regGrid.setVgap(5);
        
        // Vous pouvez ajouter des champs pour voir les registres en temps réel
        
        root.getChildren().addAll(bpLabel, breakpointList, bpControls, regLabel, regGrid);
        
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        
        // Mettre à jour initialement
        updateBreakpointList();
    }
    
    private void updateBreakpointList() {
        breakpointList.getItems().clear();
        for (int bp : backend.getBreakpoints()) {
            breakpointList.getItems().add("$" + String.format("%04X", bp));
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void show() {
        stage.show();
    }
}