package motorola6809.io;

public interface OutputDevice {
    
    /**
     * Écrit un octet vers le périphérique
     */
    void write(int data);
    
    /**
     * Vide le buffer de sortie
     */
    void flush();
    
    /**
     * Réinitialise le périphérique
     */
    void reset();
    
    /**
     * Obtient le nom du périphérique
     */
    String getName();
}