package DAL;

import DAL.Database.DatabaseTourLogHandler;
import DAL.Database.DatabaseTourHandler;
import DAL.Exceptions.DatabaseInstanceException;
import DAL.Exceptions.ModelOperationException;
import DAL.Exceptions.TourDatabaseOperationException;
import DAL.Exceptions.TourLogDatabaseOperationException;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Model {

    private final Logger log = LogManager.getLogger(Model.class);;
    private DatabaseTourHandler databaseTourHandler;
    private DatabaseTourLogHandler databaseTourLogHandler;
    private static Model instance=null;
    private static Model testInstance=null;


    private Model() throws ModelOperationException {
        try {
            databaseTourHandler =new DatabaseTourHandler();
            databaseTourLogHandler = new DatabaseTourLogHandler();

        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't instantiate TourLogHandler for Model",e);
        } catch (DatabaseInstanceException e) {
            throw new ModelOperationException("couldn't get DatabaseInstance for Model",e);
        }

        log.debug("Tours pulled from Database and saved locally");
        log.debug("DAL Layer logic instantiated");
        //tours=new ArrayList<>();
    }

    private Model(boolean Test){
        if (Test){
            databaseTourHandler =null;
        }
    }


    public static Model getModelInstance() throws ModelOperationException {
        if(instance==null){
            instance=new Model();
        }
        return instance;
    }



    public List<Tour> getTours() throws ModelOperationException {
        log.debug("DAL Layer return all Tours");
        try {
            return databaseTourHandler.getAllToursFromBackend();
        } catch (TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't get all Tours",e);
        }
    }


    public void addTour(Tour newTour) throws ModelOperationException{
        try {
            databaseTourHandler.createTour(newTour);
            databaseTourHandler.updateTourVectorToken(newTour.getTourName());
        } catch (TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't add Tour",e);
        }
        log.debug("DAL Layer add Tour in Backend");
    }

    public void deleteTour(Tour tour) throws ModelOperationException {
        if (tour==null){
            return;
        }
        try {
            databaseTourHandler.deleteTour(tour);
            log.debug("DAL Layer delete Tour in Backend");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't delete Tour",throwables);
        }

    }

    public Tour getTourDetails(int tourID,String tourName) throws ModelOperationException {
        log.debug("DAL Layer return TourDetails unconditionally");
        try {
            return databaseTourHandler.getTourDetails(tourID,tourName);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get tourDetails",throwables);
        }


    }

    public void updateTour(String oldTourName,Tour newTour) throws ModelOperationException {
        try {
            databaseTourHandler.updateTour(oldTourName,newTour);
            log.debug("DAL Layer update TourDetails unconditionally");
            databaseTourHandler.updateTourVectorToken(newTour.getTourName());
            log.debug("Update Vector for Tour Indexing");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get update TourAttributes",throwables);
        }

    }

    public List<Tour> fullTextSearch(String input) throws ModelOperationException {
        try {
            log.debug("Return all Searched Tours from Model");
            return databaseTourHandler.getToursFromSearch(input);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get searched Tours",throwables);
        }
    }

    public void updateDistance(String tourName,Double distance) throws ModelOperationException {
        try {
            databaseTourHandler.updateTourDistance(tourName,distance);
            log.debug("Model update Tour Distance from MapApi");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't update TourDistance for selected TourLog",throwables);
        }
    }

    public void addTourLog(String tourName) throws ModelOperationException {
        try {
            databaseTourLogHandler.addTourLog(databaseTourHandler.getTourID(tourName));
            log.debug("Model added TourLog");
        } catch (TourLogDatabaseOperationException | TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't add TourLog Model",e);
        }
    }

    public void deleteTourLog(String timestamp) throws ModelOperationException {
        try {
            databaseTourLogHandler.deleteTourLog(timestamp);
            log.debug("Model deleted TourLog");
        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't delete tourLog",e);
        }
    }

    public List<TourLog> getAllTourLogs(String tourName) throws ModelOperationException {
        try {
            log.debug("Model returning all TourLogs for Tour");
            return databaseTourLogHandler.getAllTourLogs(databaseTourHandler.getTourID(tourName));
        } catch (TourLogDatabaseOperationException | TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't get all tourLogs for tour",e);
        }
    }

    public TourLog getTourLog(String timestamp) throws ModelOperationException {
        try {
            log.debug("Model returning specific TourLog for Tour");
            return databaseTourLogHandler.getTourLog(timestamp);
        } catch (TourLogDatabaseOperationException e) {
           throw new ModelOperationException("couldn't get TourLog Details for selected TourLog",e);
        }
    }

    public void updateTourLog(TourLog tourLog) throws ModelOperationException {
        try {
            databaseTourLogHandler.updateTourLog(tourLog);
            databaseTourLogHandler.updateTourLogVector(tourLog);
            log.debug("Model updating specific TourLog for Tour");
        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't update TourLog Details for selected TourLog",e);
        }

    }


}
