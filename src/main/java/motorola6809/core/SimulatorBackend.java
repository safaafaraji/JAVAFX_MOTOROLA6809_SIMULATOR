package motorola6809.core;

import motorola6809.assembler.Assembler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;  // IMPORTANT: Ajouter cette importation

public class SimulatorBackend {
    
    private static SimulatorBackend instance;
    private CPU cpu;
    private Assembler assembler;
    
    // État d'exécution
    private boolean isRunning = false;
    private boolean isPaused = false;
    private ScheduledExecutorService executor;
    
    // Données du programme
    private byte[] currentProgram;
    private int programStartAddress = 0x1000;
    private List<String> executionLog;
    private int instructionCount = 0;
    
    // Observateurs
    private List<SimulatorObserver> observers = new ArrayList<>();
    
    // Pour suivre les valeurs précédentes et détecter les changements
    private int prevA = 0;
    private int prevB = 0;
    private int prevPC = 0;
    private int prevX = 0;
    private int prevY = 0;
    private int prevS = 0;
    private int prevU = 0;
    private int prevDP = 0;
    private int prevCC = 0;
    
    // Breakpoints
    private List<Integer> breakpoints = new ArrayList<>();
    
    private SimulatorBackend() {
        this.cpu = new CPU();
        this.assembler = new Assembler();
        this.executionLog = new ArrayList<>();
        initialize();
    }
    
    public static synchronized SimulatorBackend getInstance() {
        if (instance == null) {
            instance = new SimulatorBackend();
        }
        return instance;
    }
    
    // ==================== INTERFACE OBSERVER ====================
    
    public interface SimulatorObserver {
        void onRegisterUpdate(String register, int value);
        void onFlagUpdate(String flag, boolean value);
        void onMemoryUpdate(int address, int value);
        void onExecutionStateChange(boolean running, boolean paused);
        void onProgramLoaded(int startAddress, int size);
        void onExecutionStep(int pc, int opcode, int cycles);
    }
    
    public void addObserver(SimulatorObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(SimulatorObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyRegisterUpdate(String register, int value) {
        for (SimulatorObserver observer : observers) {
            observer.onRegisterUpdate(register, value);
        }
    }
    
    private void notifyFlagUpdate(String flag, boolean value) {
        for (SimulatorObserver observer : observers) {
            observer.onFlagUpdate(flag, value);
        }
    }
    
    private void notifyMemoryUpdate(int address, int value) {
        for (SimulatorObserver observer : observers) {
            observer.onMemoryUpdate(address, value);
        }
    }
    
    private void notifyExecutionStateChange() {
        for (SimulatorObserver observer : observers) {
            observer.onExecutionStateChange(isRunning, isPaused);
        }
    }
    
    private void notifyProgramLoaded() {
        for (SimulatorObserver observer : observers) {
            observer.onProgramLoaded(programStartAddress, 
                currentProgram != null ? currentProgram.length : 0);
        }
    }
    
    private void notifyExecutionStep(int pc, int opcode, int cycles) {
        for (SimulatorObserver observer : observers) {
            observer.onExecutionStep(pc, opcode, cycles);
        }
    }
    
    // ==================== INITIALISATION ====================
    
    private void initialize() {
        cpu.reset();
        executionLog.clear();
        instructionCount = 0;
        isRunning = false;
        isPaused = false;
        breakpoints.clear();
        
        // Initialiser les valeurs précédentes
        prevA = cpu.getRegisters().getA();
        prevB = cpu.getRegisters().getB();
        prevPC = cpu.getRegisters().getPC();
        prevX = cpu.getRegisters().getX();
        prevY = cpu.getRegisters().getY();
        prevS = cpu.getRegisters().getS();
        prevU = cpu.getRegisters().getU();
        prevDP = cpu.getRegisters().getDP();
        prevCC = cpu.getFlags().getCC();
        
        log("Simulateur initialisé");
        notifyAllRegisters();
        notifyAllFlags();
    }
    
    // ==================== ASSEMBLAGE ====================
    
    public boolean assemble(String sourceCode) {
        try {
            log("Début de l'assemblage...");
            
            currentProgram = assembler.assemble(sourceCode);
            cpu.loadProgram(currentProgram, programStartAddress);
            
            log("Programme assemblé : " + currentProgram.length + " octets");
            log("Adresse de départ : $" + String.format("%04X", programStartAddress));
            
            notifyProgramLoaded();
            notifyAllRegisters();
            
            return true;
            
        } catch (Exception e) {
            log("Erreur d'assemblage : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== CONTRÔLE D'EXÉCUTION ====================
    
    public void start() {
        if (currentProgram == null || currentProgram.length == 0) {
            log("Erreur : Aucun programme chargé");
            return;
        }
        
        if (isRunning) return;
        
        isRunning = true;
        isPaused = false;
        
        log("Exécution démarrée");
        notifyExecutionStateChange();
        
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (!isRunning || isPaused) return;
            
            try {
                executeStepInternal();
            } catch (Exception e) {
                log("Erreur d'exécution : " + e.getMessage());
                stop();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
    
    public void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            log("Exécution en pause");
        } else {
            log("Exécution reprise");
        }
        notifyExecutionStateChange();
    }
    
    public void stop() {
        isRunning = false;
        isPaused = false;
        
        if (executor != null) {
            executor.shutdown();
        }
        
        log("Exécution arrêtée");
        notifyExecutionStateChange();
    }
    
    public void step() {
        if (currentProgram == null) {
            log("Erreur : Aucun programme chargé");
            return;
        }
        
        try {
            executeStepInternal();
        } catch (Exception e) {
            log("Erreur pas à pas : " + e.getMessage());
        }
    }
    
    public void reset() {
        stop();
        initialize();
        
        // Recharger le programme si existant
        if (currentProgram != null) {
            cpu.loadProgram(currentProgram, programStartAddress);
        }
        
        log("Simulateur réinitialisé");
        notifyAllRegisters();
        notifyAllFlags();
    }
    
    // ==================== EXÉCUTION INTERNE ====================
    
    private void executeStepInternal() {
        if (cpu.isHalted()) {
            log("CPU arrêté (HALT)");
            stop();
            return;
        }
        
        int pcBefore = cpu.getRegisters().getPC();
        
        // Vérifier les breakpoints
        if (breakpoints.contains(pcBefore)) {
            log("⏸ Breakpoint atteint à $" + formatHex16(pcBefore));
            pause();
            return;
        }
        
        int cycles = cpu.executeInstruction();
        instructionCount++;
        
        // Log
        log("Instruction exécutée : PC=$" + String.format("%04X", pcBefore) + 
            " Cycles=" + cycles);
        
        // Notifier le pas d'exécution
        notifyExecutionStep(pcBefore, 
            cpu.getMemory().readByte(pcBefore), 
            cycles);
        
        // Vérifier et notifier les changements de registres
        checkAndNotifyRegisterChanges();
        
        // Vérifier et notifier les changements de flags
        checkAndNotifyFlagChanges();
        
        // Vérifier la limite d'instructions
        if (instructionCount >= 1000) {
            log("⚠️ Limite d'instructions atteinte (" + instructionCount + ")");
            stop();
        }
    }
    
    // ==================== DÉTECTION DES CHANGEMENTS ====================
    
    private void checkAndNotifyRegisterChanges() {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        // Vérifier chaque registre et notifier seulement s'il a changé
        if (regs.getPC() != prevPC) {
            prevPC = regs.getPC();
            notifyRegisterUpdate("PC", prevPC);
        }
        
        if (regs.getA() != prevA) {
            prevA = regs.getA();
            notifyRegisterUpdate("A", prevA);
        }
        
        if (regs.getB() != prevB) {
            prevB = regs.getB();
            notifyRegisterUpdate("B", prevB);
        }
        
        if (regs.getX() != prevX) {
            prevX = regs.getX();
            notifyRegisterUpdate("X", prevX);
        }
        
        if (regs.getY() != prevY) {
            prevY = regs.getY();
            notifyRegisterUpdate("Y", prevY);
        }
        
        if (regs.getS() != prevS) {
            prevS = regs.getS();
            notifyRegisterUpdate("S", prevS);
        }
        
        if (regs.getU() != prevU) {
            prevU = regs.getU();
            notifyRegisterUpdate("U", prevU);
        }
        
        if (regs.getDP() != prevDP) {
            prevDP = regs.getDP();
            notifyRegisterUpdate("DP", prevDP);
        }
        
        // Notifier aussi le registre D (combinaison A:B)
        notifyRegisterUpdate("D", regs.getD());
    }
    
    private void checkAndNotifyFlagChanges() {
        StatusFlags flags = cpu.getFlags();
        int currentCC = flags.getCC();
        
        // Vérifier si le registre CC a changé
        if (currentCC != prevCC) {
            prevCC = currentCC;
            
            // Notifier chaque flag individuellement
            notifyFlagUpdate("E", flags.getEntire());
            notifyFlagUpdate("F", flags.getFIRQMask());
            notifyFlagUpdate("H", flags.getHalfCarry());
            notifyFlagUpdate("I", flags.getIRQMask());
            notifyFlagUpdate("N", flags.getNegative());
            notifyFlagUpdate("Z", flags.getZero());
            notifyFlagUpdate("V", flags.getOverflow());
            notifyFlagUpdate("C", flags.getCarry());
        } else {
            // Vérifier individuellement chaque flag
            boolean e = flags.getEntire();
            boolean f = flags.getFIRQMask();
            boolean h = flags.getHalfCarry();
            boolean i = flags.getIRQMask();
            boolean n = flags.getNegative();
            boolean z = flags.getZero();
            boolean v = flags.getOverflow();
            boolean c = flags.getCarry();
            
            // Récupérer les flags précédents du registre CC
            boolean prevE = ((prevCC >> 7) & 1) == 1;
            boolean prevF = ((prevCC >> 6) & 1) == 1;
            boolean prevH = ((prevCC >> 5) & 1) == 1;
            boolean prevI = ((prevCC >> 4) & 1) == 1;
            boolean prevN = ((prevCC >> 3) & 1) == 1;
            boolean prevZ = ((prevCC >> 2) & 1) == 1;
            boolean prevV = ((prevCC >> 1) & 1) == 1;
            boolean prevC = (prevCC & 1) == 1;
            
            // Notifier seulement les flags qui ont changé
            if (e != prevE) notifyFlagUpdate("E", e);
            if (f != prevF) notifyFlagUpdate("F", f);
            if (h != prevH) notifyFlagUpdate("H", h);
            if (i != prevI) notifyFlagUpdate("I", i);
            if (n != prevN) notifyFlagUpdate("N", n);
            if (z != prevZ) notifyFlagUpdate("Z", z);
            if (v != prevV) notifyFlagUpdate("V", v);
            if (c != prevC) notifyFlagUpdate("C", c);
        }
    }
    
    // ==================== NOTIFICATIONS COMPLÈTES ====================
    
    private void notifyAllRegisters() {
        Register regs = cpu.getRegisters();
        prevA = regs.getA();
        prevB = regs.getB();
        prevPC = regs.getPC();
        prevX = regs.getX();
        prevY = regs.getY();
        prevS = regs.getS();
        prevU = regs.getU();
        prevDP = regs.getDP();
        
        notifyRegisterUpdate("PC", prevPC);
        notifyRegisterUpdate("A", prevA);
        notifyRegisterUpdate("B", prevB);
        notifyRegisterUpdate("X", prevX);
        notifyRegisterUpdate("Y", prevY);
        notifyRegisterUpdate("S", prevS);
        notifyRegisterUpdate("U", prevU);
        notifyRegisterUpdate("DP", prevDP);
        notifyRegisterUpdate("D", regs.getD());
    }
    
    private void notifyAllFlags() {
        StatusFlags flags = cpu.getFlags();
        prevCC = flags.getCC();
        
        notifyFlagUpdate("E", flags.getEntire());
        notifyFlagUpdate("F", flags.getFIRQMask());
        notifyFlagUpdate("H", flags.getHalfCarry());
        notifyFlagUpdate("I", flags.getIRQMask());
        notifyFlagUpdate("N", flags.getNegative());
        notifyFlagUpdate("Z", flags.getZero());
        notifyFlagUpdate("V", flags.getOverflow());
        notifyFlagUpdate("C", flags.getCarry());
    }
    
    // ==================== BREAKPOINTS ====================
    
    public void addBreakpoint(int address) {
        if (!breakpoints.contains(address)) {
            breakpoints.add(address);
            log("Breakpoint ajouté à $" + formatHex16(address));
        }
    }
    
    public void removeBreakpoint(int address) {
        breakpoints.remove(Integer.valueOf(address));
        log("Breakpoint retiré à $" + formatHex16(address));
    }
    
    public List<Integer> getBreakpoints() {
        return new ArrayList<>(breakpoints);
    }
    
    public void clearBreakpoints() {
        breakpoints.clear();
        log("Tous les breakpoints effacés");
    }
    
    // ==================== ACCÈS MEMOIRE ====================
    
 // ==================== ACCÈS MEMOIRE AVEC NOTIFICATION ====================

    public byte readMemory(int address) {
        return (byte) cpu.getMemory().readByte(address);
    }

    public void writeMemory(int address, byte value) {
        // Écrire en mémoire
        cpu.getMemory().writeByte(address, value & 0xFF);
        
        // Notifier les observateurs
        notifyMemoryUpdate(address, value & 0xFF);
        
        // Log
        log("Mémoire [$" + formatHex16(address) + "] = $" + formatHex8(value));
    }

    public void writeMemoryWord(int address, int value) {
        // Écrire le mot (16 bits)
        cpu.getMemory().writeWord(address, value & 0xFFFF);
        
        // Notifier les deux octets
        notifyMemoryUpdate(address, (value >> 8) & 0xFF);
        notifyMemoryUpdate(address + 1, value & 0xFF);
        
        // Log
        log("Mémoire [$" + formatHex16(address) + "] = $" + formatHex16(value));
    }

    public int readMemoryWord(int address) {
        return cpu.getMemory().readWord(address);
    }

    public byte[] getMemoryRange(int start, int length) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = (byte) cpu.getMemory().readByte(start + i);
        }
        return data;
    }
    
    // ==================== ACCÈS REGISTRES ====================
    
    public int getPC() {
        return cpu.getRegisters().getPC();
    }
    
    public void setPC(int value) {
        cpu.getRegisters().setPC(value);
        prevPC = value;
        notifyRegisterUpdate("PC", value);
    }
    
    public int getA() {
        return cpu.getRegisters().getA();
    }
    
    public void setA(int value) {
        cpu.getRegisters().setA(value);
        prevA = value;
        notifyRegisterUpdate("A", value);
        notifyRegisterUpdate("D", cpu.getRegisters().getD());
    }
    
    public int getB() {
        return cpu.getRegisters().getB();
    }
    
    public void setB(int value) {
        cpu.getRegisters().setB(value);
        prevB = value;
        notifyRegisterUpdate("B", value);
        notifyRegisterUpdate("D", cpu.getRegisters().getD());
    }
    
    public int getD() {
        return cpu.getRegisters().getD();
    }
    
    public void setD(int value) {
        cpu.getRegisters().setD(value);
        notifyRegisterUpdate("A", cpu.getRegisters().getA());
        notifyRegisterUpdate("B", cpu.getRegisters().getB());
    }
    
    public int getX() {
        return cpu.getRegisters().getX();
    }
    
    public void setX(int value) {
        cpu.getRegisters().setX(value);
        prevX = value;
        notifyRegisterUpdate("X", value);
    }
    
    public int getY() {
        return cpu.getRegisters().getY();
    }
    
    public void setY(int value) {
        cpu.getRegisters().setY(value);
        prevY = value;
        notifyRegisterUpdate("Y", value);
    }
    
    public int getS() {
        return cpu.getRegisters().getS();
    }
    
    public void setS(int value) {
        cpu.getRegisters().setS(value);
        prevS = value;
        notifyRegisterUpdate("S", value);
    }
    
    public int getU() {
        return cpu.getRegisters().getU();
    }
    
    public void setU(int value) {
        cpu.getRegisters().setU(value);
        prevU = value;
        notifyRegisterUpdate("U", value);
    }
    
    public int getDP() {
        return cpu.getRegisters().getDP();
    }
    
    public void setDP(int value) {
        cpu.getRegisters().setDP(value);
        prevDP = value;
        notifyRegisterUpdate("DP", value);
    }
    
    // ==================== ACCÈS FLAGS ====================
    
    public boolean getFlag(String flagName) {
        StatusFlags flags = cpu.getFlags();
        switch (flagName.toUpperCase()) {
            case "E": return flags.getEntire();
            case "F": return flags.getFIRQMask();
            case "H": return flags.getHalfCarry();
            case "I": return flags.getIRQMask();
            case "N": return flags.getNegative();
            case "Z": return flags.getZero();
            case "V": return flags.getOverflow();
            case "C": return flags.getCarry();
            default: return false;
        }
    }
    
    public void setFlag(String flagName, boolean value) {
        StatusFlags flags = cpu.getFlags();
        switch (flagName.toUpperCase()) {
            case "E": flags.setEntire(value); break;
            case "F": flags.setFIRQMask(value); break;
            case "H": flags.setHalfCarry(value); break;
            case "I": flags.setIRQMask(value); break;
            case "N": flags.setNegative(value); break;
            case "Z": flags.setZero(value); break;
            case "V": flags.setOverflow(value); break;
            case "C": flags.setCarry(value); break;
        }
        prevCC = flags.getCC();
        notifyFlagUpdate(flagName, value);
    }
    
    // ==================== LOGGING ====================
    
    private void log(String message) {
        executionLog.add(message);
        if (executionLog.size() > 100) {
            executionLog.remove(0);
        }
    }
    
    public List<String> getExecutionLog() {
        return new ArrayList<>(executionLog);
    }
    
    public void clearLog() {
        executionLog.clear();
    }
    
    // ==================== FORCE REFRESH ====================
    
    public void forceRefreshAll() {
        Platform.runLater(() -> {
            notifyAllRegisters();
            notifyAllFlags();
        });
    }
    
    // ==================== GETTERS/SETTERS ====================
    
    public CPU getCPU() {
        return cpu;
    }
    
    public byte[] getCurrentProgram() {
        return currentProgram;
    }
    
    public int getProgramStartAddress() {
        return programStartAddress;
    }
    
    public void setProgramStartAddress(int address) {
        this.programStartAddress = address & 0xFFFF;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public boolean isPaused() {
        return isPaused;
    }
    
    public int getInstructionCount() {
        return instructionCount;
    }
    
    public long getCycleCount() {
        return cpu.getCycleCount();
    }
    
    public String getCPUState() {
        return cpu.toString();
    }
    
    // ==================== UTILITAIRES ====================
    
    public static String formatHex16(int value) {
        return String.format("%04X", value & 0xFFFF);
    }
    
    public static String formatHex8(int value) {
        return String.format("%02X", value & 0xFF);
    }
    
    public static int parseHex(String hex) {
        if (hex.startsWith("$") || hex.startsWith("0x")) {
            hex = hex.substring(hex.startsWith("$") ? 1 : 2);
        }
        return Integer.parseInt(hex, 16);
    }
}