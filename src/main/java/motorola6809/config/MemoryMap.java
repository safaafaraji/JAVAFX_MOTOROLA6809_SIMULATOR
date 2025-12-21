package motorola6809.config;

public class MemoryMap {
    
    private int ramStart;
    private int ramEnd;
    private int romStart;
    private int romEnd;
    
    public MemoryMap() {
        this.ramStart = Constants.RAM_START;
        this.ramEnd = Constants.RAM_END;
        this.romStart = Constants.ROM_START;
        this.romEnd = Constants.ROM_END;
    }
    
    /**
     * Vérifie si une adresse est dans la RAM
     */
    public boolean isRAM(int address) {
        return address >= ramStart && address <= ramEnd;
    }
    
    /**
     * Vérifie si une adresse est dans la ROM
     */
    public boolean isROM(int address) {
        return address >= romStart && address <= romEnd;
    }
    
    /**
     * Vérifie si une adresse est accessible
     */
    public boolean isAccessible(int address) {
        return isRAM(address) || isROM(address);
    }
    
    /**
     * Obtient le type de zone mémoire
     */
    public String getMemoryType(int address) {
        if (isRAM(address)) return "RAM";
        if (isROM(address)) return "ROM";
        return "UNMAPPED";
    }
    
    // Getters
    public int getRamStart() { return ramStart; }
    public int getRamEnd() { return ramEnd; }
    public int getRomStart() { return romStart; }
    public int getRomEnd() { return romEnd; }
}