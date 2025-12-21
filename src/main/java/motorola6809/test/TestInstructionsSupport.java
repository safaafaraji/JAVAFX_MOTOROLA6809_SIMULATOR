package motorola6809.test;

import motorola6809.assembler.OpcodeGenerator;

public class TestInstructionsSupport {
    
    public static void main(String[] args) {
        System.out.println("=== VÉRIFICATION DES INSTRUCTIONS SUPPORTÉES ===\n");
        
        String[] allInstructions = {
            "LDA", "LDB", "LDD", "LDX", "LDY", "LDS", "LDU",
            "STA", "STB", "STD", "STX", "STY", "STS", "STU",
            "TFR", "EXG",
            "ADDA", "ADDB", "ADDD", "SUBA", "SUBB", "SUBD",
            "INCA", "INCB", "INC", "DECA", "DECB", "DEC",
            "MUL", "DAA",
            "ANDA", "ANDB", "ANDCC", "ORA", "ORB", "ORCC",
            "EORA", "EORB", "COMA", "COMB", "COM",
            "NEGA", "NEGB", "NEG",
            "CMPA", "CMPB", "CMPD", "CMPX", "CMPY",
            "TSTA", "TSTB", "TST",
            "JMP", "JSR", "BSR", "RTS", "RTI",
            "BRA", "BRN", "BHI", "BLS", "BCC", "BCS", "BNE", "BEQ",
            "BVC", "BVS", "BPL", "BMI", "BGE", "BLT", "BGT", "BLE",
            "LBRA", "LBSR",
            "NOP", "SYNC", "CWAI", "SWI", "SWI2", "SWI3",
            "CLRA", "CLRB", "CLR", "ABX", "SEX",
            "PSHS", "PSHU", "PULS", "PULU",
            "LEAX", "LEAY", "LEAS", "LEAU"
        };
        
        String[] addressingModes = {"IMMEDIATE", "DIRECT", "EXTENDED", "INDEXED", "INHERENT", "RELATIVE"};
        
        int supported = 0;
        int total = 0;
        
        for (String instr : allInstructions) {
            System.out.print(instr + ": ");
            boolean instrSupported = false;
            
            for (String mode : addressingModes) {
                try {
                    OpcodeGenerator.getOpcode(instr, mode);
                    System.out.print(mode.charAt(0) + " ");
                    instrSupported = true;
                    total++;
                } catch (Exception e) {
                    // Mode non supporté pour cette instruction
                }
            }
            
            if (instrSupported) {
                supported++;
                System.out.println("✓");
            } else {
                System.out.println("✗ (non supportée)");
            }
        }
        
        System.out.println("\n=== RÉSUMÉ ===");
        System.out.println("Instructions supportées: " + supported + "/" + allInstructions.length);
        System.out.println("Combinaisons instruction/mode: " + total);
        System.out.println("\nPourcentage de couverture: " + (supported * 100 / allInstructions.length) + "%");
    }
}