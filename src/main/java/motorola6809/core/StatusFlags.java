package motorola6809.core;

import motorola6809.utils.BitOperations;
import motorola6809.config.Constants;

public class StatusFlags {
    
    private int cc; // Registre Condition Code (8 bits)
    
    public StatusFlags() {
        this.cc = Constants.DEFAULT_CC;
    }
    
    /**
     * Obtient la valeur complète du registre CC
     */
    public int getCC() {
        return cc;
    }
    
    /**
     * Définit la valeur complète du registre CC
     */
    public void setCC(int value) {
        this.cc = value & Constants.MASK_8BIT;
    }
    
    // Getters individuels pour chaque flag
    public boolean getCarry() {
        return BitOperations.testBit(cc, Constants.CC_C);
    }
    
    public boolean getOverflow() {
        return BitOperations.testBit(cc, Constants.CC_V);
    }
    
    public boolean getZero() {
        return BitOperations.testBit(cc, Constants.CC_Z);
    }
    
    public boolean getNegative() {
        return BitOperations.testBit(cc, Constants.CC_N);
    }
    
    public boolean getIRQMask() {
        return BitOperations.testBit(cc, Constants.CC_I);
    }
    
    public boolean getHalfCarry() {
        return BitOperations.testBit(cc, Constants.CC_H);
    }
    
    public boolean getFIRQMask() {
        return BitOperations.testBit(cc, Constants.CC_F);
    }
    
    public boolean getEntire() {
        return BitOperations.testBit(cc, Constants.CC_E);
    }
    
    // Setters individuels pour chaque flag
    public void setCarry(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_C) 
                   : BitOperations.clearBit(cc, Constants.CC_C);
    }
    
    public void setOverflow(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_V) 
                   : BitOperations.clearBit(cc, Constants.CC_V);
    }
    
    public void setZero(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_Z) 
                   : BitOperations.clearBit(cc, Constants.CC_Z);
    }
    
    public void setNegative(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_N) 
                   : BitOperations.clearBit(cc, Constants.CC_N);
    }
    
    public void setIRQMask(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_I) 
                   : BitOperations.clearBit(cc, Constants.CC_I);
    }
    
    public void setHalfCarry(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_H) 
                   : BitOperations.clearBit(cc, Constants.CC_H);
    }
    
    public void setFIRQMask(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_F) 
                   : BitOperations.clearBit(cc, Constants.CC_F);
    }
    
    public void setEntire(boolean value) {
        cc = value ? BitOperations.setBit(cc, Constants.CC_E) 
                   : BitOperations.clearBit(cc, Constants.CC_E);
    }
    
    /**
     * Met à jour les flags N et Z selon une valeur 8 bits
     */
    public void updateNZ8(int value) {
        value &= Constants.MASK_8BIT;
        setZero(value == 0);
        setNegative((value & 0x80) != 0);
    }
    
    /**
     * Met à jour les flags N et Z selon une valeur 16 bits
     */
    public void updateNZ16(int value) {
        value &= Constants.MASK_16BIT;
        setZero(value == 0);
        setNegative((value & 0x8000) != 0);
    }
    
    /**
     * Met à jour tous les flags arithmétiques pour une addition 8 bits
     */
    public void updateFlagsAdd8(int op1, int op2, int result) {
        op1 &= 0xFF;
        op2 &= 0xFF;
        result &= 0x1FF; // 9 bits pour détecter la retenue
        
        setCarry((result & 0x100) != 0);
        setOverflow(((op1 ^ result) & (op2 ^ result) & 0x80) != 0);
        setHalfCarry(((op1 & 0x0F) + (op2 & 0x0F)) > 0x0F);
        updateNZ8(result);
    }
    
    /**
     * Met à jour tous les flags arithmétiques pour une addition 16 bits
     */
    public void updateFlagsAdd16(int op1, int op2, int result) {
        op1 &= 0xFFFF;
        op2 &= 0xFFFF;
        result &= 0x1FFFF; // 17 bits pour détecter la retenue
        
        setCarry((result & 0x10000) != 0);
        setOverflow(((op1 ^ result) & (op2 ^ result) & 0x8000) != 0);
        updateNZ16(result);
    }
    
    /**
     * Met à jour tous les flags arithmétiques pour une soustraction 8 bits
     */
    public void updateFlagsSub8(int op1, int op2, int result) {
        op1 &= 0xFF;
        op2 &= 0xFF;
        
        setCarry(op2 > op1); // Borrow
        setOverflow(((op1 ^ op2) & (op1 ^ result) & 0x80) != 0);
        updateNZ8(result);
    }
    
    /**
     * Met à jour tous les flags arithmétiques pour une soustraction 16 bits
     */
    public void updateFlagsSub16(int op1, int op2, int result) {
        op1 &= 0xFFFF;
        op2 &= 0xFFFF;
        
        setCarry(op2 > op1); // Borrow
        setOverflow(((op1 ^ op2) & (op1 ^ result) & 0x8000) != 0);
        updateNZ16(result);
    }
    
    /**
     * Réinitialise tous les flags
     */
    public void reset() {
        this.cc = Constants.DEFAULT_CC;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("E=").append(getEntire() ? "1" : "0");
        sb.append(" F=").append(getFIRQMask() ? "1" : "0");
        sb.append(" H=").append(getHalfCarry() ? "1" : "0");
        sb.append(" I=").append(getIRQMask() ? "1" : "0");
        sb.append(" N=").append(getNegative() ? "1" : "0");
        sb.append(" Z=").append(getZero() ? "1" : "0");
        sb.append(" V=").append(getOverflow() ? "1" : "0");
        sb.append(" C=").append(getCarry() ? "1" : "0");
        return sb.toString();
    }
}