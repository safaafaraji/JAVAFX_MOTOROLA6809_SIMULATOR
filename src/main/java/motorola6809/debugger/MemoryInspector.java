package motorola6809.debugger;

import motorola6809.core.Memory;

public class MemoryInspector {
    
    private Memory memory;
    
    public MemoryInspector(Memory memory) {
        this.memory = memory;
    }
    
    public String dump(int startAddress, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("     00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F\n");
        sb.append("     -----------------------------------------------\n");
        
        for (int i = 0; i < length; i += 16) {
            int addr = startAddress + i;
            sb.append(String.format("%04X | ", addr));
            
            for (int j = 0; j < 16 && (i + j) < length; j++) {
                sb.append(String.format("%02X ", memory.readByte(addr + j)));
            }
            
            for (int j = i + 16 - i; j < 16; j++) {
                sb.append("   ");
            }
            
            sb.append("| ");
            for (int j = 0; j < 16 && (i + j) < length; j++) {
                int value = memory.readByte(addr + j);
                char c = (value >= 32 && value <= 126) ? (char) value : '.';
                sb.append(c);
            }
            
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    public int readByte(int address) {
        return memory.readByte(address);
    }
    
    public int readWord(int address) {
        return memory.readWord(address);
    }
    
    public void writeByte(int address, int value) {
        memory.writeByte(address, value);
    }
    
    public void writeWord(int address, int value) {
        memory.writeWord(address, value);
    }
    
    public int[] search(int startAddress, int endAddress, byte[] pattern) {
        java.util.ArrayList<Integer> results = new java.util.ArrayList<>();
        
        for (int addr = startAddress; addr <= endAddress - pattern.length; addr++) {
            boolean match = true;
            for (int i = 0; i < pattern.length; i++) {
                if (memory.readByte(addr + i) != (pattern[i] & 0xFF)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                results.add(addr);
            }
        }
        
        return results.stream().mapToInt(Integer::intValue).toArray();
    }
    
    public boolean compare(int addr1, int addr2, int length) {
        for (int i = 0; i < length; i++) {
            if (memory.readByte(addr1 + i) != memory.readByte(addr2 + i)) {
                return false;
            }
        }
        return true;
    }
}