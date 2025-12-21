package motorola6809.debugger;

public class Breakpoint {
    
    private int address;
    private boolean enabled;
    private String condition; // Pour les breakpoints conditionnels (optionnel)
    private int hitCount;
    
    public Breakpoint(int address) {
        this.address = address;
        this.enabled = true;
        this.condition = null;
        this.hitCount = 0;
    }
    
    public Breakpoint(int address, String condition) {
        this.address = address;
        this.enabled = true;
        this.condition = condition;
        this.hitCount = 0;
    }
    
    public void hit() {
        hitCount++;
    }
    
    public void reset() {
        hitCount = 0;
    }
    
    // Getters et Setters
    public int getAddress() {
        return address;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public int getHitCount() {
        return hitCount;
    }
    
    @Override
    public String toString() {
        return String.format("Breakpoint at %04X [%s] Hits: %d", 
            address, enabled ? "ON" : "OFF", hitCount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Breakpoint) {
            return this.address == ((Breakpoint) obj).address;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return address;
    }
}