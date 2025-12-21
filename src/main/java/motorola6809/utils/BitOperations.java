package motorola6809.utils;

public class BitOperations {
    
    /**
     * Teste si un bit spécifique est défini
     */
    public static boolean testBit(int value, int bitPosition) {
        return ((value >> bitPosition) & 1) == 1;
    }
    
    /**
     * Définit un bit à 1
     */
    public static int setBit(int value, int bitPosition) {
        return value | (1 << bitPosition);
    }
    
    /**
     * Définit un bit à 0
     */
    public static int clearBit(int value, int bitPosition) {
        return value & ~(1 << bitPosition);
    }
    
    /**
     * Inverse un bit
     */
    public static int toggleBit(int value, int bitPosition) {
        return value ^ (1 << bitPosition);
    }
    
    /**
     * Teste le bit de poids fort (MSB)
     */
    public static boolean testMSB(int value, int bits) {
        return testBit(value, bits - 1);
    }
    
    /**
     * Teste le bit de poids faible (LSB)
     */
    public static boolean testLSB(int value) {
        return testBit(value, 0);
    }
    
    /**
     * Obtient l'octet de poids fort d'un mot 16 bits
     */
    public static int getHighByte(int value) {
        return (value >> 8) & 0xFF;
    }
    
    /**
     * Obtient l'octet de poids faible d'un mot 16 bits
     */
    public static int getLowByte(int value) {
        return value & 0xFF;
    }
    
    /**
     * Combine deux octets en un mot 16 bits
     */
    public static int makeWord(int high, int low) {
        return ((high & 0xFF) << 8) | (low & 0xFF);
    }
    
    /**
     * Extension de signe 8 bits vers 16 bits
     */
    public static int signExtend8to16(int value) {
        if ((value & 0x80) != 0) {
            return value | 0xFF00;
        }
        return value & 0xFF;
    }
    
    /**
     * Extension de signe 5 bits vers 16 bits (pour offset indexé)
     */
    public static int signExtend5to16(int value) {
        if ((value & 0x10) != 0) {
            return value | 0xFFE0;
        }
        return value & 0x1F;
    }
}