package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import motorola6809.core.SimulatorBackend;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemoryWindow implements SimulatorBackend.SimulatorObserver {
    
    private Stage stage;
    private SimulatorBackend backend;
    private TableView<MemoryRow> tableView;
    private ObservableList<MemoryRow> memoryData;
    private int startAddress = 0x0000;
    private boolean autoRefresh = true;
    
    public MemoryWindow(SimulatorBackend backend) {
        this.backend = backend;
        this.stage = new Stage();
        this.memoryData = FXCollections.observableArrayList();
        
        backend.addObserver(this);
        createUI();
        refreshMemory();
    }
    
    private void createUI() {
        stage.setTitle("Mémoire 6809 - RAM/ROM");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.setPrefSize(800, 600);
        
        // Contrôles
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Vue Mémoire");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        
        TextField addressField = new TextField("0000");
        addressField.setPrefWidth(80);
        addressField.setPromptText("Adresse hex");
        
        Button goBtn = new Button("Aller à");
        goBtn.setOnAction(e -> {
            try {
                String text = addressField.getText().trim();
                if (text.startsWith("$")) text = text.substring(1);
                startAddress = Integer.parseInt(text, 16);
                refreshMemory();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Adresse invalide");
            }
        });
        
        Button refreshBtn = new Button("🔄 Rafraîchir");
        refreshBtn.setOnAction(e -> refreshMemory());
        
        Button autoBtn = new Button("✅ Auto-refresh");
        autoBtn.setOnAction(e -> {
            autoRefresh = !autoRefresh;
            autoBtn.setText(autoRefresh ? "✅ Auto-refresh" : "⭕ Auto-refresh");
        });
        
        controls.getChildren().addAll(title, new Label("Adresse:"), addressField, 
                                     goBtn, refreshBtn, autoBtn);
        
        // Table de mémoire
        tableView = new TableView<>();
        tableView.setPrefHeight(500);
        
        // Colonnes
        TableColumn<MemoryRow, String> addrCol = new TableColumn<>("Adresse");
        addrCol.setPrefWidth(80);
        addrCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        
        TableColumn<MemoryRow, String> hexCol = new TableColumn<>("Hex");
        hexCol.setPrefWidth(300);
        hexCol.setCellValueFactory(cell -> cell.getValue().hexProperty());
        
        TableColumn<MemoryRow, String> asciiCol = new TableColumn<>("ASCII");
        asciiCol.setPrefWidth(200);
        asciiCol.setCellValueFactory(cell -> cell.getValue().asciiProperty());
        
        TableColumn<MemoryRow, String> decCol = new TableColumn<>("Décimal");
        decCol.setPrefWidth(100);
        decCol.setCellValueFactory(cell -> cell.getValue().decimalProperty());
        
        tableView.getColumns().addAll(addrCol, hexCol, asciiCol, decCol);
        tableView.setItems(memoryData);
        
        root.getChildren().addAll(controls, tableView);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setX(600);
        stage.setY(100);
    }
    
    private void refreshMemory() {
        Platform.runLater(() -> {
            memoryData.clear();
            
            // Lire 256 octets de mémoire
            for (int i = 0; i < 256; i += 16) {
                int currentAddr = startAddress + i;
                
                StringBuilder hexLine = new StringBuilder();
                StringBuilder asciiLine = new StringBuilder();
                StringBuilder decLine = new StringBuilder();
                
                for (int j = 0; j < 16; j++) {
                    int addr = currentAddr + j;
                    if (addr <= 0xFFFF) {
                        byte value = backend.readMemory(addr);
                        hexLine.append(String.format("%02X ", value));
                        
                        // ASCII
                        if (value >= 32 && value < 127) {
                            asciiLine.append((char) value);
                        } else {
                            asciiLine.append(".");
                        }
                        
                        // Décimal
                        decLine.append(String.format("%3d ", value & 0xFF));
                    } else {
                        hexLine.append("   ");
                        asciiLine.append(" ");
                        decLine.append("    ");
                    }
                }
                
                memoryData.add(new MemoryRow(
                    String.format("$%04X", currentAddr),
                    hexLine.toString().trim(),
                    asciiLine.toString(),
                    decLine.toString().trim()
                ));
            }
        });
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== IMPLÉMENTATION OBSERVER ====================
    
    @Override
    public void onMemoryUpdate(int address, int value) {
        if (autoRefresh) {
            // Vérifier si l'adresse mise à jour est dans la plage affichée
            if (address >= startAddress && address < startAddress + 256) {
                Platform.runLater(() -> {
                    refreshMemory();
                    
                    // Surligner la ligne modifiée
                    int rowIndex = (address - startAddress) / 16;
                    if (rowIndex >= 0 && rowIndex < memoryData.size()) {
                        tableView.getSelectionModel().select(rowIndex);
                        tableView.scrollTo(rowIndex);
                    }
                });
            }
        }
    }
    
    @Override
    public void onRegisterUpdate(String register, int value) {
        // Pas utilisé ici
    }
    
    @Override
    public void onFlagUpdate(String flag, boolean value) {
        // Pas utilisé ici
    }
    
    @Override
    public void onExecutionStateChange(boolean running, boolean paused) {
        Platform.runLater(() -> {
            if (running) {
                stage.setTitle("Mémoire 6809 - En exécution");
            } else {
                stage.setTitle("Mémoire 6809 - Arrêté");
            }
        });
    }
    
    @Override
    public void onProgramLoaded(int startAddress, int size) {
        Platform.runLater(() -> {
            this.startAddress = startAddress;
            refreshMemory();
            stage.setTitle("Mémoire 6809 - Programme chargé (" + size + " octets)");
        });
    }
    
    @Override
    public void onExecutionStep(int pc, int opcode, int cycles) {
        if (autoRefresh) {
            // Suivre le PC
            if (pc >= startAddress && pc < startAddress + 256) {
                Platform.runLater(() -> {
                    int rowIndex = (pc - startAddress) / 16;
                    if (rowIndex >= 0 && rowIndex < memoryData.size()) {
                        tableView.getSelectionModel().select(rowIndex);
                        tableView.scrollTo(rowIndex);
                    }
                });
            }
        }
    }
    
    public void show() {
        Platform.runLater(() -> {
            stage.show();
            refreshMemory();
        });
    }
    
    public void hide() {
        Platform.runLater(() -> {
            stage.hide();
        });
    }
    
    // ==================== CLASSE INTERNE ====================
    
    public static class MemoryRow {
        private final javafx.beans.property.SimpleStringProperty address;
        private final javafx.beans.property.SimpleStringProperty hex;
        private final javafx.beans.property.SimpleStringProperty ascii;
        private final javafx.beans.property.SimpleStringProperty decimal;
        
        public MemoryRow(String addr, String hexStr, String asciiStr, String decStr) {
            this.address = new javafx.beans.property.SimpleStringProperty(addr);
            this.hex = new javafx.beans.property.SimpleStringProperty(hexStr);
            this.ascii = new javafx.beans.property.SimpleStringProperty(asciiStr);
            this.decimal = new javafx.beans.property.SimpleStringProperty(decStr);
        }
        
        public String getAddress() { return address.get(); }
        public javafx.beans.property.SimpleStringProperty addressProperty() { return address; }
        
        public String getHex() { return hex.get(); }
        public javafx.beans.property.SimpleStringProperty hexProperty() { return hex; }
        
        public String getAscii() { return ascii.get(); }
        public javafx.beans.property.SimpleStringProperty asciiProperty() { return ascii; }
        
        public String getDecimal() { return decimal.get(); }
        public javafx.beans.property.SimpleStringProperty decimalProperty() { return decimal; }
    }
}