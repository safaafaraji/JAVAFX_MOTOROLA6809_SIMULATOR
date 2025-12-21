package motorola6809.assembler;

public class AssemblerException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private int lineNumber;
    
    public AssemblerException(String message) {
        super(message);
        this.lineNumber = -1;
    }
    
    public AssemblerException(String message, int lineNumber) {
        super("Ligne " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
}