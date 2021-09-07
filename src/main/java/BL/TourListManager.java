package BL;

import BL.Exceptions.TourListManagerException;
import DAL.Exceptions.ModelOperationException;
import DATATYPES.Tour;
import DAL.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class TourListManager {

    private Model model;
    private Logger log;

    public TourListManager() throws TourListManagerException {
        try {
            model=Model.getModelInstance();
            log = LogManager.getLogger(TourListManager.class);
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not get DAL ModelInterface",e);
        }
    }
    public TourListManager(boolean test) {
        if (test) {
            model = Model.getTestModelInstance();
        }
    }

    public void addTour (Tour newTour) throws TourListManagerException {
        try {
            model.addTour(newTour);
            log.debug("BL added Tour to List");
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not add Tour Distance",e);
        }
    }

    public void deleteTour (Tour tour) throws TourListManagerException {
        try {
            model.deleteTour(tour);
            log.debug("BL deleted Tour from List");
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not delete Tour",e);
        }
    }

    public void updateTour (String currentTour,String tourDescription,String tourName,String
                            routeInformation, Double tourDistance,String from,String to) throws TourListManagerException {

        try {
            model.updateTour(currentTour,tourDescription,tourName,routeInformation,tourDistance,from,to);
            log.debug("BL updated Tour in List");
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not update TourAttributes",e);
        }
    }

    public Tour getTourAttributes(String tourName) throws TourListManagerException {
        Tour tourDetails= null;
        try {
            tourDetails = model.getTourDetails(0,tourName);
            log.debug("BL retrieved specific Tour from List");
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not get TourAttributes",e);
        }
        return tourDetails;
    }


    public List<Tour> getTours() throws TourListManagerException {
        try {
            if(model.getTours()==null){
                return Collections.emptyList();
            }
            log.debug("BL returned all available Tours");
            return model.getTours();
        } catch (ModelOperationException e) {
            throw new TourListManagerException("couldn't get Tour List from Backend",e);
        }
    }


    public List<Tour> fullTextSearch(String input) throws TourListManagerException {
        try {
            log.debug("BL performed full text search on Tours");
            return model.fullTextSearch(input);
        } catch (ModelOperationException e) {
            throw new TourListManagerException("Could not perform fullTextSearch",e);
        }
    }

    public void updateTourDistance(String tourName,Double distance) throws TourListManagerException {
        try {
            log.debug("BL updated Tour Distance based on mapApiRequest");
            model.updateDistance(tourName,distance);
        } catch (ModelOperationException e) {
           throw new TourListManagerException("Could not update Tour Distance",e);
        }
    }

    public List<Tour> getAllToursAttributes(){
        log.debug("BL retrieved all Tours with all of their Info");
        return model.getAllToursDetails();
    }
}
