package motorola6809.debugger;

import motorola6809.core.CPU;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Debugger {
    
    private CPU cpu;
    private Map<Integer, Breakpoint> breakpoints;
    private ExecutionController executionController;
    private MemoryInspector memoryInspector;
    private List<String> executionHistory;
    private int historySize;
    
    public Debugger(CPU cpu) {
        this.cpu = cpu;
        this.breakpoints = new HashMap<>();
        this.executionController = new ExecutionController(cpu);
        this.memoryInspector = new MemoryInspector(cpu.getMemory());
        this.executionHistory = new ArrayList<>();
        this.historySize = 100; // Garde les 100 dernières instructions
    }
    
    // Gestion des breakpoints
    
    /**
     * Ajoute un breakpoint
     */
    public void addBreakpoint(int address) {
        breakpoints.put(address, new Breakpoint(address));
    }
    
    /**
     * Ajoute un breakpoint conditionnel
     */
    public void addBreakpoint(int address, String condition) {
        breakpoints.put(address, new Breakpoint(address, condition));
    }
    
    /**
     * Retire un breakpoint
     */
    public void removeBreakpoint(int address) {
        breakpoints.remove(address);
    }
    
    /**
     * Active/désactive un breakpoint
     */
    public void toggleBreakpoint(int address) {
        Breakpoint bp = breakpoints.get(address);
        if (bp != null) {
            bp.setEnabled(!bp.isEnabled());
        }
    }
    
    /**
     * Retire tous les breakpoints
     */
    public void clearAllBreakpoints() {
        breakpoints.clear();
    }
    
    /**
     * Vérifie si on est sur un breakpoint
     */
    public boolean isAtBreakpoint() {
        int pc = cpu.getRegisters().getPC();
        Breakpoint bp = breakpoints.get(pc);
        if (bp != null && bp.isEnabled()) {
            bp.hit();
            return true;
        }
        return false;
    }
    
    /**
     * Liste tous les breakpoints
     */
    public List<Breakpoint> listBreakpoints() {
        return new ArrayList<>(breakpoints.values());
    }
    
    // Contrôle d'exécution
    
    /**
     * Exécute pas à pas
     */
    public void step() {
        recordHistory();
        executionController.stepInto();
    }
    
    /**
     * Exécute en sautant les appels
     */
    public void stepOver() {
        recordHistory();
        executionController.stepOver();
    }
    
    /**
     * Exécute jusqu'à la fin de la fonction
     */
    public void stepOut() {
        recordHistory();
        executionController.stepOut();
    }
    
    /**
     * Continue l'exécution jusqu'au prochain breakpoint
     */
    public void run() {
        while (!cpu.isHalted() && !isAtBreakpoint()) {
            recordHistory();
            cpu.step();
        }
    }
    
    /**
     * Exécute jusqu'à une adresse
     */
    public void runTo(int address) {
        executionController.runUntil(address);
    }
    
    /**
     * Pause l'exécution
     */
    public void pause() {
        executionController.pause();
    }
    
    // Inspection de la mémoire
    
    public String dumpMemory(int startAddress, int length) {
        return memoryInspector.dump(startAddress, length);
    }
    
    public int readByte(int address) {
        return memoryInspector.readByte(address);
    }
    
    public void writeByte(int address, int value) {
        memoryInspector.writeByte(address, value);
    }
    
    // Historique d'exécution
    
    private void recordHistory() {
        String state = String.format("%04X: %s", 
            cpu.getRegisters().getPC(),
            cpu.getRegisters().toString()
        );
        
        executionHistory.add(state);
        
        // Limite la taille de l'historique
        if (executionHistory.size() > historySize) {
            executionHistory.remove(0);
        }
    }
    
    public List<String> getExecutionHistory() {
        return new ArrayList<>(executionHistory);
    }
    
    public void clearHistory() {
        executionHistory.clear();
    }
    
    // État du CPU
    
    public String getCPUState() {
        return cpu.toString();
    }
    
    public String getRegisterState() {
        return cpu.getRegisters().toString();
    }
    
    public String getFlagsState() {
        return cpu.getFlags().toString();
    }
    
    // Getters
    public ExecutionController getExecutionController() {
        return executionController;
    }
    
    public MemoryInspector getMemoryInspector() {
        return memoryInspector;
    }
}

