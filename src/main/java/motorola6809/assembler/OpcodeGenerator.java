package motorola6809.assembler;

import java.util.HashMap;
import java.util.Map;

public class OpcodeGenerator {
    
    private static Map<String, Map<String, Integer>> opcodeTable;
    
    static {
        opcodeTable = new HashMap<>();
        initializeOpcodeTable();
    }
    
    private static void initializeOpcodeTable() {
        // ==============================================
        // LOAD INSTRUCTIONS (Chargement)
        // ==============================================
        
        // LDA - Load Accumulator A
        addOpcode("LDA", "IMMEDIATE", 0x86);
        addOpcode("LDA", "DIRECT", 0x96);
        addOpcode("LDA", "EXTENDED", 0xB6);
        addOpcode("LDA", "INDEXED", 0xA6);
        
        // LDB - Load Accumulator B
        addOpcode("LDB", "IMMEDIATE", 0xC6);
        addOpcode("LDB", "DIRECT", 0xD6);
        addOpcode("LDB", "EXTENDED", 0xF6);
        addOpcode("LDB", "INDEXED", 0xE6);
        
        // LDD - Load Double Accumulator D (A:B)
        addOpcode("LDD", "IMMEDIATE", 0xCC);
        addOpcode("LDD", "DIRECT", 0xDC);
        addOpcode("LDD", "EXTENDED", 0xFC);
        addOpcode("LDD", "INDEXED", 0xEC);
        
        // LDS - Load Stack Pointer S
        addOpcode("LDS", "IMMEDIATE", 0x10CE);
        addOpcode("LDS", "DIRECT", 0x10DE);
        addOpcode("LDS", "EXTENDED", 0x10FE);
        addOpcode("LDS", "INDEXED", 0x10EE);
        
        // LDU - Load User Stack Pointer U
        addOpcode("LDU", "IMMEDIATE", 0xCE);
        addOpcode("LDU", "DIRECT", 0xDE);
        addOpcode("LDU", "EXTENDED", 0xFE);
        addOpcode("LDU", "INDEXED", 0xEE);
        
        // LDX - Load Index Register X
        addOpcode("LDX", "IMMEDIATE", 0x8E);
        addOpcode("LDX", "DIRECT", 0x9E);
        addOpcode("LDX", "EXTENDED", 0xBE);
        addOpcode("LDX", "INDEXED", 0xAE);
        
        // LDY - Load Index Register Y
        addOpcode("LDY", "IMMEDIATE", 0x108E);
        addOpcode("LDY", "DIRECT", 0x109E);
        addOpcode("LDY", "EXTENDED", 0x10BE);
        addOpcode("LDY", "INDEXED", 0x10AE);
        
        // ==============================================
        // STORE INSTRUCTIONS (Stockage)
        // ==============================================
        
        // STA - Store Accumulator A
        addOpcode("STA", "DIRECT", 0x97);
        addOpcode("STA", "EXTENDED", 0xB7);
        addOpcode("STA", "INDEXED", 0xA7);
        
        // STB - Store Accumulator B
        addOpcode("STB", "DIRECT", 0xD7);
        addOpcode("STB", "EXTENDED", 0xF7);
        addOpcode("STB", "INDEXED", 0xE7);
        
        // STD - Store Double Accumulator D
        addOpcode("STD", "DIRECT", 0xDD);
        addOpcode("STD", "EXTENDED", 0xFD);
        addOpcode("STD", "INDEXED", 0xED);
        
        // STS - Store Stack Pointer S
        addOpcode("STS", "DIRECT", 0x10DF);
        addOpcode("STS", "EXTENDED", 0x10FF);
        addOpcode("STS", "INDEXED", 0x10EF);
        
        // STU - Store User Stack Pointer U
        addOpcode("STU", "DIRECT", 0xDF);
        addOpcode("STU", "EXTENDED", 0xFF);
        addOpcode("STU", "INDEXED", 0xEF);
        
        // STX - Store Index Register X
        addOpcode("STX", "DIRECT", 0x9F);
        addOpcode("STX", "EXTENDED", 0xBF);
        addOpcode("STX", "INDEXED", 0xAF);
        
        // STY - Store Index Register Y
        addOpcode("STY", "DIRECT", 0x109F);
        addOpcode("STY", "EXTENDED", 0x10BF);
        addOpcode("STY", "INDEXED", 0x10AF);
        
        // ==============================================
        // TRANSFER AND EXCHANGE (Transfert et Échange)
        // ==============================================
        
        // TFR - Transfer Register to Register
        addOpcode("TFR", "REGISTER", 0x1F);
        
        // EXG - Exchange Registers
        addOpcode("EXG", "REGISTER", 0x1E);
        
        // TSTA - Test A
        addOpcode("TSTA", "INHERENT", 0x4D);
        
        // TSTB - Test B
        addOpcode("TSTB", "INHERENT", 0x5D);
        
        // TST - Test Memory
        addOpcode("TST", "DIRECT", 0x0D);
        addOpcode("TST", "EXTENDED", 0x7D);
        addOpcode("TST", "INDEXED", 0x6D);
        
        // ==============================================
        // ARITHMETIC OPERATIONS (Opérations arithmétiques)
        // ==============================================
        
        // ADDA - Add to A
        addOpcode("ADDA", "IMMEDIATE", 0x8B);
        addOpcode("ADDA", "DIRECT", 0x9B);
        addOpcode("ADDA", "EXTENDED", 0xBB);
        addOpcode("ADDA", "INDEXED", 0xAB);
        
        // ADDB - Add to B
        addOpcode("ADDB", "IMMEDIATE", 0xCB);
        addOpcode("ADDB", "DIRECT", 0xDB);
        addOpcode("ADDB", "EXTENDED", 0xFB);
        addOpcode("ADDB", "INDEXED", 0xEB);
        
        // ADDD - Add to D (16-bit)
        addOpcode("ADDD", "IMMEDIATE", 0xC3);
        addOpcode("ADDD", "DIRECT", 0xD3);
        addOpcode("ADDD", "EXTENDED", 0xF3);
        addOpcode("ADDD", "INDEXED", 0xE3);
        
        // SUBA - Subtract from A
        addOpcode("SUBA", "IMMEDIATE", 0x80);
        addOpcode("SUBA", "DIRECT", 0x90);
        addOpcode("SUBA", "EXTENDED", 0xB0);
        addOpcode("SUBA", "INDEXED", 0xA0);
        
        // SUBB - Subtract from B
        addOpcode("SUBB", "IMMEDIATE", 0xC0);
        addOpcode("SUBB", "DIRECT", 0xD0);
        addOpcode("SUBB", "EXTENDED", 0xF0);
        addOpcode("SUBB", "INDEXED", 0xE0);
        
        // SUBD - Subtract from D (16-bit)
        addOpcode("SUBD", "IMMEDIATE", 0x83);
        addOpcode("SUBD", "DIRECT", 0x93);
        addOpcode("SUBD", "EXTENDED", 0xB3);
        addOpcode("SUBD", "INDEXED", 0xA3);
        
        // CMPA - Compare with A
        addOpcode("CMPA", "IMMEDIATE", 0x81);
        addOpcode("CMPA", "DIRECT", 0x91);
        addOpcode("CMPA", "EXTENDED", 0xB1);
        addOpcode("CMPA", "INDEXED", 0xA1);
        
        // CMPB - Compare with B
        addOpcode("CMPB", "IMMEDIATE", 0xC1);
        addOpcode("CMPB", "DIRECT", 0xD1);
        addOpcode("CMPB", "EXTENDED", 0xF1);
        addOpcode("CMPB", "INDEXED", 0xE1);
        
        // CMPD - Compare with D (16-bit)
        addOpcode("CMPD", "IMMEDIATE", 0x1083);
        addOpcode("CMPD", "DIRECT", 0x1093);
        addOpcode("CMPD", "EXTENDED", 0x10B3);
        addOpcode("CMPD", "INDEXED", 0x10A3);
        
        // CMPS - Compare with S
        addOpcode("CMPS", "IMMEDIATE", 0x118C);
        addOpcode("CMPS", "DIRECT", 0x1193);
        addOpcode("CMPS", "EXTENDED", 0x11B3);
        addOpcode("CMPS", "INDEXED", 0x11A3);
        
        // CMPU - Compare with U
        addOpcode("CMPU", "IMMEDIATE", 0x1183);
        addOpcode("CMPU", "DIRECT", 0x1193);
        addOpcode("CMPU", "EXTENDED", 0x11B3);
        addOpcode("CMPU", "INDEXED", 0x11A3);
        
        // CMPX - Compare with X
        addOpcode("CMPX", "IMMEDIATE", 0x8C);
        addOpcode("CMPX", "DIRECT", 0x9C);
        addOpcode("CMPX", "EXTENDED", 0xBC);
        addOpcode("CMPX", "INDEXED", 0xAC);
        
        // CMPY - Compare with Y
        addOpcode("CMPY", "IMMEDIATE", 0x108C);
        addOpcode("CMPY", "DIRECT", 0x109C);
        addOpcode("CMPY", "EXTENDED", 0x10BC);
        addOpcode("CMPY", "INDEXED", 0x10AC);
        
        // MUL - Multiply A × B → D
        addOpcode("MUL", "INHERENT", 0x3D);
        
        // ==============================================
        // INCREMENT AND DECREMENT (Incrémentation/Décrémentation)
        // ==============================================
        
        // INCA - Increment A
        addOpcode("INCA", "INHERENT", 0x4C);
        
        // INCB - Increment B
        addOpcode("INCB", "INHERENT", 0x5C);
        
        // INC - Increment Memory
        addOpcode("INC", "DIRECT", 0x0C);
        addOpcode("INC", "EXTENDED", 0x7C);
        addOpcode("INC", "INDEXED", 0x6C);
        
        // DECA - Decrement A
        addOpcode("DECA", "INHERENT", 0x4A);
        
        // DECB - Decrement B
        addOpcode("DECB", "INHERENT", 0x5A);
        
        // DEC - Decrement Memory
        addOpcode("DEC", "DIRECT", 0x0A);
        addOpcode("DEC", "EXTENDED", 0x7A);
        addOpcode("DEC", "INDEXED", 0x6A);
        
        // ==============================================
        // LOGICAL OPERATIONS (Opérations logiques)
        // ==============================================
        
        // ANDA - Logical AND with A
        addOpcode("ANDA", "IMMEDIATE", 0x84);
        addOpcode("ANDA", "DIRECT", 0x94);
        addOpcode("ANDA", "EXTENDED", 0xB4);
        addOpcode("ANDA", "INDEXED", 0xA4);
        
        // ANDB - Logical AND with B
        addOpcode("ANDB", "IMMEDIATE", 0xC4);
        addOpcode("ANDB", "DIRECT", 0xD4);
        addOpcode("ANDB", "EXTENDED", 0xF4);
        addOpcode("ANDB", "INDEXED", 0xE4);
        
        // ANDCC - AND Condition Codes
        addOpcode("ANDCC", "IMMEDIATE", 0x1C);
        
        // ORA - Logical OR with A
        addOpcode("ORA", "IMMEDIATE", 0x8A);
        addOpcode("ORA", "DIRECT", 0x9A);
        addOpcode("ORA", "EXTENDED", 0xBA);
        addOpcode("ORA", "INDEXED", 0xAA);
        
        // ORB - Logical OR with B
        addOpcode("ORB", "IMMEDIATE", 0xCA);
        addOpcode("ORB", "DIRECT", 0xDA);
        addOpcode("ORB", "EXTENDED", 0xFA);
        addOpcode("ORB", "INDEXED", 0xEA);
        
        // ORCC - OR Condition Codes
        addOpcode("ORCC", "IMMEDIATE", 0x1A);
        
        // EORA - Exclusive OR with A
        addOpcode("EORA", "IMMEDIATE", 0x88);
        addOpcode("EORA", "DIRECT", 0x98);
        addOpcode("EORA", "EXTENDED", 0xB8);
        addOpcode("EORA", "INDEXED", 0xA8);
        
        // EORB - Exclusive OR with B
        addOpcode("EORB", "IMMEDIATE", 0xC8);
        addOpcode("EORB", "DIRECT", 0xD8);
        addOpcode("EORB", "EXTENDED", 0xF8);
        addOpcode("EORB", "INDEXED", 0xE8);
        
        // COMA - Complement A (1's complement)
        addOpcode("COMA", "INHERENT", 0x43);
        
        // COMB - Complement B (1's complement)
        addOpcode("COMB", "INHERENT", 0x53);
        
        // COM - Complement Memory
        addOpcode("COM", "DIRECT", 0x03);
        addOpcode("COM", "EXTENDED", 0x73);
        addOpcode("COM", "INDEXED", 0x63);
        
        // NEGA - Negate A (2's complement)
        addOpcode("NEGA", "INHERENT", 0x40);
        
        // NEGB - Negate B (2's complement)
        addOpcode("NEGB", "INHERENT", 0x50);
        
        // NEG - Negate Memory
        addOpcode("NEG", "DIRECT", 0x00);
        addOpcode("NEG", "EXTENDED", 0x70);
        addOpcode("NEG", "INDEXED", 0x60);
        
        // ==============================================
        // SHIFT AND ROTATE (Décalage et Rotation)
        // ==============================================
        
        // ASLA - Arithmetic Shift Left A
        addOpcode("ASLA", "INHERENT", 0x48);
        
        // ASLB - Arithmetic Shift Left B
        addOpcode("ASLB", "INHERENT", 0x58);
        
        // ASL - Arithmetic Shift Left Memory
        addOpcode("ASL", "DIRECT", 0x08);
        addOpcode("ASL", "EXTENDED", 0x78);
        addOpcode("ASL", "INDEXED", 0x68);
        
        // ASRA - Arithmetic Shift Right A
        addOpcode("ASRA", "INHERENT", 0x47);
        
        // ASRB - Arithmetic Shift Right B
        addOpcode("ASRB", "INHERENT", 0x57);
        
        // ASR - Arithmetic Shift Right Memory
        addOpcode("ASR", "DIRECT", 0x07);
        addOpcode("ASR", "EXTENDED", 0x77);
        addOpcode("ASR", "INDEXED", 0x67);
        
        // LSLA - Logical Shift Left A (same as ASLA)
        addOpcode("LSLA", "INHERENT", 0x48);
        
        // LSLB - Logical Shift Left B (same as ASLB)
        addOpcode("LSLB", "INHERENT", 0x58);
        
        // LSL - Logical Shift Left Memory
        addOpcode("LSL", "DIRECT", 0x08);
        addOpcode("LSL", "EXTENDED", 0x78);
        addOpcode("LSL", "INDEXED", 0x68);
        
        // LSRA - Logical Shift Right A
        addOpcode("LSRA", "INHERENT", 0x44);
        
        // LSRB - Logical Shift Right B
        addOpcode("LSRB", "INHERENT", 0x54);
        
        // LSR - Logical Shift Right Memory
        addOpcode("LSR", "DIRECT", 0x04);
        addOpcode("LSR", "EXTENDED", 0x74);
        addOpcode("LSR", "INDEXED", 0x64);
        
        // ROLA - Rotate Left A
        addOpcode("ROLA", "INHERENT", 0x49);
        
        // ROLB - Rotate Left B
        addOpcode("ROLB", "INHERENT", 0x59);
        
        // ROL - Rotate Left Memory
        addOpcode("ROL", "DIRECT", 0x09);
        addOpcode("ROL", "EXTENDED", 0x79);
        addOpcode("ROL", "INDEXED", 0x69);
        
        // RORA - Rotate Right A
        addOpcode("RORA", "INHERENT", 0x46);
        
        // RORB - Rotate Right B
        addOpcode("RORB", "INHERENT", 0x56);
        
        // ROR - Rotate Right Memory
        addOpcode("ROR", "DIRECT", 0x06);
        addOpcode("ROR", "EXTENDED", 0x76);
        addOpcode("ROR", "INDEXED", 0x66);
        
       // BITA (Bit Test A) - instruction logique, PAS un branchement
        addOpcode("BITA", "IMMEDIATE", 0x85);
        addOpcode("BITA", "DIRECT", 0x95);
        addOpcode("BITA", "EXTENDED", 0xB5);
        addOpcode("BITA", "INDEXED", 0xA5);

        // BITB (Bit Test B) - instruction logique, PAS un branchement
        addOpcode("BITB", "IMMEDIATE", 0xC5);
        addOpcode("BITB", "DIRECT", 0xD5);
        addOpcode("BITB", "EXTENDED", 0xF5);
    
        // ==============================================
        // JUMP INSTRUCTIONS (Sauts)
        // ==============================================
        
        // JMP - Jump
        addOpcode("JMP", "DIRECT", 0x0E);
        addOpcode("JMP", "EXTENDED", 0x7E);
        addOpcode("JMP", "INDEXED", 0x6E);
        
        // JSR - Jump to Subroutine
        addOpcode("JSR", "DIRECT", 0x9D);
        addOpcode("JSR", "EXTENDED", 0xBD);
        addOpcode("JSR", "INDEXED", 0xAD);
        
        // ==============================================
        // SUBROUTINE AND INTERRUPT (Sous-routines et interruptions)
        // ==============================================
        
        // RTS - Return from Subroutine
        addOpcode("RTS", "INHERENT", 0x39);
        
        // RTI - Return from Interrupt
        addOpcode("RTI", "INHERENT", 0x3B);
        
        // SWI - Software Interrupt
        addOpcode("SWI", "INHERENT", 0x3F);
        
        // SWI2 - Software Interrupt 2
        addOpcode("SWI2", "INHERENT", 0x103F);
        
        // SWI3 - Software Interrupt 3
        addOpcode("SWI3", "INHERENT", 0x113F);
        
        // ==============================================
        // STACK OPERATIONS (Opérations de pile)
        // ==============================================
        
        // PSHS - Push Registers onto Hardware Stack
        addOpcode("PSHS", "IMMEDIATE", 0x34);
        
        // PSHU - Push Registers onto User Stack
        addOpcode("PSHU", "IMMEDIATE", 0x36);
        
        // PULS - Pull Registers from Hardware Stack
        addOpcode("PULS", "IMMEDIATE", 0x35);
        
        // PULU - Pull Registers from User Stack
        addOpcode("PULU", "IMMEDIATE", 0x37);
        
        // LEAS - Load Effective Address into S
        addOpcode("LEAS", "INDEXED", 0x32);
        
        // LEAU - Load Effective Address into U
        addOpcode("LEAU", "INDEXED", 0x33);
        
        // LEAX - Load Effective Address into X
        addOpcode("LEAX", "INDEXED", 0x30);
        
        // LEAY - Load Effective Address into Y
        addOpcode("LEAY", "INDEXED", 0x31);
        
        // ==============================================
        // CONDITION CODE OPERATIONS (Codes condition)
        // ==============================================
        
        // CLRA - Clear A
        addOpcode("CLRA", "INHERENT", 0x4F);
        
        // CLRB - Clear B
        addOpcode("CLRB", "INHERENT", 0x5F);
        
        // CLR - Clear Memory
        addOpcode("CLR", "DIRECT", 0x0F);
        addOpcode("CLR", "EXTENDED", 0x7F);
        addOpcode("CLR", "INDEXED", 0x6F);
        
        // SEX - Sign Extend B into A
        addOpcode("SEX", "INHERENT", 0x1D);
        
        // DAA - Decimal Adjust A
        addOpcode("DAA", "INHERENT", 0x19);
        
        // CWAI - Wait for Interrupt
        addOpcode("CWAI", "IMMEDIATE", 0x3C);
        
        // SYNC - Synchronize with Interrupt
        addOpcode("SYNC", "INHERENT", 0x13);
        
        // ==============================================
        // MISCELLANEOUS (Divers)
        // ==============================================
        
        // NOP - No Operation
        addOpcode("NOP", "INHERENT", 0x12);
        
        // ABX - Add B to X (unsigned)
        addOpcode("ABX", "INHERENT", 0x3A);
        
        // ==============================================
        // DIRECTIVES ASSEMBLER (Directives d'assemblage)
        // ==============================================
        
        // Directives (pseudo-opcodes)
        addOpcode("ORG", "DIRECTIVE", 0x0000);
        addOpcode("EQU", "DIRECTIVE", 0x0000);
        addOpcode("END", "DIRECTIVE", 0x0000);
        addOpcode("RMB", "DIRECTIVE", 0x0000);
        addOpcode("FCB", "DIRECTIVE", 0x0000);
        addOpcode("FDB", "DIRECTIVE", 0x0000);
        addOpcode("FCC", "DIRECTIVE", 0x0000);
        addOpcode("BSZ", "DIRECTIVE", 0x0000);
        addOpcode("ZMB", "DIRECTIVE", 0x0000);
        addOpcode("SETDP", "DIRECTIVE", 0x0000);
        addOpcode("PAGE", "DIRECTIVE", 0x0000);
        addOpcode("TTL", "DIRECTIVE", 0x0000);
        addOpcode("NAM", "DIRECTIVE", 0x0000);
        addOpcode("OPT", "DIRECTIVE", 0x0000);
    
     // TFR et EXG utilisent le même opcode avec un postbyte
  
      
        addOpcode("PSHS", "IMMEDIATE", 0x34);
        addOpcode("PULS", "IMMEDIATE", 0x35);
        addOpcode("PSHU", "IMMEDIATE", 0x36);
        addOpcode("PULU", "IMMEDIATE", 0x37);
        addOpcode("TFR", "IMMEDIATE", 0x1F);
        addOpcode("EXG", "IMMEDIATE", 0x1E);
        addOpcode("ABA", "INHERENT", 0x1B);  // Add B to A

        // Branchements conditionnels 8-bit
        addOpcode("BRA", "RELATIVE", 0x20);
        addOpcode("BRN", "RELATIVE", 0x21); // Branch Never
        addOpcode("BHI", "RELATIVE", 0x22);
        addOpcode("BLS", "RELATIVE", 0x23);
        addOpcode("BCC", "RELATIVE", 0x24);
        addOpcode("BCS", "RELATIVE", 0x25);
        addOpcode("BNE", "RELATIVE", 0x26);
        addOpcode("BEQ", "RELATIVE", 0x27);
        addOpcode("BVC", "RELATIVE", 0x28);
        addOpcode("BVS", "RELATIVE", 0x29);
        addOpcode("BPL", "RELATIVE", 0x2A);
        addOpcode("BMI", "RELATIVE", 0x2B);
        addOpcode("BGE", "RELATIVE", 0x2C);
        addOpcode("BLT", "RELATIVE", 0x2D);
        addOpcode("BGT", "RELATIVE", 0x2E);
        addOpcode("BLE", "RELATIVE", 0x2F);
        
        // Branchements longs 16-bit
        addOpcode("LBRA", "RELATIVE", 0x16);
        addOpcode("LBSR", "RELATIVE", 0x17);
        addOpcode("LBNE", "RELATIVE", 0x1026);
        addOpcode("LBEQ", "RELATIVE", 0x1027);
        
        // Instruction personnalisée
        addOpcode("MAX", "INHERENT", 0xCD);
        
     
    }
    
    private static void addOpcode(String mnemonic, String mode, int opcode) {
        if (!opcodeTable.containsKey(mnemonic)) {
            opcodeTable.put(mnemonic, new HashMap<>());
        }
        opcodeTable.get(mnemonic).put(mode, opcode);
    }
    
    /**
     * Obtient l'opcode pour une instruction et un mode d'adressage
     */
    public static int getOpcode(String mnemonic, String addressingMode) {
        mnemonic = mnemonic.toUpperCase();
        addressingMode = addressingMode.toUpperCase();
        
        Map<String, Integer> modes = opcodeTable.get(mnemonic);
        if (modes == null) {
            throw new RuntimeException("Instruction inconnue: " + mnemonic);
        }
        
        Integer opcode = modes.get(addressingMode);
        if (opcode == null) {
            // Pour certaines instructions 16-bit, vérifier les préfixes
            if (addressingMode.equals("IMMEDIATE") && modes.containsKey("DIRECTIVE")) {
                return modes.get("DIRECTIVE");
            }
            
            // Chercher un mode alternatif
            for (Map.Entry<String, Integer> entry : modes.entrySet()) {
                if (entry.getKey().startsWith(addressingMode) || 
                    addressingMode.startsWith(entry.getKey())) {
                    return entry.getValue();
                }
            }
            
            throw new RuntimeException(
                "Mode d'adressage " + addressingMode + 
                " non supporté pour " + mnemonic
            );
        }
        
        return opcode;
    }
    
    /**
     * Vérifie si une instruction existe
     */
    public static boolean hasInstruction(String mnemonic) {
        return opcodeTable.containsKey(mnemonic.toUpperCase());
    }
    
    /**
     * Affiche toutes les instructions supportées
     */
    public static void printAllInstructions() {
        System.out.println("=== INSTRUCTIONS MOTOROLA 6809 SUPPORTÉES ===");
        System.out.println("Total: " + opcodeTable.size() + " mnemoniques\n");
        
        for (Map.Entry<String, Map<String, Integer>> entry : opcodeTable.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            for (Map.Entry<String, Integer> modeEntry : entry.getValue().entrySet()) {
                if (modeEntry.getValue() != 0x0000) { // Ne pas afficher les directives
                    System.out.printf("%s=$%04X ", modeEntry.getKey(), modeEntry.getValue());
                }
            }
            System.out.println();
        }
        
        System.out.println("\n=== DIRECTIVES ASSEMBLEUR ===");
        for (Map.Entry<String, Map<String, Integer>> entry : opcodeTable.entrySet()) {
            Map<String, Integer> modes = entry.getValue();
            if (modes.containsKey("DIRECTIVE") && modes.get("DIRECTIVE") == 0x0000) {
                System.out.print(entry.getKey() + " ");
            }
        }
        System.out.println();
    }
    
    /**
     * Teste toutes les instructions
     */
    public static void testAllInstructions() {
        System.out.println("=== TEST DE TOUTES LES INSTRUCTIONS ===");
        int totalTests = 0;
        int passedTests = 0;
        
        String[] testInstructions = {
            // Load/Store
            "LDA", "LDB", "LDD", "LDS", "LDU", "LDX", "LDY",
            "STA", "STB", "STD", "STS", "STU", "STX", "STY",
            
            // Arithmetic
            "ADDA", "ADDB", "ADDD", "SUBA", "SUBB", "SUBD",
            "CMPA", "CMPB", "CMPD", "CMPS", "CMPU", "CMPX", "CMPY",
            "INCA", "INCB", "DECA", "DECB", "MUL",
            
            // Logical
            "ANDA", "ANDB", "ORA", "ORB", "EORA", "EORB",
            "COMA", "COMB", "NEGA", "NEGB",
            
            // Shift/Rotate
            "ASLA", "ASLB", "ASRA", "ASRB", "LSRA", "LSRB",
            "ROLA", "ROLB", "RORA", "RORB",
            
           /* // Branch
            "BRA", "BCC", "BCS", "BEQ", "BNE", "BGE", "BLT",
            "BGT", "BLE", "BHI", "BLS", "BVC", "BVS", "BPL", "BMI",*/
            
            // Jump/Subroutine
            "JMP", "JSR", "BSR", "RTS", "RTI",
            
            // Stack
            "PSHS", "PSHU", "PULS", "PULU",
            
            // Miscellaneous
            "NOP", "SWI", "SWI2", "SWI3", "CWAI", "SYNC", "DAA", "SEX", "ABX",
            
            // Directives
            "ORG", "EQU", "END", "RMB", "FCB", "FDB"
        };
        
        for (String instruction : testInstructions) {
            try {
                if (hasInstruction(instruction)) {
                    System.out.printf("✓ %-6s - SUPPORTÉ\n", instruction);
                    passedTests++;
                } else {
                    System.out.printf("✗ %-6s - NON SUPPORTÉ\n", instruction);
                }
                totalTests++;
            } catch (Exception e) {
                System.out.printf("✗ %-6s - ERREUR: %s\n", instruction, e.getMessage());
            }
        }
        
        System.out.println("\n=== RÉSULTATS ===");
        System.out.printf("Tests: %d/%d réussis (%.1f%%)\n", 
            passedTests, totalTests, (passedTests * 100.0 / totalTests));
        
        if (passedTests == totalTests) {
            System.out.println("✅ TOUTES LES INSTRUCTIONS SONT SUPPORTÉES !");
        } else {
            System.out.println("⚠️  Certaines instructions ne sont pas supportées");
        }
    }
    
    /**
     * Méthode main pour tester
     */
    public static void main(String[] args) {
        System.out.println("=== OPCODE GENERATOR MOTOROLA 6809 ===\n");
        
        // Test 1: Instructions spécifiques
        System.out.println("1. Test d'instructions spécifiques:");
        System.out.printf("   LDA IMMEDIATE: 0x%02X\n", getOpcode("LDA", "IMMEDIATE"));
        System.out.printf("   LDB DIRECT: 0x%02X\n", getOpcode("LDB", "DIRECT"));
        System.out.printf("   LDX EXTENDED: 0x%02X\n", getOpcode("LDX", "EXTENDED"));
        System.out.printf("   INCA INHERENT: 0x%02X\n", getOpcode("INCA", "INHERENT"));
        System.out.printf("   MUL INHERENT: 0x%02X\n", getOpcode("MUL", "INHERENT"));
        System.out.printf("   NOP INHERENT: 0x%02X\n", getOpcode("NOP", "INHERENT"));
        
        // Test 2: Vérification d'existence
        System.out.println("\n2. Vérification d'existence:");
        System.out.println("   LDA existe: " + hasInstruction("LDA"));
        System.out.println("   INVALIDE existe: " + hasInstruction("INVALIDE"));
        
        // Test 3: Test complet
        System.out.println("\n3. Test complet de toutes les instructions:");
        testAllInstructions();
        
        // Test 4: Liste complète (optionnel - décommenter si besoin)
        // printAllInstructions();
        
        System.out.println("\n=== TESTS TERMINÉS ===");
    }
}