package motorola6809.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import motorola6809.assembler.Assembler;
import motorola6809.assembler.OpcodeGenerator;
import javafx.geometry.*;
import javafx.scene.text.Font;

public class SimpleTestApp extends Application {
    
    private TextArea inputArea;
    private TextArea outputArea;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Motorola 6809 Tester - Tous en un");
        
        // Panneau supérieur : Input
        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        
        Label inputLabel = new Label("Code Assembleur 6809:");
        inputLabel.setFont(Font.font("Arial", 16));
        
        inputArea = new TextArea();
        inputArea.setFont(Font.font("Monospaced", 12));
        inputArea.setPrefHeight(300);
        inputArea.setText(getDefaultCode());
        
        // Boutons
        HBox buttonBox = new HBox(10);
        
        Button testBtn = new Button("Tester Opcodes");
        testBtn.setOnAction(e -> testOpcodes());
        testBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        Button assembleBtn = new Button("Assembler");
        assembleBtn.setOnAction(e -> assembleCode());
        assembleBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button clearBtn = new Button("Effacer");
        clearBtn.setOnAction(e -> {
            inputArea.clear();
            outputArea.clear();
        });
        
        buttonBox.getChildren().addAll(testBtn, assembleBtn, clearBtn);
        
        inputBox.getChildren().addAll(inputLabel, inputArea, buttonBox);
        
        // Panneau inférieur : Output
        VBox outputBox = new VBox(10);
        outputBox.setPadding(new Insets(10));
        
        Label outputLabel = new Label("Résultats:");
        outputLabel.setFont(Font.font("Arial", 16));
        
        outputArea = new TextArea();
        outputArea.setFont(Font.font("Monospaced", 12));
        outputArea.setPrefHeight(300);
        outputArea.setEditable(false);
        
        outputBox.getChildren().addAll(outputLabel, outputArea);
        
        // SplitPane pour séparer input/output
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(inputBox, outputBox);
        splitPane.setDividerPositions(0.5);
        
        // Layout principal
        BorderPane root = new BorderPane();
        root.setCenter(splitPane);
        
        // Menu supérieur
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("Fichier");
        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.setOnAction(e -> primaryStage.close());
        fileMenu.getItems().add(exitItem);
        
        Menu examplesMenu = new Menu("Exemples");
        
        MenuItem ex1 = new MenuItem("1. Arithmétique");
        ex1.setOnAction(e -> inputArea.setText(getExample1()));
        
        MenuItem ex2 = new MenuItem("2. Logique");
        ex2.setOnAction(e -> inputArea.setText(getExample2()));
        
        MenuItem ex3 = new MenuItem("3. Transfert");
        ex3.setOnAction(e -> inputArea.setText(getExample3()));
        
        MenuItem ex4 = new MenuItem("4. Sauts");
        ex4.setOnAction(e -> inputArea.setText(getExample4()));
        
        examplesMenu.getItems().addAll(ex1, ex2, ex3, ex4);
        
        Menu helpMenu = new Menu("Aide");
        MenuItem aboutItem = new MenuItem("À propos");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, examplesMenu, helpMenu);
        root.setTop(menuBar);
        
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void testOpcodes() {
        StringBuilder result = new StringBuilder();
        result.append("=== TEST DE TOUS LES OPCODES ===\n\n");
        
        // Liste des instructions à tester
        String[][] tests = {
            {"COMA", "INHERENT", "43"},
            {"COMB", "INHERENT", "53"},
            {"INCA", "INHERENT", "4C"},
            {"INCB", "INHERENT", "5C"},
            {"DECA", "INHERENT", "4A"},
            {"DECB", "INHERENT", "5A"},
            {"NOP", "INHERENT", "12"},
            {"MUL", "INHERENT", "3D"},
            {"DAA", "INHERENT", "19"},
            {"EXG", "INHERENT", "1E"},
            {"CWAI", "IMMEDIATE", "3C"},
            {"LDA", "IMMEDIATE", "86"},
            {"LDB", "IMMEDIATE", "C6"},
            {"LDD", "IMMEDIATE", "CC"},
            {"LDX", "IMMEDIATE", "8E"},
            {"EORA", "IMMEDIATE", "88"},
            {"EORB", "IMMEDIATE", "C8"}
        };
        
        int passed = 0;
        int total = tests.length;
        
        for (String[] test : tests) {
            String mnemonic = test[0];
            String mode = test[1];
            String expected = test[2];
            
            try {
                int opcode = OpcodeGenerator.getOpcode(mnemonic, mode);
                String actual = String.format("%02X", opcode);
                
                if (actual.equals(expected)) {
                    result.append("✓ ");
                    passed++;
                } else {
                    result.append("✗ ");
                }
                
                result.append(String.format("%-6s %-12s: 0x%s", 
                    mnemonic, mode, actual));
                
                if (!actual.equals(expected)) {
                    result.append(" (attendu: 0x").append(expected).append(")");
                }
                
                result.append("\n");
                
            } catch (Exception e) {
                result.append("✗ ").append(mnemonic).append(" ").append(mode)
                      .append(": ERREUR - ").append(e.getMessage()).append("\n");
            }
        }
        
        result.append("\nRésultat: ").append(passed).append("/").append(total)
              .append(" tests réussis\n");
        
        outputArea.setText(result.toString());
    }
    
    private void assembleCode() {
        String code = inputArea.getText();
        
        if (code.trim().isEmpty()) {
            outputArea.setText("Erreur: Code source vide.");
            return;
        }
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(code);
            
            StringBuilder result = new StringBuilder();
            result.append("=== ASSEMBLAGE RÉUSSI ===\n\n");
            result.append("Taille: ").append(machineCode.length).append(" octets\n\n");
            result.append("Code machine (hex):\n");
            
            // Afficher en hex
            for (int i = 0; i < machineCode.length; i++) {
                if (i % 16 == 0) {
                    result.append(String.format("%04X: ", i));
                }
                result.append(String.format("%02X ", machineCode[i] & 0xFF));
                if (i % 16 == 15 || i == machineCode.length - 1) {
                    result.append("\n");
                }
            }
            
            result.append("\nVue détaillée:\n");
            result.append("Adr  Code Instruction\n");
            result.append("-------------------\n");
            
            // Simulation simple de désassemblage
            String[] lines = code.split("\n");
            int address = 0;
            
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith(";")) {
                    continue;
                }
                
                if (line.startsWith("ORG")) {
                    String addr = line.replaceAll(".*\\$", "").trim();
                    try {
                        address = Integer.parseInt(addr, 16);
                    } catch (NumberFormatException e) {
                        address = 0;
                    }
                    result.append(String.format("%04X     %s\n", address, line));
                } else if (!line.startsWith("END")) {
                    // Extraire le mnémonique
                    String mnemonic = line.split("\\s+")[0];
                    result.append(String.format("%04X     ??      %s\n", address, line));
                    address += estimateSize(mnemonic, line);
                }
            }
            
            outputArea.setText(result.toString());
            
        } catch (Exception e) {
            outputArea.setText("ERREUR D'ASSEMBLAGE:\n" + e.getMessage());
        }
    }
    
    private int estimateSize(String mnemonic, String line) {
        // Estimation simple de la taille
        if (mnemonic.matches("COMA|COMB|INCA|INCB|DECA|DECB|NOP|MUL|DAA|EXG|RTS")) {
            return 1;
        } else if (line.contains("#")) { // Immédiat
            if (mnemonic.matches("LDA|LDB|EORA|EORB|ADDA|ADDB")) {
                return 2;
            } else if (mnemonic.matches("LDD|LDX|LDY|LDS|LDU")) {
                return 3;
            }
        } else if (line.contains("$")) { // Adresse
            String addr = line.replaceAll(".*\\$", "");
            if (addr.length() <= 2) { // Page 0
                return 2;
            } else { // Étendu
                return 3;
            }
        }
        return 1;
    }
    
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Motorola 6809 Simulator - Tester");
        alert.setContentText("Version 1.0\n\n" +
                           "Interface de test complète pour le simulateur 6809.\n" +
                           "Inclut:\n" +
                           "- Test des opcodes\n" +
                           "- Assembleur\n" +
                           "- Exemples prédéfinis\n" +
                           "- Affichage des résultats");
        alert.showAndWait();
    }
    
    private String getDefaultCode() {
        return "; Exemple de programme Motorola 6809\n" +
               "        ORG $1000\n" +
               "START   LDA #$FF      ; Charger FF dans A\n" +
               "        LDB #$AA      ; Charger AA dans B\n" +
               "        STA $2000     ; Stocker A\n" +
               "        STB $2001     ; Stocker B\n" +
               "        MUL           ; Multiplier A*B\n" +
               "        END";
    }
    
    private String getExample1() {
        return "; === EXEMPLE 1: ARITHMÉTIQUE ===\n" +
               "        ORG $2000\n" +
               "        LDA #$05\n" +
               "        LDB #$04\n" +
               "        MUL           ; 5 * 4 = 20\n" +
               "        INCA          ; A++\n" +
               "        DECB          ; B--\n" +
               "        LDA #$99\n" +
               "        ADDA #$01\n" +
               "        DAA           ; Correction BCD\n" +
               "        END";
    }
    
    private String getExample2() {
        return "; === EXEMPLE 2: LOGIQUE ===\n" +
               "        ORG $3000\n" +
               "        LDA #%10101010  ; AA\n" +
               "        COMA            ; 55\n" +
               "        LDB #%01010101  ; 55\n" +
               "        COMB            ; AA\n" +
               "        EORA #%11110000 ; A5\n" +
               "        EORB #%00001111 ; A5\n" +
               "        END";
    }
    
    private String getExample3() {
        return "; === EXEMPLE 3: TRANSFERT ===\n" +
               "        ORG $4000\n" +
               "        LDA #$11\n" +
               "        LDB #$22\n" +
               "        LDD #$3344\n" +
               "        LDX #$1234\n" +
               "        EXG           ; Échanger A et B\n" +
               "        STA $5000\n" +
               "        STB $5001\n" +
               "        STD $5002\n" +
               "        STX $5004\n" +
               "        END";
    }
    
    private String getExample4() {
        return "; === EXEMPLE 4: SAUTS ===\n" +
               "        ORG $6000\n" +
               "MAIN    LDA #$01\n" +
               "        JSR DELAY\n" +
               "        LDB #$02\n" +
               "        JSR DELAY\n" +
               "        JMP FIN\n" +
               "DELAY   NOP\n" +
               "        NOP\n" +
               "        NOP\n" +
               "        RTS\n" +
               "FIN     CWAI #$FF\n" +
               "        END";
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}