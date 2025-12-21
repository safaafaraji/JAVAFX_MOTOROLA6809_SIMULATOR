package motorola6809.assembler;

import motorola6809.utils.HexConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assembler {
    
    private SymbolTable symbolTable;
    private LabelResolver labelResolver;
    private int currentAddress;
    private int originAddress;
    private boolean processingDirective;
    
    public Assembler() {
        this.symbolTable = new SymbolTable();
        this.labelResolver = new LabelResolver(symbolTable);
        this.currentAddress = 0;
        this.originAddress = 0;
        this.processingDirective = false;
    }
    
    /**
     * Assemble un programme complet
     */
    public byte[] assemble(String program) {
        symbolTable.clear();
        
        // Parse le programme
        List<Parser.ParsedLine> lines = Parser.parseProgram(program);
        
        // PREMIÈRE PASSE: Construit la table des symboles
        firstPass(lines);
        
        // DEUXIÈME PASSE: Génère le code machine
        return secondPass(lines);
    }
    
    /**
     * PREMIÈRE PASSE: Identifie les étiquettes et calcule les adresses
     */
    private void firstPass(List<Parser.ParsedLine> lines) {
        currentAddress = originAddress;
        
        for (Parser.ParsedLine line : lines) {
            if (line.isEmpty) {
                continue;
            }
            
            // DEBUG
            System.out.printf("[firstPass] Ligne %d: ", line.lineNumber);
            System.out.printf("label='%s', mnemonic='%s', operand='%s'\n",
                line.label, line.mnemonic, line.operand);
            
            // ORG
            if ("ORG".equals(line.mnemonic)) {
                currentAddress = parseValue(line.operand);
                originAddress = currentAddress;
                System.out.printf("  -> ORG à $%04X\n", currentAddress);
                continue;
            }
            
            // EQU
            if ("EQU".equals(line.mnemonic)) {
                if (line.label != null) {
                    int value = parseValue(line.operand);
                    System.out.printf("  -> EQU: %s = $%04X\n", line.label, value);
                    symbolTable.addConstant(line.label, value);
                }
                continue;
            }
            
            // END
            if ("END".equals(line.mnemonic)) {
                continue;
            }
            
            // AJOUTER L'ÉTIQUETTE (IMPORTANT : AVANT getInstructionSize !)
            if (line.label != null) {
                System.out.printf("  -> AJOUT LABEL '%s' à $%04X\n", 
                    line.label, currentAddress);
                symbolTable.addLabel(line.label, currentAddress);
            }
            
            // Calculer la taille
            int size = getInstructionSize(line);
            System.out.printf("  -> Taille: %d octets\n", size);
            currentAddress += size;
        }
    }
    
    /**
     * DEUXIÈME PASSE: Génère le code machine
     */
    private byte[] secondPass(List<Parser.ParsedLine> lines) {
        List<Byte> machineCode = new ArrayList<>();
        currentAddress = originAddress;
        
        for (Parser.ParsedLine line : lines) {
            if (line.isEmpty) {
                continue;
            }
            
            // Directives
            if (isDirective(line.mnemonic)) {
                processDirective(line, machineCode);
                continue;
            }
            
            if ("EQU".equals(line.mnemonic)) {
                // EQU est déjà traité dans firstPass, rien à générer
                continue;
            }
            
            if ("END".equals(line.mnemonic)) {
                break;
            }
            
            // Génère l'instruction
            try {
                byte[] instruction = generateInstruction(line);
                for (byte b : instruction) {
                    machineCode.add(b);
                }
                currentAddress += instruction.length;
            } catch (Exception e) {
                throw new AssemblerException(
                    e.getMessage() + " dans '" + line.mnemonic + "'",
                    line.lineNumber
                );
            }
        }
        
        // Convertit List<Byte> en byte[]
        byte[] result = new byte[machineCode.size()];
        for (int i = 0; i < machineCode.size(); i++) {
            result[i] = machineCode.get(i);
        }
        
        return result;
    }
    
    /**
     * Génère le code machine pour une instruction
     */
    private byte[] generateInstruction(Parser.ParsedLine line) {
        // Utilisation simple
        String mode = Parser.getAddressingMode(line.mnemonic, line.operand);
        int opcode = OpcodeGenerator.getOpcode(line.mnemonic, mode);
        
        List<Byte> bytes = new ArrayList<>();
        bytes.add((byte) opcode);
        
        // Ajoute l'opérande si nécessaire
        if (line.operand != null && !line.operand.isEmpty()) {
            byte[] operandBytes = encodeOperand(line.operand, mode, line.mnemonic);
            for (byte b : operandBytes) {
                bytes.add(b);
            }
        }
        
        byte[] result = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i);
        }
        
        return result;
    }

    
    /**
     * Encode un opérande selon le mode d'adressage
     */
    private byte[] encodeOperand(String operand, String mode, String mnemonic) {
        operand = operand.trim();
        
        // Cas spécial pour PSHx/PULx/TFR/EXG
        if ("PSHS".equals(mnemonic) || "PSHU".equals(mnemonic) ||
            "PULS".equals(mnemonic) || "PULU".equals(mnemonic) ||
            "TFR".equals(mnemonic) || "EXG".equals(mnemonic)) {
            return encodeRegisterPostbyte(operand, mnemonic);
        }
        
        switch (mode) {
            case "IMMEDIATE":
                String value = operand.substring(1);
                int val = parseValue(value);
                
                if (is16BitImmediate(mnemonic)) {
                    return new byte[] { 
                        (byte) (val >> 8), 
                        (byte) val 
                    };
                } else {
                    return new byte[] { (byte) val };
                }
                
            case "DIRECT":
                int directVal = parseValue(operand);
                return new byte[] { (byte) directVal };
                
            case "EXTENDED":
                int extVal = parseValue(operand);
                return new byte[] { 
                    (byte) (extVal >> 8), 
                    (byte) extVal 
                };
                
            case "INDEXED":
                return encodeIndexedOperand(operand);
   
                
            default:
                return new byte[0];
        }
    }
    /**
     * Encode un postbyte de registres pour PSHx/PULx/TFR/EXG
     * Format: A,B ou X,Y ou S,U ou D,PC etc.
     */
    private byte[] encodeRegisterPostbyte(String operand, String mnemonic) {
        // Table de correspondance registre -> bit
        // PSHS/PULS: PC=80, U/S=40, Y=20, X=10, DP=08, B=04, A=02, CC=01
        // PSHU/PULU: PC=80, S=40, Y=20, X=10, DP=08, B=04, A=02, CC=01
        // TFR/EXG: voir encodage spécial
        
        if (operand == null || operand.isEmpty()) {
            return new byte[] { (byte) 0x00 }; // Aucun registre
        }
        
        // Pour TFR/EXG: format source,destination
        if ("TFR".equals(mnemonic) || "EXG".equals(mnemonic)) {
            return encodeTfrExgPostbyte(operand);
        }
        
        // Pour PSHx/PULx: liste de registres séparés par des virgules
        int postbyte = 0;
        String[] regs = operand.split(",");
        
        for (String reg : regs) {
            reg = reg.trim().toUpperCase();
            
            switch (reg) {
                case "CC": postbyte |= 0x01; break;
                case "A": postbyte |= 0x02; break;
                case "B": postbyte |= 0x04; break;
                case "DP": case "DPR": postbyte |= 0x08; break;
                case "X": postbyte |= 0x10; break;
                case "Y": postbyte |= 0x20; break;
                case "S": 
                    if ("PSHU".equals(mnemonic) || "PULU".equals(mnemonic)) {
                        postbyte |= 0x40; // S dans PSHU/PULU
                    }
                    break;
                case "U":
                    if ("PSHS".equals(mnemonic) || "PULS".equals(mnemonic)) {
                        postbyte |= 0x40; // U dans PSHS/PULS
                    }
                    break;
                case "PC": postbyte |= 0x80; break;
            }
        }
        
        return new byte[] { (byte) postbyte };
    }

    /**
     * Encode postbyte pour TFR/EXG
     * Format: source,destination (ex: A,B ou X,Y)
     */
    private byte[] encodeTfrExgPostbyte(String operand) {
        // Table TFR/EXG:
        // D=0, X=1, Y=2, U=3, S=4, PC=5, ?=6, ?=7
        // A=8, B=9, CC=10, DP=11, ?=12, ?=13, ?=14, ?=15
        
        java.util.Map<String, Integer> regCodes = new java.util.HashMap<>();
        regCodes.put("D", 0x0);
        regCodes.put("X", 0x1);
        regCodes.put("Y", 0x2);
        regCodes.put("U", 0x3);
        regCodes.put("S", 0x4);
        regCodes.put("PC", 0x5);
        regCodes.put("A", 0x8);
        regCodes.put("B", 0x9);
        regCodes.put("CC", 0xA);
        regCodes.put("DP", 0xB);
        
        String[] parts = operand.split(",");
        if (parts.length != 2) {
            throw new AssemblerException("Format TFR/EXG invalide: " + operand);
        }
        
        String src = parts[0].trim().toUpperCase();
        String dst = parts[1].trim().toUpperCase();
        
        if (!regCodes.containsKey(src) || !regCodes.containsKey(dst)) {
            throw new AssemblerException("Registre invalide dans TFR/EXG: " + operand);
        }
        
        int postbyte = (regCodes.get(src) << 4) | regCodes.get(dst);
        return new byte[] { (byte) postbyte };
    }
    
    
    
    /**
     * Encode un opérande indexé
     */
    private byte[] encodeIndexedOperand(String operand) {
        // Format: offset,register ou ,register ou offset,register+
        operand = operand.trim();
        
        // Simplifié pour commencer - juste ,X
        if (operand.equals(",X") || operand.equals(",Y") || 
            operand.equals(",U") || operand.equals(",S")) {
            // Postbyte de base: 00rr0xxx
            // rr = register (X=00, Y=01, U=10, S=11)
            // xxx = mode (000 = no offset)
            char reg = operand.charAt(1); // X, Y, U, ou S
            int postbyte = 0;
            
            switch (reg) {
                case 'X': postbyte = 0x00; break; // 00000
                case 'Y': postbyte = 0x20; break; // 00100
                case 'U': postbyte = 0x40; break; // 01000
                case 'S': postbyte = 0x60; break; // 01100
            }
            
            return new byte[] { (byte) postbyte };
        }
        
        // Pour offset simple (ex: 5,X)
        if (operand.contains(",")) {
            String[] parts = operand.split(",");
            if (parts.length == 2) {
                String offsetStr = parts[0].trim();
                String register = parts[1].trim();
                
                try {
                    int offset = parseValue(offsetStr);
                    
                    // Déterminer le postbyte selon la taille de l'offset
                    int postbyte = 0;
                    char reg = register.charAt(0);
                    
                    switch (reg) {
                        case 'X': postbyte = 0x80; break; // 5-bit offset
                        case 'Y': postbyte = 0xA0; break;
                        case 'U': postbyte = 0xC0; break;
                        case 'S': postbyte = 0xE0; break;
                    }
                    
                    // Pour un offset 5-bit signé (-16 à +15)
                    if (offset >= -16 && offset <= 15) {
                        int offset5bit = offset & 0x1F;
                        postbyte |= offset5bit;
                        return new byte[] { (byte) postbyte };
                    }
                    // Pour un offset 8-bit
                    else if (offset >= -128 && offset <= 127) {
                        postbyte = 0x88; // 8-bit offset, X
                        if (reg == 'Y') postbyte = 0xA8;
                        if (reg == 'U') postbyte = 0xC8;
                        if (reg == 'S') postbyte = 0xE8;
                        
                        return new byte[] { 
                            (byte) postbyte, 
                            (byte) offset 
                        };
                    }
                    // Pour un offset 16-bit
                    else {
                        postbyte = 0x89; // 16-bit offset, X
                        if (reg == 'Y') postbyte = 0xA9;
                        if (reg == 'U') postbyte = 0xC9;
                        if (reg == 'S') postbyte = 0xE9;
                        
                        return new byte[] { 
                            (byte) postbyte,
                            (byte) (offset >> 8),
                            (byte) offset
                        };
                    }
                } catch (Exception e) {
                    // Format non supporté pour l'instant
                    return new byte[] { (byte) 0x84 }; // ,X par défaut
                }
            }
        }
        
        // Par défaut, retourne ,X
        return new byte[] { (byte) 0x84 };
    }
    


    
    /**
     * Parse une valeur (hexa, décimal, ou étiquette)
     */
    private int parseValue(String value) {
        value = value.trim();
        
        // 1. Vérifier d'abord si c'est une étiquette (avant les nombres)
        if (symbolTable.hasLabel(value)) {
            return symbolTable.getAddress(value);
        }
        
        // 2. Vérifier si c'est une constante
        if (symbolTable.hasConstant(value)) {
            return symbolTable.getConstant(value);
        }
        
        // 3. Hexadécimal
        if (value.startsWith("$")) {
            return HexConverter.hexToInt(value);
        }
        
        // 4. Binaire
        if (value.startsWith("%")) {
            return Integer.parseInt(value.substring(1), 2);
        }
        
        // 5. Décimal
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // 6. Expression simple (ex: START+5)
            if (value.contains("+")) {
                String[] parts = value.split("\\+");
                int sum = 0;
                for (String part : parts) {
                    sum += parseValue(part.trim());
                }
                return sum;
            }
            
            if (value.contains("-")) {
                String[] parts = value.split("-");
                int result = parseValue(parts[0].trim());
                for (int i = 1; i < parts.length; i++) {
                    result -= parseValue(parts[i].trim());
                }
                return result;
            }
            
            // 7. Si c'est une instruction (comme PCR, etc.)
            if (value.equalsIgnoreCase("PCR")) {
                return currentAddress; // Program Counter Relative
            }
        }
        
        throw new AssemblerException("Valeur invalide: " + value);
    }
    
    /**
     * Calcule la taille d'une instruction en octets
     */
    private int getInstructionSize(Parser.ParsedLine line) {
        // TFR et EXG: opcode (1) + postbyte (1) = 2 octets
        if ("TFR".equals(line.mnemonic) || "EXG".equals(line.mnemonic)) {
            return 2;
        }       
        String mode = Parser.getAddressingMode(line.operand);
        
        // Taille de l'opcode
        int size = 1;
        
        // Ajoute la taille de l'opérande
        switch (mode) {
            case "INHERENT":
                size += 0;
                break;
                
            case "IMMEDIATE":
                if (is16BitImmediate(line.mnemonic)) {
                    size += 2;
                } else {
                    size += 1;
                }
                break;
                
            case "DIRECT":
                size += 1;
                break;
                
            case "EXTENDED":
                size += 2;
                break;
                
            case "INDEXED":
                // Taille variable selon le mode d'indexation
                size += getIndexedOperandSize(line.operand);
                break;
                
         
        }
        
        return size;
    }
    
    /**
     * Détermine si une instruction immédiate utilise 16 bits
     */
    private boolean is16BitImmediate(String mnemonic) {
        // Instructions qui utilisent des opérandes 16 bits en immédiat
        String[] sixteenBitInstructions = {
            "LDD", "LDX", "LDY", "LDS", "LDU", "CMPD", "CMPX", "CMPY",
            "CMPS", "CMPU", "ADDD", "SUBD", "ANDD", "ORD", "EORD"
        };
        
        for (String instr : sixteenBitInstructions) {
            if (instr.equals(mnemonic)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Calcule la taille d'un opérande indexé
     */
    private int getIndexedOperandSize(String operand) {
        if (operand == null || operand.isEmpty()) {
            return 1; // ,X par défaut
        }
        
        operand = operand.trim();
        
        // ,X ,Y ,U ,S
        if (operand.equals(",X") || operand.equals(",Y") || 
            operand.equals(",U") || operand.equals(",S")) {
            return 1;
        }
        
        // offset,register
        if (operand.contains(",")) {
            String[] parts = operand.split(",");
            if (parts.length == 2) {
                String offsetStr = parts[0].trim();
                try {
                    int offset = parseValue(offsetStr);
                    
                    if (offset >= -16 && offset <= 15) {
                        return 1; // 5-bit offset
                    } else if (offset >= -128 && offset <= 127) {
                        return 2; // 8-bit offset
                    } else {
                        return 3; // 16-bit offset
                    }
                } catch (Exception e) {
                    return 1; // Par défaut
                }
            }
        }
        
        return 1; // Par défaut
    }
    
    /**
     * Vérifie si une mnémonique est une directive
     */
    private boolean isDirective(String mnemonic) {
        String[] directives = {
            "ORG", "EQU", "END", "RMB", "FCB", "FDB", 
            "ZMB", "BSZ", "FCC", "SETDP", "PAGE", "NAM"
        };
        
        
        for (String directive : directives) {
            if (directive.equalsIgnoreCase(mnemonic)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Traite une directive assembleur
     */
    private void processDirective(Parser.ParsedLine line, List<Byte> machineCode) {
        switch (line.mnemonic.toUpperCase()) {
            case "ORG":
                currentAddress = parseValue(line.operand);
                originAddress = currentAddress;
                break;
                
            case "EQU":
                // Déjà traité dans firstPass
                break;
                
            case "END":
                // Arrête l'assemblage
                break;
                
            case "RMB":
                // Reserve Memory Bytes
                int rmbBytes = parseValue(line.operand);
                for (int i = 0; i < rmbBytes; i++) {
                    machineCode.add((byte) 0x00);
                }
                currentAddress += rmbBytes;
                break;
                
            case "FCB":
                // Form Constant Byte
                String[] fcbValues = line.operand.split(",");
                for (String val : fcbValues) {
                    int byteValue = parseValue(val.trim());
                    machineCode.add((byte) byteValue);
                }
                currentAddress += fcbValues.length;
                break;
                
            case "FDB":
                // Form Double Byte
                String[] fdbValues = line.operand.split(",");
                for (String val : fdbValues) {
                    int wordValue = parseValue(val.trim());
                    machineCode.add((byte) (wordValue >> 8));
                    machineCode.add((byte) wordValue);
                }
                currentAddress += fdbValues.length * 2;
                break;
                
            case "ZMB":
            case "BSZ":
                // Zero Memory Bytes / Block Storage Zero
                int zmbBytes = parseValue(line.operand);
                for (int i = 0; i < zmbBytes; i++) {
                    machineCode.add((byte) 0x00);
                }
                currentAddress += zmbBytes;
                break;
                
            case "FCC":
                // Form Constant Character (chaîne)
                if (line.operand.startsWith("\"") && line.operand.endsWith("\"")) {
                    String str = line.operand.substring(1, line.operand.length() - 1);
                    for (char c : str.toCharArray()) {
                        machineCode.add((byte) c);
                    }
                    currentAddress += str.length();
                }
                break;
                
            case "SETDP":
                // Set Direct Page (ignoré pour l'instant)
                break;
                
            case "PAGE":
                // Page (ignoré)
                break;
                
            case "NAM":
                // Name (ignoré)
                break;
        }
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
    
    public void reset() {
        symbolTable.clear();
        currentAddress = 0;
        originAddress = 0;
    }
    
    /**
     * Méthode utilitaire pour afficher le code machine
     */
    public static String formatMachineCode(byte[] code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length; i++) {
            sb.append(String.format("%02X ", code[i] & 0xFF));
            if ((i + 1) % 16 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}