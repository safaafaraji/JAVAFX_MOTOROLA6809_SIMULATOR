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
                }
                continue;
            }
            
            // END
            if ("END".equals(line.mnemonic)) {
                continue;
            }
            
            // Ajouter l'étiquette
            if (line.label != null) {
                symbolTable.addLabel(line.label, currentAddress);
            }
            
            // Calculer la taille
            currentAddress += getInstructionSize(line);
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
    
    private byte[] generateInstruction(Parser.ParsedLine line) {
        String mode = Parser.getAddressingMode(line.mnemonic, line.operand);
        int opcode = OpcodeGenerator.getOpcode(line.mnemonic, mode);
        
        List<Byte> bytes = new ArrayList<>();
        bytes.add((byte) opcode);
        
        // Opérande
        if (line.operand != null && !line.operand.isEmpty()) {
            byte[] operandBytes = encodeOperand(line.operand, mode, line.mnemonic);
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
    }
    
    private byte[] encodeOperand(String operand, String mode, String mnemonic) {
        operand = operand.trim();
        
        // Mode immédiat
        if ("IMMEDIATE".equals(mode)) {
            String value = operand.substring(1); // Retire #
            int val = parseValue(value);
            
            if (is16BitImmediate(mnemonic)) {
                return new byte[] { 
                    (byte) (val >> 8), 
                    (byte) val 
                };
            } else {
                return new byte[] { (byte) val };
            }
        }
        
        // Mode direct
        if ("DIRECT".equals(mode)) {
            int val = parseValue(operand);
            return new byte[] { (byte) val };
        }
        
        // Mode étendu
        if ("EXTENDED".equals(mode)) {
            int val = parseValue(operand);
            return new byte[] { 
                (byte) (val >> 8), 
                (byte) val 
            };
        }
        
        return new byte[0];
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