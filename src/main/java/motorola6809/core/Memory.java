package motorola6809.core;

import motorola6809.config.Constants;
import motorola6809.config.MemoryMap;
import motorola6809.utils.BitOperations;
import java.util.Arrays;

public class Memory {
    
    private byte[] ram;
    private byte[] rom;
    private MemoryMap memoryMap;
    
    public Memory() {
        this.ram = new byte[Constants.RAM_SIZE];
        this.rom = new byte[Constants.ROM_SIZE];
        this.memoryMap = new MemoryMap();
        reset();
    }
    
    /**
     * Réinitialise la mémoire
     */
    public void reset() {
        Arrays.fill(ram, (byte) 0x00);
        Arrays.fill(rom, (byte) 0xFF);
    }
    
    /**
     * Lit un octet de la mémoire
     */
    public int readByte(int address) {
        address &= Constants.MASK_16BIT;
        
        if (memoryMap.isRAM(address)) {
            int offset = address - memoryMap.getRamStart();
            return ram[offset] & 0xFF;
        } else if (memoryMap.isROM(address)) {
            int offset = address - memoryMap.getRomStart();
            return rom[offset] & 0xFF;
        }
        
        // Adresse non mappée
        return 0xFF;
    }
    
    /**
     * Écrit un octet en mémoire
     */
    public void writeByte(int address, int value) {
        address &= Constants.MASK_16BIT;
        value &= 0xFF;
        
        if (memoryMap.isRAM(address)) {
            int offset = address - memoryMap.getRamStart();
            ram[offset] = (byte) value;
        } else if (memoryMap.isROM(address)) {
            int offset = address - memoryMap.getRomStart();
            rom[offset] = (byte) value;
        }
        // Ignore les écritures vers des zones non mappées
    }
    
    /**
     * Lit un mot (16 bits) de la mémoire (Big Endian)
     */
    public int readWord(int address) {
        int high = readByte(address);
        int low = readByte(address + 1);
        return BitOperations.makeWord(high, low);
    }
    
    /**
     * Écrit un mot (16 bits) en mémoire (Big Endian)
     */
    public void writeWord(int address, int value) {
        value &= Constants.MASK_16BIT;
        writeByte(address, BitOperations.getHighByte(value));
        writeByte(address + 1, BitOperations.getLowByte(value));
    }
    
    /**
     * Charge un programme en mémoire
     */
    public void loadProgram(byte[] program, int startAddress) {
        for (int i = 0; i < program.length; i++) {
            writeByte(startAddress + i, program[i] & 0xFF);
        }
    }
    
    /**
     * Obtient une copie de la RAM
     */
    public byte[] getRAMCopy() {
        return Arrays.copyOf(ram, ram.length);
    }
    
    /**
     * Obtient une copie de la ROM
     */
    public byte[] getROMCopy() {
        return Arrays.copyOf(rom, rom.length);
    }
    
    /**
     * Dump de la mémoire (pour débogage)
     */
    public String dumpMemory(int startAddress, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i += 16) {
            sb.append(String.format("%04X: ", startAddress + i));
            for (int j = 0; j < 16 && (i + j) < length; j++) {
                sb.append(String.format("%02X ", readByte(startAddress + i + j)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

