package motorola6809.instruction;

import motorola6809.core.CPU;
import motorola6809.core.Register;
import motorola6809.core.StatusFlags;

public class InstructionDecoder {
    
    public static int decodeAndExecute(CPU cpu, int opcode) {
        int cycles = 0;
        
        System.out.println("Décodage opcode: 0x" + String.format("%02X", opcode) + 
                          " à PC=0x" + String.format("%04X", cpu.getRegisters().getPC() - 1));
        
        switch (opcode) {
            // ==================== NOP ====================
            case 0x12: // NOP
                cycles = executeNOP(cpu);
                break;
                
            // ==================== LDA ====================
            case 0x86: // LDA #imm
                cycles = executeLDA_immediate(cpu);
                break;
            case 0x96: // LDA dir
                cycles = executeLDA_direct(cpu);
                break;
            case 0xB6: // LDA ext
                cycles = executeLDA_extended(cpu);
                break;
                
            // ==================== LDB ====================
            case 0xC6: // LDB #imm
                cycles = executeLDB_immediate(cpu);
                break;
            case 0xD6: // LDB dir
                cycles = executeLDB_direct(cpu);
                break;
            case 0xF6: // LDB ext
                cycles = executeLDB_extended(cpu);
                break;
                
            // ==================== STA ====================
            case 0x97: // STA dir
                cycles = executeSTA_direct(cpu);
                break;
            case 0xB7: // STA ext
                cycles = executeSTA_extended(cpu);
                break;
                
            // ==================== STB ====================
            case 0xD7: // STB dir
                cycles = executeSTB_direct(cpu);
                break;
            case 0xF7: // STB ext
                cycles = executeSTB_extended(cpu);
                break;
                
            // ==================== ADDA ====================
            case 0x8B: // ADDA #imm
                cycles = executeADDA_immediate(cpu);
                break;
            case 0x9B: // ADDA dir
                cycles = executeADDA_direct(cpu);
                break;
                
            // ==================== ADDB ====================
            case 0xCB: // ADDB #imm
                cycles = executeADDB_immediate(cpu);
                break;
            case 0xDB: // ADDB dir
                cycles = executeADDB_direct(cpu);
                break;
                
            // ==================== INCA/DECA ====================
            case 0x4C: // INCA
                cycles = executeINCA(cpu);
                break;
            case 0x4A: // DECA
                cycles = executeDECA(cpu);
                break;
                
            // ==================== INCB/DECB ====================
            case 0x5C: // INCB
                cycles = executeINCB(cpu);
                break;
            case 0x5A: // DECB
                cycles = executeDECB(cpu);
                break;
                
            // ==================== MUL ====================
            case 0x3D: // MUL
                cycles = executeMUL(cpu);
                break;
                
            // ==================== SWI ====================
            case 0x3F: // SWI
                cycles = executeSWI(cpu);
                break;
                
            // ==================== CMPA ====================
            case 0x81: // CMPA #imm
                cycles = executeCMPA_immediate(cpu);
                break;
                
            // ==================== CMPB ====================
            case 0xC1: // CMPB #imm
                cycles = executeCMPB_immediate(cpu);
                break;
                
            // ==================== AND/OR/EOR ====================
            case 0x84: // ANDA #imm
                cycles = executeANDA_immediate(cpu);
                break;
            case 0x8A: // ORA #imm
                cycles = executeORA_immediate(cpu);
                break;
            case 0x88: // EORA #imm
                cycles = executeEORA_immediate(cpu);
                break;
                
            // ==================== COMA/COMB ====================
            case 0x43: // COMA
                cycles = executeCOMA(cpu);
                break;
            case 0x53: // COMB
                cycles = executeCOMB(cpu);
                break;
                
            // ==================== JMP ====================
            case 0x7E: // JMP ext
                cycles = executeJMP_extended(cpu);
                break;
                
            // ==================== RTS ====================
            case 0x39: // RTS
                cycles = executeRTS(cpu);
                break;
                
            // ==================== TSTA/TSTB ====================
            case 0x4D: // TSTA
                cycles = executeTSTA(cpu);
                break;
            case 0x5D: // TSTB
                cycles = executeTSTB(cpu);
                break;
                
            case 0x1B: // ABA - Add B to A
                cycles = executeABA(cpu);
                break;
            case 0xC8: // EORB #imm
                cycles = executeEORB_immediate(cpu);
                break;

                
            default:
                System.err.println("Opcode inconnu: 0x" + String.format("%02X", opcode) + 
                                 " à PC=0x" + String.format("%04X", cpu.getRegisters().getPC() - 1));
                // Pour le debug, on arrête sur opcode inconnu
                cpu.halt();
                cycles = 0;
        }
        
        return cycles;
    }
    
    // ==================== IMPLÉMENTATIONS ====================
    
    private static int executeNOP(CPU cpu) {
        return 2;
    }
    
    private static int executeLDA_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setA(value);
        cpu.getFlags().updateNZ8(value);
        return 2;
    }
    
    private static int executeLDA_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        cpu.getRegisters().setA(value);
        cpu.getFlags().updateNZ8(value);
        return 4;
    }
    
    private static int executeLDA_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        cpu.getRegisters().setA(value);
        cpu.getFlags().updateNZ8(value);
        return 5;
    }
    
    private static int executeLDB_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setB(value);
        cpu.getFlags().updateNZ8(value);
        return 2;
    }
    
    private static int executeLDB_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        cpu.getRegisters().setB(value);
        cpu.getFlags().updateNZ8(value);
        return 4;
    }
    
    private static int executeLDB_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        cpu.getRegisters().setB(value);
        cpu.getFlags().updateNZ8(value);
        return 5;
    }
    
    private static int executeSTA_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getA();
        cpu.getMemory().writeByte(address, value);
        return 4;
    }
    
    private static int executeSTA_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getA();
        cpu.getMemory().writeByte(address, value);
        return 5;
    }
    
    private static int executeSTB_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getB();
        cpu.getMemory().writeByte(address, value);
        return 4;
    }
    
    private static int executeSTB_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getB();
        cpu.getMemory().writeByte(address, value);
        return 5;
    }
    
    private static int executeADDA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a + value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, value, result);
        return 2;
    }
    
    private static int executeADDA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a + value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, value, result);
        return 4;
    }
    
    private static int executeADDB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b + value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(b, value, result);
        return 2;
    }
    
    private static int executeADDB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b + value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(b, value, result);
        return 4;
    }
    
    private static int executeINCA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int result = (a + 1) & 0xFF;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeDECA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int result = (a - 1) & 0xFF;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeINCB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (b + 1) & 0xFF;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeDECB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (b - 1) & 0xFF;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeMUL(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a * b;
        cpu.getRegisters().setD(result);
        
        // Flags pour MUL
        cpu.getFlags().setCarry((result & 0xFF00) != 0);
        cpu.getFlags().setZero(result == 0);
        cpu.getFlags().setNegative(false);
        
        return 11;
    }
    
    private static int executeSWI(CPU cpu) {
        // Sauvegarde complète des registres
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.pushWordS(cpu.getRegisters().getU());
        cpu.pushWordS(cpu.getRegisters().getY());
        cpu.pushWordS(cpu.getRegisters().getX());
        cpu.pushS(cpu.getRegisters().getDP());
        cpu.pushS(cpu.getRegisters().getB());
        cpu.pushS(cpu.getRegisters().getA());
        cpu.pushS(cpu.getFlags().getCC());
        
        // Définit les flags
        cpu.getFlags().setEntire(true);
        cpu.getFlags().setFIRQMask(true);
        cpu.getFlags().setIRQMask(true);
        
        // Va au vecteur d'interruption
        int vector = cpu.getMemory().readWord(0xFFFA);
        cpu.getRegisters().setPC(vector);
        
        return 19;
    }
    
    private static int executeCMPA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a - value;
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 2;
    }
    
    private static int executeCMPB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b - value;
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 2;
    }
    
    private static int executeANDA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a & value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeORA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a | value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeEORA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a ^ value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeCOMA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int result = (~a) & 0xFF;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true); // COM met toujours C=1
        return 2;
    }
    
    private static int executeCOMB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (~b) & 0xFF;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true); // COM met toujours C=1
        return 2;
    }
    
    private static int executeJMP_extended(CPU cpu) {
        int address = cpu.fetchWord();
        cpu.getRegisters().setPC(address);
        return 3;
    }
    
    private static int executeRTS(CPU cpu) {
        int address = cpu.popWordS();
        cpu.getRegisters().setPC(address);
        return 5;
    }
    
    private static int executeTSTA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        cpu.getFlags().updateNZ8(a);
        cpu.getFlags().setCarry(false);
        cpu.getFlags().setOverflow(false);
        return 2;
    }
    
    private static int executeTSTB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        cpu.getFlags().updateNZ8(b);
        cpu.getFlags().setCarry(false);
        cpu.getFlags().setOverflow(false);
        return 2;
    }
    private static int executeABA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a + b;
        
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, b, result);
        
        return 2;
    }
    private static int executeEORB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b ^ value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(false);
        cpu.getFlags().setOverflow(false);
        return 2;
    }


}