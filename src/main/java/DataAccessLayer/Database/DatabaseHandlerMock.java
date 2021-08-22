package DataAccessLayer.Database;

import DataAccessLayer.Exceptions.DatabaseInstanceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandlerMock implements IDAL {

    private HashMap<String,String> connectionData;
    private Connection connection;
    private static DatabaseHandlerMock instance=null;

    private DatabaseHandlerMock() throws DatabaseInstanceException {
        // List of connection parameters
        connectionData= new HashMap<String, String>();
        connection=null;
        // ObjectMapper to conver json lines to String
        ObjectMapper mapper = new ObjectMapper();
        // convert JSON file to map
        Map<?, ?> readValues = null;
        try {
            readValues = mapper.readValue(Paths.get("config.json").toFile(), Map.class);
        } catch (IOException e) {
            throw new DatabaseInstanceException("could not read config file in MockDB Instance",e);
        }
        // read lines into connection Data
        for (Map.Entry<?, ?> entry : readValues.entrySet()) {
            connectionData.put(entry.getKey().toString(),entry.getValue().toString());
        }
        // Build Connenction
        initialize();

    }

    @Override
    public void initialize()  {
        System.out.println("Mock Database Connected");
    }

    public static DatabaseHandlerMock getDatabaseInstance() throws DatabaseInstanceException {
        if(instance==null){
            instance=new DatabaseHandlerMock();
        }
        return  instance;
    }

    @Override
    public Connection getConnection(){
        return connection;
    }
    @Override
    public HashMap<String,String> getConnectionData (){
        return connectionData;
    }

}
