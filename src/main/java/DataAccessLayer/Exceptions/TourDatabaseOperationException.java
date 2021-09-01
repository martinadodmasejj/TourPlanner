package DataAccessLayer.Exceptions;

public class TourDatabaseOperationException extends Exception {
    public TourDatabaseOperationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
