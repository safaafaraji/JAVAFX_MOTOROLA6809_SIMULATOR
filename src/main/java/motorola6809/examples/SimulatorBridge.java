package motorola6809.examples;

import javafx.application.Platform;

/**
 * Pont entre le backend et l'interface JavaFX
 * Version simplifiée sans dépendances manquantes
 */
public class SimulatorBridge {
    
    private boolean stepMode;
    
    public SimulatorBridge() {
        this.stepMode = false;
    }
    
    /**
     * Compile et charge le programme (version simplifiée)
     */
    public void compileAndLoad(String sourceCode) {
        try {
            System.out.println("Compilation du programme...");
            // TODO: Implémenter la compilation réelle
            
            // Simulation de succès
            System.out.println("Programme chargé avec succès");
            
        } catch (Exception e) {
            System.err.println("Erreur de compilation: " + e.getMessage());
        }
    }
    
    /**
     * Exécute une instruction (pas à pas)
     */
    public void executeStep() {
        System.out.println("Exécution pas à pas...");
        // TODO: Implémenter l'exécution réelle
    }
    
    /**
     * Exécute le programme complet
     */
    public void executeAll() {
        System.out.println("Exécution complète du programme...");
        // TODO: Implémenter l'exécution réelle
    }
    
    /**
     * Réinitialise le simulateur
     */
    public void reset() {
        System.out.println("Réinitialisation du simulateur...");
        stepMode = false;
    }
    
    public boolean isStepMode() {
        return stepMode;
    }
    
    public void setStepMode(boolean stepMode) {
        this.stepMode = stepMode;
    }
}