package motorola6809.instruction;

import motorola6809.core.CPU;
import motorola6809.core.Register;
import motorola6809.core.StatusFlags;

public class InstructionDecoder {
    
    public static int decodeAndExecute(CPU cpu, int opcode) {
        int cycles = 0;
        
        // Log minimal pour performance
        int currentPC = cpu.getRegisters().getPC() - 1;
        
        switch (opcode) {
            // ==================== LOAD INSTRUCTIONS ====================
            case 0x86: // LDA #imm
                cycles = executeLDA_immediate(cpu);
                break;
            case 0x96: // LDA dir
                cycles = executeLDA_direct(cpu);
                break;
            case 0xB6: // LDA ext
                cycles = executeLDA_extended(cpu);
                break;
            case 0xA6: // LDA idx
                cycles = executeLDA_indexed(cpu);
                break;
                
            case 0xC6: // LDB #imm
                cycles = executeLDB_immediate(cpu);
                break;
            case 0xD6: // LDB dir
                cycles = executeLDB_direct(cpu);
                break;
            case 0xF6: // LDB ext
                cycles = executeLDB_extended(cpu);
                break;
            case 0xE6: // LDB idx
                cycles = executeLDB_indexed(cpu);
                break;
                
            case 0xCC: // LDD #imm
                cycles = executeLDD_immediate(cpu);
                break;
            case 0xDC: // LDD dir
                cycles = executeLDD_direct(cpu);
                break;
            case 0xFC: // LDD ext
                cycles = executeLDD_extended(cpu);
                break;
            case 0xEC: // LDD idx
                cycles = executeLDD_indexed(cpu);
                break;
                
            case 0x8E: // LDX #imm
                cycles = executeLDX_immediate(cpu);
                break;
            case 0x9E: // LDX dir
                cycles = executeLDX_direct(cpu);
                break;
            case 0xBE: // LDX ext
                cycles = executeLDX_extended(cpu);
                break;
            case 0xAE: // LDX idx
                cycles = executeLDX_indexed(cpu);
                break;
                
            case 0xCE: // LDU #imm
                cycles = executeLDU_immediate(cpu);
                break;
            case 0xDE: // LDU dir
                cycles = executeLDU_direct(cpu);
                break;
            case 0xFE: // LDU ext
                cycles = executeLDU_extended(cpu);
                break;
            case 0xEE: // LDU idx
                cycles = executeLDU_indexed(cpu);
                break;
                
            // ==================== STORE INSTRUCTIONS ====================
            case 0x97: // STA dir
                cycles = executeSTA_direct(cpu);
                break;
            case 0xB7: // STA ext
                cycles = executeSTA_extended(cpu);
                break;
            case 0xA7: // STA idx
                cycles = executeSTA_indexed(cpu);
                break;
                
            case 0xD7: // STB dir
                cycles = executeSTB_direct(cpu);
                break;
            case 0xF7: // STB ext
                cycles = executeSTB_extended(cpu);
                break;
            case 0xE7: // STB idx
                cycles = executeSTB_indexed(cpu);
                break;
                
            case 0xDD: // STD dir
                cycles = executeSTD_direct(cpu);
                break;
            case 0xFD: // STD ext
                cycles = executeSTD_extended(cpu);
                break;
            case 0xED: // STD idx
                cycles = executeSTD_indexed(cpu);
                break;
                
            case 0x9F: // STX dir
                cycles = executeSTX_direct(cpu);
                break;
            case 0xFF: // STU ext - C'EST LE PROBLÈME !
                cycles = executeSTU_extended(cpu);
                break;
            case 0xAF: // STX idx
                cycles = executeSTX_indexed(cpu);
                break;
                
            case 0xDF: // STU dir
                cycles = executeSTU_direct(cpu);
                break;

            case 0xEF: // STU idx
                cycles = executeSTU_indexed(cpu);
                break;
                
            // ==================== ARITHMETIC INSTRUCTIONS ====================
            case 0x8B: // ADDA #imm
                cycles = executeADDA_immediate(cpu);
                break;
            case 0x9B: // ADDA dir
                cycles = executeADDA_direct(cpu);
                break;
            case 0xBB: // ADDA ext
                cycles = executeADDA_extended(cpu);
                break;
            case 0xAB: // ADDA idx
                cycles = executeADDA_indexed(cpu);
                break;
                
            case 0xCB: // ADDB #imm
                cycles = executeADDB_immediate(cpu);
                break;
            case 0xDB: // ADDB dir
                cycles = executeADDB_direct(cpu);
                break;
            case 0xFB: // ADDB ext
                cycles = executeADDB_extended(cpu);
                break;
            case 0xEB: // ADDB idx
                cycles = executeADDB_indexed(cpu);
                break;
                
            case 0xC3: // ADDD #imm
                cycles = executeADDD_immediate(cpu);
                break;
            case 0xD3: // ADDD dir
                cycles = executeADDD_direct(cpu);
                break;
            case 0xF3: // ADDD ext
                cycles = executeADDD_extended(cpu);
                break;
            case 0xE3: // ADDD idx
                cycles = executeADDD_indexed(cpu);
                break;
                
            case 0x80: // SUBA #imm
                cycles = executeSUBA_immediate(cpu);
                break;
            case 0x90: // SUBA dir
                cycles = executeSUBA_direct(cpu);
                break;
            case 0xB0: // SUBA ext
                cycles = executeSUBA_extended(cpu);
                break;
            case 0xA0: // SUBA idx
                cycles = executeSUBA_indexed(cpu);
                break;
                
            case 0xC0: // SUBB #imm
                cycles = executeSUBB_immediate(cpu);
                break;
            case 0xD0: // SUBB dir
                cycles = executeSUBB_direct(cpu);
                break;
            case 0xF0: // SUBB ext
                cycles = executeSUBB_extended(cpu);
                break;
            case 0xE0: // SUBB idx
                cycles = executeSUBB_indexed(cpu);
                break;
                
            case 0x83: // SUBD #imm
                cycles = executeSUBD_immediate(cpu);
                break;
            case 0x93: // SUBD dir
                cycles = executeSUBD_direct(cpu);
                break;
            case 0xB3: // SUBD ext
                cycles = executeSUBD_extended(cpu);
                break;
            case 0xA3: // SUBD idx
                cycles = executeSUBD_indexed(cpu);
                break;
                
            case 0x1B: // ABA
                cycles = executeABA(cpu);
                break;
                
            // ==================== INCREMENT/DECREMENT ====================
            case 0x4C: // INCA
                cycles = executeINCA(cpu);
                break;
            case 0x5C: // INCB
                cycles = executeINCB(cpu);
                break;
            case 0x0C: // INC dir
                cycles = executeINC_direct(cpu);
                break;
            case 0x7C: // INC ext
                cycles = executeINC_extended(cpu);
                break;
            case 0x6C: // INC idx
                cycles = executeINC_indexed(cpu);
                break;
                
            case 0x4A: // DECA
                cycles = executeDECA(cpu);
                break;
            case 0x5A: // DECB
                cycles = executeDECB(cpu);
                break;
            case 0x0A: // DEC dir
                cycles = executeDEC_direct(cpu);
                break;
            case 0x7A: // DEC ext
                cycles = executeDEC_extended(cpu);
                break;
            case 0x6A: // DEC idx
                cycles = executeDEC_indexed(cpu);
                break;
                
            // ==================== MULTIPLY/DAA ====================
            case 0x3D: // MUL
                cycles = executeMUL(cpu);
                break;
            case 0x19: // DAA
                cycles = executeDAA(cpu);
                break;
                
            // ==================== LOGICAL INSTRUCTIONS ====================
            case 0x84: // ANDA #imm
                cycles = executeANDA_immediate(cpu);
                break;
            case 0x94: // ANDA dir
                cycles = executeANDA_direct(cpu);
                break;
            case 0xB4: // ANDA ext
                cycles = executeANDA_extended(cpu);
                break;
            case 0xA4: // ANDA idx
                cycles = executeANDA_indexed(cpu);
                break;
                
            case 0xC4: // ANDB #imm
                cycles = executeANDB_immediate(cpu);
                break;
            case 0xD4: // ANDB dir
                cycles = executeANDB_direct(cpu);
                break;
            case 0xF4: // ANDB ext
                cycles = executeANDB_extended(cpu);
                break;
            case 0xE4: // ANDB idx
                cycles = executeANDB_indexed(cpu);
                break;
                
            case 0x1C: // ANDCC #imm
                cycles = executeANDCC_immediate(cpu);
                break;
                
            case 0x8A: // ORA #imm
                cycles = executeORA_immediate(cpu);
                break;
            case 0x9A: // ORA dir
                cycles = executeORA_direct(cpu);
                break;
            case 0xBA: // ORA ext
                cycles = executeORA_extended(cpu);
                break;
            case 0xAA: // ORA idx
                cycles = executeORA_indexed(cpu);
                break;
                
            case 0xCA: // ORB #imm
                cycles = executeORB_immediate(cpu);
                break;
            case 0xDA: // ORB dir
                cycles = executeORB_direct(cpu);
                break;
            case 0xFA: // ORB ext
                cycles = executeORB_extended(cpu);
                break;
            case 0xEA: // ORB idx
                cycles = executeORB_indexed(cpu);
                break;
                
            case 0x1A: // ORCC #imm
                cycles = executeORCC_immediate(cpu);
                break;
                
            case 0x88: // EORA #imm
                cycles = executeEORA_immediate(cpu);
                break;
            case 0x98: // EORA dir
                cycles = executeEORA_direct(cpu);
                break;
            case 0xB8: // EORA ext
                cycles = executeEORA_extended(cpu);
                break;
            case 0xA8: // EORA idx
                cycles = executeEORA_indexed(cpu);
                break;
                
            case 0xC8: // EORB #imm
                cycles = executeEORB_immediate(cpu);
                break;
            case 0xD8: // EORB dir
                cycles = executeEORB_direct(cpu);
                break;
            case 0xF8: // EORB ext
                cycles = executeEORB_extended(cpu);
                break;
            case 0xE8: // EORB idx
                cycles = executeEORB_indexed(cpu);
                break;
                
            case 0x43: // COMA
                cycles = executeCOMA(cpu);
                break;
            case 0x53: // COMB
                cycles = executeCOMB(cpu);
                break;
            case 0x03: // COM dir
                cycles = executeCOM_direct(cpu);
                break;
            case 0x73: // COM ext
                cycles = executeCOM_extended(cpu);
                break;
            case 0x63: // COM idx
                cycles = executeCOM_indexed(cpu);
                break;
                
            case 0x40: // NEGA
                cycles = executeNEGA(cpu);
                break;
            case 0x50: // NEGB
                cycles = executeNEGB(cpu);
                break;
            case 0x00: // NEG dir
                cycles = executeNEG_direct(cpu);
                break;
            case 0x70: // NEG ext
                cycles = executeNEG_extended(cpu);
                break;
            case 0x60: // NEG idx
                cycles = executeNEG_indexed(cpu);
                break;
                
            // ==================== COMPARE/TEST ====================
            case 0x81: // CMPA #imm
                cycles = executeCMPA_immediate(cpu);
                break;
            case 0x91: // CMPA dir
                cycles = executeCMPA_direct(cpu);
                break;
            case 0xB1: // CMPA ext
                cycles = executeCMPA_extended(cpu);
                break;
            case 0xA1: // CMPA idx
                cycles = executeCMPA_indexed(cpu);
                break;
                
            case 0xC1: // CMPB #imm
                cycles = executeCMPB_immediate(cpu);
                break;
            case 0xD1: // CMPB dir
                cycles = executeCMPB_direct(cpu);
                break;
            case 0xF1: // CMPB ext
                cycles = executeCMPB_extended(cpu);
                break;
            case 0xE1: // CMPB idx
                cycles = executeCMPB_indexed(cpu);
                break;
                
            case 0x4D: // TSTA
                cycles = executeTSTA(cpu);
                break;
            case 0x5D: // TSTB
                cycles = executeTSTB(cpu);
                break;
            case 0x0D: // TST dir
                cycles = executeTST_direct(cpu);
                break;
            case 0x7D: // TST ext
                cycles = executeTST_extended(cpu);
                break;
            case 0x6D: // TST idx
                cycles = executeTST_indexed(cpu);
                break;
                
            // ==================== JUMP/BRANCH ====================
            case 0x0E: // JMP dir
                cycles = executeJMP_direct(cpu);
                break;
            case 0x7E: // JMP ext
                cycles = executeJMP_extended(cpu);
                break;
            case 0x6E: // JMP idx
                cycles = executeJMP_indexed(cpu);
                break;
                
            case 0x9D: // JSR dir
                cycles = executeJSR_direct(cpu);
                break;
            case 0xBD: // JSR ext
                cycles = executeJSR_extended(cpu);
                break;
            case 0xAD: // JSR idx
                cycles = executeJSR_indexed(cpu);
                break;
                
            case 0x8D: // BSR rel
                cycles = executeBSR_relative(cpu);
                break;
                
            case 0x39: // RTS
                cycles = executeRTS(cpu);
                break;
            case 0x3B: // RTI
                cycles = executeRTI(cpu);
                break;
                
            // Branchements conditionnels
            case 0x20: // BRA
                cycles = executeBRA_relative(cpu);
                break;
            case 0x21: // BRN
                cycles = executeBRN_relative(cpu);
                break;
            case 0x22: // BHI
                cycles = executeBHI_relative(cpu);
                break;
            case 0x23: // BLS
                cycles = executeBLS_relative(cpu);
                break;
            case 0x24: // BCC/BHS
                cycles = executeBCC_relative(cpu);
                break;
            case 0x25: // BCS/BLO
                cycles = executeBCS_relative(cpu);
                break;
            case 0x26: // BNE
                cycles = executeBNE_relative(cpu);
                break;
            case 0x27: // BEQ
                cycles = executeBEQ_relative(cpu);
                break;
            case 0x28: // BVC
                cycles = executeBVC_relative(cpu);
                break;
            case 0x29: // BVS
                cycles = executeBVS_relative(cpu);
                break;
            case 0x2A: // BPL
                cycles = executeBPL_relative(cpu);
                break;
            case 0x2B: // BMI
                cycles = executeBMI_relative(cpu);
                break;
            case 0x2C: // BGE
                cycles = executeBGE_relative(cpu);
                break;
            case 0x2D: // BLT
                cycles = executeBLT_relative(cpu);
                break;
            case 0x2E: // BGT
                cycles = executeBGT_relative(cpu);
                break;
            case 0x2F: // BLE
                cycles = executeBLE_relative(cpu);
                break;
                
            case 0x16: // LBRA
                cycles = executeLBRA_relative(cpu);
                break;
            case 0x17: // LBSR
                cycles = executeLBSR_relative(cpu);
                break;
                
            // ==================== TRANSFER/EXCHANGE ====================
            case 0x1F: // TFR
                cycles = executeTFR(cpu);
                break;
            case 0x1E: // EXG
                cycles = executeEXG(cpu);
                break;
                
            // ==================== MISC INSTRUCTIONS ====================
            case 0x12: // NOP
                cycles = executeNOP(cpu);
                break;
            case 0x13: // SYNC
                cycles = executeSYNC(cpu);
                break;
            case 0x3C: // CWAI #imm
                cycles = executeCWAI_immediate(cpu);
                break;
            case 0x3F: // SWI - CORRECTEMENT IMPLÉMENTÉ
                cycles = executeSWI(cpu);
                break;
                
            case 0x4F: // CLRA
                cycles = executeCLRA(cpu);
                break;
            case 0x5F: // CLRB
                cycles = executeCLRB(cpu);
                break;
            case 0x0F: // CLR dir
                cycles = executeCLR_direct(cpu);
                break;
            case 0x7F: // CLR ext
                cycles = executeCLR_extended(cpu);
                break;
            case 0x6F: // CLR idx
                cycles = executeCLR_indexed(cpu);
                break;
                
            case 0x3A: // ABX
                cycles = executeABX(cpu);
                break;
            case 0x1D: // SEX
                cycles = executeSEX(cpu);
                break;
                
            // ==================== STACK INSTRUCTIONS ====================
            case 0x34: // PSHS
                cycles = executePSHS(cpu);
                break;
            case 0x35: // PULS
                cycles = executePULS(cpu);
                break;
            case 0x36: // PSHU
                cycles = executePSHU(cpu);
                break;
            case 0x37: // PULU
                cycles = executePULU(cpu);
                break;
                
            // ==================== LEA ====================
            case 0x30: // LEAX
                cycles = executeLEAX(cpu);
                break;
            case 0x31: // LEAY
                cycles = executeLEAY(cpu);
                break;
            case 0x32: // LEAS
                cycles = executeLEAS(cpu);
                break;
            case 0x33: // LEAU
                cycles = executeLEAU(cpu);
                break;
                
            default:
                // Pour 0xFF, c'est STU extended - on devrait l'arrêter
                if ((opcode & 0xFF00) == 0x1000) {
                    cycles = handlePrefixedOpcode(cpu, opcode);
                } else {
                    System.err.println("Opcode inconnu: 0x" + String.format("%02X", opcode) + 
                                     " à PC=0x" + String.format("%04X", currentPC));
                    cpu.halt();
                    cycles = 0;
                }

        }
        
        return cycles;
    }
    
    // ==================== MÉTHODES D'EXÉCUTION ====================
    
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
    
    private static int executeLDA_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
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
    
    private static int executeLDB_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        cpu.getRegisters().setB(value);
        cpu.getFlags().updateNZ8(value);
        return 5;
    }
    
    private static int executeLDD_immediate(CPU cpu) {
        int value = cpu.fetchWord();
        cpu.getRegisters().setD(value);
        cpu.getFlags().updateNZ16(value);
        return 3;
    }
    
    private static int executeLDD_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setD(value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeLDD_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setD(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeLDD_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setD(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeLDX_immediate(CPU cpu) {
        int value = cpu.fetchWord();
        cpu.getRegisters().setX(value);
        cpu.getFlags().updateNZ16(value);
        return 3;
    }
    
    private static int executeLDX_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setX(value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeLDX_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setX(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeLDX_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setX(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeLDU_immediate(CPU cpu) {
        int value = cpu.fetchWord();
        cpu.getRegisters().setU(value);
        cpu.getFlags().updateNZ16(value);
        return 3;
    }
    
    private static int executeLDU_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setU(value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeLDU_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setU(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeLDU_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readWord(address);
        cpu.getRegisters().setU(value);
        cpu.getFlags().updateNZ16(value);
        return 6;
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
    
    private static int executeSTA_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getRegisters().getA();
        cpu.getMemory().writeByte(address, value);
        return 5;
    }
    
    private static int executeSTB_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getB();
        cpu.getMemory().writeByte(address, value);
        cpu.getFlags().updateNZ8(value);
        return 4;
    }
    
    private static int executeSTB_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getB();
        cpu.getMemory().writeByte(address, value);
        cpu.getFlags().updateNZ8(value);
        return 5;
    }
    
    private static int executeSTB_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getRegisters().getB();
        cpu.getMemory().writeByte(address, value);
        cpu.getFlags().updateNZ8(value);
        return 5;
    }
    
    private static int executeSTD_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getD();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeSTD_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getD();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeSTD_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getRegisters().getD();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeSTX_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getX();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeSTX_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getX();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeSTX_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getRegisters().getX();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeSTU_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getU();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 5;
    }
    
    private static int executeSTU_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getRegisters().getU();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
    }
    
    private static int executeSTU_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getRegisters().getU();
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        return 6;
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
    
    private static int executeADDA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a + value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, value, result);
        return 5;
    }
    
    private static int executeADDA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a + value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, value, result);
        return 5;
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
    
    private static int executeADDB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b + value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(b, value, result);
        return 5;
    }
    
    private static int executeADDB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b + value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(b, value, result);
        return 5;
    }
    
    private static int executeADDD_immediate(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int value = cpu.fetchWord();
        int result = d + value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsAdd16(d, value, result);
        return 4;
    }
    
    private static int executeADDD_direct(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readWord(address);
        int result = d + value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsAdd16(d, value, result);
        return 6;
    }
    
    private static int executeADDD_extended(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readWord(address);
        int result = d + value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsAdd16(d, value, result);
        return 7;
    }
    
    private static int executeADDD_indexed(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readWord(address);
        int result = d + value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsAdd16(d, value, result);
        return 7;
    }
    
    private static int executeSUBA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a - value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 2;
    }
    
    private static int executeSUBA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 4;
    }
    
    private static int executeSUBA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 5;
    }
    
    private static int executeSUBA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 5;
    }
    
    private static int executeSUBB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b - value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 2;
    }
    
    private static int executeSUBB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 4;
    }
    
    private static int executeSUBB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 5;
    }
    
    private static int executeSUBB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getRegisters().setB(result & 0xFF);
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 5;
    }
    
    private static int executeSUBD_immediate(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int value = cpu.fetchWord();
        int result = d - value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsSub16(d, value, result);
        return 4;
    }
    
    private static int executeSUBD_direct(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readWord(address);
        int result = d - value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsSub16(d, value, result);
        return 6;
    }
    
    private static int executeSUBD_extended(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readWord(address);
        int result = d - value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsSub16(d, value, result);
        return 7;
    }
    
    private static int executeSUBD_indexed(CPU cpu) {
        int d = cpu.getRegisters().getD();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readWord(address);
        int result = d - value;
        cpu.getRegisters().setD(result & 0xFFFF);
        cpu.getFlags().updateFlagsSub16(d, value, result);
        return 7;
    }
    
    private static int executeABA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a + b;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateFlagsAdd8(a, b, result);
        return 2;
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
    
    private static int executeINC_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = (value + 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 6;
    }
    
    private static int executeINC_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = (value + 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 7;
    }
    
    private static int executeINC_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = (value + 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 7;
    }
    
    private static int executeDEC_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = (value - 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 6;
    }
    
    private static int executeDEC_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = (value - 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 7;
    }
    
    private static int executeDEC_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = (value - 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        return 7;
    }
    
    private static int executeMUL(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a * b;
        cpu.getRegisters().setD(result);
        
        cpu.getFlags().setCarry((result & 0xFF00) != 0);
        cpu.getFlags().setZero(result == 0);
        cpu.getFlags().setNegative(false);
        
        return 11;
    }
    
    private static int executeDAA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int correction = 0;
        
        if (((a & 0x0F) > 0x09) || cpu.getFlags().getHalfCarry()) {
            correction |= 0x06;
        }
        
        if (((a & 0xF0) > 0x90) || cpu.getFlags().getCarry() ||
            (((a & 0xF0) > 0x80) && ((a & 0x0F) > 0x09))) {
            correction |= 0x60;
            cpu.getFlags().setCarry(true);
        }
        
        int result = a + correction;
        cpu.getRegisters().setA(result & 0xFF);
        cpu.getFlags().updateNZ8(result);
        
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
    
    private static int executeANDA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a & value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeANDA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a & value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeANDA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a & value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeANDB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b & value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeANDB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b & value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeANDB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b & value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeANDB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b & value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeANDCC_immediate(CPU cpu) {
        int cc = cpu.getFlags().getCC();
        int value = cpu.fetchByte();
        int result = cc & value;
        cpu.getFlags().setCC(result);
        return 3;
    }
    
    private static int executeORA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a | value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeORA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a | value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeORA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a | value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeORA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a | value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeORB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b | value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeORB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b | value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeORB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b | value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeORB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b | value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeORCC_immediate(CPU cpu) {
        int cc = cpu.getFlags().getCC();
        int value = cpu.fetchByte();
        int result = cc | value;
        cpu.getFlags().setCC(result);
        return 3;
    }
    
    private static int executeEORA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a ^ value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeEORA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a ^ value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeEORA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a ^ value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeEORA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a ^ value;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeEORB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b ^ value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 2;
    }
    
    private static int executeEORB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b ^ value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 4;
    }
    
    private static int executeEORB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b ^ value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeEORB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b ^ value;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        return 5;
    }
    
    private static int executeCOMA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int result = (~a) & 0xFF;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true);
        return 2;
    }
    
    private static int executeCOMB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (~b) & 0xFF;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true);
        return 2;
    }
    
    private static int executeCOM_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = (~value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true);
        return 6;
    }
    
    private static int executeCOM_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = (~value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true);
        return 7;
    }
    
    private static int executeCOM_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = (~value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry(true);
        return 7;
    }
    
    private static int executeNEGA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int result = (-a) & 0xFF;
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateFlagsSub8(0, a, -a);
        return 2;
    }
    
    private static int executeNEGB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (-b) & 0xFF;
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateFlagsSub8(0, b, -b);
        return 2;
    }
    
    private static int executeNEG_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = (-value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateFlagsSub8(0, value, -value);
        return 6;
    }
    
    private static int executeNEG_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = (-value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateFlagsSub8(0, value, -value);
        return 7;
    }
    
    private static int executeNEG_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = (-value) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        cpu.getFlags().updateFlagsSub8(0, value, -value);
        return 7;
    }
    
    private static int executeCMPA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a - value;
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 2;
    }
    
    private static int executeCMPA_direct(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 4;
    }
    
    private static int executeCMPA_extended(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 5;
    }
    
    private static int executeCMPA_indexed(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = a - value;
        cpu.getFlags().updateFlagsSub8(a, value, result);
        return 5;
    }
    
    private static int executeCMPB_immediate(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int value = cpu.fetchByte();
        int result = b - value;
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 2;
    }
    
    private static int executeCMPB_direct(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 4;
    }
    
    private static int executeCMPB_extended(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 5;
    }
    
    private static int executeCMPB_indexed(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        int result = b - value;
        cpu.getFlags().updateFlagsSub8(b, value, result);
        return 5;
    }
    
    private static int executeTSTA(CPU cpu) {
        int a = cpu.getRegisters().getA();
        cpu.getFlags().updateNZ8(a);
        cpu.getFlags().setCarry(false);
        return 2;
    }
    
    private static int executeTSTB(CPU cpu) {
        int b = cpu.getRegisters().getB();
        cpu.getFlags().updateNZ8(b);
        cpu.getFlags().setCarry(false);
        return 2;
    }
    
    private static int executeTST_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getMemory().readByte(address);
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setCarry(false);
        return 6;
    }
    
    private static int executeTST_extended(CPU cpu) {
        int address = cpu.fetchWord();
        int value = cpu.getMemory().readByte(address);
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setCarry(false);
        return 7;
    }
    
    private static int executeTST_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        int value = cpu.getMemory().readByte(address);
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setCarry(false);
        return 7;
    }
    
    private static int executeJMP_direct(CPU cpu) {
        int address = cpu.fetchByte();
        cpu.getRegisters().setPC(address);
        return 3;
    }
    
    private static int executeJMP_extended(CPU cpu) {
        int address = cpu.fetchWord();
        cpu.getRegisters().setPC(address);
        return 4;
    }
    
    private static int executeJMP_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getRegisters().setPC(address);
        return 4;
    }
    
    private static int executeJSR_direct(CPU cpu) {
        int address = cpu.fetchByte();
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.getRegisters().setPC(address);
        return 7;
    }
    
    private static int executeJSR_extended(CPU cpu) {
        int address = cpu.fetchWord();
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.getRegisters().setPC(address);
        return 8;
    }
    
    private static int executeJSR_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.getRegisters().setPC(address);
        return 8;
    }
    
    private static int executeBSR_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return 7;
    }
    
    private static int executeRTS(CPU cpu) {
        int address = cpu.popWordS();
        cpu.getRegisters().setPC(address);
        return 5;
    }
    
    private static int executeRTI(CPU cpu) {
        int cc = cpu.popS();
        cpu.getFlags().setCC(cc);
        
        if (cpu.getFlags().getEntire()) {
            int a = cpu.popS();
            int b = cpu.popS();
            int dp = cpu.popS();
            int x = cpu.popWordS();
            int y = cpu.popWordS();
            int u = cpu.popWordS();
            int pc = cpu.popWordS();
            
            cpu.getRegisters().setA(a);
            cpu.getRegisters().setB(b);
            cpu.getRegisters().setDP(dp);
            cpu.getRegisters().setX(x);
            cpu.getRegisters().setY(y);
            cpu.getRegisters().setU(u);
            cpu.getRegisters().setPC(pc);
            return 15;
        } else {
            int pc = cpu.popWordS();
            cpu.getRegisters().setPC(pc);
            return 10;
        }
    }
    
    private static int executeBRA_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return 3;
    }
    
    private static int executeBRN_relative(CPU cpu) {
        cpu.fetchByte();
        return 2;
    }
    
    private static int executeBHI_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (!cpu.getFlags().getCarry() && !cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBLS_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getCarry() || cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBCC_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (!cpu.getFlags().getCarry()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBCS_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getCarry()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBNE_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (!cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBEQ_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBVC_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (!cpu.getFlags().getOverflow()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBVS_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getOverflow()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBPL_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (!cpu.getFlags().getNegative()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBMI_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getNegative()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBGE_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getNegative() == cpu.getFlags().getOverflow()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBLT_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if (cpu.getFlags().getNegative() != cpu.getFlags().getOverflow()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBGT_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if ((cpu.getFlags().getNegative() == cpu.getFlags().getOverflow()) && !cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeBLE_relative(CPU cpu) {
        int offset = cpu.fetchByte();
        if ((offset & 0x80) != 0) offset |= 0xFF00;
        if ((cpu.getFlags().getNegative() != cpu.getFlags().getOverflow()) || cpu.getFlags().getZero()) {
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return 3;
        }
        return 2;
    }
    
    private static int executeLBRA_relative(CPU cpu) {
        int offset = cpu.fetchWord();
        if ((offset & 0x8000) != 0) offset |= 0xFFFF0000;
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return 5;
    }
    
    private static int executeLBSR_relative(CPU cpu) {
        int offset = cpu.fetchWord();
        if ((offset & 0x8000) != 0) offset |= 0xFFFF0000;
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return 9;
    }
    
    private static int executeTFR(CPU cpu) {
        int postByte = cpu.fetchByte();
        int sourceReg = (postByte >> 4) & 0x0F;
        int destReg = postByte & 0x0F;
        
        int value = getRegisterValue(cpu, sourceReg);
        setRegisterValue(cpu, destReg, value);
        
        return 6;
    }
    
    private static int executeEXG(CPU cpu) {
        int postByte = cpu.fetchByte();
        int reg1 = (postByte >> 4) & 0x0F;
        int reg2 = postByte & 0x0F;
        
        int value1 = getRegisterValue(cpu, reg1);
        int value2 = getRegisterValue(cpu, reg2);
        
        setRegisterValue(cpu, reg1, value2);
        setRegisterValue(cpu, reg2, value1);
        
        return 8;
    }
    
    private static int getRegisterValue(CPU cpu, int reg) {
        Register registers = cpu.getRegisters();
        switch (reg) {
            case 0: return registers.getD();
            case 1: return registers.getX();
            case 2: return registers.getY();
            case 3: return registers.getU();
            case 4: return registers.getS();
            case 5: return registers.getPC();
            case 8: return registers.getA();
            case 9: return registers.getB();
            case 10: return cpu.getFlags().getCC();
            case 11: return registers.getDP();
            default: return 0;
        }
    }
    
    private static void setRegisterValue(CPU cpu, int reg, int value) {
        Register registers = cpu.getRegisters();
        switch (reg) {
            case 0: registers.setD(value); break;
            case 1: registers.setX(value); break;
            case 2: registers.setY(value); break;
            case 3: registers.setU(value); break;
            case 4: registers.setS(value); break;
            case 5: registers.setPC(value); break;
            case 8: registers.setA(value); break;
            case 9: registers.setB(value); break;
            case 10: cpu.getFlags().setCC(value); break;
            case 11: registers.setDP(value); break;
        }
    }
    
    private static int executeSYNC(CPU cpu) {
        cpu.sync();
        return 2;
    }
    
    private static int executeCWAI_immediate(CPU cpu) {
        int mask = cpu.fetchByte();
        cpu.getFlags().setCC(cpu.getFlags().getCC() & mask);
        cpu.waitForInterrupt();
        return 20;
    }
    
    private static int executeSWI(CPU cpu) {
        // CORRECTION IMPORTANTE : SWI devrait arrêter l'exécution
        // ou au moins ne pas continuer à exécuter du code aléatoire
        
        System.out.println("SWI exécuté - Arrêt du programme");
        
        // Option 1: Arrêter complètement
        cpu.halt();
        return 19;
        
        // Option 2: Continuer avec le vecteur d'interruption
        // cpu.pushWordS(cpu.getRegisters().getPC());
        // cpu.pushWordS(cpu.getRegisters().getU());
        // cpu.pushWordS(cpu.getRegisters().getY());
        // cpu.pushWordS(cpu.getRegisters().getX());
        // cpu.pushS(cpu.getRegisters().getDP());
        // cpu.pushS(cpu.getRegisters().getB());
        // cpu.pushS(cpu.getRegisters().getA());
        // cpu.pushS(cpu.getFlags().getCC());
        
        // cpu.getFlags().setEntire(true);
        // cpu.getFlags().setFIRQMask(true);
        // cpu.getFlags().setIRQMask(true);
        
        // int vector = cpu.getMemory().readWord(0xFFFA);
        // cpu.getRegisters().setPC(vector);
        
        // return 19;
    }
    
    private static int executeCLRA(CPU cpu) {
        cpu.getRegisters().setA(0);
        cpu.getFlags().updateNZ8(0);
        cpu.getFlags().setCarry(false);
        return 2;
    }
    
    private static int executeCLRB(CPU cpu) {
        cpu.getRegisters().setB(0);
        cpu.getFlags().updateNZ8(0);
        cpu.getFlags().setCarry(false);
        return 2;
    }
    
    private static int executeCLR_direct(CPU cpu) {
        int address = cpu.fetchByte();
        cpu.getMemory().writeByte(address, 0);
        cpu.getFlags().updateNZ8(0);
        cpu.getFlags().setCarry(false);
        return 6;
    }
    
    private static int executeCLR_extended(CPU cpu) {
        int address = cpu.fetchWord();
        cpu.getMemory().writeByte(address, 0);
        cpu.getFlags().updateNZ8(0);
        cpu.getFlags().setCarry(false);
        return 7;
    }
    
    private static int executeCLR_indexed(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getMemory().writeByte(address, 0);
        cpu.getFlags().updateNZ8(0);
        cpu.getFlags().setCarry(false);
        return 7;
    }
    
    private static int executeABX(CPU cpu) {
        int x = cpu.getRegisters().getX();
        int b = cpu.getRegisters().getB() & 0xFF;
        cpu.getRegisters().setX(x + b);
        return 3;
    }
    
    private static int executeSEX(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int result = (b & 0x80) != 0 ? 0xFF00 | b : b;
        cpu.getRegisters().setD(result);
        cpu.getFlags().updateNZ16(result);
        return 2;
    }
    
    private static int executePSHS(CPU cpu) {
        int postByte = cpu.fetchByte();
        Register reg = cpu.getRegisters();
        int cycles = 5;
        
        if ((postByte & 0x80) != 0) { // PC
            cpu.pushWordS(reg.getPC());
            cycles += 2;
        }
        if ((postByte & 0x40) != 0) { // U/S/Y/X
            if ((postByte & 0x10) != 0) { // U
                cpu.pushWordS(reg.getU());
                cycles += 2;
            }
            if ((postByte & 0x20) != 0) { // Y
                cpu.pushWordS(reg.getY());
                cycles += 2;
            }
            if ((postByte & 0x08) != 0) { // X
                cpu.pushWordS(reg.getX());
                cycles += 2;
            }
        }
        if ((postByte & 0x04) != 0) { // DP
            cpu.pushS(reg.getDP());
            cycles += 1;
        }
        if ((postByte & 0x02) != 0) { // B
            cpu.pushS(reg.getB());
            cycles += 1;
        }
        if ((postByte & 0x01) != 0) { // A
            cpu.pushS(reg.getA());
            cycles += 1;
        }
        
        return cycles;
    }
    
    private static int executePULS(CPU cpu) {
        int postByte = cpu.fetchByte();
        Register reg = cpu.getRegisters();
        int cycles = 5;
        
        if ((postByte & 0x01) != 0) { // CC
            int cc = cpu.popS();
            cpu.getFlags().setCC(cc);
            cycles += 1;
        }
        if ((postByte & 0x02) != 0) { // A
            reg.setA(cpu.popS());
            cycles += 1;
        }
        if ((postByte & 0x04) != 0) { // B
            reg.setB(cpu.popS());
            cycles += 1;
        }
        if ((postByte & 0x08) != 0) { // DP
            reg.setDP(cpu.popS());
            cycles += 1;
        }
        if ((postByte & 0x10) != 0) { // X
            reg.setX(cpu.popWordS());
            cycles += 2;
        }
        if ((postByte & 0x20) != 0) { // Y
            reg.setY(cpu.popWordS());
            cycles += 2;
        }
        if ((postByte & 0x40) != 0) { // U
            reg.setU(cpu.popWordS());
            cycles += 2;
        }
        if ((postByte & 0x80) != 0) { // PC
            reg.setPC(cpu.popWordS());
            cycles += 2;
        }
        
        return cycles;
    }
    
    private static int executePSHU(CPU cpu) {
        int postByte = cpu.fetchByte();
        Register reg = cpu.getRegisters();
        int cycles = 5;
        
        if ((postByte & 0x80) != 0) { // PC
            cpu.pushWordU(reg.getPC());
            cycles += 2;
        }
        if ((postByte & 0x40) != 0) { // S/Y/X
            if ((postByte & 0x10) != 0) { // S
                cpu.pushWordU(reg.getS());
                cycles += 2;
            }
            if ((postByte & 0x20) != 0) { // Y
                cpu.pushWordU(reg.getY());
                cycles += 2;
            }
            if ((postByte & 0x08) != 0) { // X
                cpu.pushWordU(reg.getX());
                cycles += 2;
            }
        }
        if ((postByte & 0x04) != 0) { // DP
            cpu.pushU(reg.getDP());
            cycles += 1;
        }
        if ((postByte & 0x02) != 0) { // B
            cpu.pushU(reg.getB());
            cycles += 1;
        }
        if ((postByte & 0x01) != 0) { // A
            cpu.pushU(reg.getA());
            cycles += 1;
        }
        
        return cycles;
    }
    
    private static int executePULU(CPU cpu) {
        int postByte = cpu.fetchByte();
        Register reg = cpu.getRegisters();
        int cycles = 5;
        
        if ((postByte & 0x01) != 0) { // CC
            int cc = cpu.popU();
            cpu.getFlags().setCC(cc);
            cycles += 1;
        }
        if ((postByte & 0x02) != 0) { // A
            reg.setA(cpu.popU());
            cycles += 1;
        }
        if ((postByte & 0x04) != 0) { // B
            reg.setB(cpu.popU());
            cycles += 1;
        }
        if ((postByte & 0x08) != 0) { // DP
            reg.setDP(cpu.popU());
            cycles += 1;
        }
        if ((postByte & 0x10) != 0) { // X
            reg.setX(cpu.popWordU());
            cycles += 2;
        }
        if ((postByte & 0x20) != 0) { // Y
            reg.setY(cpu.popWordU());
            cycles += 2;
        }
        if ((postByte & 0x40) != 0) { // S
            reg.setS(cpu.popWordU());
            cycles += 2;
        }
        if ((postByte & 0x80) != 0) { // PC
            reg.setPC(cpu.popWordU());
            cycles += 2;
        }
        
        return cycles;
    }
    
    private static int executeLEAX(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getRegisters().setX(address);
        cpu.getFlags().updateNZ16(address);
        return 4;
    }
    
    private static int executeLEAY(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getRegisters().setY(address);
        cpu.getFlags().updateNZ16(address);
        return 4;
    }
    
    private static int executeLEAS(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getRegisters().setS(address);
        return 4;
    }
    
    private static int executeLEAU(CPU cpu) {
        int address = decodeIndexedAddress(cpu);
        cpu.getRegisters().setU(address);
        return 4;
    }
    
    private static int decodeIndexedAddress(CPU cpu) {
        int postByte = cpu.fetchByte();
        Register reg = cpu.getRegisters();
        
        int address = 0;
        
        switch (postByte & 0x1F) {
            case 0x00: case 0x01: case 0x02: case 0x03: case 0x04: case 0x05: case 0x06: case 0x07:
                int offset = postByte & 0x0F;
                if ((postByte & 0x10) != 0) offset = -offset;
                address = reg.getX() + offset;
                break;
                
            case 0x08: case 0x09: case 0x0A: case 0x0B:
                offset = postByte & 0x03;
                if ((postByte & 0x04) != 0) offset = -offset;
                address = reg.getY() + offset;
                break;
                
            case 0x0C: case 0x0D:
                offset = postByte & 0x01;
                if ((postByte & 0x02) != 0) offset = -offset;
                address = reg.getU() + offset;
                break;
                
            case 0x0E: case 0x0F:
                offset = postByte & 0x01;
                if ((postByte & 0x02) != 0) offset = -offset;
                address = reg.getS() + offset;
                break;
                
            case 0x10:
                address = reg.getX();
                break;
                
            case 0x11:
                address = reg.getY();
                break;
                
            case 0x12:
                address = reg.getU();
                break;
                
            case 0x13:
                address = reg.getS();
                break;
                
            case 0x14:
                address = reg.getX();
                reg.setX(address + 1);
                break;
                
            case 0x15:
                address = reg.getX();
                reg.setX(address + 2);
                break;
                
            case 0x16:
                reg.setX(reg.getX() - 1);
                address = reg.getX();
                break;
                
            case 0x17:
                reg.setX(reg.getX() - 2);
                address = reg.getX();
                break;
                
            case 0x18:
                address = 0;
                break;
                
            case 0x19:
                offset = reg.getA();
                if ((offset & 0x80) != 0) offset |= 0xFF00;
                address = reg.getX() + offset;
                break;
                
            case 0x1A:
                offset = reg.getB();
                if ((offset & 0x80) != 0) offset |= 0xFF00;
                address = reg.getX() + offset;
                break;
                
            case 0x1B:
                offset = reg.getD();
                address = reg.getX() + offset;
                break;
                
            case 0x1C:
                offset = cpu.fetchByte();
                if ((offset & 0x80) != 0) offset |= 0xFF00;
                address = reg.getPC() + offset;
                break;
                
            case 0x1D:
                offset = cpu.fetchWord();
                address = reg.getPC() + offset;
                break;
                
            case 0x1E:
                address = cpu.fetchWord();
                address = cpu.getMemory().readWord(address);
                break;
                
            case 0x1F:
                address = cpu.fetchWord();
                break;
        }
        
        return address & 0xFFFF;
    }
    
    private static int handlePrefixedOpcode(CPU cpu, int opcode) {
        System.err.println("Opcode préfixé non implémenté: 0x" + String.format("%04X", opcode));
        cpu.halt();
        return 0;
    }
}