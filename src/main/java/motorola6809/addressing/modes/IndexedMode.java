package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;
import motorola6809.utils.HexConverter;
import motorola6809.utils.BitOperations;

public class IndexedMode extends AddressingMode {
    
    private int additionalCycles;
    private int operandSize;
    private boolean indirect;
    
    public IndexedMode(String operand) {
        super(operand);
        this.additionalCycles = 0;
        this.operandSize = 0;
        this.indirect = false;
    }
    
    @Override
    public void resolve(CPU cpu) {
        String op = operand.trim();
        
        // Détecte l'indirection [...]
        if (op.startsWith("[") && op.endsWith("]")) {
            indirect = true;
            op = op.substring(1, op.length() - 1);
            additionalCycles += 3;
        }
        
        // Analyse le mode indexé
        if (op.startsWith(",")) {
            // Modes sans offset: ,X  ,-X  ,X+  ,X++  ,--X
            resolveNoOffset(cpu, op);
        } else if (op.contains(",")) {
            // Modes avec offset: $20,X  A,X  D,X
            resolveWithOffset(cpu, op);
        }
        
        // Si indirect, lire l'adresse effective depuis la mémoire
        if (indirect) {
            effectiveAddress = cpu.getMemory().readWord(effectiveAddress);
        }
    }
    
    private void resolveNoOffset(CPU cpu, String op) {
        char register = getRegisterChar(op);
        int regValue = getRegisterValue(cpu, register);
        
        if (op.contains("++")) {
            // Post-incrément de 2: ,X++
            effectiveAddress = regValue;
            setRegisterValue(cpu, register, regValue + 2);
            additionalCycles += 3;
        } else if (op.contains("+")) {
            // Post-incrément de 1: ,X+
            effectiveAddress = regValue;
            setRegisterValue(cpu, register, regValue + 1);
            additionalCycles += 2;
        } else if (op.contains("--")) {
            // Pré-décrément de 2: ,--X
            regValue -= 2;
            setRegisterValue(cpu, register, regValue);
            effectiveAddress = regValue;
            additionalCycles += 3;
        } else if (op.contains("-")) {
            // Pré-décrément de 1: ,-X
            regValue -= 1;
            setRegisterValue(cpu, register, regValue);
            effectiveAddress = regValue;
            additionalCycles += 2;
        } else {
            // Simple: ,X
            effectiveAddress = regValue;
            additionalCycles += 1;
        }
    }
    
    private void resolveWithOffset(CPU cpu, String op) {
        int commaPos = op.indexOf(',');
        String offsetPart = op.substring(0, commaPos).trim();
        String registerPart = op.substring(commaPos + 1).trim();
        
        char register = registerPart.charAt(0);
        int regValue = getRegisterValue(cpu, register);
        int offset = 0;
        
        if (offsetPart.matches("[A-Z]")) {
            // Offset via un registre accumulateur: A,X  B,X  D,X
            offset = getRegisterValue(cpu, offsetPart.charAt(0));
            additionalCycles += 1;
        } else if (offsetPart.startsWith("$")) {
            // Offset hexadécimal
            offset = HexConverter.hexToInt(offsetPart.substring(1));
            
            // Détermine si offset 5, 8 ou 16 bits
            if (offset >= -16 && offset <= 15) {
                // 5 bits
                additionalCycles += 1;
                operandSize = 1;
            } else if (offset >= -128 && offset <= 127) {
                // 8 bits
                offset = BitOperations.signExtend8to16(offset);
                additionalCycles += 1;
                operandSize = 1;
            } else {
                // 16 bits
                additionalCycles += 4;
                operandSize = 2;
            }
        } else {
            // Offset décimal
            offset = Integer.parseInt(offsetPart);
            if (offset >= -128 && offset <= 127) {
                offset = BitOperations.signExtend8to16(offset);
                additionalCycles += 1;
            } else {
                additionalCycles += 4;
            }
        }
        
        effectiveAddress = (regValue + offset) & 0xFFFF;
    }
    
    private char getRegisterChar(String op) {
        // Extrait le caractère du registre (X, Y, U, S)
        if (op.contains("X")) return 'X';
        if (op.contains("Y")) return 'Y';
        if (op.contains("U")) return 'U';
        if (op.contains("S")) return 'S';
        throw new IllegalArgumentException("Invalid indexed register: " + op);
    }
    
    private int getRegisterValue(CPU cpu, char register) {
        switch (register) {
            case 'X': return cpu.getRegisters().getX();
            case 'Y': return cpu.getRegisters().getY();
            case 'U': return cpu.getRegisters().getU();
            case 'S': return cpu.getRegisters().getS();
            case 'A': return cpu.getRegisters().getA();
            case 'B': return cpu.getRegisters().getB();
            case 'D': return cpu.getRegisters().getD();
            default: throw new IllegalArgumentException("Invalid register: " + register);
        }
    }
    
    private void setRegisterValue(CPU cpu, char register, int value) {
        value &= 0xFFFF;
        switch (register) {
            case 'X': cpu.getRegisters().setX(value); break;
            case 'Y': cpu.getRegisters().setY(value); break;
            case 'U': cpu.getRegisters().setU(value); break;
            case 'S': cpu.getRegisters().setS(value); break;
            default: throw new IllegalArgumentException("Cannot modify register: " + register);
        }
    }
    
    @Override
    public int getValue(CPU cpu) {
        return cpu.getMemory().readByte(effectiveAddress);
    }
    
    @Override
    public void setValue(CPU cpu, int value) {
        cpu.getMemory().writeByte(effectiveAddress, value);
    }
    
    @Override
    public int getAdditionalCycles() {
        return additionalCycles;
    }
    
    @Override
    public int getOperandSize() {
        return operandSize;
    }
}