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
        public boolean isLabelOnly;  // Indique si la ligne contient seulement une étiquette
        
        public ParsedLine(int lineNumber) {
            this.lineNumber = lineNumber;
            this.isEmpty = false;
            this.isLabelOnly = false;
        }
        
        @Override
        public String toString() {
            return String.format("Line %d: label='%s', mnemonic='%s', operand='%s', isLabelOnly=%b",
                lineNumber, label, mnemonic, operand, isLabelOnly);
        }
    }
    
    /**
     * Parse une ligne de code assembleur
     */
    public static ParsedLine parseLine(String line, int lineNumber) {
        ParsedLine result = new ParsedLine(lineNumber);
        line = line.trim();
        
        // Ligne vide
        if (line.isEmpty()) {
            result.isEmpty = true;
            return result;
        }
        
        // Commentaire complet
        if (line.startsWith(";")) {
            result.comment = line.substring(1).trim();
            result.isEmpty = true;
            return result;
        }
        
        // Extraire le commentaire si présent
        int commentPos = line.indexOf(';');
        if (commentPos >= 0) {
            result.comment = line.substring(commentPos + 1).trim();
            line = line.substring(0, commentPos).trim();
        }
        
        // Séparer en mots
        String[] parts = line.split("\\s+");
        if (parts.length == 0) {
            result.isEmpty = true;
            return result;
        }
        
        // Détecter les directives EQU en premier
        for (int i = 0; i < parts.length; i++) {
            if ("EQU".equalsIgnoreCase(parts[i])) {
                // C'est une directive EQU
                result.mnemonic = "EQU";
                if (i > 0) {
                    result.label = parts[i - 1];
                }
                if (i + 1 < parts.length) {
                    // Rassembler le reste comme opérande
                    StringBuilder operand = new StringBuilder();
                    for (int j = i + 1; j < parts.length; j++) {
                        operand.append(parts[j]);
                        if (j < parts.length - 1) {
                            operand.append(" ");
                        }
                    }
                    result.operand = operand.toString();
                }
                return result;
            }
        }
        
        // Vérifier si c'est une étiquette (avec :)
        if (parts[0].endsWith(":")) {
            result.label = parts[0].substring(0, parts[0].length() - 1);
            
            if (parts.length == 1) {
                // Seulement une étiquette
                result.isLabelOnly = true;
                return result;
            }
            
            // Il y a quelque chose après l'étiquette
            result.mnemonic = parts[1].toUpperCase();
            
            if (parts.length > 2) {
                // Rassembler le reste comme opérande
                StringBuilder operand = new StringBuilder();
                for (int i = 2; i < parts.length; i++) {
                    operand.append(parts[i]);
                    if (i < parts.length - 1) {
                        operand.append(" ");
                    }
                }
                result.operand = operand.toString();
            }
            return result;
        }
        
        // Vérifier si c'est une directive
        String firstWord = parts[0].toUpperCase();
        if (isDirective(firstWord)) {
            result.mnemonic = firstWord;
            
            if (parts.length > 1) {
                StringBuilder operand = new StringBuilder();
                for (int i = 1; i < parts.length; i++) {
                    operand.append(parts[i]);
                    if (i < parts.length - 1) {
                        operand.append(" ");
                    }
                }
                result.operand = operand.toString();
            }
            return result;
        }
        
        // Si c'est une instruction
        result.mnemonic = firstWord;
        
        if (parts.length > 1) {
            StringBuilder operand = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                operand.append(parts[i]);
                if (i < parts.length - 1) {
                    operand.append(" ");
                }
            }
            result.operand = operand.toString();
        }
        
        return result;
    }
    
    /**
     * Vérifie si c'est une directive d'assembleur
     */
    public static boolean isDirective(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        
        String[] directives = {
            "ORG", "EQU", "END", "RMB", "FCB", "FDB", "FCC",
            "BSZ", "ZMB", "SETDP", "PAGE", "TTL", "NAM", "OPT"
        };
        
        for (String d : directives) {
            if (d.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie si c'est une instruction valide
     */
    public static boolean isValidInstruction(String mnemonic) {
        if (mnemonic == null || mnemonic.isEmpty()) {
            return false;
        }
        
        // Vérifie d'abord si c'est une directive
        if (isDirective(mnemonic)) {
            return false;
        }
        
        // Vérifie avec OpcodeGenerator
        try {
            return OpcodeGenerator.hasInstruction(mnemonic);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Détermine le mode d'adressage (version améliorée avec mnémonique)
     */
    public static String getAddressingMode(String mnemonic, String operand) {
        if (operand == null || operand.isEmpty()) {
            return "INHERENT";
        }
        
        operand = operand.trim();
        
        // Instructions spéciales avec postbyte
        String[] immediateWithRegs = {"PSHS", "PSHU", "PULS", "PULU", "TFR", "EXG"};
        for (String instr : immediateWithRegs) {
            if (instr.equals(mnemonic)) {
                return "IMMEDIATE";
            }
        }
        
        // Mode immédiat
        if (operand.startsWith("#")) {
            return "IMMEDIATE";
        }
        
        // Mode indexé
        if (operand.contains(",") || operand.startsWith("[") || 
            operand.contains("++") || operand.contains("--")) {
            return "INDEXED";
        }
        
        // Mode direct ou étendu (adresses)
        if (operand.startsWith("$")) {
            String hex = operand.substring(1);
            if (hex.length() <= 2) {
                return "DIRECT";
            } else {
                return "EXTENDED";
            }
        }
        
        // Si c'est un nombre
        try {
            // Essayer de parser comme nombre
            if (operand.matches("[0-9]+")) {
                int value = Integer.parseInt(operand);
                if (value <= 0xFF) {
                    return "DIRECT";
                } else {
                    return "EXTENDED";
                }
            }
        } catch (NumberFormatException e) {
            // Pas un nombre
        }
        
        // Sinon, c'est probablement une étiquette = mode étendu
        return "EXTENDED";
    }
    
    /**
     * Version simplifiée sans mnémonique
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
            try {
                ParsedLine parsed = parseLine(rawLines[i], i + 1);
                lines.add(parsed);
            } catch (Exception e) {
                System.err.println("Erreur de parsing ligne " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return lines;
    }
    
    /**
     * Test du parser
     */
    public static void main(String[] args) {
        String testCode = 
            "MAXVAL EQU $FF\n" +
            "        ORG $1400\n" +
            "START: LDA #$05\n" +
            "       STA $1000\n" +
            "LOOP   DECA\n" +
            "       BNE LOOP\n" +
            "       END";
        
        List<ParsedLine> lines = parseProgram(testCode);
        
        for (ParsedLine line : lines) {
            if (!line.isEmpty) {
                System.out.println(line);
            }
        }
    }
}