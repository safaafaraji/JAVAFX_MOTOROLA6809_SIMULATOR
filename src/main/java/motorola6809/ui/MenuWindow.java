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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MenuWindow extends Stage {
    
    private SimulatorBackend backend;
    private EditeurWindow editeurWindow;
    private ArchitectureWindow architectureWindow;
    
    private Label statusLabel;
    private Label pcLabel;
    private Label aLabel;
    private Label bLabel;
    private TextArea logArea;
    private ScheduledExecutorService refreshExecutor;
    
    public MenuWindow() {
        super();
        setTitle("Motorola 6809 Simulator - Console Principale");
        
        // Initialiser le backend
        backend = SimulatorBackend.getInstance();
        
        // Créer l'interface
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
        
        // Démarrer les mises à jour
        startRefreshTimer();
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
        
        TextArea editorArea = new TextArea();
        editorArea.setFont(Font.font("Monospaced", 13));
        editorArea.setPrefHeight(250);
        editorArea.setText(
            "; ============================================\n" +
            "; Programme Motorola 6809\n" +
            "; Tapez votre code assembleur ici\n" +
            "; ============================================\n\n" +
            "        ORG $1000           ; Adresse de départ\n\n" +
            "START   LDA #$05           ; Charger 5 dans A\n" +
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
            javafx.application.Platform.runLater(() -> {
                updateStatus();
                updateLog();
            });
        }, 0, 100, TimeUnit.MILLISECONDS); // Mise à jour 10x par seconde
    }
    
    private void updateStatus() {
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
        memStage.setTitle("Vue Mémoire - " + title);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        // Table de mémoire
        TableView<MemoryRow> table = new TableView<>();
        
        TableColumn<MemoryRow, String> addrCol = new TableColumn<>("Adresse");
        addrCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        
        TableColumn<MemoryRow, String> dataCol = new TableColumn<>("Donnée");
        dataCol.setCellValueFactory(cell -> cell.getValue().dataProperty());
        
        TableColumn<MemoryRow, String> asciiCol = new TableColumn<>("ASCII");
        asciiCol.setCellValueFactory(cell -> cell.getValue().asciiProperty());
        
        table.getColumns().addAll(addrCol, dataCol, asciiCol);
        
        // Données
        javafx.collections.ObservableList<MemoryRow> data = 
            javafx.collections.FXCollections.observableArrayList();
        
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
        table.setPrefHeight(400);
        
        root.getChildren().add(table);
        
        Scene scene = new Scene(root, 600, 450);
        memStage.setScene(scene);
        memStage.show();
    }
    
    @Override
    public void close() {
        if (refreshExecutor != null) {
            refreshExecutor.shutdown();
        }
        backend.stop();
        super.close();
    }
    
    // Classe pour les lignes de mémoire
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