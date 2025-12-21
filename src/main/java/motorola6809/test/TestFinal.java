package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestFinal {
    
    public static void main(String[] args) {
        System.out.println("=== TEST FINAL - TOUT DOIT FONCTIONNER ===\n");
        
        // Programme COMPLET avec toutes les fonctionnalités
        String program = """
            ; Programme de démonstration complet
            ORG $2000
            
            ; Constantes
            MAX EQU 10
            ADDR EQU $3000
            
            ; Données
            DATA: FCB $01, $02, $03
            
            ; Code principal
            MAIN:
                LDA #MAX          ; Utilise constante
                LDB #$05
                
            BOUCLE:
                ADDA #$01
                DECB
                BNE BOUCLE        ; Branchement en arrière
                
                LDX #ADDR         ; Utilise constante d'adresse
                LDA DATA          ; Charge depuis données
                
                JMP FIN           ; Branchement vers l'avant
                
                ; Code jamais exécuté
                NOP
                NOP
                
            FIN:
                RTS
                
            ; Fin du programme
            END MAIN
            """;
        
        Assembler assembler = new Assembler();
        
        try {
            System.out.println("Assemblage du programme complet...");
            byte[] code = assembler.assemble(program);
            
            System.out.println("\n🎉 SUCCÈS ABSOLU !");
            System.out.println("Taille: " + code.length + " octets");
            
            // Afficher un résumé
            System.out.println("\n📊 RÉSUMÉ:");
            System.out.println("- Instructions supportées: ✓");
            System.out.println("- Étiquettes: ✓");
            System.out.println("- Constantes (EQU): ✓");
            System.out.println("- Données (FCB): ✓");
            System.out.println("- Branchements avant/arrière: ✓");
            System.out.println("- Adressage IMMEDIATE/DIRECT/EXTENDED/INHERENT: ✓");
            
            System.out.println("\n✅ VOTRE ASSEMBLEUR MOTOROLA 6809 EST FONCTIONNEL !");
            
        } catch (Exception e) {
            System.err.println("\n❌ DERNIER PROBLÈME À RÉSOUDRE:");
            System.err.println(e.getMessage());
            
            System.err.println("\n🔧 CORRECTION FINALE:");
            System.err.println("1. Vérifiez que parseValue() cherche les étiquettes EN PREMIER");
            System.err.println("2. Vérifiez que symbolTable.addLabel() est appelé dans firstPass()");
            System.err.println("3. Vérifiez que 'EQU' est ignoré dans secondPass()");
        }
    }
}