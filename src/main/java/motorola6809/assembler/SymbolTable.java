package motorola6809.assembler;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    
    private Map<String, Integer> symbols;
    private Map<String, Integer> constants;
    
    public SymbolTable() {
        this.symbols = new HashMap<>();
        this.constants = new HashMap<>();
    }
    
    public void addLabel(String label, int address) {
        symbols.put(label.toUpperCase(), address);
    }
    
    public void addConstant(String name, int value) {
        constants.put(name.toUpperCase(), value);
    }
    
    public Integer getAddress(String label) {
        return symbols.get(label.toUpperCase());
    }
    
    public Integer getConstant(String name) {
        return constants.get(name.toUpperCase());
    }
    
    public boolean hasLabel(String label) {
        return symbols.containsKey(label.toUpperCase());
    }
    
    public boolean hasConstant(String name) {
        return constants.containsKey(name.toUpperCase());
    }
    
    public void clear() {
        symbols.clear();
        constants.clear();
    }
    
    public Map<String, Integer> getSymbols() {
        return new HashMap<>(symbols);
    }
}