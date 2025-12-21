package motorola6809.debugger;

import motorola6809.core.CPU;

public class ExecutionController {
    
    private CPU cpu;
    private boolean stepMode;
    private boolean runToAddress;
    private int targetAddress;
    private long maxCycles; // Pour éviter les boucles infinies
    
    public ExecutionController(CPU cpu) {
        this.cpu = cpu;
        this.stepMode = false;
        this.runToAddress = false;
        this.targetAddress = -1;
        this.maxCycles = 1000000; // 1 million par défaut
    }
    
    /**
     * Exécute une seule instruction (step into)
     */
    public void stepInto() {
        if (!cpu.isHalted()) {
            cpu.step();
        }
    }
    
    /**
     * Exécute jusqu'à la prochaine instruction (step over)
     */
    public void stepOver() {
        if (cpu.isHalted()) {
            return;
        }
        
        int currentPC = cpu.getRegisters().getPC();
        int opcode = cpu.getMemory().readByte(currentPC);
        
        // Si c'est un JSR (0xBD, 0x9D, 0xAD, 0x8D), on exécute jusqu'au retour
        if (opcode == 0xBD || opcode == 0x9D || opcode == 0xAD || opcode == 0x8D) {
            // Trouve l'adresse de retour
            int returnAddress = currentPC + getInstructionSize(opcode);
            runUntil(returnAddress);
        } else {
            stepInto();
        }
    }
    
    /**
     * Exécute jusqu'à sortir de la fonction courante (step out)
     */
    public void stepOut() {
        if (cpu.isHalted()) {
            return;
        }
        
        // Exécute jusqu'à un RTS (0x39)
        while (!cpu.isHalted() && cpu.getMemory().readByte(cpu.getRegisters().getPC()) != 0x39) {
            cpu.step();
            if (cpu.getCycleCount() > maxCycles) {
                break;
            }
        }
        
        // Exécute le RTS
        if (!cpu.isHalted()) {
            cpu.step();
        }
    }
    
    /**
     * Exécute jusqu'à une adresse spécifique
     */
    public void runUntil(int address) {
        targetAddress = address;
        runToAddress = true;
        
        long startCycles = cpu.getCycleCount();
        while (!cpu.isHalted() && cpu.getRegisters().getPC() != targetAddress) {
            cpu.step();
            if (cpu.getCycleCount() - startCycles > maxCycles) {
                break;
            }
        }
        
        runToAddress = false;
    }
    
    /**
     * Exécute un nombre spécifique de cycles
     */
    public void runCycles(long cycles) {
        long startCycles = cpu.getCycleCount();
        long targetCycles = startCycles + cycles;
        
        while (!cpu.isHalted() && cpu.getCycleCount() < targetCycles) {
            cpu.step();
        }
    }
    
    /**
     * Continue l'exécution normale
     */
    public void continueExecution() {
        cpu.start();
        cpu.run();
    }
    
    /**
     * Pause l'exécution
     */
    public void pause() {
        cpu.stop();
    }
    
    /**
     * Obtient la taille d'une instruction (simplifié)
     */
    private int getInstructionSize(int opcode) {
        // Ceci est simplifié, devrait être basé sur une table complète
        if (opcode >= 0x80 && opcode <= 0x8F) return 2; // Immédiat 8 bits
        if (opcode >= 0x90 && opcode <= 0x9F) return 2; // Direct
        if (opcode >= 0xA0 && opcode <= 0xAF) return 2; // Indexé
        if (opcode >= 0xB0 && opcode <= 0xBF) return 3; // Étendu
        return 1; // Inhérent par défaut
    }
    
    // Getters et Setters
    public void setMaxCycles(long maxCycles) {
        this.maxCycles = maxCycles;
    }
    
    public long getMaxCycles() {
        return maxCycles;
    }
}