package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class EXG extends Instruction {
    
    public EXG() {
        super("EXG", 0x1E, 8);
    }
    
    @Override
    public int execute(CPU cpu) {
        int postbyte = cpu.fetchByte();
        int reg1 = (postbyte >> 4) & 0x0F;
        int reg2 = postbyte & 0x0F;
        
        int temp = getRegisterValue(cpu, reg1);
        setRegisterValue(cpu, reg1, getRegisterValue(cpu, reg2));
        setRegisterValue(cpu, reg2, temp);
        
        return baseCycles;
    }
    
    private int getRegisterValue(CPU cpu, int regCode) {
        switch (regCode) {
            case 0x0: return cpu.getRegisters().getD();
            case 0x1: return cpu.getRegisters().getX();
            case 0x2: return cpu.getRegisters().getY();
            case 0x3: return cpu.getRegisters().getU();
            case 0x4: return cpu.getRegisters().getS();
            case 0x5: return cpu.getRegisters().getPC();
            case 0x8: return cpu.getRegisters().getA();
            case 0x9: return cpu.getRegisters().getB();
            case 0xA: return cpu.getFlags().getCC();
            case 0xB: return cpu.getRegisters().getDP();
            default: return 0;
        }
    }
    
    private void setRegisterValue(CPU cpu, int regCode, int value) {
        switch (regCode) {
            case 0x0: cpu.getRegisters().setD(value); break;
            case 0x1: cpu.getRegisters().setX(value); break;
            case 0x2: cpu.getRegisters().setY(value); break;
            case 0x3: cpu.getRegisters().setU(value); break;
            case 0x4: cpu.getRegisters().setS(value); break;
            case 0x5: cpu.getRegisters().setPC(value); break;
            case 0x8: cpu.getRegisters().setA(value); break;
            case 0x9: cpu.getRegisters().setB(value); break;
            case 0xA: cpu.getFlags().setCC(value); break;
            case 0xB: cpu.getRegisters().setDP(value); break;
        }
    }
}