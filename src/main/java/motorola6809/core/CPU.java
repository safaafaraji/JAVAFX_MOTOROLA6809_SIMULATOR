package motorola6809.core;

import motorola6809.config.Constants;
import motorola6809.instruction.InstructionDecoder;

public class CPU {
    
    private Register registers;
    private StatusFlags flags;
    private Memory memory;
    private boolean running;
    private boolean halted;
    private long cycleCount;
    
    public CPU() {
        this.registers = new Register();
        this.flags = new StatusFlags();
        this.memory = new Memory();
        this.running = false;
        this.halted = false;
        this.cycleCount = 0;
    }
    
    public void reset() {
        registers.reset();
        flags.reset();
        memory.reset();
        running = false;
        halted = false;
        cycleCount = 0;
        
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
    }
    
    /**
     * Exécute une seule instruction
     */
    public int executeInstruction() {
        if (halted) {
            return 0;
        }
        
        int opcode = fetch();
        
        // Décoder et exécuter
        int cycles = InstructionDecoder.decodeAndExecute(this, opcode);
        
        cycleCount += cycles;
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
            "CPU State:\n%s\n%s\nCycles: %d Running: %b Halted: %b",
            registers.toString(),
            flags.toString(),
            cycleCount,
            running,
            halted
        );
    }
}