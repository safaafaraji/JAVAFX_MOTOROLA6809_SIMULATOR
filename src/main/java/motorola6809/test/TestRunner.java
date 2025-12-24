// TestRunner.java - Pour tester l'exécution
package motorola6809.test;

import motorola6809.core.SimulatorBackend;
import motorola6809.assembler.Assembler;

public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== TESTEUR SIMULATEUR 6809 ===\n");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        Assembler assembler = new Assembler();
        
        // Programme de test SIMPLE
        String testProgram = 
        	    "; === PROGRAMME TEST 6809 VALIDE ===\n" +
        	    "        ORG $1000\n" +
        	    "\n" +
        	    "START :\n"+
        	    "		 LDA #$05        ; A = 5\n" +
        	    "        LDB #$03        ; B = 3\n" +
        	    "        ADDA #$02       ; A = 5 + 2 = 7  (mode IMMEDIATE)\n" +
        	    "        ADDB #$01       ; B = 3 + 1 = 4  (mode IMMEDIATE)\n" +
        	    "        MUL             ; D = A * B = 28 (0x001C)\n" +
        	    "        STA $2000       ; Stocker A (0x00 après MUL)\n" +
        	    "        STB $2001       ; Stocker B (0x1C après MUL)\n" +
        	    "        SWI             ; Software Interrupt\n" +
        	    "        END\n";
        
        System.out.println("Code source:");
        System.out.println(testProgram);
        
        try {
            // Assembler
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(testProgram);
            
            System.out.println("Code machine (" + machineCode.length + " octets):");
            for (int i = 0; i < machineCode.length; i++) {
                System.out.print(String.format("%02X ", machineCode[i] & 0xFF));
            }
            System.out.println();
            
            // Charger et exécuter
            backend.assemble(testProgram);
            
            System.out.println("\n=== ÉTAT INITIAL ===");
            printRegisters(backend);
            
            // Exécuter pas à pas
            System.out.println("\n=== EXÉCUTION PAS À PAS ===");
            
            for (int i = 0; i < 10; i++) {
                System.out.println("\nInstruction " + (i + 1) + ":");
                backend.step();
                printRegisters(backend);
                
                if (!backend.getCPU().isRunning()) {
                    System.out.println("CPU arrêté (SWI)");
                    break;
                }
            }
            
            // Vérifier la mémoire
            System.out.println("\n=== MÉMOIRE À 0x2000 ===");
            byte mem = backend.readMemory(0x2000);
            System.out.println("0x2000 = 0x" + String.format("%02X", mem) + " (" + mem + ")");
            
        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void printRegisters(SimulatorBackend backend) {
        System.out.printf("PC: %04X  A: %02X  B: %02X  D: %04X\n",
            backend.getPC(), backend.getA(), backend.getB(), backend.getD());
        System.out.printf("X:  %04X  Y: %04X  S: %04X  U: %04X  DP: %02X\n",
            backend.getX(), backend.getY(), backend.getS(), backend.getU(), backend.getDP());
        
        boolean[] flags = backend.getFlags();
        System.out.printf("Flags: E=%d F=%d H=%d I=%d N=%d Z=%d V=%d C=%d\n",
            flags[0]?1:0, flags[1]?1:0, flags[2]?1:0, flags[3]?1:0,
            flags[4]?1:0, flags[5]?1:0, flags[6]?1:0, flags[7]?1:0);
    }
}