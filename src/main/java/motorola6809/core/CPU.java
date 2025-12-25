package motorola6809.core;

import motorola6809.config.Constants;
import motorola6809.instruction.InstructionDecoder;

public class CPU {
    
    private Register registers;
    private StatusFlags flags;
    private Memory memory;
    private boolean running;
    private boolean halted;
    private boolean waitingForInterrupt;  // ← Nom correct
    private boolean sync;
    private long cycleCount;
    private int instructionsExecuted;
    private int maxInstructions; // Limite d'instructions
    
    public CPU() {
        this.registers = new Register();
        this.flags = new StatusFlags();
        this.memory = new Memory();
        this.running = false;
        this.halted = false;
        this.waitingForInterrupt = false;  // ← Initialisé
        this.sync = false;
        this.cycleCount = 0;
        this.instructionsExecuted = 0;
        this.maxInstructions = 1000; // Limite par défaut
    }
    
    public void reset() {
        registers.reset();
        flags.reset();
        memory.reset();
        running = false;
        halted = false;
        waitingForInterrupt = false;
        sync = false;
        cycleCount = 0;
        instructionsExecuted = 0;
        
        int resetVector = memory.readWord(Constants.VECTOR_RESET);
        if (resetVector != 0xFFFF) {
            registers.setPC(resetVector);
        } else {
            registers.setPC(Constants.ROM_START);
        }
    }
    
    public void loadProgram(byte[] program, int startAddress) {
        memory.loadProgram(program, startAddress);
        registers.setPC(startAddress);
        instructionsExecuted = 0;
    }
    
    /**
     * Exécute une seule instruction
     */
    public int executeInstruction() {
        if (halted) {
            return 0;
        }
        
        if (waitingForInterrupt || sync) {
            return 1;
        }
        
        // Protection contre les boucles infinies
        if (instructionsExecuted >= maxInstructions) {
            System.err.println("⚠️ Arrêt: limite de " + maxInstructions + " instructions atteinte");
            halt();
            return 0;
        }
        
        // Protection PC hors limites
        if (registers.getPC() > 0xFFFF) {
            System.err.println("⚠️ Arrêt: PC hors limites (0x" + 
                             String.format("%04X", registers.getPC()) + ")");
            halt();
            return 0;
        }
        
        int opcode = fetch();
        int cycles = InstructionDecoder.decodeAndExecute(this, opcode);
        
        cycleCount += cycles;
        instructionsExecuted++;
        
        return cycles;
    }
    
    private int fetch() {
        int value = memory.readByte(registers.getPC());
        registers.incrementPC();
        return value;
    }
    
    public int fetchByte() {
        return fetch();
    }
    
    public int fetchWord() {
        int high = fetch();
        int low = fetch();
        return (high << 8) | low;
    }
    
    // Méthodes de pile
    public void pushS(int value) {
        registers.setS(registers.getS() - 1);
        memory.writeByte(registers.getS(), value);
    }
    
    public int popS() {
        int value = memory.readByte(registers.getS());
        registers.setS(registers.getS() + 1);
        return value;
    }
    
    public void pushU(int value) {
        registers.setU(registers.getU() - 1);
        memory.writeByte(registers.getU(), value);
    }
    
    public int popU() {
        int value = memory.readByte(registers.getU());
        registers.setU(registers.getU() + 1);
        return value;
    }
    
    public void pushWordS(int value) {
        pushS(value & 0xFF);
        pushS((value >> 8) & 0xFF);
    }
    
    public int popWordS() {
        int high = popS();
        int low = popS();
        return (high << 8) | low;
    }
    
    public void pushWordU(int value) {
        pushU(value & 0xFF);
        pushU((value >> 8) & 0xFF);
    }
    
    public int popWordU() {
        int high = popU();
        int low = popU();
        return (high << 8) | low;
    }
    
    public void halt() {
        this.halted = true;
        this.running = false;
    }
    
    public void start() {
        this.running = true;
        this.halted = false;
        this.waitingForInterrupt = false;
        this.sync = false;
    }
    
    public void stop() {
        this.running = false;
    }
    
    public void step() {
        if (!halted) {
            executeInstruction();
        }
    }
    
    public void run() {
        running = true;
        while (running && !halted) {
            executeInstruction();
        }
    }
    
    public void sync() {
        this.sync = true;
    }
    
    public void waitForInterrupt() {
        this.waitingForInterrupt = true;
    }
    
    // Permet de changer la limite d'instructions
    public void setMaxInstructions(int max) {
        this.maxInstructions = max;
    }
    
    public int getInstructionsExecuted() {
        return instructionsExecuted;
    }
    
    public int getMaxInstructions() {
        return maxInstructions;
    }
    
    public boolean isWaitingForInterrupt() {
        return waitingForInterrupt;
    }
    
    public boolean isSync() {
        return sync;
    }
    
    public Register getRegisters() {
        return registers;
    }
    
    public StatusFlags getFlags() {
        return flags;
    }
    
    public Memory getMemory() {
        return memory;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public boolean isHalted() {
        return halted;
    }
    
    public long getCycleCount() {
        return cycleCount;
    }
    
    @Override
    public String toString() {
        return String.format(
            "CPU State:\n%s\n%s\nCycles: %d Instructions: %d/%d",
            registers.toString(),
            flags.toString(),
            cycleCount,
            instructionsExecuted,
            maxInstructions
        );
    }
}