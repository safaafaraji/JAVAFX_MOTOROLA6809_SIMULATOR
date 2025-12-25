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
        // 8-bit Arithmetic
        register(new GenericInstruction("ABA", 0x1B, 2, "Add B to A"));
        register(new GenericInstruction("CBA", 0x11, 2, "Compare B with A"));
        
        // 16-bit Arithmetic
        register(new GenericInstruction("ABX", 0x3A, 3, "Add B to X"));
        register(new GenericInstruction("ADDD", 0xC3, 4, "Add to D"));
        register(new GenericInstruction("SUBD", 0x83, 4, "Subtract from D"));
        register(new GenericInstruction("CMPD", 0x1083, 5, "Compare with D"));
        
        // Logical Operations
        register(new GenericInstruction("ANDA", 0x84, 2, "AND with A"));
        register(new GenericInstruction("ANDB", 0xC4, 2, "AND with B"));
        register(new GenericInstruction("BITA", 0x85, 2, "Bit test A"));
        register(new GenericInstruction("BITB", 0xC5, 2, "Bit test B"));
        register(new GenericInstruction("EORA", 0x88, 2, "Exclusive OR A"));
        register(new GenericInstruction("EORB", 0xC8, 2, "Exclusive OR B"));
        register(new GenericInstruction("ORAA", 0x8A, 2, "OR with A"));
        register(new GenericInstruction("ORAB", 0xCA, 2, "OR with B"));
        
        // Shifts and Rotates
        register(new GenericInstruction("ASLA", 0x48, 2, "Arithmetic Shift Left A"));
        register(new GenericInstruction("ASLB", 0x58, 2, "Arithmetic Shift Left B"));
        register(new GenericInstruction("ASRA", 0x47, 2, "Arithmetic Shift Right A"));
        register(new GenericInstruction("ASRB", 0x57, 2, "Arithmetic Shift Right B"));
        register(new GenericInstruction("LSLA", 0x48, 2, "Logical Shift Left A"));
        register(new GenericInstruction("LSLB", 0x58, 2, "Logical Shift Left B"));
        register(new GenericInstruction("LSRA", 0x44, 2, "Logical Shift Right A"));
        register(new GenericInstruction("LSRB", 0x54, 2, "Logical Shift Right B"));
        register(new GenericInstruction("ROLA", 0x49, 2, "Rotate Left A"));
        register(new GenericInstruction("ROLB", 0x59, 2, "Rotate Left B"));
        register(new GenericInstruction("RORA", 0x46, 2, "Rotate Right A"));
        register(new GenericInstruction("RORB", 0x56, 2, "Rotate Right B"));
        
        // Stack Operations
        register(new GenericInstruction("PSHS", 0x34, 5, "Push registers (S)"));
        register(new GenericInstruction("PULS", 0x35, 5, "Pull registers (S)"));
        register(new GenericInstruction("PSHU", 0x36, 5, "Push registers (U)"));
        register(new GenericInstruction("PULU", 0x37, 5, "Pull registers (U)"));
        
        // Subroutine
        register(new GenericInstruction("BSR", 0x8D, 7, "Branch to Subroutine"));
        register(new GenericInstruction("RTS", 0x39, 5, "Return from Subroutine"));
        
        // Interrupts
        register(new GenericInstruction("RTI", 0x3B, 10, "Return from Interrupt"));
        register(new GenericInstruction("SWI", 0x3F, 19, "Software Interrupt"));
        register(new GenericInstruction("SWI2", 0x103F, 20, "Software Interrupt 2"));
        register(new GenericInstruction("SWI3", 0x113F, 20, "Software Interrupt 3"));
        register(new GenericInstruction("CWAI", 0x3C, 20, "Wait for Interrupt"));
        
        // Conditional Branches (16-bit)
        register(new GenericInstruction("LBRA", 0x16, 5, "Long Branch Always"));
        register(new GenericInstruction("LBSR", 0x17, 9, "Long Branch to Subroutine"));
        
        // Clear
        register(new GenericInstruction("CLRA", 0x4F, 2, "Clear A"));
        register(new GenericInstruction("CLRB", 0x5F, 2, "Clear B"));
        
        // Complement
        register(new GenericInstruction("COMA", 0x43, 2, "Complement A"));
        register(new GenericInstruction("COMB", 0x53, 2, "Complement B"));
        
        // Negate
        register(new GenericInstruction("NEGA", 0x40, 2, "Negate A"));
        register(new GenericInstruction("NEGB", 0x50, 2, "Negate B"));
        
        // Decrement/Increment
        register(new GenericInstruction("DECA", 0x4A, 2, "Decrement A"));
        register(new GenericInstruction("DECB", 0x5A, 2, "Decrement B"));
        register(new GenericInstruction("INCA", 0x4C, 2, "Increment A"));
        register(new GenericInstruction("INCB", 0x5C, 2, "Increment B"));
        
        // Test
        register(new GenericInstruction("TSTA", 0x4D, 2, "Test A"));
        register(new GenericInstruction("TSTB", 0x5D, 2, "Test B"));
        
        // Sign Extend
        register(new GenericInstruction("SEX", 0x1D, 2, "Sign Extend B to A"));
        
        // Decimal Adjust
        register(new GenericInstruction("DAA", 0x19, 2, "Decimal Adjust A"));
        
        // Transfer/Exchange
        register(new GenericInstruction("TFR", 0x1F, 6, "Transfer Register"));
        register(new GenericInstruction("EXG", 0x1E, 8, "Exchange Registers"));
        // Branchements conditionnels
        register(new BNE());
        register(new BEQ());
        register(new LBNE());
        register(new LBEQ());
        
        // Instruction personnalisée MAX
        register(new MAX());
        
        // Autres branchements
        register(new BRA());    // Branch Always (0x20)
        register(new BCC());    // Branch if Carry Clear (0x24)
        register(new BCS());    // Branch if Carry Set (0x25)
        register(new BMI());    // Branch if Minus (0x2B)
        register(new BPL());    // Branch if Plus (0x2A)
        register(new BVC());    // Branch if Overflow Clear (0x28)
        register(new BVS());    // Branch if Overflow Set (0x29)
        register(new BGE());    // Branch if Greater or Equal (0x2C)
        register(new BGT());    // Branch if Greater Than (0x2E)
        register(new BLE());    // Branch if Less or Equal (0x2F)
        register(new BLT());    // Branch if Less Than (0x2D)
        register(new BHI());    // Branch if Higher (0x22)
        register(new BLS());    // Branch if Lower or Same (0x23)
        register(new BSR());    // Branch to Subroutine (0x8D)
        register(new LBRA());   // Long Branch Always (0x16)
        register(new LBSR());   // Long Branch to Subroutine (0x17)
        
        register(new ABA());
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