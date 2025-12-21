// ArchitectureWindow.java - Version nettoyée et professionnelle
package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.*;

public class ArchitectureWindow {
    
    private Stage stage;
    
    // Labels pour les registres
    private static Label pcLabel;
    private static Label instruPcLabel;
    private static Label sLabel;
    private static Label uLabel;
    private static Label aLabel;
    private static Label bLabel;
    private static Label dpLabel;
    private static Label xLabel;
    private static Label yLabel;
    
    // Labels pour les flags
    private static Label eLabel;
    private static Label fLabel;
    private static Label hLabel;
    private static Label iLabel;
    private static Label nLabel;
    private static Label zLabel;
    private static Label vLabel;
    private static Label cLabel;
    
    public ArchitectureWindow() {
        stage = new Stage();
        stage.setTitle("Architecture 6809 - Vue Détaillée");
        stage.setResizable(false);
        
        // Pane principal
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        
        // Titre
        Label title = new Label("ARCHITECTURE MOTOROLA 6809");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));
        title.setPadding(new Insets(0, 0, 20, 0));
        
        // Conteneur principal avec grille
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        
        // Font pour les valeurs
        Font valueFont = Font.font("Monospaced", FontWeight.BOLD, 14);
        Font labelFont = Font.font("Arial", FontWeight.BOLD, 12);
        
        // Ligne 1: PC, S, U
        pcLabel = createRegisterField(grid, 0, 0, "PC", "0000", "Program Counter", labelFont, valueFont);
        sLabel = createRegisterField(grid, 1, 0, "S", "1FFF", "Stack Pointer", labelFont, valueFont);
        uLabel = createRegisterField(grid, 2, 0, "U", "1E00", "User Stack", labelFont, valueFont);
        
        // Ligne 2: A, B, D
        aLabel = createRegisterField(grid, 0, 1, "A", "00", "Accumulator A", labelFont, valueFont);
        bLabel = createRegisterField(grid, 1, 1, "B", "00", "Accumulator B", labelFont, valueFont);
        createRegisterField(grid, 2, 1, "D", "0000", "Double (A:B)", labelFont, valueFont);
        
        // Ligne 3: X, Y, DP
        xLabel = createRegisterField(grid, 0, 2, "X", "0000", "Index X", labelFont, valueFont);
        yLabel = createRegisterField(grid, 1, 2, "Y", "0000", "Index Y", labelFont, valueFont);
        dpLabel = createRegisterField(grid, 2, 2, "DP", "00", "Direct Page", labelFont, valueFont);
        
        // Instruction courante
        VBox instructionBox = new VBox(5);
        instructionBox.setPadding(new Insets(15, 0, 0, 0));
        
        Label instruTitle = new Label("Instruction en cours:");
        instruTitle.setFont(Font.font("Arial", 12));
        instruTitle.setTextFill(Color.web("#7f8c8d"));
        
        instruPcLabel = new Label("NOP");
        instruPcLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        instruPcLabel.setTextFill(Color.web("#2c3e50"));
        instruPcLabel.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 5 10 5 10; -fx-background-radius: 3;");
        
        instructionBox.getChildren().addAll(instruTitle, instruPcLabel);
        
        // Section Flags
        VBox flagsSection = new VBox(10);
        flagsSection.setPadding(new Insets(20, 0, 0, 0));
        
        Label flagsTitle = new Label("Condition Code Register");
        flagsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        flagsTitle.setTextFill(Color.web("#2c3e50"));
        
        // Grille des flags
        GridPane flagsGrid = new GridPane();
        flagsGrid.setHgap(8);
        flagsGrid.setVgap(5);
        flagsGrid.setPadding(new Insets(10));
        flagsGrid.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        
        String[] flagNames = {"E", "F", "H", "I", "N", "Z", "V", "C"};
        String[] flagDescriptions = {
            "Entire Flag",
            "FIRQ Mask",
            "Half Carry",
            "IRQ Mask",
            "Negative",
            "Zero",
            "Overflow",
            "Carry"
        };
        
        for (int i = 0; i < flagNames.length; i++) {
            VBox flagBox = new VBox(2);
            flagBox.setAlignment(Pos.CENTER);
            
            // Label du nom
            Label nameLabel = new Label(flagNames[i]);
            nameLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
            nameLabel.setTextFill(Color.web("#495057"));
            
            // Label de la valeur
            Label valueLabel = new Label(i == 5 ? "1" : "0");
            valueLabel.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
            valueLabel.setTextFill(i == 5 ? Color.GREEN : Color.web("#2c3e50"));
            valueLabel.setMinWidth(25);
            valueLabel.setAlignment(Pos.CENTER);
            valueLabel.setStyle("-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 3; -fx-padding: 3;");
            
            // Description
            Label descLabel = new Label(flagDescriptions[i]);
            descLabel.setFont(Font.font("Arial", 9));
            descLabel.setTextFill(Color.web("#6c757d"));
            
            flagBox.getChildren().addAll(nameLabel, valueLabel, descLabel);
            
            // Assigner aux labels statiques
            switch (flagNames[i]) {
                case "E": eLabel = valueLabel; break;
                case "F": fLabel = valueLabel; break;
                case "H": hLabel = valueLabel; break;
                case "I": iLabel = valueLabel; break;
                case "N": nLabel = valueLabel; break;
                case "Z": zLabel = valueLabel; break;
                case "V": vLabel = valueLabel; break;
                case "C": cLabel = valueLabel; break;
            }
            
            flagsGrid.add(flagBox, i, 0);
        }
        
        flagsSection.getChildren().addAll(flagsTitle, flagsGrid);
        
        root.getChildren().addAll(title, grid, instructionBox, flagsSection);
        
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setX(50);
        stage.setY(100);
    }
    
    private static Label createRegisterField(GridPane grid, int col, int row, String name, 
                                             String value, String description, Font labelFont, Font valueFont) {
        VBox box = new VBox(3);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 4;");
        box.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setFont(labelFont);
        nameLabel.setTextFill(Color.web("#495057"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(valueFont);
        valueLabel.setTextFill(Color.web("#2c3e50"));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 9));
        descLabel.setTextFill(Color.web("#6c757d"));
        descLabel.setWrapText(true);
        
        box.getChildren().addAll(nameLabel, valueLabel, descLabel);
        grid.add(box, col, row);
        
        return valueLabel;
    }
    
    // Méthodes statiques pour mettre à jour les valeurs
    public static void setPC(String value) {
        pcLabel.setText(value);
    }
    
    public static void setInstruPC(String value) {
        instruPcLabel.setText(value);
    }
    
    public static void setS(String value) {
        sLabel.setText(value);
    }
    
    public static void setU(String value) {
        uLabel.setText(value);
    }
    
    public static void setA(String value) {
        aLabel.setText(value);
    }
    
    public static void setB(String value) {
        bLabel.setText(value);
    }
    
    public static void setD(String value) {
        // Mettre à jour A et B à partir de D
        if (value.length() == 4) {
            aLabel.setText(value.substring(0, 2));
            bLabel.setText(value.substring(2));
        }
    }
    
    public static void setDP(String value) {
        dpLabel.setText(value);
    }
    
    public static void setX(String value) {
        xLabel.setText(value);
    }
    
    public static void setY(String value) {
        yLabel.setText(value);
    }
    
    public static void setE(String value) {
        eLabel.setText(value);
        eLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setF(String value) {
        fLabel.setText(value);
        fLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setH(String value) {
        hLabel.setText(value);
        hLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setI(String value) {
        iLabel.setText(value);
        iLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setN(String value) {
        nLabel.setText(value);
        nLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setZ(String value) {
        zLabel.setText(value);
        zLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setV(String value) {
        vLabel.setText(value);
        vLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void setC(String value) {
        cLabel.setText(value);
        cLabel.setTextFill(value.equals("1") ? Color.GREEN : Color.web("#2c3e50"));
    }
    
    public static void resetValues() {
        setPC("0000");
        setInstruPC("NOP");
        setS("1FFF");
        setU("1E00");
        setA("00");
        setB("00");
        setDP("00");
        setX("0000");
        setY("0000");
        setE("0");
        setF("0");
        setH("0");
        setI("0");
        setN("0");
        setZ("1");
        setV("0");
        setC("0");
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