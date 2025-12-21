// InstructionDecoder.java - CORRIGÉ
package motorola6809.instruction;

import motorola6809.core.CPU;

public class InstructionDecoder {
    
    public static int decodeAndExecute(CPU cpu, int opcode) {
        int cycles = 0;
        
        switch (opcode) {
            // NOP
            case 0x12:
                cycles = executeNOP(cpu);
                break;
                
            // LDA # (IMMEDIATE)
            case 0x86:
                cycles = executeLDA_immediate(cpu);
                break;
                
            // LDB # (IMMEDIATE)
            case 0xC6:
                cycles = executeLDB_immediate(cpu);
                break;
                
            // MUL
            case 0x3D:
                cycles = executeMUL(cpu);
                break;
                
            // STA (DIRECT)
            case 0x97:
                cycles = executeSTA_direct(cpu);
                break;
                
            // STB (DIRECT)
            case 0xD7:
                cycles = executeSTB_direct(cpu);
                break;
                
            // SWI
            case 0x3F:
                cycles = executeSWI(cpu);
                break;
                
            // ADDA #
            case 0x8B:
                cycles = executeADDA_immediate(cpu);
                break;
			    
            case 0xFF: // Mémoire non initialisée (fin de programme)
                System.out.println("Fin du programme - mémoire non initialisée");
                cpu.halt();
                return 0;
                
          
                
            default:
                throw new IllegalStateException(
                    String.format("Unknown opcode: 0x%02X at PC=0x%04X", 
                        opcode, cpu.getRegisters().getPC() - 1)
                );
        }
        
        return cycles;
    }
    
    private static int executeNOP(CPU cpu) {
        // Ne fait rien
        return 2;
    }
    
    private static int executeLDA_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setA(value);
        
        // Met à jour les flags
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setCarry(false);
        cpu.getFlags().setOverflow(false);
        
        return 2;
    }
    
    private static int executeLDB_immediate(CPU cpu) {
        int value = cpu.fetchByte();
        cpu.getRegisters().setB(value);
        
        // Met à jour les flags
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setCarry(false);
        cpu.getFlags().setOverflow(false);
        
        return 2;
    }
    
    private static int executeADDA_immediate(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int value = cpu.fetchByte();
        int result = a + value;
        
        cpu.getRegisters().setA(result & 0xFF);
        
        // Met à jour les flags
        cpu.getFlags().updateFlagsAdd8(a, value, result);
        
        return 2;
    }
    
    private static int executeMUL(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a * b;
        
        cpu.getRegisters().setD(result);
        
        // Met à jour les flags
        cpu.getFlags().setCarry((result & 0xFF00) != 0);
        cpu.getFlags().setZero(result == 0);
        cpu.getFlags().setNegative(false);
        
        return 11;
    }
    
    private static int executeSTA_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getA();
        
        cpu.getMemory().writeByte(address, value);
        
        return 4;
    }
    
    private static int executeSTB_direct(CPU cpu) {
        int address = cpu.fetchByte();
        int value = cpu.getRegisters().getB();
        
        cpu.getMemory().writeByte(address, value);
        
        return 4;
    }
    
    private static int executeSWI(CPU cpu) {
        // Interruption logicielle
        // Sauvegarde les registres
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.pushS(cpu.getRegisters().getU() & 0xFF);
        cpu.pushS(cpu.getRegisters().getY() & 0xFF);
        cpu.pushS(cpu.getRegisters().getX() & 0xFF);
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
}