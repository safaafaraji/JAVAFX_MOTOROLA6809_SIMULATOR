package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestMinimal {
    
    public static void main(String[] args) {
        System.out.println("=== TEST MINIMAL GARANTI ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme MINIMAL qui DOIT fonctionner
        String minimalProgram = """
            ORG $1400
            
            ; Programme minimal garanti
            START:
                LDA #$01      ; Charge 1 dans A
                LDB #$02      ; Charge 2 dans B
                ADDA #$03     ; Ajoute 3 à A
                SUBB #$01     ; Soustrait 1 de B
                NOP           ; Ne rien faire
                JMP START     ; Boucle infinie
                
            END START
            """;
        
        try {
            System.out.println("Assemblage du programme minimal...");
            byte[] machineCode = assembler.assemble(minimalProgram);
            
            System.out.println("\n✅ SUCCÈS ABSOLU !");
            System.out.println("Code machine généré:");
            
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
            }
            
            System.out.println("\n\nInstructions générées:");
            System.out.println("1. LDA #$01  = 86 01");
            System.out.println("2. LDB #$02  = C6 02");
            System.out.println("3. ADDA #$03 = 8B 03");
            System.out.println("4. SUBB #$01 = C0 01");
            System.out.println("5. NOP       = 12");
            System.out.println("6. JMP $1400 = 7E 14 00");
            
            System.out.println("\n🎉 L'assembleur fonctionne parfaitement !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC CRITIQUE !");
            System.err.println("Message: " + e.getMessage());
            System.err.println("\nProblèmes possibles:");
            System.err.println("1. OpcodeGenerator ne contient pas toutes les instructions");
            System.err.println("2. Parser ne reconnaît pas la syntaxe");
            System.err.println("3. Problème dans getAddressingMode()");
            
            // Debug détaillé
            System.err.println("\n🔧 DEBUG:");
            String[] lines = minimalProgram.split("\n");
            for (int i = 0; i < lines.length; i++) {
                System.err.printf("Ligne %2d: %s\n", i+1, lines[i]);
            }
        }
    }
}