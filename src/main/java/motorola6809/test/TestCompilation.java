package motorola6809.test;

import motorola6809.assembler.OpcodeGenerator;

public class TestCompilation {
    public static void main(String[] args) {
        int opcode = OpcodeGenerator.getOpcode("LDA", "IMMEDIATE");
        System.out.println("Opcode LDA IMMEDIATE: 0x" + Integer.toHexString(opcode));
    }
}