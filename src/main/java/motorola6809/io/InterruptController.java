package motorola6809.io;

import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class InterruptController {
    
    private CPU cpu;
    private boolean irqPending;
    private boolean firqPending;
    private boolean nmiPending;
    
    public InterruptController(CPU cpu) {
        this.cpu = cpu;
        reset();
    }
    
    /**
     * Réinitialise le contrôleur d'interruptions
     */
    public void reset() {
        irqPending = false;
        firqPending = false;
        nmiPending = false;
    }
    
    /**
     * Déclenche une IRQ
     */
    public void triggerIRQ() {
        irqPending = true;
    }
    
    /**
     * Déclenche une FIRQ
     */
    public void triggerFIRQ() {
        firqPending = true;
    }
    
    /**
     * Déclenche une NMI (Non-Maskable Interrupt)
     */
    public void triggerNMI() {
        nmiPending = true;
    }
    
    /**
     * Traite les interruptions en attente
     */
    public void processInterrupts() {
        // NMI a la plus haute priorité (non masquable)
        if (nmiPending) {
            handleNMI();
            nmiPending = false;
            return;
        }
        
        // FIRQ
        if (firqPending && !cpu.getFlags().getFIRQMask()) {
            handleFIRQ();
            firqPending = false;
            return;
        }
        
        // IRQ
        if (irqPending && !cpu.getFlags().getIRQMask()) {
            handleIRQ();
            irqPending = false;
        }
    }
    
    /**
     * Gère une IRQ
     */
    private void handleIRQ() {
        // Sauvegarde l'état complet sur la pile
        cpu.getFlags().setEntire(true);
        pushState(true);
        
        // Masque les interruptions
        cpu.getFlags().setIRQMask(true);
        
        // Charge le vecteur IRQ
        int vector = cpu.getMemory().readWord(Constants.VECTOR_IRQ);
        cpu.getRegisters().setPC(vector);
    }
    
    /**
     * Gère une FIRQ
     */
    private void handleFIRQ() {
        // Sauvegarde partielle (PC et CC uniquement)
        cpu.getFlags().setEntire(false);
        pushState(false);
        
        // Masque les interruptions
        cpu.getFlags().setIRQMask(true);
        cpu.getFlags().setFIRQMask(true);
        
        // Charge le vecteur FIRQ
        int vector = cpu.getMemory().readWord(Constants.VECTOR_FIRQ);
        cpu.getRegisters().setPC(vector);
    }
    
    /**
     * Gère une NMI
     */
    private void handleNMI() {
        // Sauvegarde l'état complet
        cpu.getFlags().setEntire(true);
        pushState(true);
        
        // Masque les interruptions
        cpu.getFlags().setIRQMask(true);
        cpu.getFlags().setFIRQMask(true);
        
        // Charge le vecteur NMI
        int vector = cpu.getMemory().readWord(Constants.VECTOR_NMI);
        cpu.getRegisters().setPC(vector);
    }
    
    /**
     * Sauvegarde l'état sur la pile
     */
    private void pushState(boolean entireState) {
        if (entireState) {
            // Sauvegarde complète
            cpu.pushWordS(cpu.getRegisters().getPC());
            cpu.pushWordS(cpu.getRegisters().getU());
            cpu.pushWordS(cpu.getRegisters().getY());
            cpu.pushWordS(cpu.getRegisters().getX());
            cpu.pushS(cpu.getRegisters().getDP());
            cpu.pushS(cpu.getRegisters().getB());
            cpu.pushS(cpu.getRegisters().getA());
            cpu.pushS(cpu.getFlags().getCC());
        } else {
            // Sauvegarde partielle (FIRQ)
            cpu.pushWordS(cpu.getRegisters().getPC());
            cpu.pushS(cpu.getFlags().getCC());
        }
    }
    
    // Getters
    public boolean isIRQPending() {
        return irqPending;
    }
    
    public boolean isFIRQPending() {
        return firqPending;
    }
    
    public boolean isNMIPending() {
        return nmiPending;
    }
}


