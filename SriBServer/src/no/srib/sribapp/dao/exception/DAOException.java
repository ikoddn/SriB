package no.srib.sribapp.dao.exception;

/**
 * Thrown when a Data Access Object encounters an error.
 * 
 * @author Sveinung
 *
 */
public class DAOException extends Exception {

    private static final long serialVersionUID = 1L;

    public DAOException() {
        super();
    }
    
    public DAOException(final Throwable cause) {
        super(cause);
    }
}
