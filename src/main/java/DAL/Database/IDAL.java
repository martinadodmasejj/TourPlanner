package DAL.Database;

import DAL.Exceptions.DatabaseInstanceException;

import java.sql.Connection;
import java.util.HashMap;


public interface IDAL {
    public void initialize() throws DatabaseInstanceException;
    public Connection getConnection();
    public HashMap<String,String> getConnectionData();
}
