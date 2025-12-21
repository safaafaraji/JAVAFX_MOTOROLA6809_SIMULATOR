package motorola6809.io;

public interface InputDevice {
    
    /**
     * Lit un octet depuis le périphérique
     */
    int read();
    
    /**
     * Vérifie si des données sont disponibles
     */
    boolean hasData();
    
    /**
     * Réinitialise le périphérique
     */
    void reset();
    
    /**
     * Obtient le nom du périphérique
     */
    String getName();
}