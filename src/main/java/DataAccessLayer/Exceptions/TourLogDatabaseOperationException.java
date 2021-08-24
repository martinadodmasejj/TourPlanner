package DataAccessLayer.Exceptions;

import java.sql.SQLException;

public class TourLogDatabaseOperationException extends Exception {
    public TourLogDatabaseOperationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
