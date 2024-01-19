package estorePojo.exceptions;

public class InvalidCartException extends Exception {
    
    private static final long serialVersionUID = -6561524790558683191L;

    public InvalidCartException(String msg) {
        super(msg);
    }
    
}
