package DataAccessLayer.Database;

import DataAccessLayer.Exceptions.DatabaseInstanceException;
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

public class DatabaseHandler implements IDAL {

    private HashMap <String,String> connectionData;
    private Connection connection;
    private static DatabaseHandler instance=null;
    private final Logger log;

    private DatabaseHandler() throws DatabaseInstanceException {
        log = LogManager.getLogger(DatabaseHandler.class);
        // List of connection parameters
        connectionData = new HashMap<String, String>();
        // ObjectMapper to convert json lines to String
        ObjectMapper mapper = new ObjectMapper();
        // convert JSON file to map
        Map<?, ?> readValues = null;
        try {
            readValues = mapper.readValue(Paths.get("config.json").toFile(), Map.class);
            // read lines into connection Data
            for (Map.Entry<?, ?> entry : readValues.entrySet()) {
                connectionData.put(entry.getKey().toString(), entry.getValue().toString());
            }
            log.info("Read Connection Attributes from config file");
            // Build Connenction
            initialize();
        } catch (JsonMappingException jsonMappingException) {
            throw new DatabaseInstanceException("could not convert JsonMapping into Java Logic",jsonMappingException);
        } catch (JsonParseException jsonParseException) {
            throw new DatabaseInstanceException("could not parse JsonData into Java",jsonParseException);
        } catch (IOException ioException) {
            throw new DatabaseInstanceException("could not read config file",ioException);
        }

    }


    @Override
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

    public static DatabaseHandler getDatabaseInstance() throws DatabaseInstanceException {
        if(instance==null){
            instance=new DatabaseHandler();
        }
        return  instance;
    }

    @Override
    public Connection getConnection(){
        log.debug("Connection Instance accessed");
        return connection;
    }
    @Override
    public HashMap<String,String> getConnectionData (){
        return connectionData;
    }

}
