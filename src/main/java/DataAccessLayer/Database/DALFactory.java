package DataAccessLayer.Database;

import DataAccessLayer.Exceptions.DatabaseInstanceException;

import java.io.IOException;
import java.sql.SQLException;

public class DALFactory {
    private static boolean useMock=false;

    public static void useMock(){
        useMock=true;
    }

    public static IDAL getDAL() throws DatabaseInstanceException {
        if(useMock){
            return DatabaseHandlerMock.getDatabaseInstance();

        }
        else{
            return DatabaseHandler.getDatabaseInstance();
        }
    }
}
