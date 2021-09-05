package DAL.Database;

import DAL.Exceptions.DatabaseInstanceException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DatabaseConnection {

    private HashMap <String,String> connectionData;
    private Connection connection;
    private static DatabaseConnection instance=null;
    private final Logger log;

    private DatabaseConnection() throws DatabaseInstanceException {

        log = LogManager.getLogger(DatabaseConnection.class);
        connectionData = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> readValues = null;

        try {
            readValues = mapper.readValue(Paths.get("config.json").toFile(), Map.class);
            for (Map.Entry<?, ?> entry : readValues.entrySet()) {
                connectionData.put(entry.getKey().toString(), entry.getValue().toString());
            }
            log.info("Read Connection Attributes from config file");
            initialize();
        } catch (JsonMappingException jsonMappingException) {
            throw new DatabaseInstanceException("could not convert JsonMapping into Java Logic",jsonMappingException);
        } catch (JsonParseException jsonParseException) {
            throw new DatabaseInstanceException("could not parse JsonData into Java",jsonParseException);
        } catch (IOException ioException) {
            throw new DatabaseInstanceException("could not read config file",ioException);
        }



    }

    public void initialize()  throws DatabaseInstanceException{
        try {
            connection = DriverManager.getConnection(
                    connectionData.get("jdbcURL"),
                    connectionData.get("username"),
                    connectionData.get("password"));
        } catch (SQLException throwables) {
            throw new DatabaseInstanceException("could initialize Database Connection with config data",throwables);
        }

        System.out.println("Database Connected");
        log.info("Database Connected");
    }

    public static DatabaseConnection getDatabaseInstance() throws DatabaseInstanceException {
        if(instance==null){
            instance=new DatabaseConnection();
        }
        return  instance;
    }

    public Connection getConnection(){
        log.debug("Connection Instance accessed");
        return connection;
    }

    public HashMap<String,String> getConnectionData (){
        return connectionData;
    }

}
