package motorola6809.test;


// Testeur UAL Motorola 6809 - Version ligne de commande
 // AUCUNE dépendance JavaFX - Juste Java standard

public class UALCommandLineTest {
    
    // Registres
    private int a = 0;
    private int b = 0;
    private int x = 0;
    private int y = 0;
    private int pc = 0x1000;
    private int s = 0x1FFF;
    private int u = 0x1E00;
    private int dp = 0;
    
    // Flags
    private boolean flagC = false; // Carry
    private boolean flagZ = true;  // Zero
    private boolean flagN = false; // Negative
    private boolean flagV = false; // Overflow
    private boolean flagH = false; // Half Carry
    
    // Mémoire
    private int[] memory = new int[0x10000];
    
    public static void main(String[] args) {
        UALCommandLineTest test = new UALCommandLineTest();
        test.runAllTests();
    }
    
    public UALCommandLineTest() {
        // Initialiser la mémoire
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0x00;
        }
    }
    
    public void runAllTests() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║  TESTEUR UAL MOTOROLA 6809 - LIGNE CMD     ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        System.out.println("=== TEST 1: ADDITION ===");
        testAddition();
        showRegisters();
        
        System.out.println("\n=== TEST 2: MULTIPLICATION ===");
        testMultiplication();
        showRegisters();
        
        System.out.println("\n=== TEST 3: OPÉRATIONS LOGIQUES ===");
        testLogical();
        showRegisters();
        
        System.out.println("\n=== TEST 4: INCREMENTATION ===");
        testIncrement();
        showRegisters();
        
        System.out.println("\n=== TEST 5: COMPARAISON ===");
        testComparison();
        showRegisters();
        
        System.out.println("\n=== TEST 6: MÉMOIRE ===");
        testMemory();
        showMemory(0x2000, 0x2010);
        
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║      ✓ TOUS LES TESTS RÉUSSIS ! ✓            ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
    
    // ============ TESTS ============
    
    private void testAddition() {
        System.out.println("Test: 5 + 3");
        a = 0x05;
        b = 0x03;
        executeADD();
        
        System.out.printf("  Résultat: A = %d (0x%02X)\n", a, a);
        System.out.printf("  Flags: Z=%s, N=%s, C=%s, V=%s\n", 
            boolToStr(flagZ), boolToStr(flagN), boolToStr(flagC), boolToStr(flagV));
        
        if (a != 0x08) {
            throw new RuntimeException("ERREUR: 5 + 3 devrait donner 8");
        }
        System.out.println("  ✓ Addition correcte");
    }
    
    private void testMultiplication() {
        System.out.println("Test: 4 * 5");
        a = 0x04;
        b = 0x05;
        executeMUL();
        
        int d = (a << 8) | b;
        System.out.printf("  Résultat: D = %d (0x%04X)\n", d, d);
        System.out.printf("  A=0x%02X, B=0x%02X\n", a, b);
        System.out.printf("  Flags: Z=%s, C=%s\n", boolToStr(flagZ), boolToStr(flagC));
        
        if (d != 20) {
            throw new RuntimeException("ERREUR: 4 * 5 devrait donner 20");
        }
        System.out.println("  ✓ Multiplication correcte");
    }
    
    private void testLogical() {
        System.out.println("Test: Opérations logiques");
        
        a = 0xAA; // 10101010
        b = 0x55; // 01010101
        
        // AND
        executeAND();
        System.out.printf("  AND: AA & 55 = 0x%02X (Z=%s)\n", a, boolToStr(flagZ));
        if (a != 0x00 || !flagZ) {
            throw new RuntimeException("ERREUR AND");
        }
        
        // OR
        executeOR(0xF0);
        System.out.printf("  OR:  00 | F0 = 0x%02X\n", a);
        if (a != 0xF0) {
            throw new RuntimeException("ERREUR OR");
        }
        
        // XOR
        executeXOR(0x0F);
        System.out.printf("  XOR: F0 ^ 0F = 0x%02X\n", a);
        if (a != 0xFF) {
            throw new RuntimeException("ERREUR XOR");
        }
        
        System.out.println("  ✓ Logique correcte");
    }
    
    private void testIncrement() {
        System.out.println("Test: Incrémentation");
        
        a = 0xFE; // -2
        
        executeINC();
        System.out.printf("  INC FE -> 0x%02X (Z=%s)\n", a, boolToStr(flagZ));
        if (a != 0xFF || flagZ) {
            throw new RuntimeException("ERREUR INC 1");
        }
        
        executeINC();
        System.out.printf("  INC FF -> 0x%02X (Z=%s)\n", a, boolToStr(flagZ));
        if (a != 0x00 || !flagZ) {
            throw new RuntimeException("ERREUR INC 2");
        }
        
        executeINC();
        System.out.printf("  INC 00 -> 0x%02X (Z=%s)\n", a, boolToStr(flagZ));
        if (a != 0x01 || flagZ) {
            throw new RuntimeException("ERREUR INC 3");
        }
        
        System.out.println("  ✓ Incrémentation correcte");
    }
    
    private void testComparison() {
        System.out.println("Test: Comparaison");
        
        a = 0x10; // 16
        b = 0x05; // 5
        executeCMP();
        
        System.out.printf("  CMP: 10 vs 05 -> Z=%s, N=%s, C=%s\n", 
            boolToStr(flagZ), boolToStr(flagN), boolToStr(flagC));
        
        if (flagZ || flagC) {
            throw new RuntimeException("ERREUR CMP");
        }
        System.out.println("  ✓ Comparaison correcte");
    }
    
    private void testMemory() {
        System.out.println("Test: Lecture/Écriture mémoire");
        
        // Écrire
        a = 0x42;
        writeMemory(0x2000, a);
        writeMemory(0x2001, 0x99);
        
        // Lire
        int value1 = readMemory(0x2000);
        int value2 = readMemory(0x2001);
        
        System.out.printf("  Écrit: 0x2000=0x%02X, 0x2001=0x%02X\n", a, 0x99);
        System.out.printf("  Lecture: 0x2000=0x%02X, 0x2001=0x%02X\n", value1, value2);
        
        if (value1 != 0x42 || value2 != 0x99) {
            throw new RuntimeException("ERREUR Mémoire");
        }
        System.out.println("  ✓ Mémoire correcte");
    }
    
    // ============ IMPLÉMENTATION UAL ============
    
    private void executeADD() {
        int result = a + b;
        a = result & 0xFF;
        
        flagC = (result & 0x100) != 0;
        flagV = ((a ^ result) & (b ^ result) & 0x80) != 0;
        flagH = ((a & 0x0F) + (b & 0x0F)) > 0x0F;
        flagZ = a == 0;
        flagN = (a & 0x80) != 0;
        pc += 2;
    }
    
    private void executeMUL() {
        int result = a * b;
        a = (result >> 8) & 0xFF;
        b = result & 0xFF;
        
        flagZ = (result & 0xFFFF) == 0;
        flagC = (result & 0xFF00) != 0;
        flagN = false; // MUL ne met pas N sur 6809
        pc += 11; // 11 cycles
    }
    
    private void executeAND() {
        a = a & b;
        flagZ = a == 0;
        flagN = (a & 0x80) != 0;
        flagC = false;
        pc += 2;
    }
    
    private void executeOR(int value) {
        a = a | value;
        flagZ = a == 0;
        flagN = (a & 0x80) != 0;
        flagC = false;
        pc += 2;
    }
    
    private void executeXOR(int value) {
        a = a ^ value;
        flagZ = a == 0;
        flagN = (a & 0x80) != 0;
        flagC = false;
        pc += 2;
    }
    
    private void executeINC() {
        int oldA = a;
        a = (a + 1) & 0xFF;
        
        flagZ = a == 0;
        flagN = (a & 0x80) != 0;
        flagV = oldA == 0x7F; // Overflow si 127 -> 128
        pc += 2;
    }
    
    private void executeCMP() {
        int result = a - b;
        
        flagZ = (result & 0xFF) == 0;
        flagN = (result & 0x80) != 0;
        flagC = b > a; // Borrow
        flagV = ((a ^ b) & (a ^ result) & 0x80) != 0;
        pc += 2;
    }
    
    // ============ MÉMOIRE ============
    
    private void writeMemory(int address, int value) {
        memory[address & 0xFFFF] = value & 0xFF;
    }
    
    private int readMemory(int address) {
        return memory[address & 0xFFFF] & 0xFF;
    }
    
    // ============ AFFICHAGE ============
    
    private void showRegisters() {
        System.out.println("\nÉtat des registres:");
        System.out.println("───────────────────");
        System.out.printf("PC: 0x%04X  A: 0x%02X  B: 0x%02X  D: 0x%04X\n", 
            pc, a, b, (a << 8) | b);
        System.out.printf("X:  0x%04X  Y: 0x%04X  S: 0x%04X  U: 0x%04X  DP: 0x%02X\n",
            x, y, s, u, dp);
        
        System.out.print("Flags: ");
        System.out.print("C=" + (flagC ? "1" : "0") + " ");
        System.out.print("Z=" + (flagZ ? "1" : "0") + " ");
        System.out.print("N=" + (flagN ? "1" : "0") + " ");
        System.out.print("V=" + (flagV ? "1" : "0") + " ");
        System.out.print("H=" + (flagH ? "1" : "0"));
        System.out.println();
        
        // Valeurs signées
        System.out.printf("A (signé): %d  B (signé): %d\n", 
            a > 127 ? a - 256 : a,
            b > 127 ? b - 256 : b);
    }
    
    private void showMemory(int start, int end) {
        System.out.printf("\nMémoire 0x%04X-0x%04X:\n", start, end);
        System.out.println("Adresse  +0 +1 +2 +3 +4 +5 +6 +7 +8 +9 +A +B +C +D +E +F  ASCII");
        System.out.println("-------- -----------------------------------------------  -----");
        
        for (int addr = start; addr <= end; addr += 16) {
            System.out.printf("%04X:   ", addr);
            
            // Hex
            for (int offset = 0; offset < 16 && (addr + offset) <= end; offset++) {
                System.out.printf("%02X ", readMemory(addr + offset));
            }
            
            // Espace
            for (int i = 16 - Math.min(16, end - addr + 1); i > 0; i--) {
                System.out.print("   ");
            }
            
            System.out.print("  ");
            
            // ASCII
            for (int offset = 0; offset < 16 && (addr + offset) <= end; offset++) {
                int value = readMemory(addr + offset);
                System.out.print(value >= 32 && value < 127 ? (char) value : '.');
            }
            
            System.out.println();
        }
    }
    
    private String boolToStr(boolean value) {
        return value ? "1" : "0";
    }
    
    // ============ TEST INTERACTIF ============
    
    public void interactiveTest() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║     MODE TEST INTERACTIF UAL 6809          ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (true) {
            showRegisters();
            System.out.println("\nCommandes disponibles:");
            System.out.println("  add    - Addition A + B");
            System.out.println("  mul    - Multiplication A * B");
            System.out.println("  and    - AND logique A & B");
            System.out.println("  or     - OR logique A | B");
            System.out.println("  xor    - XOR logique A ^ B");
            System.out.println("  inc    - Incrémenter A");
            System.out.println("  cmp    - Comparer A et B");
            System.out.println("  set a XX - Définir A (hex)");
            System.out.println("  set b XX - Définir B (hex)");
            System.out.println("  mem    - Afficher mémoire");
            System.out.println("  quit   - Quitter");
            
            System.out.print("\n> ");
            String command = scanner.nextLine().trim().toLowerCase();
            
            if (command.equals("quit")) {
                break;
            }
            
            try {
                processCommand(command, scanner);
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        
        scanner.close();
        System.out.println("Test terminé. Merci !");
    }
    
    private void processCommand(String command, java.util.Scanner scanner) {
        String[] parts = command.split("\\s+");
        
        switch (parts[0]) {
            case "add":
                executeADD();
                System.out.println("Addition effectuée");
                break;
                
            case "mul":
                executeMUL();
                System.out.println("Multiplication effectuée");
                break;
                
            case "and":
                executeAND();
                System.out.println("AND effectué");
                break;
                
            case "or":
                if (parts.length > 1) {
                    int value = Integer.parseInt(parts[1], 16);
                    executeOR(value);
                } else {
                    executeOR(0xF0);
                }
                System.out.println("OR effectué");
                break;
                
            case "xor":
                if (parts.length > 1) {
                    int value = Integer.parseInt(parts[1], 16);
                    executeXOR(value);
                } else {
                    executeXOR(0x0F);
                }
                System.out.println("XOR effectué");
                break;
                
            case "inc":
                executeINC();
                System.out.println("Incrémentation effectuée");
                break;
                
            case "cmp":
                executeCMP();
                System.out.println("Comparaison effectuée");
                break;
                
            case "set":
                if (parts.length < 3) {
                    System.out.println("Usage: set a 05  ou  set b FF");
                    return;
                }
                int value = Integer.parseInt(parts[2], 16);
                if (parts[1].equals("a")) {
                    a = value & 0xFF;
                } else if (parts[1].equals("b")) {
                    b = value & 0xFF;
                }
                System.out.println("Registre mis à jour");
                break;
                
            case "mem":
                showMemory(0x2000, 0x2010);
                break;
                
            default:
                System.out.println("Commande inconnue");
        }
    }
}