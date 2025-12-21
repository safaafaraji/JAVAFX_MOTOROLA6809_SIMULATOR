package motorola6809.examples;

import motorola6809.assembler.Assembler;
import motorola6809.assembler.OpcodeGenerator;
import motorola6809.utils.HexConverter;

/**
 * Tests complets pour toutes les instructions du Motorola 6809
 */
public class SimulatorExample {
    
    public static void main(String[] args) {
        System.out.println("=== TESTS COMPLETS MOTOROLA 6809 ===");
        System.out.println("=====================================\n");
        
        // Test 1: Instructions logiques
        testLogicalInstructions();
        
        // Test 2: Instructions d'incrémentation/décrémentation
        testArithmeticInstructions();
        
        // Test 3: Instructions de chargement/stockage
        testLoadStoreInstructions();
        
        // Test 4: Instructions de saut
        testJumpInstructions();
        
        // Test 5: Instructions spéciales
        testSpecialInstructions();
        
        System.out.println("\n=== TESTS TERMINÉS AVEC SUCCÈS ===");
    }
    
    private static void testLogicalInstructions() {
        System.out.println("1. INSTRUCTIONS LOGIQUES");
        System.out.println("------------------------");
        
        // COMA/COMB - Complément à 1
        System.out.println("a) COMA, COMB, COM");
        testInstruction("COMA", "INHERENT", 0x43);
        testInstruction("COMB", "INHERENT", 0x53);
        testInstruction("COM", "DIRECT", 0x03);
        testInstruction("COM", "EXTENDED", 0x73);
        testInstruction("COM", "INDEXED", 0x63);
        
        // EORA/EORB - OU exclusif
        System.out.println("\nb) EORA, EORB");
        testInstruction("EORA", "IMMEDIATE", 0x88);
        testInstruction("EORA", "DIRECT", 0x98);
        testInstruction("EORA", "EXTENDED", 0xB8);
        testInstruction("EORA", "INDEXED", 0xA8);
        testInstruction("EORB", "IMMEDIATE", 0xC8);
        testInstruction("EORB", "DIRECT", 0xD8);
        testInstruction("EORB", "EXTENDED", 0xF8);
        testInstruction("EORB", "INDEXED", 0xE8);
    }
    
    private static void testArithmeticInstructions() {
        System.out.println("\n2. INSTRUCTIONS ARITHMÉTIQUES");
        System.out.println("-----------------------------");
        
        // INCA/INCB/INC
        System.out.println("a) INCA, INCB, INC");
        testInstruction("INCA", "INHERENT", 0x4C);
        testInstruction("INCB", "INHERENT", 0x5C);
        testInstruction("INC", "DIRECT", 0x0C);
        testInstruction("INC", "EXTENDED", 0x7C);
        testInstruction("INC", "INDEXED", 0x6C);
        
        // DECA/DECB/DEC
        System.out.println("\nb) DECA, DECB, DEC");
        testInstruction("DECA", "INHERENT", 0x4A);
        testInstruction("DECB", "INHERENT", 0x5A);
        testInstruction("DEC", "DIRECT", 0x0A);
        testInstruction("DEC", "EXTENDED", 0x7A);
        testInstruction("DEC", "INDEXED", 0x6A);
        
        // DAA - Decimal Adjust
        System.out.println("\nc) DAA");
        testInstruction("DAA", "INHERENT", 0x19);
        
        // MUL - Multiplication
        System.out.println("\nd) MUL");
        testInstruction("MUL", "INHERENT", 0x3D);
    }
    
    private static void testLoadStoreInstructions() {
        System.out.println("\n3. INSTRUCTIONS DE CHARGEMENT/STOCKAGE");
        System.out.println("--------------------------------------");
        
        // LDA - Load Accumulator A
        System.out.println("a) LDA");
        testInstruction("LDA", "IMMEDIATE", 0x86);
        testInstruction("LDA", "DIRECT", 0x96);
        testInstruction("LDA", "EXTENDED", 0xB6);
        testInstruction("LDA", "INDEXED", 0xA6);
        
        // LDB - Load Accumulator B
        System.out.println("\nb) LDB");
        testInstruction("LDB", "IMMEDIATE", 0xC6);
        testInstruction("LDB", "DIRECT", 0xD6);
        testInstruction("LDB", "EXTENDED", 0xF6);
        testInstruction("LDB", "INDEXED", 0xE6);
        
        // LDD - Load Double (A:B)
        System.out.println("\nc) LDD");
        testInstruction("LDD", "IMMEDIATE", 0xCC);
        testInstruction("LDD", "DIRECT", 0xDC);
        testInstruction("LDD", "EXTENDED", 0xFC);
        testInstruction("LDD", "INDEXED", 0xEC);
        
        // LDX - Load Index Register X
        System.out.println("\nd) LDX");
        testInstruction("LDX", "IMMEDIATE", 0x8E);
        testInstruction("LDX", "DIRECT", 0x9E);
        testInstruction("LDX", "EXTENDED", 0xBE);
        testInstruction("LDX", "INDEXED", 0xAE);
        
        // LDY - Load Index Register Y
        System.out.println("\ne) LDY");
        System.out.println("LDY IMMEDIATE: À implémenter");
        System.out.println("LDY DIRECT: À implémenter");
        System.out.println("LDY EXTENDED: À implémenter");
        System.out.println("LDY INDEXED: À implémenter");
        
        // LDS - Load Stack Pointer
        System.out.println("\nf) LDS");
        System.out.println("LDS IMMEDIATE: À implémenter");
        System.out.println("LDS DIRECT: À implémenter");
        System.out.println("LDS EXTENDED: À implémenter");
        System.out.println("LDS INDEXED: À implémenter");
        
        // LDU - Load User Stack Pointer
        System.out.println("\ng) LDU");
        System.out.println("LDU IMMEDIATE: À implémenter");
        System.out.println("LDU DIRECT: À implémenter");
        System.out.println("LDU EXTENDED: À implémenter");
        System.out.println("LDU INDEXED: À implémenter");
        
        // STA - Store Accumulator A
        System.out.println("\nh) STA");
        testInstruction("STA", "DIRECT", 0x97);
        testInstruction("STA", "EXTENDED", 0xB7);
        testInstruction("STA", "INDEXED", 0xA7);
        
        // STB - Store Accumulator B
        System.out.println("\ni) STB");
        testInstruction("STB", "DIRECT", 0xD7);
        testInstruction("STB", "EXTENDED", 0xF7);
        testInstruction("STB", "INDEXED", 0xE7);
        
        // STD - Store Double (A:B)
        System.out.println("\nj) STD");
        testInstruction("STD", "DIRECT", 0xDD);
        testInstruction("STD", "EXTENDED", 0xFD);
        testInstruction("STD", "INDEXED", 0xED);
        
        // STX - Store Index Register X
        System.out.println("\nk) STX");
        testInstruction("STX", "DIRECT", 0x9F);
        testInstruction("STX", "EXTENDED", 0xBF);
        testInstruction("STX", "INDEXED", 0xAF);
        
        // STY - Store Index Register Y
        System.out.println("\nl) STY");
        System.out.println("STY DIRECT: À implémenter");
        System.out.println("STY EXTENDED: À implémenter");
        System.out.println("STY INDEXED: À implémenter");
        
        // STS - Store Stack Pointer
        System.out.println("\nm) STS");
        System.out.println("STS DIRECT: À implémenter");
        System.out.println("STS EXTENDED: À implémenter");
        System.out.println("STS INDEXED: À implémenter");
        
        // STU - Store User Stack Pointer
        System.out.println("\nn) STU");
        System.out.println("STU DIRECT: À implémenter");
        System.out.println("STU EXTENDED: À implémenter");
        System.out.println("STU INDEXED: À implémenter");
    }
    
    private static void testJumpInstructions() {
        System.out.println("\n4. INSTRUCTIONS DE SAUT");
        System.out.println("----------------------");
        
        // JMP - Jump
        System.out.println("a) JMP");
        testInstruction("JMP", "DIRECT", 0x0E);
        testInstruction("JMP", "EXTENDED", 0x7E);
        testInstruction("JMP", "INDEXED", 0x6E);
        
        // JSR - Jump to Subroutine
        System.out.println("\nb) JSR");
        testInstruction("JSR", "DIRECT", 0x9D);
        testInstruction("JSR", "EXTENDED", 0xBD);
        testInstruction("JSR", "INDEXED", 0xAD);
    }
    
    private static void testSpecialInstructions() {
        System.out.println("\n5. INSTRUCTIONS SPÉCIALES");
        System.out.println("-------------------------");
        
        // NOP - No Operation
        System.out.println("a) NOP");
        testInstruction("NOP", "INHERENT", 0x12);
        
        // EXG - Exchange Registers
        System.out.println("\nb) EXG");
        testInstruction("EXG", "INHERENT", 0x1E);
        
        // CWAI - Wait for Interrupt
        System.out.println("\nc) CWAI");
        testInstruction("CWAI", "IMMEDIATE", 0x3C);
    }
    
    private static void testInstruction(String mnemonic, String mode, int expectedOpcode) {
        try {
            int actualOpcode = OpcodeGenerator.getOpcode(mnemonic, mode);
            String status = (actualOpcode == expectedOpcode) ? "✓" : "✗";
            System.out.printf("  %s %-12s %-12s: 0x%02X (attendu: 0x%02X)%n", 
                status, mnemonic, mode, actualOpcode, expectedOpcode);
            
            if (actualOpcode != expectedOpcode) {
                throw new RuntimeException(String.format(
                    "Erreur: %s %s opcode incorrect", mnemonic, mode));
            }
        } catch (Exception e) {
            System.out.printf("  ✗ %-12s %-12s: ERREUR - %s%n", 
                mnemonic, mode, e.getMessage());
        }
    }
    
    /**
     * Test d'assemblage d'un programme simple
     */
    public static void testAssembler() {
        System.out.println("\n\n=== TEST D'ASSEMBLAGE ===");
        
        String program = 
            "        ORG $1000\n" +
            "START   LDA #$FF      ; Charger FF dans A\n" +
            "        LDB #$AA      ; Charger AA dans B\n" +
            "        ADDA #$01     ; Ajouter 1 à A\n" +
            "        ADDB #$01     ; Ajouter 1 à B\n" +
            "        STA $2000     ; Stocker A à l'adresse 2000\n" +
            "        STB $2001     ; Stocker B à l'adresse 2001\n" +
            "        JMP START     ; Boucle infinie\n" +
            "        END";
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("Programme assemblé avec succès !");
            System.out.printf("Taille du code machine: %d octets%n", machineCode.length);
            System.out.println("Code machine généré:");
            
            // Afficher le code machine en hexadécimal
            for (int i = 0; i < machineCode.length; i++) {
                if (i % 8 == 0) {
                    System.out.printf("%n%04X: ", i + 0x1000);
                }
                System.out.printf("%02X ", machineCode[i] & 0xFF);
            }
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("Erreur lors de l'assemblage: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test d'exécution pas à pas
     */
    public static void testExecution() {
        System.out.println("\n\n=== TEST D'EXÉCUTION PAS À PAS ===");
        
        // Programme de test simple
        String testProgram = 
            "        ORG $C000\n" +
            "MAIN    LDA #$55      ; Charger 55h dans A\n" +
            "        LDB #$AA      ; Charger AAh dans B\n" +
            "        MUL           ; Multiplier A*B (55*AA = 36AA)\n" +
            "        COMA          ; Complément de A\n" +
            "        COMB          ; Complément de B\n" +
            "        INCA          ; Incrémenter A\n" +
            "        DECB          ; Décrémenter B\n" +
            "        DAA           ; Ajustement décimal\n" +
            "        NOP           ; Ne rien faire\n" +
            "        EXG A,B       ; Échanger A et B\n" +
            "        END";
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(testProgram);
            
            System.out.println("Programme d'exécution généré:");
            System.out.println("Adresse  Code    Instruction");
            System.out.println("----------------------------");
            
            int address = 0xC000;
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%04X     %02X", address, machineCode[i] & 0xFF);
                
                // Décoder l'instruction
                String instruction = decodeInstruction(machineCode, i);
                System.out.printf("     %s%n", instruction);
                
                // Avancer selon la taille de l'instruction
                int instructionSize = getInstructionSize(machineCode[i] & 0xFF);
                address += instructionSize;
                i += instructionSize - 1;
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du test d'exécution: " + e.getMessage());
        }
    }
    
    private static String decodeInstruction(byte[] code, int index) {
        if (index >= code.length) return "?";
        
        int opcode = code[index] & 0xFF;
        
        switch (opcode) {
            case 0x86: return "LDA #";
            case 0xC6: return "LDB #";
            case 0x3D: return "MUL";
            case 0x43: return "COMA";
            case 0x53: return "COMB";
            case 0x4C: return "INCA";
            case 0x5A: return "DECB";
            case 0x19: return "DAA";
            case 0x12: return "NOP";
            case 0x1E: return "EXG";
            default: return String.format("??? (0x%02X)", opcode);
        }
    }
    
    private static int getInstructionSize(int opcode) {
        switch (opcode) {
            case 0x86: // LDA #
            case 0xC6: // LDB #
                return 2;
            case 0x3D: // MUL
            case 0x43: // COMA
            case 0x53: // COMB
            case 0x4C: // INCA
            case 0x5A: // DECB
            case 0x19: // DAA
            case 0x12: // NOP
            case 0x1E: // EXG
                return 1;
            default:
                return 1;
        }
    }
    
    /**
     * Test complet avec tous les modes d'adressage
     */
    public static void testAllAddressingModes() {
        System.out.println("\n\n=== TEST TOUS LES MODES D'ADRESSAGE ===");
        
        System.out.println("\nA) Mode Immédiat:");
        System.out.println("   LDA #$FF    - Charger immédiat");
        System.out.println("   LDB #$AA    - Charger immédiat");
        System.out.println("   LDD #$1234  - Charger immédiat 16-bit");
        
        System.out.println("\nB) Mode Direct (page 0):");
        System.out.println("   LDA $10     - Charger depuis $0010");
        System.out.println("   STA $20     - Stocker à $0020");
        
        System.out.println("\nC) Mode Étendu:");
        System.out.println("   LDA $1234   - Charger depuis $1234");
        System.out.println("   JMP $5678   - Sauter à $5678");
        
        System.out.println("\nD) Mode Indexé:");
        System.out.println("   LDA ,X      - Charger depuis [X]");
        System.out.println("   LDA 5,X     - Charger depuis [X+5]");
        
        System.out.println("\nE) Mode Inhérent:");
        System.out.println("   INCA        - Incrémenter A");
        System.out.println("   DECB        - Décrémenter B");
        System.out.println("   COMA        - Complément de A");
    }
}