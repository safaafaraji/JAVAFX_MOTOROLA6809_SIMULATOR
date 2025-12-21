package motorola6809.instruction;

import motorola6809.instruction.impl.*;
import java.util.HashMap;
import java.util.Map;

public class InstructionSet {
    
    private static Map<Integer, Instruction> instructions;
    
    static {
        instructions = new HashMap<>();
        registerAllInstructions();
    }
    
    private static void registerAllInstructions() {
        register(new NOP());
        
        // LDA
        register(new LDA(0x86, "LDA", 2));
        register(new LDA(0x96, "LDA", 4));
        register(new LDA(0xB6, "LDA", 5));
        register(new LDA(0xA6, "LDA", 4));
        
        // LDB
        register(new LDB(0xC6, "LDB", 2));
        register(new LDB(0xD6, "LDB", 4));
        register(new LDB(0xF6, "LDB", 5));
        register(new LDB(0xE6, "LDB", 4));
        
        // LDD
        register(new LDD(0xCC, "LDD", 3));
        register(new LDD(0xDC, "LDD", 5));
        register(new LDD(0xFC, "LDD", 6));
        register(new LDD(0xEC, "LDD", 5));
        
        // LDX
        register(new LDX(0x8E, "LDX", 3));
        register(new LDX(0x9E, "LDX", 5));
        register(new LDX(0xBE, "LDX", 6));
        register(new LDX(0xAE, "LDX", 5));
        
        // STA
        register(new STA(0x97, "STA", 4));
        register(new STA(0xB7, "STA", 5));
        register(new STA(0xA7, "STA", 4));
        
        // STB
        register(new STB(0xD7, "STB", 4));
        register(new STB(0xF7, "STB", 5));
        register(new STB(0xE7, "STB", 4));
        
        // STD
        register(new STD(0xDD, "STD", 5));
        register(new STD(0xFD, "STD", 6));
        register(new STD(0xED, "STD", 5));
        
        // STX
        register(new STX(0x9F, "STX", 5));
        register(new STX(0xBF, "STX", 6));
        register(new STX(0xAF, "STX", 5));
        
        register(new INCA());
        register(new INCB());
        register(new INC(0x0C, "INC", 6));
        register(new INC(0x7C, "INC", 7));
        register(new INC(0x6C, "INC", 6));
        
        register(new DECA());
        register(new DECB());
        register(new DEC(0x0A, "DEC", 6));
        register(new DEC(0x7A, "DEC", 7));
        register(new DEC(0x6A, "DEC", 6));
        
        register(new COMA());
        register(new COMB());
        register(new COM(0x03, "COM", 6));
        register(new COM(0x73, "COM", 7));
        register(new COM(0x63, "COM", 6));
        
        register(new EORA(0x88, "EORA", 2));
        register(new EORA(0x98, "EORA", 4));
        register(new EORA(0xB8, "EORA", 5));
        register(new EORA(0xA8, "EORA", 4));
        
        register(new EORB(0xC8, "EORB", 2));
        register(new EORB(0xD8, "EORB", 4));
        register(new EORB(0xF8, "EORB", 5));
        register(new EORB(0xE8, "EORB", 4));
        
        register(new EXG());
        register(new MUL());
        register(new DAA());
        register(new CWAI());
        
        register(new JMP(0x0E, "JMP", 3));
        register(new JMP(0x7E, "JMP", 4));
        register(new JMP(0x6E, "JMP", 3));
        
        register(new JSR(0x9D, "JSR", 7));
        register(new JSR(0xBD, "JSR", 8));
        register(new JSR(0xAD, "JSR", 7));
    }
    
    private static void register(Instruction instruction) {
        instructions.put(instruction.getOpcode(), instruction);
    }
    
    public static Instruction getInstruction(int opcode) {
        return instructions.get(opcode);
    }
    
    public static boolean hasInstruction(int opcode) {
        return instructions.containsKey(opcode);
    }
}