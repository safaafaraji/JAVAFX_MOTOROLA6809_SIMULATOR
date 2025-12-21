package motorola6809.config;

public class Configuration {
    
    private static Configuration instance;
    private MemoryMap memoryMap;
    private boolean debugMode;
    private int executionSpeed; // Instructions par seconde (0 = max)
    
    private Configuration() {
        this.memoryMap = new MemoryMap();
        this.debugMode = false;
        this.executionSpeed = 0; // Vitesse maximale par défaut
    }
    
    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    /**
     * Réinitialise la configuration aux valeurs par défaut
     */
    public void reset() {
        this.memoryMap = new MemoryMap();
        this.debugMode = false;
        this.executionSpeed = 0;
    }
    
    // Getters et Setters
    public MemoryMap getMemoryMap() {
        return memoryMap;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public int getExecutionSpeed() {
        return executionSpeed;
    }
    
    public void setExecutionSpeed(int speed) {
        this.executionSpeed = Math.max(0, speed);
    }
}