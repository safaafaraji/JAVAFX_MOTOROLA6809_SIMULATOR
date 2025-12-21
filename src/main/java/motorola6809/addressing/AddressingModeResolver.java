package motorola6809.addressing;

import motorola6809.addressing.modes.*;

public class AddressingModeResolver {
    
    /**
     * Détermine le mode d'adressage selon la syntaxe de l'opérande
     */
    public static AddressingMode resolve(String operand, int instrSize) {
        if (operand == null || operand.isEmpty()) {
            return new InherentMode();
        }
        
        operand = operand.trim();
        
        // Mode IMMÉDIAT: #$XX ou #$XXXX
        if (operand.startsWith("#")) {
            int size = (operand.length() <= 4) ? 1 : 2; // #$XX = 1, #$XXXX = 2
            return new ImmediateMode(operand, size);
        }
        
        // Mode INDEXÉ: contient une virgule
        if (operand.contains(",") || operand.startsWith("[")) {
            return new IndexedMode(operand);
        }
        
        // Mode DIRECT ou ÉTENDU: commence par $
        if (operand.startsWith("$")) {
            String hex = operand.substring(1);
            if (hex.length() <= 2) {
                // $XX = Mode Direct
                return new DirectMode(operand);
            } else {
                // $XXXX = Mode Étendu
                return new ExtendedMode(operand);
            }
        }
        
        // Par défaut, essaie de déterminer par la longueur
        if (operand.length() <= 2) {
            return new DirectMode(operand);
        } else {
            return new ExtendedMode(operand);
        }
    }
    
    /**
     * Crée un mode relatif pour les branchements
     */
    public static AddressingMode createRelative(boolean longBranch) {
        return new RelativeMode("", longBranch);
    }
}