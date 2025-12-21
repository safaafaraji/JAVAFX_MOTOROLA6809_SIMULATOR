package motorola6809.assembler;

public class LabelResolver {
    
    private SymbolTable symbolTable;
    
    public LabelResolver(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    /**
     * Résout une référence à une étiquette
     */
    public int resolve(String reference, int currentAddress) {
        // Retire les espaces
        reference = reference.trim();
        
        // Vérifie si c'est une constante
        if (symbolTable.hasConstant(reference)) {
            return symbolTable.getConstant(reference);
        }
        
        // Vérifie si c'est une étiquette
        if (symbolTable.hasLabel(reference)) {
            return symbolTable.getAddress(reference);
        }
        
        throw new AssemblerException("Label non défini: " + reference);
    }
    
    /**
     * Calcule l'offset relatif pour les branches
     */
    public int calculateRelativeOffset(String label, int currentAddress) {
        int targetAddress = resolve(label, currentAddress);
        int offset = targetAddress - currentAddress;
        
        // Vérifie si l'offset tient sur 8 bits (-128 à +127)
        if (offset < -128 || offset > 127) {
            throw new AssemblerException(
                "Offset de branchement trop grand: " + offset + 
                " (doit être entre -128 et +127)"
            );
        }
        
        return offset & 0xFF;
    }
    
    /**
     * Vérifie si une référence peut être résolue
     */
    public boolean canResolve(String reference) {
        return symbolTable.hasLabel(reference) || symbolTable.hasConstant(reference);
    }
}