package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestPSHSImmediat {
    
    public static void main(String[] args) {
        System.out.println("=== TEST PSHS EN MODE IMMÉDIAT ===\n");
        
        Assembler assembler = new Assembler();
        
        String program = 
        	    "        ORG $2000\n" +
        	    "\n" +
        	    "        ; Test avec postbyte\n" +
        	    "        PSHS A,B,CC,DP      ; Postbyte = 000001111\n" +
        	    "        PULS X,Y            ; Postbyte = 00110000\n" +
        	    "        PSHU S,U            ; Postbyte = 01010000\n" +
        	    "        PULU D,PC           ; Postbyte = 10000001\n" +
        	    "\n" +
        	    "        ; TFR et EXG\n" +
        	    "        TFR A,B             ; Postbyte = 10001001\n" +
        	    "        EXG X,Y             ; Postbyte = 00010010\n" +
        	    "\n" +
        	    "        END\n";
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Code généré (" + machineCode.length + " octets):");
            
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.println();
            }
            
            System.out.println("\n\nDécodage attendu:");
            System.out.println("PSHS A,B  = 34 03");
            System.out.println("PULS X,Y  = 35 30");
            System.out.println("PSHU S,U  = 36 50");
            System.out.println("PULU D,PC = 37 81");
            System.out.println("TFR A,B   = 1F 89");
            System.out.println("EXG X,Y   = 1E 12");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            
            System.err.println("\n🔧 CORRECTION NÉCESSAIRE:");
            System.err.println("1. Dans OpcodeGenerator.java:");
            System.err.println("   addOpcode(\"PSHS\", \"IMMEDIATE\", 0x34);");
            System.err.println("   addOpcode(\"PULS\", \"IMMEDIATE\", 0x35);");
            System.err.println("   addOpcode(\"PSHU\", \"IMMEDIATE\", 0x36);");
            System.err.println("   addOpcode(\"PULU\", \"IMMEDIATE\", 0x37);");
            System.err.println("   addOpcode(\"TFR\", \"IMMEDIATE\", 0x1F);");
            System.err.println("   addOpcode(\"EXG\", \"IMMEDIATE\", 0x1E);");
            
            System.err.println("\n2. Dans Parser.getAddressingMode():");
            System.err.println("   Pour EXG/TFR/PSHS/PULS/PSHU/PULU, retourner \"IMMEDIATE\"");
        }
    }
}