package motorola6809.ui;

import motorola6809.core.SimulatorBackend;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// NOUVEAUX IMPORTS AJOUTÉS :
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuWindow extends Stage implements SimulatorBackend.SimulatorObserver {
    
    private SimulatorBackend backend;
    private EditeurWindow editeurWindow;
    private ArchitectureWindow architectureWindow;
    
    // Composants UI
    private Label statusLabel;
    private Label pcLabel;
    private Label aLabel;
    private Label bLabel;
    private TextArea logArea;
    private TextArea editorArea;
    private ScheduledExecutorService refreshExecutor;
    
    public MenuWindow() {
        super();
        setTitle("Motorola 6809 Simulator - Console Principale");
        
        // Initialiser le backend
        backend = SimulatorBackend.getInstance();
        backend.addObserver(this); // S'enregistrer comme observateur
        
        // Créer l'interface
        createUI();
        
        // Démarrer les mises à jour
        startRefreshTimer();
    }
    
    private void createUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2c3e50;");
        
        // Top bar
        root.setTop(createTopBar());
        
        // Center
        root.setCenter(createCenterPanel());
        
        // Right sidebar
        root.setRight(createSidebar());
        
        Scene scene = new Scene(root, 1000, 700);
        setScene(scene);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(15);
        topBar.setStyle("-fx-background-color: #34495e; -fx-padding: 15;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // Logo
        Label logo = new Label("⚙️ Motorola 6809 Simulator");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo.setTextFill(Color.WHITE);
        
        // Espace
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Statut rapide
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER);
        
        statusLabel = new Label("⏹ Arrêté");
        statusLabel.setFont(Font.font("Arial", 14));
        statusLabel.setTextFill(Color.WHITE);
        
        pcLabel = new Label("PC: 0000");
        pcLabel.setFont(Font.font("Monospaced", 12));
        pcLabel.setTextFill(Color.LIGHTGRAY);
        
        aLabel = new Label("A: 00");
        aLabel.setFont(Font.font("Monospaced", 12));
        aLabel.setTextFill(Color.LIGHTGRAY);
        
        bLabel = new Label("B: 00");
        bLabel.setFont(Font.font("Monospaced", 12));
        bLabel.setTextFill(Color.LIGHTGRAY);
        
        statusBox.getChildren().addAll(statusLabel, pcLabel, aLabel, bLabel);
        
        topBar.getChildren().addAll(logo, spacer, statusBox);
        return topBar;
    }
    
    private VBox createCenterPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white;");
        
        // Éditeur intégré
        Label editorLabel = new Label("Éditeur de Code Assembleur");
        editorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        editorArea = new TextArea();
        editorArea.setFont(Font.font("Monospaced", 13));
        editorArea.setPrefHeight(250);
        editorArea.setText(
            "; ============================================\n" +
            "; Programme Motorola 6809\n" +
            "; Tapez votre code assembleur ici\n" +
            "; ============================================\n\n" +
            "        ORG $1000           ; Adresse de départ\n\n" +
            " START                     \n" +
            "        LDA #$05           ; Charger 5 dans A\n" +
            "        LDB #$03           ; Charger 3 dans B\n" +
            "        MUL                ; Multiplier A * B -> D\n" +
            "        STA $2000          ; Stocker résultat\n" +
            "        SWI                ; Retour au système\n\n" +
            "        END                ; Fin du programme\n"
        );
        
        HBox editorButtons = new HBox(10);
        
        Button assembleBtn = new Button("Assembler");
        assembleBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        assembleBtn.setOnAction(e -> {
            boolean success = backend.assemble(editorArea.getText());
            if (success) {
                logArea.appendText("✓ Programme assemblé\n");
            } else {
                logArea.appendText("✗ Erreur d'assemblage\n");
            }
        });
        
        Button clearBtn = new Button("Effacer");
        clearBtn.setOnAction(e -> editorArea.clear());
        
        editorButtons.getChildren().addAll(assembleBtn, clearBtn);
        
        // Log
        Label logLabel = new Label("Console d'exécution");
        logLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        logArea = new TextArea();
        logArea.setFont(Font.font("Monospaced", 12));
        logArea.setEditable(false);
        logArea.setPrefHeight(200);
        
        HBox logButtons = new HBox(10);
        
        Button clearLogBtn = new Button("Effacer Log");
        clearLogBtn.setOnAction(e -> {
            backend.clearLog();
            logArea.clear();
        });
        
        logButtons.getChildren().add(clearLogBtn);
        
        panel.getChildren().addAll(editorLabel, editorArea, editorButtons, 
                                  logLabel, logArea, logButtons);
        return panel;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: #ecf0f1;");
        
        // Fenêtres
        Label windowsLabel = new Label("Fenêtres");
        windowsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox windowsBox = new VBox(10);
        
        Button editorBtn = createSidebarButton("📝 Éditeur Avancé", "#3498db");
        editorBtn.setOnAction(e -> {
            if (editeurWindow == null) {
                editeurWindow = new EditeurWindow();
            }
            editeurWindow.show();
        });
        
        Button archBtn = createSidebarButton("🏗️ Architecture", "#9b59b6");
        archBtn.setOnAction(e -> {
            if (architectureWindow == null) {
                architectureWindow = new ArchitectureWindow(backend);
            }
            architectureWindow.show();
        });
        
        windowsBox.getChildren().addAll(editorBtn, archBtn);
        
        // Contrôles
        Label ctrlLabel = new Label("Contrôles d'exécution");
        ctrlLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox ctrlBox = new VBox(10);
        
        Button startBtn = createSidebarButton("▶ Démarrer", "#27ae60");
        startBtn.setOnAction(e -> backend.start());
        
        Button pauseBtn = createSidebarButton("⏸ Pause", "#f39c12");
        pauseBtn.setOnAction(e -> backend.pause());
        
        Button stopBtn = createSidebarButton("⏹ Arrêter", "#e74c3c");
        stopBtn.setOnAction(e -> backend.stop());
        
        Button stepBtn = createSidebarButton("→ Pas à pas", "#3498db");
        stepBtn.setOnAction(e -> backend.step());
        
        Button resetBtn = createSidebarButton("↺ Réinitialiser", "#95a5a6");
        resetBtn.setOnAction(e -> {
            backend.reset();
            logArea.appendText("✓ Simulateur réinitialisé\n");
        });
        
        ctrlBox.getChildren().addAll(startBtn, pauseBtn, stopBtn, stepBtn, resetBtn);
        
        // Mémoire
        Label memLabel = new Label("Mémoire");
        memLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox memBox = new VBox(10);
        
        Button ramBtn = createSidebarButton("💾 RAM", "#34495e");
        ramBtn.setOnAction(e -> openMemoryViewer(0x0000, "RAM"));
        
        Button romBtn = createSidebarButton("🔒 ROM", "#7f8c8d");
        romBtn.setOnAction(e -> openMemoryViewer(0x1400, "ROM"));
        
        memBox.getChildren().addAll(ramBtn, romBtn);
        
        sidebar.getChildren().addAll(windowsLabel, windowsBox, ctrlLabel, ctrlBox, memLabel, memBox);
        return sidebar;
    }
    
    private Button createSidebarButton(String text, String color) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: " + color + "; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 4; " +
                    "-fx-padding: 10; " +
                    "-fx-font-size: 13;");
        return btn;
    }
    
    private void startRefreshTimer() {
        refreshExecutor = Executors.newSingleThreadScheduledExecutor();
        refreshExecutor.scheduleAtFixedRate(() -> {
            try {
                Platform.runLater(() -> {
                    try {
                        updateStatus();
                        updateLog();
                    } catch (Exception e) {
                        System.err.println("Erreur dans updateStatus/Log: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.err.println("Erreur dans le timer de rafraîchissement: " + e.getMessage());
            }
        }, 0, 500, TimeUnit.MILLISECONDS); // 500ms au lieu de 100ms pour réduire la charge
    }
    
    private void updateStatus() {
        try {
            // Statut d'exécution
            if (backend.isRunning()) {
                statusLabel.setText(backend.isPaused() ? "⏸ Pause" : "▶ En cours");
                statusLabel.setTextFill(backend.isPaused() ? Color.ORANGE : Color.GREEN);
            } else {
                statusLabel.setText("⏹ Arrêté");
                statusLabel.setTextFill(Color.RED);
            }
            
            // Registres rapides
            pcLabel.setText("PC: " + formatHex16(backend.getPC()));
            aLabel.setText("A: " + formatHex8(backend.getA()));
            bLabel.setText("B: " + formatHex8(backend.getB()));
        } catch (Exception e) {
            System.err.println("Erreur dans updateStatus: " + e.getMessage());
        }
    }
    
    private String formatHex16(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
    
    private String formatHex8(int value) {
        return String.format("%02X", value & 0xFF);
    }
    
    private void updateLog() {
        // Mettre à jour depuis les logs du backend
        java.util.List<String> logs = backend.getExecutionLog();
        if (!logs.isEmpty() && logArea != null) {
            String lastLog = logs.get(logs.size() - 1);
            if (!logArea.getText().endsWith(lastLog + "\n")) {
                logArea.appendText(lastLog + "\n");
                logArea.setScrollTop(Double.MAX_VALUE);
            }
        }
    }
    
    private void openMemoryViewer(int startAddress, String title) {
        Stage memStage = new Stage();
        memStage.setTitle("Vue Mémoire - " + title + " ($" + formatHex16(startAddress) + ")");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        // Contrôles
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_LEFT);
        
        TextField addrField = new TextField("$" + formatHex16(startAddress));
        addrField.setPrefWidth(100);
        
        Button refreshBtn = new Button("🔄 Rafraîchir");
        
        // Table de mémoire
        TableView<MemoryRow> table = createMemoryTable(startAddress);
        table.setPrefHeight(400);
        
        // Remplir la table initialement
        updateMemoryTableSafe(table, startAddress);
        
        // Configurer le bouton rafraîchir
        refreshBtn.setOnAction(e -> {
            try {
                String text = addrField.getText().trim();
                if (text.startsWith("$")) text = text.substring(1);
                int newAddr = Integer.parseInt(text, 16);
                updateMemoryTableSafe(table, newAddr);
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Adresse invalide: " + addrField.getText());
            }
        });
        
        Button writeBtn = new Button("✏️ Écrire");
        writeBtn.setOnAction(e -> openMemoryEditor(startAddress));
        
        controls.getChildren().addAll(
            new Label("Adresse:"), addrField, refreshBtn, writeBtn
        );
        
        // Checkbox pour auto-rafraîchissement (SANS Timeline pour éviter StackOverflow)
        CheckBox autoRefresh = new CheckBox("Auto-rafraîchir");
        autoRefresh.setSelected(false); // Désactivé par défaut pour sécurité
        
        // Utiliser un ScheduledExecutorService au lieu de Timeline
        ScheduledExecutorService memoryRefreshExecutor = Executors.newSingleThreadScheduledExecutor();
        
        autoRefresh.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // Activer le rafraîchissement automatique
                memoryRefreshExecutor.scheduleAtFixedRate(() -> {
                    Platform.runLater(() -> {
                        try {
                            String text = addrField.getText().trim();
                            if (text.startsWith("$")) text = text.substring(1);
                            int currentAddr = Integer.parseInt(text, 16);
                            updateMemoryTableSafe(table, currentAddr);
                        } catch (NumberFormatException ex) {
                            // Ignorer les erreurs pendant le rafraîchissement automatique
                        }
                    });
                }, 0, 2, TimeUnit.SECONDS); // Rafraîchir toutes les 2 secondes
            } else {
                // Désactiver le rafraîchissement
                memoryRefreshExecutor.shutdown();
            }
        });
        
        // Arrêter l'executor quand la fenêtre se ferme
        memStage.setOnCloseRequest(e -> {
            memoryRefreshExecutor.shutdown();
        });
        
        root.getChildren().addAll(controls, table, autoRefresh);
        
        Scene scene = new Scene(root, 600, 500);
        memStage.setScene(scene);
        memStage.show();
    }
    
    private TableView<MemoryRow> createMemoryTable(int startAddress) {
        TableView<MemoryRow> table = new TableView<>();
        
        TableColumn<MemoryRow, String> addrCol = new TableColumn<>("Adresse");
        addrCol.setPrefWidth(80);
        addrCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        
        TableColumn<MemoryRow, String> hexCol = new TableColumn<>("Hex");
        hexCol.setPrefWidth(300);
        hexCol.setCellValueFactory(cell -> cell.getValue().dataProperty());
        
        TableColumn<MemoryRow, String> asciiCol = new TableColumn<>("ASCII");
        asciiCol.setPrefWidth(160);
        asciiCol.setCellValueFactory(cell -> cell.getValue().asciiProperty());
        
        table.getColumns().addAll(addrCol, hexCol, asciiCol);
        
        return table;
    }
    
    private void updateMemoryTable(TableView<MemoryRow> table, int startAddress) {
        ObservableList<MemoryRow> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < 256; i += 16) {
            StringBuilder hexLine = new StringBuilder();
            StringBuilder asciiLine = new StringBuilder();
            
            for (int j = 0; j < 16; j++) {
                int addr = startAddress + i + j;
                byte value = backend.readMemory(addr);
                hexLine.append(String.format("%02X ", value));
                
                if (value >= 32 && value < 127) {
                    asciiLine.append((char) value);
                } else {
                    asciiLine.append(".");
                }
            }
            
            data.add(new MemoryRow(
                String.format("%04X", startAddress + i),
                hexLine.toString().trim(),
                asciiLine.toString()
            ));
        }
        
        table.setItems(data);
    }
    
    private void updateMemoryTableSafe(TableView<MemoryRow> table, int startAddress) {
        try {
            ObservableList<MemoryRow> data = FXCollections.observableArrayList();
            
            for (int i = 0; i < 256; i += 16) {
                StringBuilder hexLine = new StringBuilder();
                StringBuilder asciiLine = new StringBuilder();
                
                for (int j = 0; j < 16; j++) {
                    int addr = startAddress + i + j;
                    byte value = backend.readMemory(addr);
                    hexLine.append(String.format("%02X ", value));
                    
                    if (value >= 32 && value < 127) {
                        asciiLine.append((char) value);
                    } else {
                        asciiLine.append(".");
                    }
                }
                
                data.add(new MemoryRow(
                    String.format("%04X", startAddress + i),
                    hexLine.toString().trim(),
                    asciiLine.toString()
                ));
            }
            
            Platform.runLater(() -> {
                table.setItems(data);
            });
        } catch (Exception e) {
            System.err.println("Erreur lors du rafraîchissement de la table mémoire: " + e.getMessage());
        }
    }
    
    private void updateMemoryViewerTable(Stage stage, int newAddress) {
        // Mettre à jour le titre
        stage.setTitle("Vue Mémoire - $" + formatHex16(newAddress));
        
        // Mettre à jour le contenu de la fenêtre
        VBox root = (VBox) stage.getScene().getRoot();
        
        // Trouver la table dans les enfants
        for (javafx.scene.Node node : root.getChildren()) {
            if (node instanceof TableView) {
                @SuppressWarnings("unchecked")
                TableView<MemoryRow> table = (TableView<MemoryRow>) node;
                updateMemoryTable(table, newAddress);
                break;
            }
        }
    }
    
    private void openMemoryEditor(int address) {
        Stage editorStage = new Stage();
        editorStage.setTitle("Éditeur de Mémoire - $" + formatHex16(address));
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        
        Label addrLabel = new Label("Adresse:");
        TextField addrField = new TextField("$" + formatHex16(address));
        addrField.setPrefWidth(100);
        
        Label valueLabel = new Label("Valeur:");
        TextField valueField = new TextField();
        valueField.setPrefWidth(100);
        valueField.setPromptText("ex: $AA");
        
        // Lire la valeur actuelle
        byte currentValue = backend.readMemory(address);
        valueField.setText("$" + formatHex8(currentValue));
        
        grid.add(addrLabel, 0, 0);
        grid.add(addrField, 1, 0);
        grid.add(valueLabel, 0, 1);
        grid.add(valueField, 1, 1);
        
        HBox buttons = new HBox(10);
        
        Button readBtn = new Button("Lire");
        readBtn.setOnAction(e -> {
            try {
                String addrText = addrField.getText().trim();
                if (addrText.startsWith("$")) addrText = addrText.substring(1);
                int addr = Integer.parseInt(addrText, 16);
                
                byte value = backend.readMemory(addr);
                valueField.setText("$" + formatHex8(value));
                
                // Mettre à jour l'affichage
                showAlert("Lecture", 
                    "Adresse $" + formatHex16(addr) + 
                    " = $" + formatHex8(value));
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Adresse invalide: " + addrField.getText());
            }
        });
        
        Button writeBtn = new Button("Écrire");
        writeBtn.setOnAction(e -> {
            try {
                String addrText = addrField.getText().trim();
                String valueText = valueField.getText().trim();
                
                if (addrText.startsWith("$")) addrText = addrText.substring(1);
                if (valueText.startsWith("$")) valueText = valueText.substring(1);
                
                int addr = Integer.parseInt(addrText, 16);
                int value = Integer.parseInt(valueText, 16);
                
                backend.writeMemory(addr, (byte) value);
                
                showAlert("Écriture", 
                    "Écrit $" + formatHex8(value) + 
                    " à $" + formatHex16(addr));
                
                editorStage.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Valeur invalide: " + valueField.getText());
            }
        });
        
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> editorStage.close());
        
        buttons.getChildren().addAll(readBtn, writeBtn, cancelBtn);
        
        root.getChildren().addAll(
            new Label("Éditer la mémoire:"),
            grid,
            buttons
        );
        
        Scene scene = new Scene(root, 300, 200);
        editorStage.setScene(scene);
        editorStage.show();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== IMPLÉMENTATION OBSERVER ====================
    
    @Override
    public void onRegisterUpdate(String register, int value) {
        javafx.application.Platform.runLater(() -> {
            updateStatus();
        });
    }
    
    @Override
    public void onFlagUpdate(String flag, boolean value) {
        // Flags - pas d'affichage direct dans MenuWindow
    }
    
    @Override
    public void onMemoryUpdate(int address, int value) {
        // Mémoire - pas d'affichage direct
    }
    
    @Override
    public void onExecutionStateChange(boolean running, boolean paused) {
        javafx.application.Platform.runLater(() -> {
            updateStatus();
        });
    }
    
    @Override
    public void onProgramLoaded(int startAddress, int size) {
        javafx.application.Platform.runLater(() -> {
            logArea.appendText("✓ Programme chargé à $" + formatHex16(startAddress) + 
                             " (" + size + " octets)\n");
        });
    }
    
    @Override
    public void onExecutionStep(int pc, int opcode, int cycles) {
        // Pas nécessaire pour MenuWindow
    }
    
    @Override
    public void close() {
        if (refreshExecutor != null) {
            refreshExecutor.shutdown();
        }
        backend.stop();
        super.close();
    }
    
    // ==================== CLASSE INTERNE POUR MÉMOIRE ====================
    
    public static class MemoryRow {
        private final javafx.beans.property.SimpleStringProperty address;
        private final javafx.beans.property.SimpleStringProperty data;
        private final javafx.beans.property.SimpleStringProperty ascii;
        
        public MemoryRow(String addr, String dat, String asc) {
            this.address = new javafx.beans.property.SimpleStringProperty(addr);
            this.data = new javafx.beans.property.SimpleStringProperty(dat);
            this.ascii = new javafx.beans.property.SimpleStringProperty(asc);
        }
        
        public String getAddress() { return address.get(); }
        public javafx.beans.property.SimpleStringProperty addressProperty() { return address; }
        
        public String getData() { return data.get(); }
        public javafx.beans.property.SimpleStringProperty dataProperty() { return data; }
        
        public String getAscii() { return ascii.get(); }
        public javafx.beans.property.SimpleStringProperty asciiProperty() { return ascii; }
    }
}