package motorola6809.assembler;

import motorola6809.utils.HexConverter;
import java.util.ArrayList;
import java.util.List;

public class Assembler {
    
    private SymbolTable symbolTable;
    private int currentAddress;
    private int originAddress;
    
    public Assembler() {
        this.symbolTable = new SymbolTable();
        this.currentAddress = 0;
        this.originAddress = 0;
    }
    
    public byte[] assemble(String program) {
        symbolTable.clear();
        List<Parser.ParsedLine> lines = Parser.parseProgram(program);
        
        // Première passe : adresses et symboles
        firstPass(lines);
        
        // Deuxième passe : génération du code
        return secondPass(lines);
    }
    
    private void firstPass(List<Parser.ParsedLine> lines) {
        currentAddress = originAddress;
        
        for (Parser.ParsedLine line : lines) {
            if (line.isEmpty) continue;
            
            System.out.println("First pass: " + line); // DEBUG
            
            // Gérer les étiquettes
            if (line.label != null && !line.label.isEmpty()) {
                symbolTable.addLabel(line.label, currentAddress);
                System.out.println("  Label added: " + line.label + " = $" + 
                                 String.format("%04X", currentAddress)); // DEBUG
            }
            
            // ORG
            if ("ORG".equals(line.mnemonic)) {
                currentAddress = parseValue(line.operand);
                originAddress = currentAddress;
                continue;
            }
            
            // EQU
            if ("EQU".equals(line.mnemonic)) {
                if (line.label != null) {
                    int value = parseValue(line.operand);
                    symbolTable.addConstant(line.label, value);
                    System.out.println("  Constant added: " + line.label + " = $" + 
                                     String.format("%02X", value)); // DEBUG
                }
                continue;
            }
            
            // END
            if ("END".equals(line.mnemonic)) {
                continue;
            }
            
            // Passer les lignes avec seulement des étiquettes
            if (line.isLabelOnly) {
                continue;
            }
            
            // Si c'est une directive (FCB, FDB, etc.)
            if (Parser.isDirective(line.mnemonic)) {
                // Ces directives consomment de l'espace mémoire
                int size = calculateDirectiveSize(line);
                currentAddress += size;
                continue;
            }
            
            // Calculer la taille de l'instruction
            int size = getInstructionSize(line);
            currentAddress += size;
            
            System.out.println("  Instruction size: " + size + " bytes"); // DEBUG
        }
        
        System.out.println("First pass complete. Final address: $" + 
                         String.format("%04X", currentAddress)); // DEBUG
    }
    
    private int calculateDirectiveSize(Parser.ParsedLine line) {
        if (line.mnemonic == null) return 0;
        
        switch (line.mnemonic.toUpperCase()) {
            case "FCB":
                if (line.operand != null) {
                    String[] values = line.operand.split(",");
                    return values.length;
                }
                return 1;
            case "FDB":
                if (line.operand != null) {
                    String[] values = line.operand.split(",");
                    return values.length * 2;
                }
                return 2;
            case "FCC":
                if (line.operand != null && line.operand.startsWith("\"")) {
                    String str = line.operand.substring(1, line.operand.length() - 1);
                    return str.length();
                }
                return 0;
            case "RMB":
            case "BSZ":
            case "ZMB":
                if (line.operand != null) {
                    return parseValue(line.operand);
                }
                return 0;
            default:
                return 0;
        }
    }

    
    
    private byte[] secondPass(List<Parser.ParsedLine> lines) {
        List<Byte> machineCode = new ArrayList<>();
        currentAddress = originAddress;
        
        for (Parser.ParsedLine line : lines) {
            if (line.isEmpty) continue;
            
            // Directives
            if (isDirective(line.mnemonic)) {
                processDirective(line, machineCode);
                continue;
            }
            
            if ("EQU".equals(line.mnemonic) || "END".equals(line.mnemonic)) {
                continue;
            }
            
            // Générer l'instruction
            byte[] instruction = generateInstruction(line);
            for (byte b : instruction) {
                machineCode.add(b);
            }
            currentAddress += instruction.length;
        }
        
        // Conversion
        byte[] result = new byte[machineCode.size()];
        for (int i = 0; i < machineCode.size(); i++) {
            result[i] = machineCode.get(i);
        }
        return result;
    }
    
 // Dans Assembler.java, modifiez generateInstruction :

    private byte[] generateInstruction(Parser.ParsedLine line) {
        // Vérifier si c'est une directive
        if (isDirective(line.mnemonic)) {
            return new byte[0];
        }
        
        // Vérifier si c'est une instruction valide
        if (!OpcodeGenerator.hasInstruction(line.mnemonic)) {
            throw new AssemblerException("Instruction inconnue: " + line.mnemonic, line.lineNumber);
        }
        
        String mode = Parser.getAddressingMode(line.mnemonic, line.operand);
        
        try {
            int opcode = OpcodeGenerator.getOpcode(line.mnemonic, mode);
            
            List<Byte> bytes = new ArrayList<>();
            
            // Gérer les opcodes 16-bit (préfixés)
            if (opcode > 0xFF) {
                bytes.add((byte) ((opcode >> 8) & 0xFF)); // Préfixe
                bytes.add((byte) (opcode & 0xFF));        // Opcode principal
            } else {
                bytes.add((byte) opcode);
            }
            
            // Opérande
            if (line.operand != null && !line.operand.isEmpty()) {
                byte[] operandBytes = encodeOperand(line.operand, mode, line.mnemonic, line.lineNumber);
                for (byte b : operandBytes) {
                    bytes.add(b);
                }
            }
            
            // Conversion
            byte[] result = new byte[bytes.size()];
            for (int i = 0; i < bytes.size(); i++) {
                result[i] = bytes.get(i);
            }
            return result;
            
        } catch (RuntimeException e) {
            throw new AssemblerException(
                "Erreur avec " + line.mnemonic + ": " + e.getMessage(), 
                line.lineNumber
            );
        }
    }

    private byte[] encodeOperand(String operand, String mode, String mnemonic, int lineNumber) {
        operand = operand.trim();
        
        // Mode immédiat
        if ("IMMEDIATE".equals(mode)) {
            if (!operand.startsWith("#")) {
                throw new AssemblerException("Mode immédiat nécessite #", lineNumber);
            }
            String value = operand.substring(1);
            int val = parseValue(value);
            
            if (is16BitImmediate(mnemonic)) {
                return new byte[] { 
                    (byte) (val >> 8), 
                    (byte) (val & 0xFF)
                };
            } else {
                return new byte[] { (byte) (val & 0xFF) };
            }
        }
        
        // Mode direct
        if ("DIRECT".equals(mode)) {
            int val = parseValue(operand);
            if (val < 0 || val > 0xFF) {
                throw new AssemblerException("Valeur direct doit être 0-255", lineNumber);
            }
            return new byte[] { (byte) val };
        }
        
        // Mode étendu
        if ("EXTENDED".equals(mode)) {
            int val = parseValue(operand);
            if (val < 0 || val > 0xFFFF) {
                throw new AssemblerException("Adresse étendue doit être 0-65535", lineNumber);
            }
            return new byte[] { 
                (byte) ((val >> 8) & 0xFF), 
                (byte) (val & 0xFF)
            };
        }
        
        // Mode inhérent - pas d'opérande
        if ("INHERENT".equals(mode)) {
            return new byte[0];
        }
        
        // Mode indexé (simplifié)
        if ("INDEXED".equals(mode)) {
            // Pour l'instant, simplifions avec ,X seulement
            if (operand.equals(",X")) {
                return new byte[] { (byte) 0x84 };
            }
            throw new AssemblerException("Mode indexé non supporté: " + operand, lineNumber);
        }
        
        throw new AssemblerException("Mode d'adressage non supporté: " + mode, lineNumber);
    }
    
    private int parseValue(String value) {
        value = value.trim();
        
        // Étiquette
        if (symbolTable.hasLabel(value)) {
            return symbolTable.getAddress(value);
        }
        
        // Constante
        if (symbolTable.hasConstant(value)) {
            return symbolTable.getConstant(value);
        }
        
        // Hexa
        if (value.startsWith("$")) {
            return HexConverter.hexToInt(value);
        }
        
        // Binaire
        if (value.startsWith("%")) {
            return Integer.parseInt(value.substring(1), 2);
        }
        
        // Décimal
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new AssemblerException("Valeur invalide: " + value);
        }
    }
    
    private int getInstructionSize(Parser.ParsedLine line) {
        if ("EQU".equals(line.mnemonic) || "END".equals(line.mnemonic)) {
            return 0;
        }
        
        String mode = Parser.getAddressingMode(line.operand);
        int size = 1; // Opcode
        
        switch (mode) {
            case "IMMEDIATE":
                size += is16BitImmediate(line.mnemonic) ? 2 : 1;
                break;
            case "DIRECT":
                size += 1;
                break;
            case "EXTENDED":
                size += 2;
                break;
            case "INDEXED":
                size += 1; // Postbyte minimum
                break;
        }
        
        return size;
    }
    
    private boolean is16BitImmediate(String mnemonic) {
        return mnemonic.matches("LDD|LDX|LDY|LDS|LDU|CMPD|CMPX|CMPY|CMPS|CMPU|ADDD|SUBD");
    }
    
    private boolean isDirective(String mnemonic) {
        String[] directives = {"ORG", "EQU", "END", "RMB", "FCB", "FDB", "FCC"};
        for (String d : directives) {
            if (d.equalsIgnoreCase(mnemonic)) {
                return true;
            }
        }
        return false;
    }
    
    private void processDirective(Parser.ParsedLine line, List<Byte> machineCode) {
        switch (line.mnemonic.toUpperCase()) {
            case "ORG":
                currentAddress = parseValue(line.operand);
                originAddress = currentAddress;
                break;
                
            case "RMB":
                int rmbBytes = parseValue(line.operand);
                for (int i = 0; i < rmbBytes; i++) {
                    machineCode.add((byte) 0x00);
                }
                currentAddress += rmbBytes;
                break;
                
            case "FCB":
                String[] fcbValues = line.operand.split(",");
                for (String val : fcbValues) {
                    int byteValue = parseValue(val.trim());
                    machineCode.add((byte) byteValue);
                }
                currentAddress += fcbValues.length;
                break;
                
            case "FDB":
                String[] fdbValues = line.operand.split(",");
                for (String val : fdbValues) {
                    int wordValue = parseValue(val.trim());
                    machineCode.add((byte) (wordValue >> 8));
                    machineCode.add((byte) wordValue);
                }
                currentAddress += fdbValues.length * 2;
                break;
                
            case "FCC":
                if (line.operand.startsWith("\"") && line.operand.endsWith("\"")) {
                    String str = line.operand.substring(1, line.operand.length() - 1);
                    for (char c : str.toCharArray()) {
                        machineCode.add((byte) c);
                    }
                    currentAddress += str.length();
                }
                break;
        }
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}