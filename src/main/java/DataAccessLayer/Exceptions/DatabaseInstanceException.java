package DataAccessLayer.Exceptions;

public class DatabaseInstanceException extends Exception{
    public DatabaseInstanceException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
