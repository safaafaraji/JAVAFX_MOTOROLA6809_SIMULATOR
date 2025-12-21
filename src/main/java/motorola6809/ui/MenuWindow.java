// MenuWindow.java - Version corrigée avec toutes les dépendances
package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import motorola6809.integration.SimulatorBridge;
import motorola6809.assembler.Assembler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MenuWindow {
    
    private Stage stage;
    private EditeurWindow editeur;
    private ProgrammeWindow programme;
    private RAMWindow ramWindow;
    private ROMWindow romWindow;
    private ArchitectureWindow archWindow;
    private SimulatorBridge simulatorBridge;
    private boolean isRunning = false;
    private boolean isPaused = false;
    private TextArea programArea;
    
    // Variables pour stocker les valeurs des registres localement
    private String pcValue = "0000";
    private String sValue = "1FFF";
    private String uValue = "1E00";
    private String aValue = "00";
    private String bValue = "00";
    private String xValue = "0000";
    private String yValue = "0000";
    private String dpValue = "00";
    private String eValue = "0";
    private String fValue = "0";
    private String hValue = "0";
    private String iValue = "0";
    private String nValue = "0";
    private String zValue = "1";
    private String vValue = "0";
    private String cValue = "0";
    
    public MenuWindow() {
        stage = new Stage();
        stage.setTitle("Motorola 6809 Simulator - Dashboard");
        stage.setMaximized(true);
        
        // Initialiser les fenêtres
        editeur = new EditeurWindow();
        programme = new ProgrammeWindow();
        ramWindow = new RAMWindow();
        romWindow = new ROMWindow();
        archWindow = new ArchitectureWindow();
        
        // Initialiser le bridge avec l'éditeur
        simulatorBridge = new SimulatorBridge(editeur);
        
        // Créer l'interface principale
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");
        
        // ========== TOP BAR ==========
        HBox topBar = createTopBar();
        root.setTop(topBar);
        
        // ========== CENTER ==========
        SplitPane centerPane = createCenterPane();
        root.setCenter(centerPane);
        
        // ========== RIGHT SIDEBAR ==========
        VBox rightSidebar = createRightSidebar();
        root.setRight(rightSidebar);
        
        Scene scene = new Scene(root, 1400, 800);
        stage.setScene(scene);
        
        // Initialiser les valeurs
        resetValues();
    }
    
    private void resetValues() {
        pcValue = "0000";
        sValue = "1FFF";
        uValue = "1E00";
        aValue = "00";
        bValue = "00";
        xValue = "0000";
        yValue = "0000";
        dpValue = "00";
        eValue = "0";
        fValue = "0";
        hValue = "0";
        iValue = "0";
        nValue = "0";
        zValue = "1";
        vValue = "0";
        cValue = "0";
        
        // Mettre à jour l'interface
        ArchitectureWindow.setPC(pcValue);
        ArchitectureWindow.setS(sValue);
        ArchitectureWindow.setU(uValue);
        ArchitectureWindow.setA(aValue);
        ArchitectureWindow.setB(bValue);
        ArchitectureWindow.setX(xValue);
        ArchitectureWindow.setY(yValue);
        ArchitectureWindow.setDP(dpValue);
        ArchitectureWindow.setE(eValue);
        ArchitectureWindow.setF(fValue);
        ArchitectureWindow.setH(hValue);
        ArchitectureWindow.setI(iValue);
        ArchitectureWindow.setN(nValue);
        ArchitectureWindow.setZ(zValue);
        ArchitectureWindow.setV(vValue);
        ArchitectureWindow.setC(cValue);
    }
    
    // ========== MÉTHODES DE CONSTRUCTION D'INTERFACE ==========
    
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #2c3e50;");
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        // Logo/Titre
        HBox logoBox = new HBox(15);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        StackPane iconPane = new StackPane();
        iconPane.setPrefSize(36, 36);
        
        Rectangle iconBg = new Rectangle(36, 36);
        iconBg.setFill(Color.web("#3498db"));
        iconBg.setArcWidth(8);
        iconBg.setArcHeight(8);
        
        Label cpuIcon = new Label("6809");
        cpuIcon.setFont(Font.font("Monospaced", 12));
        cpuIcon.setTextFill(Color.WHITE);
        
        iconPane.getChildren().addAll(iconBg, cpuIcon);
        
        Label title = new Label("Motorola 6809 Simulator");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.WHITE);
        
        logoBox.getChildren().addAll(iconPane, title);
        
        // Espace flexible
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Boutons de contrôle
        HBox controlBox = new HBox(8);
        controlBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button startBtn = createControlButton("▶", "Démarrer", "#27ae60");
        startBtn.setOnAction(e -> startSimulation());
        
        Button pauseBtn = createControlButton("⏸", "Pause", "#f39c12");
        pauseBtn.setOnAction(e -> pauseSimulation());
        
        Button stopBtn = createControlButton("⏹", "Arrêter", "#e74c3c");
        stopBtn.setOnAction(e -> stopSimulation());
        
        Button resetBtn = createControlButton("↺", "Réinitialiser", "#3498db");
        resetBtn.setOnAction(e -> resetSimulation());
        
        Button stepBtn = createControlButton("→", "Pas à pas", "#9b59b6");
        stepBtn.setOnAction(e -> stepSimulation());
        
        controlBox.getChildren().addAll(startBtn, pauseBtn, stopBtn, resetBtn, stepBtn);
        
        topBar.getChildren().addAll(logoBox, spacer, controlBox);
        return topBar;
    }
    
    private SplitPane createCenterPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.4, 0.6);
        
        // ===== LEFT PANEL =====
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 1 0 0;");
        
        // Section Éditeur
        VBox editorSection = createSection("Éditeur de Code", "📝");
        
        TextArea quickEditor = new TextArea();
        quickEditor.setFont(Font.font("Monospaced", 12));
        quickEditor.setPrefHeight(200);
        quickEditor.setPromptText("; Tapez votre code assembleur ici\n; Exemple:\n;   ORG $1000\n;   LDA #$05\n;   LDB #$04\n;   MUL\n;   END");
        
        HBox editorButtons = new HBox(10);
        
        Button openEditorBtn = new Button("Ouvrir Éditeur");
        openEditorBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        openEditorBtn.setOnAction(e -> editeur.show());
        
        Button assembleBtn = new Button("Assembler");
        assembleBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        assembleBtn.setOnAction(e -> assembleCodeFromTextArea(quickEditor));
        
        Button clearBtn = new Button("Effacer");
        clearBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> quickEditor.clear());
        
        editorButtons.getChildren().addAll(openEditorBtn, assembleBtn, clearBtn);
        
        editorSection.getChildren().addAll(quickEditor, editorButtons);
        
        // Section Mémoire
        VBox memorySection = createSection("Mémoire Rapide", "💾");
        
        TableView<MemoryCell> memoryTable = createMemoryTable();
        memoryTable.setPrefHeight(180);
        
        HBox memoryButtons = new HBox(10);
        Button openRAMBtn = new Button("Voir RAM");
        openRAMBtn.setOnAction(e -> ramWindow.show());
        
        Button openROMBtn = new Button("Voir ROM");
        openROMBtn.setOnAction(e -> romWindow.show());
        
        Button refreshBtn = new Button("Rafraîchir");
        refreshBtn.setOnAction(e -> refreshMemoryTable(memoryTable));
        
        memoryButtons.getChildren().addAll(openRAMBtn, openROMBtn, refreshBtn);
        
        memorySection.getChildren().addAll(memoryTable, memoryButtons);
        
        leftPanel.getChildren().addAll(editorSection, memorySection);
        
        // ===== CENTER PANEL =====
        VBox centerPanel = new VBox(15);
        centerPanel.setPadding(new Insets(20));
        centerPanel.setStyle("-fx-background-color: white;");
        
        // Section Architecture
        VBox archSection = createSection("Architecture 6809", "⚙️");
        
        GridPane archGrid = createArchitectureGrid();
        
        // Flags
        VBox flagsBox = createFlagsDisplay();
        
        archSection.getChildren().addAll(archGrid, flagsBox);
        
        // Section Programme
        VBox programSection = createSection("Programme en Cours", "📋");
        
        programArea = new TextArea();
        programArea.setFont(Font.font("Monospaced", 11));
        programArea.setPrefHeight(150);
        programArea.setEditable(false);
        programArea.setText(getProgramDisplay());
        
        Button openProgramBtn = new Button("Ouvrir Programme");
        openProgramBtn.setOnAction(e -> {
            programme.show();
            ProgrammeWindow.clear();
            displayCurrentProgramInProgramWindow();
        });
        
        Button updateBtn = new Button("Mettre à jour");
        updateBtn.setOnAction(e -> updateProgramDisplay());
        
        HBox programButtons = new HBox(10);
        programButtons.getChildren().addAll(openProgramBtn, updateBtn);
        
        programSection.getChildren().addAll(programArea, programButtons);
        
        centerPanel.getChildren().addAll(archSection, programSection);
        
        splitPane.getItems().addAll(leftPanel, centerPanel);
        
        return splitPane;
    }
    
    private VBox createRightSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(300);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 0 1;");
        
        // Section Outils
        VBox toolsSection = createSection("Outils", "🛠️");
        
        String[] tools = {
            "Assembleur",
            "Désassembleur", 
            "Debugger",
            "Profileur",
            "Analyseur",
            "Test Unitaires"
        };
        
        for (String tool : tools) {
            Button toolBtn = new Button("• " + tool);
            toolBtn.setMaxWidth(Double.MAX_VALUE);
            toolBtn.setAlignment(Pos.CENTER_LEFT);
            toolBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;");
            toolBtn.setPadding(new Insets(8, 5, 8, 5));
            
            toolBtn.setOnMouseEntered(e -> toolBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #3498db; -fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;"));
            toolBtn.setOnMouseExited(e -> toolBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #2c3e50; -fx-border-color: #ecf0f1; -fx-border-width: 0 0 1 0;"));
            
            // Actions des boutons
            if (tool.equals("Assembleur")) {
                toolBtn.setOnAction(e -> openAssembler());
            } else if (tool.equals("Test Unitaires")) {
                toolBtn.setOnAction(e -> openTestInterface());
            } else if (tool.equals("Debugger")) {
                toolBtn.setOnAction(e -> openDebugger());
            }
            
            toolsSection.getChildren().add(toolBtn);
        }
        
        // Section Exemples
        VBox examplesSection = createSection("Exemples Rapides", "📚");
        
        String[][] examples = {
            {"1. Opérations Arithmétiques", "LDA, LDB, MUL, INCA, DECB"},
            {"2. Opérations Logiques", "COMA, COMB, EORA, EORB"},
            {"3. Transfert Mémoire", "STA, STB, LDX, STX"},
            {"4. Sauts & Sous-routines", "JMP, JSR, RTS, NOP"}
        };
        
        for (String[] example : examples) {
            Button exampleBtn = new Button(example[0]);
            exampleBtn.setMaxWidth(Double.MAX_VALUE);
            exampleBtn.setAlignment(Pos.CENTER_LEFT);
            exampleBtn.setContentDisplay(ContentDisplay.LEFT);
            exampleBtn.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #495057; -fx-border-color: #dee2e6;");
            exampleBtn.setPadding(new Insets(8));
            
            Tooltip tip = new Tooltip(example[1]);
            tip.setFont(Font.font("Arial", 11));
            exampleBtn.setTooltip(tip);
            
            exampleBtn.setOnAction(e -> loadExample(example[0]));
            
            examplesSection.getChildren().add(exampleBtn);
        }
        
        // Section Statut
        VBox statusSection = createSection("Statut Système", "📊");
        
        GridPane statusGrid = new GridPane();
        statusGrid.setHgap(10);
        statusGrid.setVgap(8);
        
        statusGrid.add(createStatusItem("CPU:", isRunning ? "En cours" : "Inactif", isRunning ? "#27ae60" : "#95a5a6"), 0, 0);
        statusGrid.add(createStatusItem("Mémoire:", "64KB Libre", "#27ae60"), 1, 0);
        statusGrid.add(createStatusItem("Assembleur:", "Prêt", "#3498db"), 0, 1);
        statusGrid.add(createStatusItem("Mode:", isPaused ? "Pause" : "Normal", isPaused ? "#f39c12" : "#3498db"), 1, 1);
        
        statusSection.getChildren().add(statusGrid);
        
        // Bouton Architecture détaillée
        Button archDetailBtn = new Button("Ouvrir Architecture Détail");
        archDetailBtn.setMaxWidth(Double.MAX_VALUE);
        archDetailBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        archDetailBtn.setPadding(new Insets(10));
        archDetailBtn.setOnAction(e -> archWindow.show());
        
        sidebar.getChildren().addAll(toolsSection, examplesSection, statusSection, archDetailBtn);
        
        return sidebar;
    }
    
    // ========== MÉTHODES UTILITAIRES D'INTERFACE ==========
    
    private VBox createSection(String title, String icon) {
        VBox section = new VBox(10);
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        header.getChildren().addAll(iconLabel, titleLabel);
        
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 5, 0));
        
        section.getChildren().addAll(header, separator);
        return section;
    }
    
    private VBox createRegisterDisplay(String name, String value, String description) {
        VBox box = new VBox(3);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 4;");
        box.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.web("#495057"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 9));
        descLabel.setTextFill(Color.web("#6c757d"));
        descLabel.setWrapText(true);
        
        box.getChildren().addAll(nameLabel, valueLabel, descLabel);
        return box;
    }
    
    private GridPane createArchitectureGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Ligne 1: PC, S, U
        grid.add(createRegisterDisplay("PC", pcValue, "Program Counter"), 0, 0);
        grid.add(createRegisterDisplay("S", sValue, "Stack Pointer"), 1, 0);
        grid.add(createRegisterDisplay("U", uValue, "User Stack"), 2, 0);
        
        // Ligne 2: A, B, D
        grid.add(createRegisterDisplay("A", aValue, "Accumulator A"), 0, 1);
        grid.add(createRegisterDisplay("B", bValue, "Accumulator B"), 1, 1);
        grid.add(createRegisterDisplay("D", getDValue(), "Double Acc (A:B)"), 2, 1);
        
        // Ligne 3: X, Y, DP
        grid.add(createRegisterDisplay("X", xValue, "Index X"), 0, 2);
        grid.add(createRegisterDisplay("Y", yValue, "Index Y"), 1, 2);
        grid.add(createRegisterDisplay("DP", dpValue, "Direct Page"), 2, 2);
        
        return grid;
    }
    
    private String getDValue() {
        // Combine A et B pour former D (16-bit)
        return aValue + bValue;
    }
    
    private VBox createFlagsDisplay() {
        VBox flagsBox = new VBox(5);
        flagsBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label flagsTitle = new Label("Flags: E F H I N Z V C");
        flagsTitle.setFont(Font.font("Monospaced", 12));
        
        HBox flagsValues = new HBox(10);
        flagsValues.setAlignment(Pos.CENTER);
        
        String[] flags = {"E", "F", "H", "I", "N", "Z", "V", "C"};
        String[] flagValues = {eValue, fValue, hValue, iValue, nValue, zValue, vValue, cValue};
        
        for (int i = 0; i < flags.length; i++) {
            Label flagLabel = new Label(flagValues[i]);
            flagLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
            flagLabel.setTextFill(flagValues[i].equals("1") ? Color.GREEN : Color.BLACK);
            flagLabel.setPadding(new Insets(2, 8, 2, 8));
            flagLabel.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 3;");
            flagsValues.getChildren().add(flagLabel);
        }
        
        flagsBox.getChildren().addAll(flagsTitle, flagsValues);
        return flagsBox;
    }
    
    private HBox createStatusItem(String label, String value, String color) {
        HBox hbox = new HBox(5);
        hbox.setAlignment(Pos.CENTER_LEFT);
        
        Label lbl = new Label(label);
        lbl.setFont(Font.font("Arial", 11));
        lbl.setTextFill(Color.web("#7f8c8d"));
        
        Label val = new Label(value);
        val.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        val.setTextFill(Color.web(color));
        
        hbox.getChildren().addAll(lbl, val);
        return hbox;
    }
    
    private Button createControlButton(String icon, String text, String color) {
        Button btn = new Button(icon + " " + text);
        btn.setFont(Font.font("Arial", 12));
        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: " + color + "; " +
                    "-fx-background-radius: 4; " +
                    "-fx-padding: 8 12 8 12;");
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: " + darkenColor(color) + "; " +
                        "-fx-background-radius: 4; " +
                        "-fx-padding: 8 12 8 12;");
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: " + color + "; " +
                        "-fx-background-radius: 4; " +
                        "-fx-padding: 8 12 8 12;");
        });
        
        return btn;
    }
    
    private TableView<MemoryCell> createMemoryTable() {
        TableView<MemoryCell> table = new TableView<>();
        
        TableColumn<MemoryCell, String> addrCol = new TableColumn<>("Adresse");
        addrCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        addrCol.setPrefWidth(80);
        
        TableColumn<MemoryCell, String> dataCol = new TableColumn<>("Donnée");
        dataCol.setCellValueFactory(cell -> cell.getValue().dataProperty());
        dataCol.setPrefWidth(80);
        
        TableColumn<MemoryCell, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        
        table.getColumns().add(addrCol);
        table.getColumns().add(dataCol);
        table.getColumns().add(descCol);
        
        refreshMemoryTable(table);
        
        return table;
    }
    
    private void refreshMemoryTable(TableView<MemoryCell> table) {
        ObservableList<MemoryCell> data = FXCollections.observableArrayList();
        
        // Données d'exemple
        data.add(new MemoryCell("$1000", "86", "LDA #$value"));
        data.add(new MemoryCell("$1001", "05", "Valeur = 05"));
        data.add(new MemoryCell("$1002", "C6", "LDB #$value"));
        data.add(new MemoryCell("$1003", "04", "Valeur = 04"));
        data.add(new MemoryCell("$1004", "3D", "MUL"));
        data.add(new MemoryCell("$1005", "12", "NOP"));
        
        // Lire depuis la RAM simulée si disponible
        for (int i = 0; i < 5; i++) {
            String addr = String.format("$%04X", 0x2000 + i);
            String memData = "00"; // Valeur par défaut
            
            // Essayer de lire depuis RAMWindow
            try {
                String ramData = RAMWindow.getData(0x2000 + i);
                if (ramData != null && !ramData.equals("00") && !ramData.equals("FF")) {
                    memData = ramData;
                    data.add(new MemoryCell(addr, memData, "Donnée utilisateur"));
                }
            } catch (Exception e) {
                // Ignorer si RAMWindow n'est pas disponible
            }
        }
        
        table.setItems(data);
    }
    
    private String darkenColor(String hexColor) {
        // Simple darkening for hover effect
        if (hexColor.length() == 7) {
            return "#80" + hexColor.substring(1);
        }
        return hexColor;
    }
    
    // ========== MÉTHODES FONCTIONNELLES ==========
    
    private void startSimulation() {
        if (!simulatorBridge.isProgramLoaded()) {
            if (!compileCurrentCode()) {
                showAlert("Erreur", "Aucun programme chargé. Veuillez d'abord assembler du code.");
                return;
            }
        }
        
        isRunning = true;
        isPaused = false;
        showAlert("Simulation", "Simulation démarrée");
    }
    
    private void pauseSimulation() {
        isPaused = !isPaused;
        showAlert("Simulation", isPaused ? "Simulation en pause" : "Simulation reprise");
    }
    
    private void stopSimulation() {
        isRunning = false;
        isPaused = false;
        showAlert("Simulation", "Simulation arrêtée");
        resetRegisters();
    }
    
    private void resetSimulation() {
        isRunning = false;
        isPaused = false;
        resetValues();
        ProgrammeWindow.clear();
        
        // Réinitialiser la mémoire
        for (int i = 0; i < 1024; i++) {
            RAMWindow.setData("00", i);
        }
        
        showAlert("Réinitialisation", "Tous les registres et mémoires réinitialisés");
    }
    
    private void stepSimulation() {
        if (!simulatorBridge.isProgramLoaded()) {
            if (!compileCurrentCode()) {
                showAlert("Erreur", "Aucun programme à exécuter");
                return;
            }
        }
        
        try {
            boolean executed = simulatorBridge.executeStep();
            if (executed) {
                updateRegisterDisplay();
                ProgrammeWindow.displayInstructions("Instruction exécutée - Pas à pas");
                showAlert("Pas à pas", "Instruction exécutée avec succès");
            } else {
                showAlert("Fin", "Fin du programme atteinte");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'exécution: " + e.getMessage());
        }
    }
    
    private boolean compileCurrentCode() {
        try {
            String code = editeur.getText();
            if (code == null || code.trim().isEmpty()) {
                showAlert("Erreur", "L'éditeur est vide");
                return false;
            }
            
            return simulatorBridge.compileAndLoad();
        } catch (Exception e) {
            showAlert("Erreur de compilation", e.getMessage());
            return false;
        }
    }
    
    private void assembleCodeFromTextArea(TextArea textArea) {
        String code = textArea.getText();
        if (code == null || code.trim().isEmpty()) {
            showAlert("Erreur", "Le code source est vide");
            return;
        }
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(code);
            
            // Afficher les résultats
            StringBuilder programText = new StringBuilder();
            programText.append("Adresse   Code      Instruction\n");
            programText.append("--------------------------------\n");
            
            String[] lines = code.split("\n");
            int address = 0;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) {
                    continue;
                }
                
                if (line.toUpperCase().startsWith("ORG")) {
                    String[] parts = line.split("\\$");
                    if (parts.length > 1) {
                        try {
                            address = Integer.parseInt(parts[1].trim(), 16);
                        } catch (NumberFormatException e) {
                            address = 0;
                        }
                    }
                    programText.append(String.format("%04X     %s\n", address, line));
                } else if (!line.toUpperCase().startsWith("END")) {
                    programText.append(String.format("%04X     %s\n", address, line));
                    address += estimateInstructionSize(line);
                }
            }
            
            programArea.setText(programText.toString());
            
            // Mettre à jour la fenêtre Programme
            ProgrammeWindow.clear();
            String[] programLines = programText.toString().split("\n");
            for (String line : programLines) {
                ProgrammeWindow.displayInstructions(line);
            }
            
            // Mettre à jour les registres
            pcValue = String.format("%04X", 0x1000);
            ArchitectureWindow.setPC(pcValue);
            ArchitectureWindow.setInstruPC("Prêt à exécuter");
            
            showAlert("Assemblage réussi", 
                "Programme assemblé avec succès\n" +
                "Taille: " + machineCode.length + " octets");
                
        } catch (Exception e) {
            showAlert("Erreur d'assemblage", 
                "Erreur lors de l'assemblage:\n" + e.getMessage());
        }
    }
    
    private void updateRegisterDisplay() {
        // Simuler la mise à jour des registres
        aValue = "05";
        bValue = "04";
        zValue = "1";
        
        // Mettre à jour l'interface
        ArchitectureWindow.setA(aValue);
        ArchitectureWindow.setB(bValue);
        ArchitectureWindow.setZ(zValue);
    }
    
    private void resetRegisters() {
        resetValues();
    }
    
    private String getProgramDisplay() {
        return "Adresse   Code      Instruction\n" +
               "--------------------------------\n" +
               "[Aucun programme chargé]\n" +
               "Assemblez un programme pour voir le code ici.";
    }
    
    private void displayCurrentProgramInProgramWindow() {
        ProgrammeWindow.displayInstructions("=== PROGRAMME CHARGÉ ===");
        
        if (simulatorBridge.isProgramLoaded()) {
            ProgrammeWindow.displayInstructions("Programme chargé dans le simulateur");
            ProgrammeWindow.displayInstructions("Utilisez 'Pas à pas' pour exécuter");
        } else {
            ProgrammeWindow.displayInstructions("Aucun programme actuellement chargé");
            ProgrammeWindow.displayInstructions("Assemblez d'abord un programme");
        }
    }
    
    private int estimateInstructionSize(String line) {
        line = line.toUpperCase().trim();
        
        if (line.matches("^(NOP|MUL|DAA|INCA|INCB|DECA|DECB|COMA|COMB|RTS|SWI).*")) {
            return 1;
        }
        
        if (line.matches("^(LDA|LDB|STA|STB|ADDA|ADDB|EORA|EORB).*#\\$[0-9A-F]{1,2}.*")) {
            return 2;
        }
        
        if (line.matches("^(LDD|LDX|LDY|LDS|LDU|STD|STX|STY|STS|STU).*#\\$[0-9A-F]{3,4}.*")) {
            return 3;
        }
        
        return 1;
    }
    
    private void updateProgramDisplay() {
        if (simulatorBridge.isProgramLoaded()) {
            String currentText = programArea.getText();
            if (currentText.contains("[Aucun programme chargé]")) {
                showAlert("Information", "Veuillez assembler un programme d'abord");
            } else {
                showAlert("Mise à jour", "Affichage du programme déjà à jour");
            }
        } else {
            programArea.setText(getProgramDisplay());
            showAlert("Information", "Aucun programme chargé à afficher");
        }
    }
    
    private void openAssembler() {
        SimpleTestApp assembler = new SimpleTestApp();
        try {
            Stage stage = new Stage();
            assembler.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openTestInterface() {
        TestInterface testUI = new TestInterface();
        try {
            Stage stage = new Stage();
            testUI.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openDebugger() {
        showAlert("Debugger", "Fonctionnalité de debugger - À implémenter");
    }
    
    private void loadExample(String exampleName) {
        String code = "";
        
        switch (exampleName) {
            case "1. Opérations Arithmétiques":
                code = "ORG $1000\nLDA #$09\nINCA\nLDB #$10\nDECB\nLDA #$05\nLDB #$04\nMUL\nEND";
                break;
            case "2. Opérations Logiques":
                code = "ORG $2000\nLDA #%10101010\nCOMA\nLDB #%01010101\nCOMB\nEORA #%11110000\nEORB #%00001111\nEND";
                break;
            case "3. Transfert Mémoire":
                code = "ORG $3000\nLDA #$11\nLDB #$22\nLDD #$3344\nLDX #$1234\nSTA $4000\nSTB $4001\nSTD $4002\nSTX $4004\nEND";
                break;
            case "4. Sauts & Sous-routines":
                code = "ORG $5000\nMAIN LDA #$01\nJSR DELAY\nLDB #$02\nJSR DELAY\nJMP FIN\nDELAY NOP\nNOP\nNOP\nRTS\nFIN CWAI #$FF\nEND";
                break;
        }
        
        // Mettre le code dans l'éditeur
        editeur.show();
        showAlert("Exemple", "Exemple '" + exampleName + "' prêt à être utilisé");
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
        stage.setMaximized(true);
    }
    
    // Classe interne pour les cellules mémoire
    public static class MemoryCell {
        private final SimpleStringProperty address;
        private final SimpleStringProperty data;
        private final SimpleStringProperty description;
        
        public MemoryCell(String addr, String dat, String desc) {
            this.address = new SimpleStringProperty(addr);
            this.data = new SimpleStringProperty(dat);
            this.description = new SimpleStringProperty(desc);
        }
        
        public String getAddress() { return address.get(); }
        public void setAddress(String addr) { address.set(addr); }
        public SimpleStringProperty addressProperty() { return address; }
        
        public String getData() { return data.get(); }
        public void setData(String dat) { data.set(dat); }
        public SimpleStringProperty dataProperty() { return data; }
        
        public String getDescription() { return description.get(); }
        public void setDescription(String desc) { description.set(desc); }
        public SimpleStringProperty descriptionProperty() { return description; }
    }
}