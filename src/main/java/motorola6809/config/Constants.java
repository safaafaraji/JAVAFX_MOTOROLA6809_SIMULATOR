package motorola6809.config;

public class Constants {
    
    // Tailles mémoire
    public static final int RAM_SIZE = 1024;        // 1 KB
    public static final int ROM_SIZE = 1024;        // 1 KB
    public static final int TOTAL_MEMORY = 65536;   // 64 KB (16-bit addressing)
    
    // Adresses de base
    public static final int RAM_START = 0x0000;
    public static final int RAM_END = 0x03FF;
    public static final int ROM_START = 0x1400;     // 5120 en décimal
    public static final int ROM_END = 0x17FF;       // 6143 en décimal
    
    // Vecteurs d'interruption (typiques pour 6809)
    public static final int VECTOR_SWI3 = 0xFFF2;
    public static final int VECTOR_SWI2 = 0xFFF4;
    public static final int VECTOR_FIRQ = 0xFFF6;
    public static final int VECTOR_IRQ = 0xFFF8;
    public static final int VECTOR_SWI = 0xFFFA;
    public static final int VECTOR_NMI = 0xFFFC;
    public static final int VECTOR_RESET = 0xFFFE;
    
    // Valeurs par défaut des registres
    public static final int DEFAULT_A = 0x00;
    public static final int DEFAULT_B = 0x00;
    public static final int DEFAULT_DP = 0x00;
    public static final int DEFAULT_X = 0x0000;
    public static final int DEFAULT_Y = 0x0000;
    public static final int DEFAULT_U = 0x0000;
    public static final int DEFAULT_S = 0x0000;
    public static final int DEFAULT_PC = ROM_START;
    public static final int DEFAULT_CC = 0x00;
    
    // Bits du registre CC (Condition Code)
    public static final int CC_C = 0;  // Carry
    public static final int CC_V = 1;  // Overflow
    public static final int CC_Z = 2;  // Zero
    public static final int CC_N = 3;  // Negative
    public static final int CC_I = 4;  // IRQ mask
    public static final int CC_H = 5;  // Half carry
    public static final int CC_F = 6;  // FIRQ mask
    public static final int CC_E = 7;  // Entire state saved
    
    // Masques pour registres
    public static final int MASK_8BIT = 0xFF;
    public static final int MASK_16BIT = 0xFFFF;
    
    // Codes de registres pour TFR/EXG
    public static final int REG_D = 0x0;
    public static final int REG_X = 0x1;
    public static final int REG_Y = 0x2;
    public static final int REG_U = 0x3;
    public static final int REG_S = 0x4;
    public static final int REG_PC = 0x5;
    public static final int REG_A = 0x8;
    public static final int REG_B = 0x9;
    public static final int REG_CC = 0xA;
    public static final int REG_DP = 0xB;
}