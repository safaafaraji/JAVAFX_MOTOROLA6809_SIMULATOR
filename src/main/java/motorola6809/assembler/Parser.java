package motorola6809.assembler;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    
    public static class ParsedLine {
        public String label;
        public String mnemonic;
        public String operand;
        public String comment;
        public int lineNumber;
        public boolean isEmpty;
        
        public ParsedLine(int lineNumber) {
            this.lineNumber = lineNumber;
            this.isEmpty = false;
        }
    }
    
    /**
     * Parse une ligne de code assembleur
     */
    public static ParsedLine parseLine(String line, int lineNumber) {
        ParsedLine result = new ParsedLine(lineNumber);
        line = line.trim();
        
        if (line.isEmpty()) {
            result.isEmpty = true;
            return result;
        }
        
        if (line.startsWith(";")) {
            result.comment = line.substring(1).trim();
            result.isEmpty = true;
            return result;
        }
        
        int commentPos = line.indexOf(';');
        if (commentPos >= 0) {
            result.comment = line.substring(commentPos + 1).trim();
            line = line.substring(0, commentPos).trim();
        }
        
        if (line.contains(":")) {
            int colonPos = line.indexOf(':');
            result.label = line.substring(0, colonPos).trim();
            line = line.substring(colonPos + 1).trim();
            
            if (line.isEmpty()) {
                result.isEmpty = true;
                return result;
            }
        }
        
        String[] parts = line.split("\\s+", 2);
        result.mnemonic = parts[0].toUpperCase();
        
        if (parts.length > 1) {
            result.operand = parts[1].trim();
        }
        
        return result;
    }
    
    /**
     * Détermine le mode d'adressage (version améliorée avec mnémonique)
     */
    public static String getAddressingMode(String mnemonic, String operand) {
        if (operand == null || operand.isEmpty()) {
            return "INHERENT";
        }
        
        operand = operand.trim();
        
        // PSHx/PULx/TFR/EXG sont IMMEDIATE (avec postbyte de registres)
        String[] immediateWithRegs = {"PSHS", "PSHU", "PULS", "PULU", "TFR", "EXG"};
        
        for (String instr : immediateWithRegs) {
            if (instr.equals(mnemonic)) {
                return "IMMEDIATE";
            }
        }
        
    
        
        // Détection simple
        if (operand.startsWith("#")) {
            return "IMMEDIATE";
        }
        
        if (operand.contains(",") || operand.startsWith("[") || 
            operand.contains("++") || operand.contains("--")) {
            return "INDEXED";
        }
        
        if (operand.startsWith("$")) {
            String hex = operand.substring(1);
            if (hex.length() <= 2) {
                return "DIRECT";
            } else {
                return "EXTENDED";
            }
        }
        
        // Si c'est un nombre décimal
        try {
            Integer.parseInt(operand);
            // Pour les nombres, c'est EXTENDED (toujours)
            return "EXTENDED";
        } catch (NumberFormatException e) {
            // Pas un nombre = étiquette = EXTENDED
            return "EXTENDED";
        }
    }
    
    /**
     * Version simplifiée sans mnémonique (pour compatibilité)
     */
    public static String getAddressingMode(String operand) {
        return getAddressingMode("", operand);
    }
    
    /**
     * Parse un programme complet
     */
    public static List<ParsedLine> parseProgram(String program) {
        List<ParsedLine> lines = new ArrayList<>();
        String[] rawLines = program.split("\n");
        
        for (int i = 0; i < rawLines.length; i++) {
            ParsedLine parsed = parseLine(rawLines[i], i + 1);
            lines.add(parsed);
        }
        
        return lines;
    }
}