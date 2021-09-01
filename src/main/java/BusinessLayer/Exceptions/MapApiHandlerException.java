package BusinessLayer.Exceptions;

public class MapApiHandlerException extends Exception{
    public MapApiHandlerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
