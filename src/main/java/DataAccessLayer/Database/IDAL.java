package DataAccessLayer.Database;
import DataAccessLayer.Exceptions.DatabaseInstanceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;


public interface IDAL {
    public void initialize() throws DatabaseInstanceException;
    public Connection getConnection();
    public HashMap<String,String> getConnectionData();
}
