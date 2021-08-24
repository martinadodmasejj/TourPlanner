package BusinessLayer.Exceptions;

public class TourLogManagerException extends Exception {
    public TourLogManagerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
