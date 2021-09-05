package DAL.Exceptions;

public class TourLogDatabaseOperationException extends Exception {
    public TourLogDatabaseOperationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
