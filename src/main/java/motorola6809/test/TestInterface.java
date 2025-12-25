package motorola6809.test;

import motorola6809.core.SimulatorBackend;
import motorola6809.ui.ArchitectureWindow;
import motorola6809.ui.EditeurWindow;
import motorola6809.ui.MenuWindow;

public class TestInterface {
    
    public static void main(String[] args) {
        System.out.println("Test de l'interface Motorola 6809 Simulator");
        
        // Test 1: SimulatorBackend
        System.out.println("\n1. Test SimulatorBackend:");
        SimulatorBackend backend = SimulatorBackend.getInstance();
        System.out.println("   SimulatorBackend créé: " + (backend != null));
        
        // Test 2: Assemblage simple
        System.out.println("\n2. Test d'assemblage:");
        String testProgram = 
            "        ORG $1000\n" +
            "START :\n"+ 
            "  LDA #$05\n" +
            "        LDB #$03\n" +
            "        MUL\n" +
            "        END";
        
        boolean assembled = backend.assemble(testProgram);
        System.out.println("   Programme assemblé: " + assembled);
        
        // Test 3: Fenêtres
        System.out.println("\n3. Test des fenêtres:");
        System.out.println("   Ces tests nécessitent JavaFX - exécutez MainApp pour tester l'interface graphique");
        
        // Test 4: Instructions
        System.out.println("\n4. Test des instructions:");
        backend.step();
        System.out.println("   PC après un pas: $" + SimulatorBackend.formatHex16(backend.getPC()));
        System.out.println("   A: $" + SimulatorBackend.formatHex8(backend.getA()));
        System.out.println("   B: $" + SimulatorBackend.formatHex8(backend.getB()));
        
        System.out.println("\n=== TESTS TERMINÉS ===");
    }
}