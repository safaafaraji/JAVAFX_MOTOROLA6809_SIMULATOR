package motorola6809.core;

import motorola6809.assembler.Assembler;
import motorola6809.instruction.InstructionDecoder;  
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        int cycles = cpu.executeInstruction();
        instructionCount++;
        
        // Log
        log("Instruction exécutée : PC=$" + String.format("%04X", pcBefore) + 
            " Cycles=" + cycles);
        
        // Notifier
        notifyExecutionStep(pcBefore, 
            cpu.getMemory().readByte(pcBefore), 
            cycles);
        notifyAllRegisters();
        notifyAllFlags();
    }
    
    // ==================== NOTIFICATIONS ====================
    
    private void notifyAllRegisters() {
        Register regs = cpu.getRegisters();
        notifyRegisterUpdate("PC", regs.getPC());
        notifyRegisterUpdate("A", regs.getA());
        notifyRegisterUpdate("B", regs.getB());
        notifyRegisterUpdate("X", regs.getX());
        notifyRegisterUpdate("Y", regs.getY());
        notifyRegisterUpdate("S", regs.getS());
        notifyRegisterUpdate("U", regs.getU());
        notifyRegisterUpdate("DP", regs.getDP());
    }
    
    private void notifyAllFlags() {
        StatusFlags flags = cpu.getFlags();
        notifyFlagUpdate("E", flags.getEntire());
        notifyFlagUpdate("F", flags.getFIRQMask());
        notifyFlagUpdate("H", flags.getHalfCarry());
        notifyFlagUpdate("I", flags.getIRQMask());
        notifyFlagUpdate("N", flags.getNegative());
        notifyFlagUpdate("Z", flags.getZero());
        notifyFlagUpdate("V", flags.getOverflow());
        notifyFlagUpdate("C", flags.getCarry());
    }
    
    // ==================== ACCÈS MEMOIRE ====================
    
    public byte readMemory(int address) {
        return (byte) cpu.getMemory().readByte(address);
    }
    
    public void writeMemory(int address, byte value) {
        cpu.getMemory().writeByte(address, value);
        // Pas de notification pour chaque écriture mémoire (trop fréquent)
    }
    
    public int readMemoryWord(int address) {
        return cpu.getMemory().readWord(address);
    }
    
    public void writeMemoryWord(int address, int value) {
        cpu.getMemory().writeWord(address, value);
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
        notifyRegisterUpdate("PC", value);
    }
    
    public int getA() {
        return cpu.getRegisters().getA();
    }
    
    public void setA(int value) {
        cpu.getRegisters().setA(value);
        notifyRegisterUpdate("A", value);
    }
    
    public int getB() {
        return cpu.getRegisters().getB();
    }
    
    public void setB(int value) {
        cpu.getRegisters().setB(value);
        notifyRegisterUpdate("B", value);
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
        notifyRegisterUpdate("X", value);
    }
    
    public int getY() {
        return cpu.getRegisters().getY();
    }
    
    public void setY(int value) {
        cpu.getRegisters().setY(value);
        notifyRegisterUpdate("Y", value);
    }
    
    public int getS() {
        return cpu.getRegisters().getS();
    }
    
    public void setS(int value) {
        cpu.getRegisters().setS(value);
        notifyRegisterUpdate("S", value);
    }
    
    public int getU() {
        return cpu.getRegisters().getU();
    }
    
    public void setU(int value) {
        cpu.getRegisters().setU(value);
        notifyRegisterUpdate("U", value);
    }
    
    public int getDP() {
        return cpu.getRegisters().getDP();
    }
    
    public void setDP(int value) {
        cpu.getRegisters().setDP(value);
        notifyRegisterUpdate("DP", value);
    }
    
    // ==================== ACCÈS FLAGS ====================
    
    public boolean[] getFlags() {
        StatusFlags flags = cpu.getFlags();
        return new boolean[] {
            flags.getEntire(),
            flags.getFIRQMask(),
            flags.getHalfCarry(),
            flags.getIRQMask(),
            flags.getNegative(),
            flags.getZero(),
            flags.getOverflow(),
            flags.getCarry()
        };
    }
    
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