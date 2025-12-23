package motorola6809.instruction;

import motorola6809.core.CPU;
import motorola6809.core.Register;
import motorola6809.core.StatusFlags;

public class InstructionDecoder {
    
    public static int decodeAndExecute(CPU cpu, int opcode) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        switch (opcode) {
            // === UAL - ARITHMÉTIQUE ===
            case 0x86: // LDA #immédiat
                return executeLDA_immediate(cpu);
            case 0xC6: // LDB #immédiat
                return executeLDB_immediate(cpu);
            case 0x8B: // ADDA #immédiat
                return executeADDA_immediate(cpu);
            case 0xCB: // ADDB #immédiat
                return executeADDB_immediate(cpu);
            case 0x80: // SUBA #immédiat
                return executeSUBA_immediate(cpu);
            case 0xC0: // SUBB #immédiat
                return executeSUBB_immediate(cpu);
            case 0x3D: // MUL
                return executeMUL(cpu);
                
            // === UAL - LOGIQUE ===
            case 0x84: // ANDA #immédiat
                return executeANDA_immediate(cpu);
            case 0xC4: // ANDB #immédiat
                return executeANDB_immediate(cpu);
            case 0x88: // EORA #immédiat
                return executeEORA_immediate(cpu);
            case 0xC8: // EORB #immédiat
                return executeEORB_immediate(cpu);
            case 0x8A: // ORA #immédiat
                return executeORA_immediate(cpu);
            case 0xCA: // ORB #immédiat
                return executeORB_immediate(cpu);
                
            // === INCRÉMENTATION/DÉCRÉMENTATION ===
            case 0x4C: // INCA
                return executeINCA(cpu);
            case 0x5C: // INCB
                return executeINCB(cpu);
            case 0x4A: // DECA
                return executeDECA(cpu);
            case 0x5A: // DECB
                return executeDECB(cpu);
                
            // === COMPARAISON ===
            case 0x81: // CMPA #immédiat
                return executeCMPA_immediate(cpu);
            case 0xC1: // CMPB #immédiat
                return executeCMPB_immediate(cpu);
                
            // === DIVERS ===
            case 0x12: // NOP
                return 2;
            case 0x3F: // SWI
                return executeSWI(cpu);
            case 0x39: // RTS
                return executeRTS(cpu);
                
            default:
                System.err.printf("Opcode inconnu: 0x%02X à PC=0x%04X\n", 
                    opcode, regs.getPC() - 1);
                // Avance quand même pour éviter de bloquer
                return 2;
        }
    }
    
    // ============ IMPLÉMENTATIONS UAL ============
    
    private static int executeLDA_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setA(value);
        cpu.getFlags().updateNZ8(value);
        return 2;
    }
    
    private static int executeLDB_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setB(value);
        cpu.getFlags().updateNZ8(value);
        return 2;
    }
    
    private static int executeADDA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a + value;
        
        regs.setA(result & 0xFF);
        flags.updateFlagsAdd8(a, value, result);
        return 2;
    }
    
    private static int executeADDB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b + value;
        
        regs.setB(result & 0xFF);
        flags.updateFlagsAdd8(b, value, result);
        return 2;
    }
    
    private static int executeSUBA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a - value;
        
        regs.setA(result & 0xFF);
        flags.updateFlagsSub8(a, value, result);
        return 2;
    }
    
    private static int executeSUBB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b - value;
        
        regs.setB(result & 0xFF);
        flags.updateFlagsSub8(b, value, result);
        return 2;
    }
    
    private static int executeMUL(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA() & 0xFF;
        int b = regs.getB() & 0xFF;
        int result = a * b;
        
        // Stocke dans D (A:B)
        regs.setD(result & 0xFFFF);
        
        // Mise à jour des flags
        flags.setZero((result & 0xFFFF) == 0);
        flags.setCarry((result & 0xFF00) != 0); // Carry si résultat > 255
        flags.setNegative(false); // MUL ne met pas N à jour sur 6809
        
        return 11; // 11 cycles pour MUL
    }
    
    // ============ LOGIQUE ============
    
    private static int executeANDA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a & value;
        
        regs.setA(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    private static int executeANDB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b & value;
        
        regs.setB(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    private static int executeEORA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a ^ value;
        
        regs.setA(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    private static int executeEORB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b ^ value;
        
        regs.setB(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    private static int executeORA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a | value;
        
        regs.setA(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    private static int executeORB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b | value;
        
        regs.setB(result);
        flags.updateNZ8(result);
        flags.setCarry(false);
        return 2;
    }
    
    // ============ INCREMENTATION ============
    
    private static int executeINCA(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int result = (a + 1) & 0xFF;
        
        regs.setA(result);
        flags.updateNZ8(result);
        flags.setOverflow(a == 0x7F); // Overflow si 127 -> 128
        return 2;
    }
    
    private static int executeINCB(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int result = (b + 1) & 0xFF;
        
        regs.setB(result);
        flags.updateNZ8(result);
        flags.setOverflow(b == 0x7F);
        return 2;
    }
    
    private static int executeDECA(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int result = (a - 1) & 0xFF;
        
        regs.setA(result);
        flags.updateNZ8(result);
        flags.setOverflow(a == 0x80); // Overflow si -128 -> 127
        return 2;
    }
    
    private static int executeDECB(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int result = (b - 1) & 0xFF;
        
        regs.setB(result);
        flags.updateNZ8(result);
        flags.setOverflow(b == 0x80);
        return 2;
    }
    
    // ============ COMPARAISON ============
    
    private static int executeCMPA_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int a = regs.getA();
        int value = cpu.fetchByte();
        int result = a - value;
        
        flags.updateFlagsSub8(a, value, result);
        return 2;
    }
    
    private static int executeCMPB_immediate(CPU cpu) {
        Register regs = cpu.getRegisters();
        StatusFlags flags = cpu.getFlags();
        
        int b = regs.getB();
        int value = cpu.fetchByte();
        int result = b - value;
        
        flags.updateFlagsSub8(b, value, result);
        return 2;
    }
    
    // ============ DIVERS ============
    
    private static int executeSWI(CPU cpu) {
        // Sauvegarde des registres sur la pile système
        Register regs = cpu.getRegisters();
        
        // Sauvegarde PC, U, Y, X, DP, B, A, CC
        cpu.pushWordS(regs.getPC());
        cpu.pushWordS(regs.getU());
        cpu.pushWordS(regs.getY());
        cpu.pushWordS(regs.getX());
        cpu.pushS(regs.getDP());
        cpu.pushS(regs.getB());
        cpu.pushS(regs.getA());
        cpu.pushS(cpu.getFlags().getCC());
        
        // Définit les flags
        cpu.getFlags().setEntire(true);
        cpu.getFlags().setFIRQMask(true);
        cpu.getFlags().setIRQMask(true);
        
        // Va au vecteur SWI (0xFFFA)
        int vector = cpu.getMemory().readWord(0xFFFA);
        regs.setPC(vector);
        
        return 19;
    }
    
    private static int executeRTS(CPU cpu) {
        // Récupère le PC de la pile
        int returnAddress = cpu.popWordS();
        cpu.getRegisters().setPC(returnAddress);
        return 5;
    }
    
    // Méthode utilitaire pour lire un octet signé
    private static int readSignedByte(CPU cpu) {
        int value = cpu.fetchByte();
        if ((value & 0x80) != 0) {
            value |= 0xFFFFFF00; // Extension de signe
        }
        return value;
    }
}