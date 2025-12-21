package motorola6809.core;

import motorola6809.utils.BitOperations;
import motorola6809.config.Constants;

public class Register {
    
    private int a;  // Accumulateur A (8 bits)
    private int b;  // Accumulateur B (8 bits)
    private int dp; // Direct Page (8 bits)
    private int x;  // Index X (16 bits)
    private int y;  // Index Y (16 bits)
    private int u;  // User Stack Pointer (16 bits)
    private int s;  // System Stack Pointer (16 bits)
    private int pc; // Program Counter (16 bits)
    
    public Register() {
        reset();
    }
    
    /**
     * Réinitialise tous les registres aux valeurs par défaut
     */
    public void reset() {
        this.a = Constants.DEFAULT_A;
        this.b = Constants.DEFAULT_B;
        this.dp = Constants.DEFAULT_DP;
        this.x = Constants.DEFAULT_X;
        this.y = Constants.DEFAULT_Y;
        this.u = Constants.DEFAULT_U;
        this.s = Constants.DEFAULT_S;
        this.pc = Constants.DEFAULT_PC;
    }
    
   
    // Getters et Setters pour registres 8 bits
   
    
    public int getA() {
        return a;
    }
    
    public void setA(int value) {
        this.a = value & Constants.MASK_8BIT;
    }
    
    public int getB() {
        return b;
    }
    
    public void setB(int value) {
        this.b = value & Constants.MASK_8BIT;
    }
    
    public int getDP() {
        return dp;
    }
    
    public void setDP(int value) {
        this.dp = value & Constants.MASK_8BIT;
    }
    
   
    // Getters et Setters pour registres 16 bits
    
    public int getX() {
        return x;
    }
    
    public void setX(int value) {
        this.x = value & Constants.MASK_16BIT;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int value) {
        this.y = value & Constants.MASK_16BIT;
    }
    
    public int getU() {
        return u;
    }
    
    public void setU(int value) {
        this.u = value & Constants.MASK_16BIT;
    }
    
    public int getS() {
        return s;
    }
    
    public void setS(int value) {
        this.s = value & Constants.MASK_16BIT;
    }
    
    public int getPC() {
        return pc;
    }
    
    public void setPC(int value) {
        this.pc = value & Constants.MASK_16BIT;
    }
    
    
    // Registre D (combinaison de A et B)
    
    public int getD() {
        return BitOperations.makeWord(a, b);
    }
    
    public void setD(int value) {
        value &= Constants.MASK_16BIT;
        this.a = BitOperations.getHighByte(value);
        this.b = BitOperations.getLowByte(value);
    }
    
    // Méthodes utilitaires
    
    /**
     * Incrémente PC
     */
    public void incrementPC() {
        pc = (pc + 1) & Constants.MASK_16BIT;
    }
    
    /**
     * Incrémente PC de n
     */
    public void incrementPC(int n) {
        pc = (pc + n) & Constants.MASK_16BIT;
    }
    
    /**
     * Obtient un registre par son nom
     */
    public int getRegisterByName(String name) {
        switch (name.toUpperCase()) {
            case "A": return getA();
            case "B": return getB();
            case "D": return getD();
            case "X": return getX();
            case "Y": return getY();
            case "U": return getU();
            case "S": return getS();
            case "PC": return getPC();
            case "DP": return getDP();
            default: throw new IllegalArgumentException("Registre inconnu: " + name);
        }
    }
    
    /**
     * Définit un registre par son nom
     */
    public void setRegisterByName(String name, int value) {
        switch (name.toUpperCase()) {
            case "A": setA(value); break;
            case "B": setB(value); break;
            case "D": setD(value); break;
            case "X": setX(value); break;
            case "Y": setY(value); break;
            case "U": setU(value); break;
            case "S": setS(value); break;
            case "PC": setPC(value); break;
            case "DP": setDP(value); break;
            default: throw new IllegalArgumentException("Registre inconnu: " + name);
        }
    }
    
    @Override
    public String toString() {
        return String.format(
            "A=%02X B=%02X D=%04X X=%04X Y=%04X U=%04X S=%04X PC=%04X DP=%02X",
            a, b, getD(), x, y, u, s, pc, dp
        );
    }
}