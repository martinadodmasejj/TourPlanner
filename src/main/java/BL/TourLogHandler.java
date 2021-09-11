package BL;

import BL.Exceptions.TourLogManagerException;
import DAL.Exceptions.ModelOperationException;
import DAL.Model;
import DATATYPES.TourLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class TourLogHandler {
    private Model model;
    private Logger log;

    public TourLogHandler() throws  TourLogManagerException {
        try {
            model=Model.getModelInstance();
            log = LogManager.getLogger(TourLogHandler.class);
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not get DAL ModelInterface",e);
        }
    }


    public void addTourLog(String tourName) throws TourLogManagerException {
        try {
            model.addTourLog(tourName);
            log.debug("BL added TourLog");
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not add TourLog",e);
        }
    }

    public void deleteTourLog(String timestamp) throws TourLogManagerException {
        try {
            model.deleteTourLog(timestamp);
            log.debug("BL deleted TourLog");
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not delete TourLog",e);
        }
    }

    public List<TourLog> getAllTourLogs(String tourName) throws TourLogManagerException {
        try {
            if(model.getAllTourLogs(tourName)==null){
                return Collections.emptyList();
            }
            log.debug("BL returned all TourLogs for specific Tour");
            return model.getAllTourLogs(tourName);
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not get All TourLogs for selected Tour",e);
        }
    }

    public TourLog getTourLog (String timestamp) throws TourLogManagerException {
        try {
            log.debug("BL returned specific TourLog");
            return model.getTourLog(timestamp);
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not get TourLog",e);
        }
    }

    public void updateTourLog (TourLog tourLog) throws TourLogManagerException {
        try {
            log.debug("BL updated specific TourLog");
            model.updateTourLog(tourLog);
        } catch (ModelOperationException e) {
            throw new TourLogManagerException("Could not update TourLog",e);
        }
    }
}
