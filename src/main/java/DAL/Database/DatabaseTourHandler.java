package DAL.Database;

import DAL.Exceptions.DatabaseInstanceException;
import DAL.Exceptions.TourDatabaseOperationException;
import DAL.Exceptions.TourLogDatabaseOperationException;
import DATATYPES.Tour;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DatabaseTourHandler {
    DatabaseConnection dbInstance;
    private final Logger log;
    DatabaseTourLogHandler tourLogManager;

    public DatabaseTourHandler() throws TourLogDatabaseOperationException , DatabaseInstanceException {

        log = LogManager.getLogger(DatabaseTourHandler.class);
        tourLogManager = new DatabaseTourLogHandler();
        dbInstance = DatabaseConnection.getDatabaseInstance();

    }


    public int createTour(Tour newTour) throws TourDatabaseOperationException {
        String sqlInsert="insert into \"TourPlanner\".tour (\"name\", \"tourDescription\", \"routeInformation\" , \"to\", \"from\")\n" +
                "values (?,?,?,?,?) RETURNING \"id\";";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(sqlInsert);
            preparedStatement.setString(1,newTour.getTourName());
            preparedStatement.setString(2,newTour.getTourDescription());
            preparedStatement.setString(3,newTour.getRouteInformation());
            preparedStatement.setString(4,newTour.getTourTo());
            preparedStatement.setString(5,newTour.getTourFrom());
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

    private void addTourVectorToken(String tourName){

    }


    public void deleteTour(Tour tour) throws TourDatabaseOperationException {
        String deleteSql="delete\n" +
                "from \"TourPlanner\".tour\n" +
                "where name=?";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(deleteSql);
            preparedStatement.setString(1,tour.getTourName());
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

    public List<Tour> getAllToursFromBackend() throws TourDatabaseOperationException {
        String sqlSelect="select *\n" +
                "from \"TourPlanner\".tour LIMIT 100";
        PreparedStatement preparedStatement= null;
        List<Tour> tourList;
        try {
            tourList = new ArrayList<>();
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
                tourList.add(tour);
            }
            log.debug("Filled LocalTourList with all Tours");
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't get TourList",throwables);
        }
        return tourList;
    }

    public void updateTour(String oldTourName,Tour newTour) throws TourDatabaseOperationException {
        String updateSql="update \"TourPlanner\".tour\n" +
                "set \"name\"  = ?, \"tourDescription\" = ?, \"routeInformation\" = ?, \"from\" = ?, \"to\" = ? \n" +
                " where \"name\" = ?";
        PreparedStatement preparedStatement= null;
        try {
            preparedStatement = dbInstance.getConnection().prepareStatement(updateSql);
            preparedStatement.setString(1,newTour.getTourName());
            preparedStatement.setString(2,newTour.getTourDescription());
            preparedStatement.setString(3,newTour.getRouteInformation());
            preparedStatement.setString(4,newTour.getTourFrom());
            preparedStatement.setString(5,newTour.getTourTo());
            preparedStatement.setString(6,oldTourName);
            preparedStatement.executeUpdate();
            log.debug("TourAttributes Updated in Database");
        } catch (SQLException throwables) {
            throw new TourDatabaseOperationException("Couldn't update Tour Attributes",throwables);
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

    public List<Tour> getToursFromSearch(String input)  throws TourDatabaseOperationException {
        String selectSql="SELECT Distinct * FROM \"TourPlanner\".tour as t\n" +
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
            List<Tour> searchedTours = new ArrayList<>();
            while (resultSet.next()){
                Tour tour=new Tour();
                tour.setTourName(resultSet.getString("name"));
                tour.setTourDistance(resultSet.getDouble("tourDistance"));
                tour.setTourDescription(resultSet.getString("tourDescription"));
                tour.setRouteInformation(resultSet.getString("routeInformation"));
                tour.setTourFrom(resultSet.getString("from"));
                tour.setTourTo(resultSet.getString("to"));
                searchedTours.add(tour);
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
