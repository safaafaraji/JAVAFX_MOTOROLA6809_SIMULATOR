package motorola6809.utils;

public class HexConverter {
	
    
    /**
     * Convertit une chaîne hexadécimale en entier
     */
    public static int hexToInt(String hex) {
        if (hex == null || hex.isEmpty()) {
            return 0;
        }
        // Retirer le préfixe $ si présent
        if (hex.startsWith("$")) {
            hex = hex.substring(1);
        }
        // Retirer le préfixe 0x si présent
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2);
        }
        return Integer.parseInt(hex, 16);
    }
    
    /**
     * Convertit un entier en chaîne hexadécimale (2 chiffres)
     */
    public static String intToHex8(int value) {
        String hex = Integer.toHexString(value & 0xFF).toUpperCase();
        return hex.length() == 1 ? "0" + hex : hex;
    }
    
    /**
     * Convertit un entier en chaîne hexadécimale (4 chiffres)
     */
    public static String intToHex16(int value) {
        String hex = Integer.toHexString(value & 0xFFFF).toUpperCase();
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return hex;
    }
    
    /**
     * Convertit hexadécimal en binaire
     */
    public static String hexToBinary(String hex, int bits) {
        int value = hexToInt(hex);
        String binary = Integer.toBinaryString(value);
        while (binary.length() < bits) {
            binary = "0" + binary;
        }
        return binary.substring(Math.max(0, binary.length() - bits));
    }
    
    /**
     * Convertit binaire en hexadécimal
     */
    public static String binaryToHex(String binary, int hexDigits) {
        int value = Integer.parseInt(binary, 2);
        String hex = Integer.toHexString(value).toUpperCase();
        while (hex.length() < hexDigits) {
            hex = "0" + hex;
        }
        return hex;
    }
    
    /**
     * Vérifie si une chaîne est un nombre hexadécimal valide
     */
    public static boolean isValidHex(String hex) {
        if (hex == null || hex.isEmpty()) {
            return false;
        }
        if (hex.startsWith("$")) {
            hex = hex.substring(1);
        }
        return hex.matches("[0-9A-Fa-f]+");
    }
}