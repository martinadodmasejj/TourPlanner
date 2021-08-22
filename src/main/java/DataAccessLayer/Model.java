package DataAccessLayer;

import DataAccessLayer.Database.BackendTourLogManager;
import DataAccessLayer.Database.BackendTourManager;
import DataAccessLayer.Exceptions.DatabaseInstanceException;
import DataAccessLayer.Exceptions.ModelOperationException;
import DataAccessLayer.Exceptions.TourDatabaseOperationException;
import DataAccessLayer.Exceptions.TourLogDatabaseOperationException;
import DataAccessLayer.Local.LocalTourList;
import Datatypes.Tour;
import Datatypes.TourLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class Model {

    //private List<String> tours;
    private final Logger log = LogManager.getLogger(Model.class);;
    private BackendTourManager backendTourManager;
    private LocalTourList localTourList;
    private BackendTourLogManager backendTourLogManager;
    private static Model instance=null;
    private static Model testInstance=null;


    private Model() throws ModelOperationException {
        localTourList = LocalTourList.getTourListManagerInstance();
        try {
            backendTourManager=new BackendTourManager();
            backendTourManager.getAllToursFromBackend(localTourList);
            backendTourLogManager = new BackendTourLogManager();

        } catch (TourLogDatabaseOperationException | TourDatabaseOperationException e) {
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
            localTourList = LocalTourList.getTourListManagerInstance();
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


    public List<String> getTours() {
        log.debug("DAL Layer return all Tours");
        return localTourList.getToursUI();
    }

    public List<Tour> getAllToursDetails(){
        return localTourList.getTours();
    }

    public void addTour(String tourName) throws ModelOperationException{
        localTourList.addTour(new Tour(tourName));
        try {
            backendTourManager.createTour(tourName);
        } catch (TourDatabaseOperationException e) {
            throw new ModelOperationException("couldn't get add Tour",e);
        }
        log.debug("DAL Layer add Tour in Backend");
    }

    public void deleteTour(String tourName) throws ModelOperationException {
        if (tourName==null){
            return;
        }
        localTourList.deleteTour(tourName);
        try {
            backendTourManager.deleteTour(tourName);
            log.debug("DAL Layer delete Tour in Backend");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't delete Tour",throwables);
        }

    }

    public Tour getTourDetails(int tourID,String tourName) throws ModelOperationException {
        Tour returnTour= localTourList.getTour(tourName);
        if(returnTour != null){
            return returnTour;
        }
        log.debug("DAL Layer return TourDetails unconditionally");
        try {
            return backendTourManager.getTourDetails(tourID,tourName);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get tourDetails",throwables);
        }


    }

    public void updateTour(String actualTourName,String tourDescription, String desTourName
            ,String routeInformation, double tourDistance) throws ModelOperationException {
        try {
            backendTourManager.updateTour(actualTourName,tourDescription,desTourName,
                    routeInformation,tourDistance);
            localTourList.updateTour(actualTourName,tourDescription,desTourName,
                    routeInformation,tourDistance);
            log.debug("DAL Layer update TourDetails unconditionally");
            backendTourManager.updateTourVectorToken(desTourName);
            log.debug("Update Vector for Tour Indexing");
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get update TourAttributes",throwables);
        }

    }

    public String generateTourRandomName() {
        while (true) {
            String randomPart=generateRandomString();
            String tourName="Tour_"+randomPart;
            if (!localTourList.containsTour(tourName)){
                log.info("Generated random name for new Tour");
                return tourName;
            }
        }
    }

    private String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        log.debug("Generated random String");
        return generatedString;
    }

    public void updateTourRoute(String tourName,String from,String to) throws ModelOperationException {
        localTourList.updateTourRoute(tourName,from,to);
        try {
            backendTourManager.updateTourRoute(tourName,from,to);
            log.debug("DAL Layer update TourRoute unconditionally");
            backendTourManager.updateTourVectorToken(tourName);
            log.debug("Update Vector for Tour Indexing");

        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get update TourRoute",throwables);
        }

    }

    public List<String> fullTextSearch(String input) throws ModelOperationException {
        List<String> searchedTours = null;
        try {
            log.debug("Return all Searched Tours from Model");
            return searchedTours = backendTourManager.getToursFromSearch(input);
        } catch (TourDatabaseOperationException throwables) {
            throw new ModelOperationException("couldn't get searched Tours",throwables);
        }
    }

    public void updateDistance(String tourName,Double distance) throws ModelOperationException {
        localTourList.updateTourDistance(tourName,distance);
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
