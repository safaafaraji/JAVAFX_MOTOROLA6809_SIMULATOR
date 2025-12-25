package motorola6809.core;

import motorola6809.config.Constants;
import motorola6809.config.MemoryMap;
import motorola6809.utils.BitOperations;
import java.util.Arrays;

public class Memory {
    
    private byte[] memory; // Une seule mémoire unifiée de 64KB
    private MemoryMap memoryMap;     // sert à dire quelle zone est ROM ou RAM
    
    public Memory() {
        this.memory = new byte[Constants.TOTAL_MEMORY]; // 64KB
        this.memoryMap = new MemoryMap();
        reset();
    }
    
    /**
     * Réinitialise la mémoire
     */
    public void reset() {
        // Remplir de 0x00 (valeur initiale nulle)
        Arrays.fill(memory, (byte) 0x00);
        
        // Initialiser les vecteurs d'interruption
        writeWord(Constants.VECTOR_RESET, 0x1400); // Reset vector vers ROM
        writeWord(Constants.VECTOR_IRQ, 0xFFFF);
        writeWord(Constants.VECTOR_FIRQ, 0xFFFF);
        writeWord(Constants.VECTOR_NMI, 0xFFFF);
        writeWord(Constants.VECTOR_SWI, 0xFFFF);
        writeWord(Constants.VECTOR_SWI2, 0xFFFF);
        writeWord(Constants.VECTOR_SWI3, 0xFFFF);
    }
    
    /**
     * Lit un octet de la mémoire
     */
    public int readByte(int address) {
        address &= Constants.MASK_16BIT;
        return memory[address] & 0xFF;
    }
    
    /**
     * Écrit un octet en mémoire
     */
    public void writeByte(int address, int value) {
        address &= Constants.MASK_16BIT;
        value &= 0xFF;
        
        // Dans le simulateur, on permet l'écriture partout
        // même en ROM (pour faciliter le débogage)
        memory[address] = (byte) value;
        
        // Debug: afficher les écritures importantes
        if (address >= 0x2000 && address <= 0x200F) {
            System.out.println("MÉMOIRE: écrit $" + String.format("%02X", value) + 
                             " à $" + String.format("%04X", address));
        }
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
        System.out.println("Chargement programme: " + program.length + 
                         " octets à $" + String.format("%04X", startAddress));
        
        for (int i = 0; i < program.length; i++) {
            writeByte(startAddress + i, program[i] & 0xFF);
        }
        
        // Afficher les premiers octets pour debug
        System.out.print("Premiers octets: ");
        for (int i = 0; i < Math.min(16, program.length); i++) {
            System.out.print(String.format("%02X ", program[i] & 0xFF));
        }
        System.out.println();
    }
    
    /**
     * Obtient une copie de la mémoire
     */
    public byte[] getMemoryCopy() {
        return Arrays.copyOf(memory, memory.length);
    }
    
    /**
     * Dump de la mémoire (pour débogage)
     */
    public String dumpMemory(int startAddress, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dump mémoire $" + String.format("%04X", startAddress) + 
                 " à $" + String.format("%04X", startAddress + length - 1) + ":\n");
        
        for (int i = 0; i < length; i += 16) {
            sb.append(String.format("%04X: ", startAddress + i));
            
            // Hex
            for (int j = 0; j < 16 && (i + j) < length; j++) {
                sb.append(String.format("%02X ", readByte(startAddress + i + j)));
            }
            
            sb.append(" ");
            
            // ASCII
            for (int j = 0; j < 16 && (i + j) < length; j++) {
                int val = readByte(startAddress + i + j);
                if (val >= 32 && val < 127) {
                    sb.append((char) val);
                } else {
                    sb.append(".");
                }
            }
            
            sb.append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Vérifie si une adresse contient une valeur spécifique
     */
    public boolean checkMemory(int address, int expectedValue) {
        return (readByte(address) & 0xFF) == (expectedValue & 0xFF);
    }
}