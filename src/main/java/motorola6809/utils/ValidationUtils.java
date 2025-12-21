package motorola6809.utils;

public class ValidationUtils {
    
    /**
     * Valide une adresse mémoire 16 bits
     */
    public static boolean isValidAddress(int address) {
        return address >= 0 && address <= 0xFFFF;
    }
    
    /**
     * Valide une valeur 8 bits
     */
    public static boolean isValid8Bit(int value) {
        return value >= 0 && value <= 0xFF;
    }
    
    /**
     * Valide une valeur 16 bits
     */
    public static boolean isValid16Bit(int value) {
        return value >= 0 && value <= 0xFFFF;
    }
    
    /**
     * Valide un nom de registre
     */
    public static boolean isValidRegister(String register) {
        return register != null && register.matches("[ABDXYUSP]|DP|PC|CC");
    }
    
    /**
     * Valide une ligne d'assembleur
     */
    public static boolean isValidAsmLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return true; // Ligne vide est valide
        }
        line = line.trim();
        // Commentaire
        if (line.startsWith(";")) {
            return true;
        }
        return true; // Validation plus poussée dans le parser
    }
    
    /**
     * Normalise une valeur à 8 bits
     */
    public static int normalize8Bit(int value) {
        return value & 0xFF;
    }
    
    /**
     * Normalise une valeur à 16 bits
     */
    public static int normalize16Bit(int value) {
        return value & 0xFFFF;
    }
}