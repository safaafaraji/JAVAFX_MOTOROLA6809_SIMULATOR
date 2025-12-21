package motorola6809.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ROMWindow {
    
    private Stage stage;
    private static TableView<MemoryRow> table;
    private static ObservableList<MemoryRow> data;
    
    public ROMWindow() {
        stage = new Stage();
        stage.setTitle("ROM");
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        
        VBox root = new VBox();
        
        table = new TableView<>();
        
        TableColumn<MemoryRow, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(100);
        
        TableColumn<MemoryRow, String> dataCol = new TableColumn<>("Data");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        dataCol.setPrefWidth(100);
        
        // FIX Warning: Cast explicite
        table.getColumns().addAll(addressCol, dataCol);
        
        data = FXCollections.observableArrayList();
        for (int i = 5120; i < 6144; i++) {
            String address = String.format("%04X", i);
            data.add(new MemoryRow(address, "FF"));
        }
        table.setItems(data);
        
        root.getChildren().add(table);
        
        Scene scene = new Scene(root, 212, 276);
        stage.setScene(scene);
        stage.setX(750);
        stage.setY(120);
    }
    
    public static void setData(int index, String value) {
        if (index >= 0 && index < data.size()) {
            data.get(index).setData(value);
            table.refresh();
        }
    }
    
    public static String getData(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index).getData();
        }
        return "FF";
    }
    
    public static String getAddress(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index).getAddress();
        }
        return "0000";
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
    
    public static class MemoryRow {
        private String address;
        private String data;
        
        public MemoryRow(String address, String data) {
            this.address = address;
            this.data = data;
        }
        
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
    }
}