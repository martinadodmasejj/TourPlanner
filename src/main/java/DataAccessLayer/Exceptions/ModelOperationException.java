package DataAccessLayer.Exceptions;

public class ModelOperationException extends Exception{
    public ModelOperationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
