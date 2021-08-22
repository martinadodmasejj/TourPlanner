package DataAccessLayer.Database;

import DataAccessLayer.Exceptions.DatabaseInstanceException;
import DataAccessLayer.Exceptions.TourDatabaseOperationException;
import DataAccessLayer.Exceptions.TourLogDatabaseOperationException;
import DataAccessLayer.Local.LocalTourList;
import Datatypes.Tour;
import Datatypes.TourLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BackendTourManager {
    IDAL dbInstance;
    private final Logger log;
    BackendTourLogManager tourLogManager;

    public BackendTourManager() throws TourLogDatabaseOperationException , DatabaseInstanceException {

        log = LogManager.getLogger(BackendTourManager.class);
        tourLogManager = new BackendTourLogManager();
        dbInstance = DALFactory.getDAL();

    }


    public int createTour(String tourName) throws TourDatabaseOperationException {
        String sqlInsert="insert into \"TourPlanner\".tour (\"name\", \"tourDescription\", \"routeInformation\", \"tourDistance\")\n" +
                "values (?,?,?,?) RETURNING \"id\";";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1,tourName);
            preparedStatement.setString(2,"------");
            preparedStatement.setString(3,"------");
            preparedStatement.setDouble(4,0.0);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                log.debug("Created Tour Successfully");
                return resultSet.getInt("id");
            }
            return 0;
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't create new Tour",throwables);
        }
    }


    public void deleteTour(String tourName) throws TourDatabaseOperationException {
        String deleteSql="delete\n" +
                "from \"TourPlanner\".tour\n" +
                "where name=?";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(deleteSql);
            preparedStatement.setString(1,tourName);
            preparedStatement.executeUpdate();
            log.debug("Tour deleted Successfully");
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't delete Tour",throwables);
        }

    }

    public Tour getTourDetails(int tourID, String tourName) throws TourDatabaseOperationException {
        Tour tourDetails = new Tour();

        String selectSql="select *\n" +
                "from \"TourPlanner\".tour\n" +
                "WHERE \"id\" = ? OR \"name\" = ?";
        if(tourID==0){
            tourID=-1;
        }
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(selectSql);
            preparedStatement.setInt(1,tourID);
            preparedStatement.setString(2,tourName);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                tourDetails.setTourName(resultSet.getString("name"));
                tourDetails.setTourDescription(resultSet.getString("tourDescription"));
                tourDetails.setTourDistance(resultSet.getDouble("tourDistance"));
                tourDetails.setRouteInformation(resultSet.getString("routeInformation"));
                tourDetails.setTourFrom(resultSet.getString("from"));
                tourDetails.setTourTo(resultSet.getString("to"));
            }
            else {
                log.error("Tour doesn't exist");
                return null;
            }
            return tourDetails;
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't get Tour Details",throwables);
        }

    }

    public void getAllToursFromBackend(LocalTourList localTourList) throws TourDatabaseOperationException {
        String sqlSelect="select *\n" +
                "from \"TourPlanner\".tour LIMIT 100";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(sqlSelect);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Tour tour=new Tour();
                tour.setTourName(resultSet.getString("name"));
                tour.setTourDistance(resultSet.getDouble("tourDistance"));
                tour.setTourDescription(resultSet.getString("tourDescription"));
                tour.setRouteInformation(resultSet.getString("routeInformation"));
                tour.setTourFrom(resultSet.getString("from"));
                tour.setTourTo(resultSet.getString("to"));
                localTourList.addTour(tour);
            }
            log.debug("Filled LocalTourList with all Tours");
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't get TourList",throwables);
        }
    }

    public void updateTour(String actualTourName,String tourDescription, String desTourName
                        ,String routeInformation, double tourDistance) throws TourDatabaseOperationException {
        String updateSql="update \"TourPlanner\".tour\n" +
                "set \"name\"  = ?, \"tourDescription\" = ?, \"routeInformation\" = ?,\n" +
                "    \"tourDistance\" = ? where \"name\" = ?";
        PreparedStatement preparedStatement= null;

        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(updateSql);
            preparedStatement.setString(1,desTourName);
            preparedStatement.setString(2,tourDescription);
            preparedStatement.setString(3,routeInformation);
            preparedStatement.setDouble(4,tourDistance);
            preparedStatement.setString(5,actualTourName);
            preparedStatement.executeUpdate();
            log.debug("TourAttributes Updated in Database");
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't update Tour Attributes",throwables);
        }

    }

    public void updateTourRoute(String tourName,String from,String to) throws TourDatabaseOperationException {
        String sqlInsert="update \"TourPlanner\".tour\n" +
                "set \"from\" = ?, \"to\" = ?\n" +
                "where name = ?";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1,from);
            preparedStatement.setString(2,to);
            preparedStatement.setString(3,tourName);
            preparedStatement.executeUpdate();
            log.debug("TourRoute Updated in Database");

        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't update Tour Route",throwables);
        }

    }

    public void updateTourVectorToken(String tourName) throws TourDatabaseOperationException {
        Tour tourDetails = getTourDetails(0,tourName);
        String insertTourText="update \"TourPlanner\".tour " +
                "set \"tourToken\" = to_tsvector(?) " +
                "where name = ?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(insertTourText);
            String allTourText = tourDetails.getTourName()+" "+tourDetails.getTourDistance()
                    +" "+tourDetails.getTourDescription()+" "+tourDetails.getRouteInformation()+" "+tourDetails.getTourTo()
                    +" "+tourDetails.getTourFrom();
            preparedStatement.setString(1,allTourText);
            preparedStatement.setString(2,tourName);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't update Tour VectorToken",throwables);
        }

    }

    public List<String> getToursFromSearch(String input)  throws TourDatabaseOperationException {
        String selectSql="SELECT Distinct name FROM \"TourPlanner\".tour as t\n" +
                "join \"TourPlanner\".\"tourLog\" as tL\n" +
                "on t.\"id\" = tl.\"tourID\"\n" +
                "WHERE  t.\"tourToken\"  @@ to_tsquery(?)\n" +
                "OR tl.\"tourLogToken\" @@ to_tsquery(?);";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(selectSql);
            preparedStatement.setString(1,input);
            preparedStatement.setString(2,input);

            ResultSet resultSet=preparedStatement.executeQuery();
            List<String> searchedTours = new ArrayList<String>();
            while (resultSet.next()){
                searchedTours.add(resultSet.getString("name"));
            }
            return searchedTours;
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't get Tours from Search",throwables);
        }
    }

    public void updateTourDistance(String tourName,Double distance) throws TourDatabaseOperationException {
        String updateSql= "update \"TourPlanner\".tour " +
                "set \"tourDistance\" = ? " +
                "where name = ?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(updateSql);
            preparedStatement.setDouble(1,distance);
            preparedStatement.setString(2,tourName);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't update Tour Distance",throwables);
        }

    }

    public int getTourID(String tourName) throws TourDatabaseOperationException {
        String selectSql="SELECT id FROM \"TourPlanner\".tour\n" +
                "WHERE tour.\"name\" = ?;";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(selectSql);
            preparedStatement.setString(1,tourName);
            ResultSet resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                return resultSet.getInt("id");
            }
            return 0;
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't get TourID",throwables);
        }

    }


}
