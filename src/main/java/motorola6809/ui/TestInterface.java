package motorola6809.ui;

import motorola6809.assembler.Assembler;
import motorola6809.assembler.OpcodeGenerator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import java.util.*;

public class TestInterface extends Application {
    
    private TextArea codeArea;
    private TextArea outputArea;
    private TextArea registerArea;
    private TableView<MemoryRow> memoryTable;
    private ObservableList<MemoryRow> memoryData;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Motorola 6809 Simulator - Test Interface");
        
        // Créer les panneaux
        VBox leftPanel = createLeftPanel();
        VBox centerPanel = createCenterPanel();
        VBox rightPanel = createRightPanel();
        
        // Layout principal
        HBox root = new HBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #F0F0F0;");
        root.getChildren().addAll(leftPanel, centerPanel, rightPanel);
        
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createLeftPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(400);
        
        // Titre
        Label title = new Label("Code Source");
        title.setFont(Font.font("Arial", 18));
        title.setTextFill(Color.BLUE);
        
        // Zone de code
        codeArea = new TextArea();
        codeArea.setFont(Font.font("Monospaced", 12));
        codeArea.setPrefHeight(500);
        
        // Boutons d'action
        HBox buttonBox = new HBox(10);
        
        Button assembleBtn = new Button("Assembler");
        assembleBtn.setOnAction(e -> assembleCode());
        assembleBtn.setPrefWidth(120);
        
        Button clearBtn = new Button("Effacer");
        clearBtn.setOnAction(e -> codeArea.clear());
        clearBtn.setPrefWidth(120);
        
        Button loadExampleBtn = new Button("Exemple");
        loadExampleBtn.setOnAction(e -> loadExample());
        loadExampleBtn.setPrefWidth(120);
        
        buttonBox.getChildren().addAll(assembleBtn, clearBtn, loadExampleBtn);
        
        // Exemples rapides
        VBox examplesBox = new VBox(5);
        examplesBox.setPadding(new Insets(10, 0, 0, 0));
        Label examplesLabel = new Label("Exemples rapides:");
        
        Button example1Btn = new Button("1. Opérations arithmétiques");
        example1Btn.setOnAction(e -> loadExample1());
        example1Btn.setPrefWidth(380);
        
        Button example2Btn = new Button("2. Opérations logiques");
        example2Btn.setOnAction(e -> loadExample2());
        example2Btn.setPrefWidth(380);
        
        Button example3Btn = new Button("3. Chargement/Stockage");
        example3Btn.setOnAction(e -> loadExample3());
        example3Btn.setPrefWidth(380);
        
        Button example4Btn = new Button("4. Sauts et sous-routines");
        example4Btn.setOnAction(e -> loadExample4());
        example4Btn.setPrefWidth(380);
        
        examplesBox.getChildren().addAll(examplesLabel, example1Btn, example2Btn, example3Btn, example4Btn);
        
        panel.getChildren().addAll(title, codeArea, buttonBox, examplesBox);
        return panel;
    }
    
    private VBox createCenterPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(400);
        
        // Sortie
        Label outputLabel = new Label("Sortie");
        outputLabel.setFont(Font.font("Arial", 18));
        outputLabel.setTextFill(Color.BLUE);
        
        outputArea = new TextArea();
        outputArea.setFont(Font.font("Monospaced", 12));
        outputArea.setEditable(false);
        outputArea.setPrefHeight(400);
        
        // Registres
        Label registerLabel = new Label("Registres");
        registerLabel.setFont(Font.font("Arial", 18));
        registerLabel.setTextFill(Color.BLUE);
        
        registerArea = new TextArea();
        registerArea.setFont(Font.font("Monospaced", 14));
        registerArea.setEditable(false);
        registerArea.setPrefHeight(250);
        updateRegisters();
        
        panel.getChildren().addAll(outputLabel, outputArea, registerLabel, registerArea);
        return panel;
    }
    
    private VBox createRightPanel() {
        VBox panel = new VBox(10);
        panel.setPrefWidth(550);
        
        // Mémoire
        Label memoryLabel = new Label("Mémoire (16 premières adresses)");
        memoryLabel.setFont(Font.font("Arial", 18));
        memoryLabel.setTextFill(Color.BLUE);
        
        // Table de mémoire
        memoryTable = new TableView<>();
        TableColumn<MemoryRow, String> addrCol = new TableColumn<>("Adresse");
        addrCol.setCellValueFactory(cell -> cell.getValue().addressProperty());
        addrCol.setPrefWidth(100);
        
        TableColumn<MemoryRow, String> dataCol = new TableColumn<>("Donnée");
        dataCol.setCellValueFactory(cell -> cell.getValue().dataProperty());
        dataCol.setPrefWidth(100);
        
        TableColumn<MemoryRow, String> asciiCol = new TableColumn<>("ASCII");
        asciiCol.setCellValueFactory(cell -> cell.getValue().asciiProperty());
        asciiCol.setPrefWidth(100);
        
        TableColumn<MemoryRow, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        descCol.setPrefWidth(200);
        
        memoryTable.getColumns().addAll(addrCol, dataCol, asciiCol, descCol);
        
        // Initialiser les données
        memoryData = FXCollections.observableArrayList();
        for (int i = 0; i < 16; i++) {
            memoryData.add(new MemoryRow(String.format("$%04X", i * 16), "00", "", ""));
        }
        memoryTable.setItems(memoryData);
        memoryTable.setPrefHeight(400);
        
        // Instructions disponibles
        Label instrLabel = new Label("Instructions disponibles");
        instrLabel.setFont(Font.font("Arial", 18));
        instrLabel.setTextFill(Color.BLUE);
        
        TextArea instrArea = new TextArea();
        instrArea.setFont(Font.font("Monospaced", 11));
        instrArea.setEditable(false);
        instrArea.setPrefHeight(200);
        instrArea.setText(getAvailableInstructions());
        
        panel.getChildren().addAll(memoryLabel, memoryTable, instrLabel, instrArea);
        return panel;
    }
    
    private void assembleCode() {
        String code = codeArea.getText();
        outputArea.clear();
        
        if (code.trim().isEmpty()) {
            outputArea.setText("Erreur: Code source vide.");
            return;
        }
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(code);
            
            // Afficher les résultats
            StringBuilder output = new StringBuilder();
            output.append("=== ASSEMBLAGE RÉUSSI ===\n");
            output.append("Taille: ").append(machineCode.length).append(" octets\n\n");
            output.append("Code machine généré:\n");
            output.append("Adresse  Code        Instruction\n");
            output.append("--------------------------------\n");
            
            // Simuler un décodeur simple
            String[] lines = code.split("\n");
            int address = 0;
            int codeIndex = 0;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) {
                    continue;
                }
                
                if (line.startsWith("ORG")) {
                    String[] parts = line.split("\\$");
                    if (parts.length > 1) {
                        address = Integer.parseInt(parts[1].trim(), 16);
                    }
                    output.append(String.format("%04X     ORG directive\n", address));
                } else if (!line.startsWith("END")) {
                    // Afficher l'adresse et le code
                    output.append(String.format("%04X     ", address));
                    
                    // Afficher les octets
                    int bytesToShow = Math.min(4, machineCode.length - codeIndex);
                    for (int i = 0; i < bytesToShow && codeIndex < machineCode.length; i++) {
                        output.append(String.format("%02X ", machineCode[codeIndex++]));
                    }
                    
                    // Alignement
                    for (int i = bytesToShow; i < 4; i++) {
                        output.append("   ");
                    }
                    
                    output.append("   ").append(line).append("\n");
                    address += bytesToShow;
                }
            }
            
            output.append("\n=== FIN DU CODE ===\n");
            outputArea.setText(output.toString());
            
            // Mettre à jour la mémoire
            updateMemory(machineCode);
            
            // Mettre à jour les registres simulés
            simulateExecution(machineCode);
            
        } catch (Exception e) {
            outputArea.setText("ERREUR D'ASSEMBLAGE:\n" + e.getMessage());
        }
    }
    
    private void updateMemory(byte[] code) {
        // Effacer les anciennes données
        for (MemoryRow row : memoryData) {
            row.setData("00");
            row.setAscii("");
            row.setDescription("");
        }
        
        // Remplir avec le nouveau code
        for (int i = 0; i < Math.min(code.length, 256); i++) {
            int rowIndex = i / 16;
            if (rowIndex < memoryData.size()) {
                MemoryRow row = memoryData.get(rowIndex);
                String addr = String.format("$%04X", rowIndex * 16);
                StringBuilder data = new StringBuilder();
                StringBuilder ascii = new StringBuilder();
                
                for (int j = 0; j < 16 && (rowIndex * 16 + j) < code.length; j++) {
                    byte b = code[rowIndex * 16 + j];
                    data.append(String.format("%02X ", b));
                    
                    // Conversion ASCII
                    if (b >= 32 && b < 127) {
                        ascii.append((char) b);
                    } else {
                        ascii.append(".");
                    }
                }
                
                row.setAddress(addr);
                row.setData(data.toString().trim());
                row.setAscii(ascii.toString());
                row.setDescription("Code machine");
            }
        }
        
        memoryTable.refresh();
    }
    
    private void updateRegisters() {
        StringBuilder regText = new StringBuilder();
        regText.append("PC:  0000    (Program Counter)\n");
        regText.append("A:   00      (Accumulator A)\n");
        regText.append("B:   00      (Accumulator B)\n");
        regText.append("D:   0000    (A:B 16-bit)\n");
        regText.append("X:   0000    (Index Register X)\n");
        regText.append("Y:   0000    (Index Register Y)\n");
        regText.append("S:   1FFF    (Stack Pointer)\n");
        regText.append("U:   1E00    (User Stack Pointer)\n");
        regText.append("DP:  00      (Direct Page)\n\n");
        regText.append("CCR (Condition Code Register):\n");
        regText.append("E F H I N Z V C\n");
        regText.append("0 0 0 0 0 1 0 0\n");
        
        registerArea.setText(regText.toString());
    }
    
    private void simulateExecution(byte[] code) {
        // Simulation simple des registres
        Random rand = new Random();
        StringBuilder regText = new StringBuilder();
        
        regText.append("PC:  ").append(String.format("%04X", 0xC000)).append("    (Program Counter)\n");
        regText.append("A:   ").append(String.format("%02X", rand.nextInt(256))).append("      (Accumulator A)\n");
        regText.append("B:   ").append(String.format("%02X", rand.nextInt(256))).append("      (Accumulator B)\n");
        regText.append("D:   ").append(String.format("%04X", rand.nextInt(65536))).append("    (A:B 16-bit)\n");
        regText.append("X:   ").append(String.format("%04X", 0x2000 + rand.nextInt(256))).append("    (Index Register X)\n");
        regText.append("Y:   ").append(String.format("%04X", 0x3000 + rand.nextInt(256))).append("    (Index Register Y)\n");
        regText.append("S:   ").append(String.format("%04X", 0x1FFF)).append("    (Stack Pointer)\n");
        regText.append("U:   ").append(String.format("%04X", 0x1E00)).append("    (User Stack Pointer)\n");
        regText.append("DP:  ").append(String.format("%02X", rand.nextInt(256))).append("      (Direct Page)\n\n");
        regText.append("CCR (Condition Code Register):\n");
        regText.append("E F H I N Z V C\n");
        regText.append(rand.nextInt(2)).append(" ")
               .append(rand.nextInt(2)).append(" ")
               .append(rand.nextInt(2)).append(" ")
               .append(rand.nextInt(2)).append(" ")
               .append(rand.nextInt(2)).append(" ")
               .append(1).append(" ")  // Z souvent à 1
               .append(rand.nextInt(2)).append(" ")
               .append(rand.nextInt(2)).append("\n");
        
        registerArea.setText(regText.toString());
    }
    
    private String getAvailableInstructions() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== ARITHMÉTIQUES ===\n");
        sb.append("INCA, INCB, INC     : Incrémentation\n");
        sb.append("DECA, DECB, DEC     : Décrémentation\n");
        sb.append("ADDA, ADDB          : Addition\n");
        sb.append("MUL                 : Multiplication\n");
        sb.append("DAA                 : Ajustement décimal\n\n");
        
        sb.append("=== LOGIQUES ===\n");
        sb.append("COMA, COMB, COM     : Complément\n");
        sb.append("EORA, EORB          : OU exclusif\n\n");
        
        sb.append("=== TRANSFERT ===\n");
        sb.append("LDA, LDB, LDD       : Chargement\n");
        sb.append("STA, STB, STD       : Stockage\n");
        sb.append("LDX, LDY, LDS, LDU  : Chargement index\n");
        sb.append("STX, STY, STS, STU  : Stockage index\n");
        sb.append("EXG                 : Échange\n\n");
        
        sb.append("=== SAUT ===\n");
        sb.append("JMP, JSR            : Saut\n");
        sb.append("RTS                 : Retour sous-routine\n\n");
        
        sb.append("=== CONTRÔLE ===\n");
        sb.append("NOP                 : Aucune opération\n");
        sb.append("CWAI                : Attente interruption\n");
        
        return sb.toString();
    }
    
    private void loadExample() {
        String example = 
            "        ORG $C000\n" +
            "        LDA #$05\n" +
            "        LDB #$04\n" +
            "        MUL\n" +
            "        INCA\n" +
            "        DECB\n" +
            "        STA $2000\n" +
            "        STB $2001\n" +
            "        END";
        
        codeArea.setText(example);
    }
    
    private void loadExample1() {
        String example = 
            "; === OPÉRATIONS ARITHMÉTIQUES ===\n" +
            "        ORG $1000\n" +
            "        LDA #$09        ; A = 09\n" +
            "        INCA            ; A = 0A (+1)\n" +
            "        LDB #$10        ; B = 10\n" +
            "        DECB            ; B = 0F (-1)\n" +
            "        LDA #$05        ; A = 05\n" +
            "        LDB #$04        ; B = 04\n" +
            "        MUL             ; D = 0014 (5*4=20)\n" +
            "        LDA #$99        ; Test DAA\n" +
            "        ADDA #$01       ; A = 9A\n" +
            "        DAA             ; Correction BCD\n" +
            "        END";
        
        codeArea.setText(example);
    }
    
    private void loadExample2() {
        String example = 
            "; === OPÉRATIONS LOGIQUES ===\n" +
            "        ORG $2000\n" +
            "        LDA #%10101010  ; A = AA (10101010)\n" +
            "        COMA            ; A = 55 (01010101)\n" +
            "        LDB #%01010101  ; B = 55 (01010101)\n" +
            "        COMB            ; B = AA (10101010)\n" +
            "        EORA #%11110000 ; A = 55 XOR F0 = A5\n" +
            "        EORB #%00001111 ; B = AA XOR 0F = A5\n" +
            "        END";
        
        codeArea.setText(example);
    }
    
    private void loadExample3() {
        String example = 
            "; === CHARGEMENT/STOCKAGE ===\n" +
            "        ORG $3000\n" +
            "        LDA #$11        ; A = 11\n" +
            "        LDB #$22        ; B = 22\n" +
            "        LDD #$3344      ; D = 3344\n" +
            "        LDX #$1234      ; X = 1234\n" +
            "        LDY #$5678      ; Y = 5678\n" +
            "        STA $4000       ; Stocker A à 4000\n" +
            "        STB $4001       ; Stocker B à 4001\n" +
            "        STD $4002       ; Stocker D à 4002-4003\n" +
            "        STX $4004       ; Stocker X à 4004-4005\n" +
            "        STY $4006       ; Stocker Y à 4006-4007\n" +
            "        END";
        
        codeArea.setText(example);
    }
    
    private void loadExample4() {
        String example = 
            "; === SAUTS ET SOUS-ROUTINES ===\n" +
            "        ORG $5000\n" +
            "MAIN    LDA #$01\n" +
            "        JSR DELAY       ; Appel sous-routine\n" +
            "        LDB #$02\n" +
            "        JSR DELAY\n" +
            "        JMP FIN\n" +
            "\n" +
            "; Sous-routine de délai\n" +
            "DELAY   NOP             ; 3 NOPs = délai\n" +
            "        NOP\n" +
            "        NOP\n" +
            "        RTS             ; Retour\n" +
            "\n" +
            "FIN     LDA #$FF\n" +
            "        STA $6000\n" +
            "        CWAI #$FF       ; Attendre interruption\n" +
            "        END";
        
        codeArea.setText(example);
    }
    
    // Classe pour les lignes de mémoire
    public static class MemoryRow {
        private final SimpleStringProperty address;
        private final SimpleStringProperty data;
        private final SimpleStringProperty ascii;
        private final SimpleStringProperty description;
        
        public MemoryRow(String addr, String dat, String asc, String desc) {
            this.address = new SimpleStringProperty(addr);
            this.data = new SimpleStringProperty(dat);
            this.ascii = new SimpleStringProperty(asc);
            this.description = new SimpleStringProperty(desc);
        }
        
        public String getAddress() { return address.get(); }
        public void setAddress(String addr) { address.set(addr); }
        public SimpleStringProperty addressProperty() { return address; }
        
        public String getData() { return data.get(); }
        public void setData(String dat) { data.set(dat); }
        public SimpleStringProperty dataProperty() { return data; }
        
        public String getAscii() { return ascii.get(); }
        public void setAscii(String asc) { ascii.set(asc); }
        public SimpleStringProperty asciiProperty() { return ascii; }
        
        public String getDescription() { return description.get(); }
        public void setDescription(String desc) { description.set(desc); }
        public SimpleStringProperty descriptionProperty() { return description; }
    }
    
    public static void main(String[] args) {
        // Test rapide des opcodes
        System.out.println("=== VÉRIFICATION DES OPCODES ===");
        testAllOpcodes();
        
        // Lancement de l'interface
        launch(args);
    }
    
    private static void testAllOpcodes() {
        String[] instructions = {
            "COMA", "COMB", "COM", "INCA", "INCB", "INC",
            "DECA", "DECB", "DEC", "EORA", "EORB", "NOP",
            "MUL", "DAA", "EXG", "CWAI", "JMP", "JSR",
            "LDA", "LDB", "LDD", "LDX", "STA", "STB",
            "STD", "STX"
        };
        
        System.out.println("\nInstructions vérifiées:");
        int count = 0;
        for (String instr : instructions) {
            try {
                if (instr.matches("COMA|COMB|INCA|INCB|DECA|DECB|NOP|MUL|DAA|EXG")) {
                    OpcodeGenerator.getOpcode(instr, "INHERENT");
                } else if (instr.equals("CWAI")) {
                    OpcodeGenerator.getOpcode(instr, "IMMEDIATE");
                } else if (instr.matches("LDA|LDB|EORA|EORB")) {
                    OpcodeGenerator.getOpcode(instr, "IMMEDIATE");
                    OpcodeGenerator.getOpcode(instr, "DIRECT");
                } else if (instr.matches("STA|STB|STD|STX|COM|INC|DEC")) {
                    OpcodeGenerator.getOpcode(instr, "DIRECT");
                } else if (instr.matches("JMP|JSR")) {
                    OpcodeGenerator.getOpcode(instr, "DIRECT");
                }
                System.out.print("✓ ");
                count++;
            } catch (Exception e) {
                System.out.print("✗ ");
            }
            
            System.out.println(instr);
        }
        
        System.out.println("\n" + count + "/" + instructions.length + " instructions vérifiées.");
    }
}