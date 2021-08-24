package BusinessLayer.Exceptions;

public class TourListManagerException extends Exception {
    public TourListManagerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
