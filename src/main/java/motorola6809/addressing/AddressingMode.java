package motorola6809.addressing;

import motorola6809.core.CPU;

public abstract class AddressingMode {
    
    protected String operand;
    protected int effectiveAddress;
    protected int value;
    protected boolean isValue; // true si mode immédiat
    
    public AddressingMode(String operand) {
        this.operand = operand;
        this.isValue = false;
    }
    
    /**
     * Résout l'adresse effective ou la valeur
     */
    public abstract void resolve(CPU cpu);
    
    /**
     * Obtient la valeur (pour lecture)
     */
    public abstract int getValue(CPU cpu);
    
    /**
     * Définit la valeur (pour écriture)
     */
    public abstract void setValue(CPU cpu, int value);
    
    /**
     * Obtient le nombre de cycles supplémentaires
     */
    public abstract int getAdditionalCycles();
    
    /**
     * Obtient le nombre d'octets de l'opérande
     */
    public abstract int getOperandSize();
    
    public String getOperand() {
        return operand;
    }
    
    public int getEffectiveAddress() {
        return effectiveAddress;
    }
    
    public boolean isValueMode() {
        return isValue;
    }
}