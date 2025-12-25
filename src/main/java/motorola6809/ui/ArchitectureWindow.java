package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.*;
import motorola6809.core.SimulatorBackend;
import javafx.application.Platform;

public class ArchitectureWindow implements SimulatorBackend.SimulatorObserver {
    
    private Stage stage;
    private SimulatorBackend backend;
    
    // Labels pour les registres
    private Label pcLabel;
    private Label aLabel;
    private Label bLabel;
    private Label xLabel;
    private Label yLabel;
    private Label sLabel;
    private Label uLabel;
    private Label dpLabel;
    private Label dLabel;
    
    // Labels pour les flags
    private Label eLabel;
    private Label fLabel;
    private Label hLabel;
    private Label iLabel;
    private Label nLabel;
    private Label zLabel;
    private Label vLabel;
    private Label cLabel;
    
    // Boutons
    private Button refreshBtn;
    private Button editBtn;
    private Button autoRefreshBtn;
    private boolean autoRefresh = true;
    
    public ArchitectureWindow(SimulatorBackend backend) {
        this.backend = backend;
        this.stage = new Stage();
        stage.setTitle("Architecture 6809 - Vue Dynamique");
        
        // S'enregistrer comme observateur
        backend.addObserver(this);
        
        createUI();
    }
    
    private void createUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // Titre avec état
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("ARCHITECTURE MOTOROLA 6809");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label statusLabel = new Label("● En attente");
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setTextFill(Color.GRAY);
        
        titleBox.getChildren().addAll(title, statusLabel);
        
        // Conteneur principal
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Font
        Font labelFont = Font.font("Arial", FontWeight.BOLD, 12);
        Font valueFont = Font.font("Monospaced", FontWeight.BOLD, 14);
        
        // Créer les champs de registres
        pcLabel = createRegisterField(grid, 0, 0, "PC", "0000", "Program Counter", labelFont, valueFont);
        aLabel = createRegisterField(grid, 1, 0, "A", "00", "Accumulator A", labelFont, valueFont);
        bLabel = createRegisterField(grid, 2, 0, "B", "00", "Accumulator B", labelFont, valueFont);
        
        xLabel = createRegisterField(grid, 0, 1, "X", "0000", "Index X", labelFont, valueFont);
        yLabel = createRegisterField(grid, 1, 1, "Y", "0000", "Index Y", labelFont, valueFont);
        dLabel = createRegisterField(grid, 2, 1, "D", "0000", "Double (A:B)", labelFont, valueFont);
        
        sLabel = createRegisterField(grid, 0, 2, "S", "1FFF", "Stack Pointer", labelFont, valueFont);
        uLabel = createRegisterField(grid, 1, 2, "U", "1E00", "User Stack", labelFont, valueFont);
        dpLabel = createRegisterField(grid, 2, 2, "DP", "00", "Direct Page", labelFont, valueFont);
        
        // Section Flags
        VBox flagsSection = new VBox(10);
        flagsSection.setPadding(new Insets(15, 0, 0, 0));
        
        Label flagsTitle = new Label("Condition Code Register (CCR)");
        flagsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        flagsTitle.setTextFill(Color.web("#2c3e50"));
        
        GridPane flagsGrid = new GridPane();
        flagsGrid.setHgap(10);
        flagsGrid.setVgap(5);
        flagsGrid.setPadding(new Insets(10));
        flagsGrid.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        
        // Créer les flags
        eLabel = createFlagField(flagsGrid, 0, 0, "E", "0", "Entire Flag");
        fLabel = createFlagField(flagsGrid, 1, 0, "F", "0", "FIRQ Mask");
        hLabel = createFlagField(flagsGrid, 2, 0, "H", "0", "Half Carry");
        iLabel = createFlagField(flagsGrid, 3, 0, "I", "0", "IRQ Mask");
        nLabel = createFlagField(flagsGrid, 4, 0, "N", "0", "Negative");
        zLabel = createFlagField(flagsGrid, 5, 0, "Z", "1", "Zero");
        vLabel = createFlagField(flagsGrid, 6, 0, "V", "0", "Overflow");
        cLabel = createFlagField(flagsGrid, 7, 0, "C", "0", "Carry");
        
        flagsSection.getChildren().addAll(flagsTitle, flagsGrid);
        
        // Boutons de contrôle
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        refreshBtn = new Button("🔄 Rafraîchir");
        refreshBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 15;");
        refreshBtn.setOnAction(e -> manualRefresh());
        
        autoRefreshBtn = new Button("✅ Auto-rafraîchir");
        autoRefreshBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 8 15;");
        autoRefreshBtn.setOnAction(e -> toggleAutoRefresh());
        
        editBtn = new Button("✏️ Éditer les registres");
        editBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-padding: 8 15;");
        editBtn.setOnAction(e -> openRegisterEditor());
        
        buttonBox.getChildren().addAll(refreshBtn, autoRefreshBtn, editBtn);
        
        root.getChildren().addAll(titleBox, grid, flagsSection, buttonBox);
        
        Scene scene = new Scene(root, 550, 500);
        stage.setScene(scene);
        stage.setX(50);
        stage.setY(100);
        
        // Rafraîchissement initial
        manualRefresh();
    }
    
    private Label createRegisterField(GridPane grid, int col, int row, String name, 
                                     String defaultValue, String description, 
                                     Font labelFont, Font valueFont) {
        VBox box = new VBox(3);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dee2e6; " +
                    "-fx-border-radius: 4; -fx-border-width: 2;");
        box.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(labelFont);
        nameLabel.setTextFill(Color.web("#495057"));
        
        Label valueLabel = new Label(defaultValue);
        valueLabel.setFont(valueFont);
        valueLabel.setTextFill(Color.web("#2c3e50"));
        valueLabel.setMinWidth(60);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; " +
                           "-fx-border-width: 1; -fx-padding: 2;");
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 9));
        descLabel.setTextFill(Color.web("#6c757d"));
        
        box.getChildren().addAll(nameLabel, valueLabel, descLabel);
        grid.add(box, col, row);
        
        return valueLabel;
    }
    
    private Label createFlagField(GridPane grid, int col, int row, String name, 
                                 String defaultValue, String description) {
        VBox box = new VBox(2);
        box.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.web("#495057"));
        
        Label valueLabel = new Label(defaultValue);
        valueLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        valueLabel.setTextFill(Color.GREEN);
        valueLabel.setMinWidth(25);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; " +
                           "-fx-border-radius: 3; -fx-padding: 3; -fx-border-width: 1;");
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 8));
        descLabel.setTextFill(Color.web("#6c757d"));
        
        box.getChildren().addAll(nameLabel, valueLabel, descLabel);
        grid.add(box, col, row);
        
        return valueLabel;
    }
    
    // ==================== IMPLÉMENTATION OBSERVER ====================
    
    @Override
    public void onRegisterUpdate(String register, int value) {
        if (!autoRefresh) return;
        
        Platform.runLater(() -> {
            try {
                switch (register) {
                    case "PC":
                        pcLabel.setText(formatHex16(value));
                        highlightRegister(pcLabel);
                        break;
                    case "A":
                        aLabel.setText(formatHex8(value));
                        dLabel.setText(formatHex16(backend.getD())); // Mettre à jour D aussi
                        highlightRegister(aLabel);
                        break;
                    case "B":
                        bLabel.setText(formatHex8(value));
                        dLabel.setText(formatHex16(backend.getD())); // Mettre à jour D aussi
                        highlightRegister(bLabel);
                        break;
                    case "D":
                        dLabel.setText(formatHex16(value));
                        highlightRegister(dLabel);
                        break;
                    case "X":
                        xLabel.setText(formatHex16(value));
                        highlightRegister(xLabel);
                        break;
                    case "Y":
                        yLabel.setText(formatHex16(value));
                        highlightRegister(yLabel);
                        break;
                    case "S":
                        sLabel.setText(formatHex16(value));
                        highlightRegister(sLabel);
                        break;
                    case "U":
                        uLabel.setText(formatHex16(value));
                        highlightRegister(uLabel);
                        break;
                    case "DP":
                        dpLabel.setText(formatHex8(value));
                        highlightRegister(dpLabel);
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour du registre " + register + ": " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onFlagUpdate(String flag, boolean value) {
        if (!autoRefresh) return;
        
        Platform.runLater(() -> {
            try {
                switch (flag) {
                    case "E": updateFlagDisplay(eLabel, value); break;
                    case "F": updateFlagDisplay(fLabel, value); break;
                    case "H": updateFlagDisplay(hLabel, value); break;
                    case "I": updateFlagDisplay(iLabel, value); break;
                    case "N": updateFlagDisplay(nLabel, value); break;
                    case "Z": updateFlagDisplay(zLabel, value); break;
                    case "V": updateFlagDisplay(vLabel, value); break;
                    case "C": updateFlagDisplay(cLabel, value); break;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour du flag " + flag + ": " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onMemoryUpdate(int address, int value) {
        // Pas utilisé ici
    }
    
    @Override
    public void onExecutionStateChange(boolean running, boolean paused) {
        Platform.runLater(() -> {
            try {
                if (running) {
                    if (paused) {
                        stage.setTitle("Architecture 6809 - ⏸ En pause");
                    } else {
                        stage.setTitle("Architecture 6809 - ▶ En cours");
                    }
                } else {
                    stage.setTitle("Architecture 6809 - ⏹ Arrêté");
                }
                
                // Rafraîchir tous les registres quand l'état change
                if (autoRefresh) {
                    manualRefresh();
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du changement d'état: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onProgramLoaded(int startAddress, int size) {
        Platform.runLater(() -> {
            try {
                stage.setTitle("Architecture 6809 - Programme chargé (" + size + " octets)");
                // Rafraîchir tous les registres
                manualRefresh();
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement du programme: " + e.getMessage());
            }
        });
    }
    
    @Override
    public void onExecutionStep(int pc, int opcode, int cycles) {
        Platform.runLater(() -> {
            try {
                // Mettre à jour le titre avec l'adresse courante
                stage.setTitle("Architecture 6809 - PC: $" + formatHex16(pc));
            } catch (Exception e) {
                System.err.println("Erreur lors du pas d'exécution: " + e.getMessage());
            }
        });
    }
    
    // ==================== MÉTHODES UTILITAIRES ====================
    
    private void highlightRegister(Label label) {
        // Sauvegarder le style original
        String originalStyle = label.getStyle();
        
        // Appliquer le surlignage
        label.setStyle("-fx-background-color: #e8f5e8; -fx-border-color: #4CAF50; " +
                      "-fx-border-width: 2; -fx-border-radius: 3; " +
                      "-fx-text-fill: #2c3e50;");
        
        // Retirer le surlignage après 300ms
        new Thread(() -> {
            try {
                Thread.sleep(300);
                Platform.runLater(() -> {
                    label.setStyle(originalStyle);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Erreur lors de la suppression du surlignage: " + e.getMessage());
            }
        }).start();
    }
    
    private void updateFlagDisplay(Label flagLabel, boolean value) {
        flagLabel.setText(value ? "1" : "0");
        flagLabel.setTextFill(value ? Color.GREEN : Color.web("#2c3e50"));
        
        // Animation de changement
        String originalStyle = flagLabel.getStyle();
        flagLabel.setStyle("-fx-border-color: #FF9800; -fx-border-width: 2;");
        
        new Thread(() -> {
            try {
                Thread.sleep(200);
                Platform.runLater(() -> {
                    flagLabel.setStyle(originalStyle);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'animation du flag: " + e.getMessage());
            }
        }).start();
    }
    
    private void manualRefresh() {
        Platform.runLater(() -> {
            try {
                // Mettre à jour tous les registres
                pcLabel.setText(formatHex16(backend.getPC()));
                aLabel.setText(formatHex8(backend.getA()));
                bLabel.setText(formatHex8(backend.getB()));
                xLabel.setText(formatHex16(backend.getX()));
                yLabel.setText(formatHex16(backend.getY()));
                sLabel.setText(formatHex16(backend.getS()));
                uLabel.setText(formatHex16(backend.getU()));
                dpLabel.setText(formatHex8(backend.getDP()));
                dLabel.setText(formatHex16(backend.getD()));
                
                // Mettre à jour tous les flags
                updateFlagDisplay(eLabel, backend.getFlag("E"));
                updateFlagDisplay(fLabel, backend.getFlag("F"));
                updateFlagDisplay(hLabel, backend.getFlag("H"));
                updateFlagDisplay(iLabel, backend.getFlag("I"));
                updateFlagDisplay(nLabel, backend.getFlag("N"));
                updateFlagDisplay(zLabel, backend.getFlag("Z"));
                updateFlagDisplay(vLabel, backend.getFlag("V"));
                updateFlagDisplay(cLabel, backend.getFlag("C"));
            } catch (Exception e) {
                System.err.println("Erreur lors du rafraîchissement manuel: " + e.getMessage());
            }
        });
    }
    
    private void toggleAutoRefresh() {
        autoRefresh = !autoRefresh;
        Platform.runLater(() -> {
            try {
                if (autoRefresh) {
                    autoRefreshBtn.setText("✅ Auto-rafraîchir");
                    autoRefreshBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                    // Rafraîchir immédiatement quand on active
                    manualRefresh();
                } else {
                    autoRefreshBtn.setText("⭕ Auto-rafraîchir");
                    autoRefreshBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du basculement auto-refresh: " + e.getMessage());
            }
        });
    }
    
    private String formatHex16(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
    
    private String formatHex8(int value) {
        return String.format("%02X", value & 0xFF);
    }
    
    private void openRegisterEditor() {
        Platform.runLater(() -> {
            try {
                // Fenêtre d'édition des registres
                Stage editorStage = new Stage();
                editorStage.setTitle("Éditeur de Registres");
                
                VBox root = new VBox(10);
                root.setPadding(new Insets(15));
                
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                
                // Champs d'édition
                TextField[] fields = new TextField[8];
                String[] labels = {"PC", "A", "B", "X", "Y", "S", "U", "DP"};
                int[] values = {
                    backend.getPC(), backend.getA(), backend.getB(),
                    backend.getX(), backend.getY(), backend.getS(),
                    backend.getU(), backend.getDP()
                };
                int[] bits = {16, 8, 8, 16, 16, 16, 16, 8};
                
                for (int i = 0; i < labels.length; i++) {
                    grid.add(new Label(labels[i] + ":"), 0, i);
                    fields[i] = new TextField();
                    fields[i].setText(bits[i] == 16 ? formatHex16(values[i]) : formatHex8(values[i]));
                    fields[i].setPrefWidth(80);
                    fields[i].setUserData(new Object[]{labels[i], bits[i]});
                    grid.add(fields[i], 1, i);
                }
                
                Button applyBtn = new Button("Appliquer");
                applyBtn.setOnAction(e -> {
                    for (TextField field : fields) {
                        try {
                            Object[] data = (Object[]) field.getUserData();
                            String regName = (String) data[0];
                            int bitsSize = (int) data[1];
                            String text = field.getText().trim();
                            
                            if (text.isEmpty()) continue;
                            
                            int value;
                            if (text.startsWith("$")) {
                                value = Integer.parseInt(text.substring(1), 16);
                            } else if (text.startsWith("0x")) {
                                value = Integer.parseInt(text.substring(2), 16);
                            } else {
                                value = Integer.parseInt(text, 16);
                            }
                            
                            switch (regName) {
                                case "PC": backend.setPC(value); break;
                                case "A": backend.setA(value); break;
                                case "B": backend.setB(value); break;
                                case "X": backend.setX(value); break;
                                case "Y": backend.setY(value); break;
                                case "S": backend.setS(value); break;
                                case "U": backend.setU(value); break;
                                case "DP": backend.setDP(value); break;
                            }
                            
                        } catch (NumberFormatException ex) {
                            showAlert("Erreur", "Valeur hexadécimale invalide: " + field.getText());
                        }
                    }
                    editorStage.close();
                    // Rafraîchir l'affichage
                    manualRefresh();
                });
                
                Button cancelBtn = new Button("Annuler");
                cancelBtn.setOnAction(e -> editorStage.close());
                
                HBox buttons = new HBox(10);
                buttons.getChildren().addAll(applyBtn, cancelBtn);
                
                root.getChildren().addAll(new Label("Modifier les registres:"), grid, buttons);
                
                Scene scene = new Scene(root, 250, 350);
                editorStage.setScene(scene);
                editorStage.show();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'ouverture de l'éditeur: " + e.getMessage());
            }
        });
    }
    
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    public void show() {
        Platform.runLater(() -> {
            try {
                stage.show();
                // Rafraîchir l'affichage quand la fenêtre s'ouvre
                manualRefresh();
            } catch (Exception e) {
                System.err.println("Erreur lors de l'affichage de la fenêtre: " + e.getMessage());
            }
        });
    }
    
    public void hide() {
        Platform.runLater(() -> {
            try {
                stage.hide();
            } catch (Exception e) {
                System.err.println("Erreur lors du masquage de la fenêtre: " + e.getMessage());
            }
        });
    }
    
    public boolean isShowing() {
        return stage.isShowing();
    }
}