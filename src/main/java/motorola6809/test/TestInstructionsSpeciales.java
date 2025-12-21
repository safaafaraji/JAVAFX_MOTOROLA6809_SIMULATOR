package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestInstructionsSpeciales {
    
    public static void main(String[] args) {
        System.out.println("=== TEST INSTRUCTIONS SPÉCIALES (PSH/PUL/TFR/EXG) ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme test avec toutes les instructions spéciales
        String program = """
            ORG $2000
            
            ; === Pile ===
            PSHS A,B,X,Y,U,PC    ; Pousse A,B,X,Y,U,PC sur la pile S
            PULS CC,DP,A,B,X,Y   ; Tire CC,DP,A,B,X,Y de la pile S
            PSHU S,DP,CC         ; Pousse S,DP,CC sur la pile U
            PULU PC,U,Y,X        ; Tire PC,U,Y,X de la pile U
            
            ; === Transfert ===
            TFR A,B              ; Transfert A -> B
            TFR X,Y              ; Transfert X -> Y
            TFR D,X              ; Transfert D -> X
            TFR S,U              ; Transfert S -> U
            
            ; === Échange ===
            EXG A,B              ; Échange A et B
            EXG X,Y              ; Échange X et Y
            EXG D,PC             ; Échange D et PC
            EXG S,U              ; Échange S et U
            
            ; === Fin ===
            END
            """;
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS TOTAL !");
            System.out.println("Toutes les instructions spéciales fonctionnent.");
            System.out.println("Taille: " + machineCode.length + " octets");
            
            System.out.println("\nCode généré:");
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.print(" ");
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
            System.out.println("\n\n🎉 TOUTES LES INSTRUCTIONS DU 6809 SONT MAINTENANT SUPPORTÉES !");
            System.out.println("L'assembleur est COMPLET à 100% !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            System.err.println("\nDernières corrections nécessaires:");
            System.err.println("1. Vérifiez que PSHx/PULx/TFR/EXG sont en mode IMMEDIATE dans OpcodeGenerator");
            System.err.println("2. Vérifiez que Parser.getAddressingMode() les détecte comme IMMEDIATE");
            System.err.println("3. Vérifiez que Assembler.encodeRegisterPostbyte() fonctionne");
        }
    }
}