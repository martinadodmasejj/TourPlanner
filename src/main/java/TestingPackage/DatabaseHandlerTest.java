package TestingPackage;

import DataAccessLayer.Database.DALFactory;
import DataAccessLayer.Database.IDAL;
import DataAccessLayer.Exceptions.DatabaseInstanceException;
import org.junit.Assert;
import org.junit.Test;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;


public class DatabaseHandlerTest {
    @Test
    public void testSingleInstance() throws DatabaseInstanceException {
        //Arrange
        DALFactory dalFactory=new DALFactory();
        dalFactory.useMock();
        boolean actualRes=false;
        //Act
        IDAL databaseHandler = dalFactory.getDAL();
        IDAL databaseHandler_test = dalFactory.getDAL();
        if(databaseHandler_test == databaseHandler){
            actualRes = true;
        }
        //Assert
        Assert.assertEquals(true,actualRes);
    }
    @Test
    public void testGetConnection() throws DatabaseInstanceException {
        //Arrange
        DALFactory dalFactory=new DALFactory();
        dalFactory.useMock();
        Connection expectedConnection=null;
        //Act
        IDAL databaseHandler_test = dalFactory.getDAL();
        Connection actualConnection = databaseHandler_test.getConnection();
        //Assert
        Assert.assertEquals(expectedConnection,actualConnection);
    }

    @Test
    public void testConnectionData() throws DatabaseInstanceException {
        //Arrange
        DALFactory dalFactory=new DALFactory();
        dalFactory.useMock();
        Connection expectedConnection=null;
        HashMap<String,String> expectedConnData = new HashMap<>();
        expectedConnData.put("jdbcURL","jdbc:postgresql://localhost:5432/TourPlanner");
        expectedConnData.put("username","postgres");
        expectedConnData.put("password","root");
        //Act
        IDAL databaseHandler_test = dalFactory.getDAL();
        HashMap<String,String> connectionData = databaseHandler_test.getConnectionData();
        //Assert
        Assert.assertEquals(expectedConnData.get("jdbcURL"),connectionData.get("jdbcURL"));
        Assert.assertEquals(expectedConnData.get("username"),connectionData.get("username"));
        Assert.assertEquals(expectedConnData.get("password"),connectionData.get("password"));
    }



}
