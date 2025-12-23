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

public class ArchitectureWindow {
    
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
    
    // Boutons de rafraîchissement
    private Button refreshBtn;
    private Button editBtn;
    
    public ArchitectureWindow(SimulatorBackend backend) {
        this.backend = backend;
        this.stage = new Stage();
        stage.setTitle("Architecture 6809 - Vue Dynamique");
        stage.setResizable(false);
        
        createUI();
    }
    
    private void createUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        // Titre
        Label title = new Label("ARCHITECTURE MOTOROLA 6809");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        
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
        refreshBtn.setOnAction(e -> refresh());
        
        editBtn = new Button("✏️ Éditer les registres");
        editBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 8 15;");
        editBtn.setOnAction(e -> openRegisterEditor());
        
        buttonBox.getChildren().addAll(refreshBtn, editBtn);
        
        root.getChildren().addAll(title, grid, flagsSection, buttonBox);
        
        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene);
        stage.setX(50);
        stage.setY(100);
        
        // Initial refresh
        refresh();
    }
    
    private Label createRegisterField(GridPane grid, int col, int row, String name, 
                                     String defaultValue, String description, 
                                     Font labelFont, Font valueFont) {
        VBox box = new VBox(3);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 4;");
        box.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(labelFont);
        nameLabel.setTextFill(Color.web("#495057"));
        
        Label valueLabel = new Label(defaultValue);
        valueLabel.setFont(valueFont);
        valueLabel.setTextFill(Color.web("#2c3e50"));
        valueLabel.setMinWidth(60);
        valueLabel.setAlignment(Pos.CENTER);
        
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
                           "-fx-border-radius: 3; -fx-padding: 3;");
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 8));
        descLabel.setTextFill(Color.web("#6c757d"));
        
        box.getChildren().addAll(nameLabel, valueLabel, descLabel);
        grid.add(box, col, row);
        
        return valueLabel;
    }
    
    // Méthodes publiques pour mettre à jour l'affichage
    public void refresh() {
        // Mettre à jour les registres depuis le backend
        pcLabel.setText(formatHex16(backend.getPC()));
        aLabel.setText(formatHex8(backend.getA()));
        bLabel.setText(formatHex8(backend.getB()));
        xLabel.setText(formatHex16(backend.getX()));
        yLabel.setText(formatHex16(backend.getY()));
        sLabel.setText(formatHex16(backend.getS()));
        uLabel.setText(formatHex16(backend.getU()));
        dpLabel.setText(formatHex8(backend.getDP()));
        dLabel.setText(formatHex16(backend.getD()));
        
        // Mettre à jour les flags
        updateFlagDisplay(eLabel, backend.getFlag("E"));
        updateFlagDisplay(fLabel, backend.getFlag("F"));
        updateFlagDisplay(hLabel, backend.getFlag("H"));
        updateFlagDisplay(iLabel, backend.getFlag("I"));
        updateFlagDisplay(nLabel, backend.getFlag("N"));
        updateFlagDisplay(zLabel, backend.getFlag("Z"));
        updateFlagDisplay(vLabel, backend.getFlag("V"));
        updateFlagDisplay(cLabel, backend.getFlag("C"));
    }
    
    private void updateFlagDisplay(Label flagLabel, boolean value) {
        flagLabel.setText(value ? "1" : "0");
        flagLabel.setTextFill(value ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    private String formatHex16(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
    
    private String formatHex8(int value) {
        return String.format("%02X", value & 0xFF);
    }
    
    private void openRegisterEditor() {
        // Créer une fenêtre d'édition des registres
        Stage editorStage = new Stage();
        editorStage.setTitle("Éditeur de Registres");
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        // Champs d'édition pour chaque registre
        TextField pcField = createEditField("PC", backend.getPC(), 16);
        TextField aField = createEditField("A", backend.getA(), 8);
        TextField bField = createEditField("B", backend.getB(), 8);
        TextField xField = createEditField("X", backend.getX(), 16);
        TextField yField = createEditField("Y", backend.getY(), 16);
        TextField sField = createEditField("S", backend.getS(), 16);
        TextField uField = createEditField("U", backend.getU(), 16);
        TextField dpField = createEditField("DP", backend.getDP(), 8);
        
        grid.add(new Label("PC:"), 0, 0);
        grid.add(pcField, 1, 0);
        grid.add(new Label("A:"), 0, 1);
        grid.add(aField, 1, 1);
        grid.add(new Label("B:"), 0, 2);
        grid.add(bField, 1, 2);
        grid.add(new Label("X:"), 2, 0);
        grid.add(xField, 3, 0);
        grid.add(new Label("Y:"), 2, 1);
        grid.add(yField, 3, 1);
        grid.add(new Label("S:"), 2, 2);
        grid.add(sField, 3, 2);
        grid.add(new Label("U:"), 4, 0);
        grid.add(uField, 5, 0);
        grid.add(new Label("DP:"), 4, 1);
        grid.add(dpField, 5, 1);
        
        Button applyBtn = new Button("Appliquer");
        applyBtn.setOnAction(e -> {
            applyRegisterChanges(pcField, aField, bField, xField, yField, sField, uField, dpField);
            refresh();
            editorStage.close();
        });
        
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> editorStage.close());
        
        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(applyBtn, cancelBtn);
        
        root.getChildren().addAll(new Label("Modifier les registres:"), grid, buttons);
        
        Scene scene = new Scene(root, 400, 250);
        editorStage.setScene(scene);
        editorStage.show();
    }
    
    private TextField createEditField(String regName, int value, int bits) {
        TextField field = new TextField();
        field.setText(bits == 16 ? formatHex16(value) : formatHex8(value));
        field.setPrefWidth(70);
        field.setUserData(new Object[]{regName, bits});
        return field;
    }
    
    private void applyRegisterChanges(TextField... fields) {
        for (TextField field : fields) {
            try {
                Object[] data = (Object[]) field.getUserData();
                String regName = (String) data[0];
                int bits = (int) data[1];
                String text = field.getText().trim();
                
                if (text.isEmpty()) continue;
                
                int value = Integer.parseInt(text.replace("$", ""), 16);
                
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
                
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Valeur invalide pour " + field.getText());
            }
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
    
    public void hide() {
        stage.hide();
    }
    
    public boolean isShowing() {
        return stage.isShowing();
    }
}