package no.srib.sribapp.dao.exception;

public class DuplicateEntryException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    public DuplicateEntryException(){
        super(); 
    }
    
    public DuplicateEntryException(final String message) {
        super(message);
    }
    
    public DuplicateEntryException(final Throwable cause) {
        super(cause);
    }
    
}
