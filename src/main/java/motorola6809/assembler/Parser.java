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
        public boolean isLabelOnly;
        public boolean isDirective;
        
        public ParsedLine(int lineNumber) {
            this.lineNumber = lineNumber;
            this.isEmpty = false;
            this.isLabelOnly = false;
            this.isDirective = false;
        }
        
        @Override
        public String toString() {
            return String.format("Line %d: label='%s', mnemonic='%s', operand='%s', isLabelOnly=%b, isDirective=%b",
                lineNumber, label, mnemonic, operand, isLabelOnly, isDirective);
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
        
        // Nettoyer les espaces multiples
        line = line.replaceAll("\\s+", " ").trim();
        
        // Séparer en mots
        String[] parts = line.split(" ");
        if (parts.length == 0) {
            result.isEmpty = true;
            return result;
        }
        
        // Détecter d'abord les directives simples (ORG, END, etc.)
        String firstWord = parts[0].toUpperCase();
        
        // Cas 1: C'est une directive
        if (isDirective(firstWord)) {
            result.mnemonic = firstWord;
            result.isDirective = true;
            
            if (parts.length > 1) {
                result.operand = joinParts(parts, 1);
            }
            return result;
        }
        
        // Cas 2: C'est une étiquette avec deux-points
        if (firstWord.endsWith(":")) {
            result.label = firstWord.substring(0, firstWord.length() - 1);
            
            if (parts.length == 1) {
                // Seulement une étiquette
                result.isLabelOnly = true;
                result.isEmpty = true;
                return result;
            }
            
            // Il y a quelque chose après l'étiquette
            String secondWord = parts[1].toUpperCase();
            
            // Vérifier si c'est une directive
            if (isDirective(secondWord)) {
                result.mnemonic = secondWord;
                result.isDirective = true;
                
                if (parts.length > 2) {
                    result.operand = joinParts(parts, 2);
                }
            } else {
                // C'est une instruction
                result.mnemonic = secondWord;
                
                if (parts.length > 2) {
                    result.operand = joinParts(parts, 2);
                }
            }
            return result;
        }
        
        // Cas 3: Étiquette sans deux-points (commence la ligne)
        // Vérifier si le premier mot n'est PAS une instruction valide
        if (!isValidInstruction(firstWord) && !isDirective(firstWord)) {
            // Alors c'est une étiquette
            result.label = firstWord;
            
            if (parts.length == 1) {
                // Seulement une étiquette
                result.isLabelOnly = true;
                result.isEmpty = true;
                return result;
            }
            
            String secondWord = parts[1].toUpperCase();
            
            // Vérifier si c'est une directive
            if (isDirective(secondWord)) {
                result.mnemonic = secondWord;
                result.isDirective = true;
                
                if (parts.length > 2) {
                    result.operand = joinParts(parts, 2);
                }
            } else {
                // C'est une instruction
                result.mnemonic = secondWord;
                
                if (parts.length > 2) {
                    result.operand = joinParts(parts, 2);
                }
            }
            return result;
        }
        
        // Cas 4: C'est une instruction normale
        result.mnemonic = firstWord;
        
        if (parts.length > 1) {
            result.operand = joinParts(parts, 1);
        }
        
        return result;
    }
    
    /**
     * Rejoindre les parties d'un tableau
     */
    private static String joinParts(String[] parts, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < parts.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
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
     * Détermine le mode d'adressage
     */
    public static String getAddressingMode(String mnemonic, String operand) {
        if (operand == null || operand.isEmpty()) {
            return "INHERENT";
        }
        
        operand = operand.trim();
        
        // Instructions spéciales
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
        
        // Mode direct ou étendu
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
        
        // Étiquette = mode étendu
        return "EXTENDED";
    }
    
    /**
     * Version simplifiée
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
                e.printStackTrace();
            }
        }
        
        return lines;
    }
    
    /**
     * Affiche les lignes parsées pour débogage
     */
    public static void printParsedLines(List<ParsedLine> lines) {
        System.out.println("\n=== PARSER DEBUG ===");
        for (ParsedLine line : lines) {
            if (!line.isEmpty) {
                System.out.println(line);
            } else if (line.comment != null && !line.comment.isEmpty()) {
                System.out.println("Line " + line.lineNumber + ": COMMENT: " + line.comment);
            }
        }
        System.out.println("====================\n");
    }
    
    /**
     * Test du parser
     */
    public static void main(String[] args) {
        String testCode = 
            "        ORG $1000\n" +
            "START   LDA #$05     ; Étiquette sans deux-points\n" +
            "        LDB #$03\n" +
            "        MUL\n" +
            "        STA $2000\n" +
            "        SWI\n" +
            "        END";
        
        List<ParsedLine> lines = parseProgram(testCode);
        printParsedLines(lines);
        
        // Tester aussi avec deux-points
        String testCode2 = 
            "        ORG $1000\n" +
            "START:  LDA #$05     ; Étiquette avec deux-points\n" +
            "        END";
        
        System.out.println("\n=== TEST AVEC DEUX-POINTS ===");
        List<ParsedLine> lines2 = parseProgram(testCode2);
        printParsedLines(lines2);
    }
}