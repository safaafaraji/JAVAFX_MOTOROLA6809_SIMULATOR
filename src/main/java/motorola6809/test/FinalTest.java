package motorola6809.test;

import motorola6809.core.SimulatorBackend;

public class FinalTest {
    
    public static void main(String[] args) {
        System.out.println("=== TEST FINAL 6809 ===\n");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        
        // Programme 6809 COMPLET avec instructions valides
        String testProgram = getCompleteTestProgram();
        
        System.out.println("Assemblage du programme...");
        
        if (backend.assemble(testProgram)) {
            System.out.println("✓ Programme assemblé\n");
            
            // Exécuter pas à pas
            System.out.println("=== EXÉCUTION PAS À PAS ===\n");
            
            for (int i = 0; i < 20; i++) {
                System.out.println("Étape " + (i + 1) + ":");
                backend.step();
                displayState(backend);
                
                if (backend.getCPU().isHalted()) {
                    System.out.println("✓ Programme terminé (SWI)");
                    break;
                }
                
                // Pause pour voir
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
            
            System.out.println("\n=== TEST RÉUSSI ===");
            
        } else {
            System.err.println("✗ Échec de l'assemblage");
        }
    }
    
    private static String getCompleteTestProgram() {
        return 
        	    "; =========================================\n" +
        	    "; PROGRAMME 6809 VALIDE - SYNTAXE CORRECTE\n" +
        	    "; =========================================\n" +
        	    "\n" +
        	    "        ORG $1000           ; Adresse de départ\n" +
        	    "\n" +
        	    "START :\n   "+ 
        	    "LDA #$05            ; A = 5\n" +
        	    "        LDB #$03            ; B = 3\n" +
        	    "        \n" +
        	    "        ; ADDITIONS\n" +
        	    "        ADDA #$02           ; A = 5 + 2 = 7\n" +
        	    "        ADDB #$01           ; B = 3 + 1 = 4\n" +
        	    "        \n" +
        	    "        ; MULTIPLICATION\n" +
        	    "        MUL                 ; D = 7 * 4 = 28 (0x001C)\n" +
        	    "                           ; A = 0x00, B = 0x1C après MUL\n" +
        	    "        \n" +
        	    "        ; STOCKAGE MÉMOIRE\n" +
        	    "        STA $2000           ; Stocker A à 0x2000 (0x00)\n" +
        	    "        STB $2001           ; Stocker B à 0x2001 (0x1C)\n" +
        	    "        \n" +
        	    "        ; OPÉRATIONS LOGIQUES\n" +
        	    "        LDA #$AA            ; A = 0xAA (10101010)\n" +
        	    "        COMA                ; A = 0x55 (01010101)\n" +
        	    "        INCA                ; A = 0x56\n" +
        	    "        DECA                ; A = 0x55\n" +
        	    "        \n" +
        	    "        LDB #$55            ; B = 0x55 (01010101)\n" +
        	    "        EORB #$FF           ; B = 0x55 ^ 0xFF = 0xAA\n" +
        	    "        \n" +
        	    "        ; COMPARAISONS\n" +
        	    "        LDA #$10            ; A = 0x10 (16)\n" +
        	    "        LDB #$05            ; B = 0x05 (5)\n" +
        	    "        CMPA #$05           ; Comparer A (16) avec 5\n" +
        	    "        CMPB #$05           ; Comparer B (5) avec 5 → Z=1\n" +
        	    "        \n" +
        	    "        ; CHARGEMENT AVEC ADRESSAGE\n" +
        	    "        LDX #$3000          ; X = 0x3000\n" +
        	    "        LDA ,X              ; Charger A depuis mémoire[X]\n" +
        	    "        \n" +
        	    "        ; FIN\n" +
        	    "        SWI                 ; Software Interrupt\n" +
        	    "        END\n";
    }
    
    private static void displayState(SimulatorBackend backend) {
        System.out.printf("PC: %04X  A: %02X  B: %02X  D: %04X  ",
            backend.getPC(), backend.getA(), backend.getB(), backend.getD());
        
        System.out.printf("X: %04X  Y: %04X\n",
            backend.getX(), backend.getY());
        
        System.out.printf("S: %04X  U: %04X  DP: %02X  ",
            backend.getS(), backend.getU(), backend.getDP());
        
        boolean[] flags = backend.getFlags();
        System.out.printf("Flags: E%d F%d H%d I%d N%d Z%d V%d C%d\n",
            flags[0]?1:0, flags[1]?1:0, flags[2]?1:0, flags[3]?1:0,
            flags[4]?1:0, flags[5]?1:0, flags[6]?1:0, flags[7]?1:0);
        
        System.out.println("----------------------------------------");
    }
}