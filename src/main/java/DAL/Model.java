package DAL;

import DAL.Database.BackendTourLogManager;
import DAL.Database.BackendTourManager;
import DAL.Exceptions.DatabaseInstanceException;
import DAL.Exceptions.ModelOperationException;
import DAL.Exceptions.TourDatabaseOperationException;
import DAL.Exceptions.TourLogDatabaseOperationException;
import DAL.Local.LocalTourList;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class Model {

    private final Logger log = LogManager.getLogger(Model.class);;
    private BackendTourManager backendTourManager;
    private BackendTourLogManager backendTourLogManager;
    private static Model instance=null;
    private static Model testInstance=null;


    private Model() throws ModelOperationException {
        try {
            backendTourManager=new BackendTourManager();
            backendTourLogManager = new BackendTourLogManager();

        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't instantiate TourLogManager for Model",e);
        } catch (DatabaseInstanceException e) {
            throw new ModelOperationException("couldn't get DatabaseInstance for Model",e);
        }

        log.debug("Tours pulled from Database and saved locally");
        log.debug("DAL Layer logic instantiated");
        //tours=new ArrayList<>();
    }

    private Model(boolean Test){
        if (Test){
            backendTourManager=null;
        }
    }


    public static Model getModelInstance() throws ModelOperationException {
        if(instance==null){
            instance=new Model();
        }
        return instance;
    }

    public static Model getTestModelInstance() {
        if(testInstance==null){
            testInstance=new Model(true);
        }
        return testInstance;
    }


    public List<Tour> getTours() throws ModelOperationException {
        log.debug("DAL Layer return all Tours");
        try {
            return backendTourManager.getAllToursFromBackend();
        } catch (TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't get all Tours",e);
        }
    }


    public void addTour(Tour newTour) throws ModelOperationException{
        try {
            backendTourManager.createTour(newTour);
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
            backendTourManager.deleteTour(tour);
            log.debug("DAL Layer delete Tour in Backend");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't delete Tour",throwables);
        }

    }

    public Tour getTourDetails(int tourID,String tourName) throws ModelOperationException {
        log.debug("DAL Layer return TourDetails unconditionally");
        try {
            return backendTourManager.getTourDetails(tourID,tourName);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get tourDetails",throwables);
        }


    }

    public void updateTour(String actualTourName,String tourDescription, String desTourName
            ,String routeInformation, double tourDistance, String from, String to) throws ModelOperationException {
        try {
            backendTourManager.updateTour(actualTourName,tourDescription,desTourName,
                    routeInformation,from,to);
            log.debug("DAL Layer update TourDetails unconditionally");
            backendTourManager.updateTourVectorToken(desTourName);
            log.debug("Update Vector for Tour Indexing");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get update TourAttributes",throwables);
        }

    }

    public List<Tour> fullTextSearch(String input) throws ModelOperationException {
        List<Tour> searchedTours = null;
        try {
            log.debug("Return all Searched Tours from Model");
            return searchedTours = backendTourManager.getToursFromSearch(input);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get searched Tours",throwables);
        }
    }

    public void updateDistance(String tourName,Double distance) throws ModelOperationException {
        try {
            backendTourManager.updateTourDistance(tourName,distance);
            log.debug("Model update Tour Distance from MapApi");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't update TourDistance for selected TourLog",throwables);
        }
    }

    public void addTourLog(String tourName) throws ModelOperationException {
        try {
            backendTourLogManager.addTourLog(backendTourManager.getTourID(tourName));
            log.debug("Model added TourLog");
        } catch (TourLogDatabaseOperationException | TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't add TourLog Model",e);
        }
    }

    public void deleteTourLog(String timestamp) throws ModelOperationException {
        try {
            backendTourLogManager.deleteTourLog(timestamp);
            log.debug("Model deleted TourLog");
        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't delete tourLog",e);
        }
    }

    public List<TourLog> getAllTourLogs(String tourName) throws ModelOperationException {
        try {
            log.debug("Model returning all TourLogs for Tour");
            return backendTourLogManager.getAllTourLogs(backendTourManager.getTourID(tourName));
        } catch (TourLogDatabaseOperationException | TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't get all tourLogs for tour",e);
        }
    }

    public TourLog getTourLog(String timestamp) throws ModelOperationException {
        try {
            log.debug("Model returning specific TourLog for Tour");
            return backendTourLogManager.getTourLog(timestamp);
        } catch (TourLogDatabaseOperationException e) {
           throw new ModelOperationException("couldn't get TourLog Details for selected TourLog",e);
        }
    }

    public void updateTourLog(TourLog tourLog) throws ModelOperationException {
        try {
            backendTourLogManager.updateTourLog(tourLog);
            backendTourLogManager.updateTourLogVector(tourLog);
            log.debug("Model updating specific TourLog for Tour");
        } catch (TourLogDatabaseOperationException e) {
            throw new ModelOperationException("couldn't update TourLog Details for selected TourLog",e);
        }

    }


}
