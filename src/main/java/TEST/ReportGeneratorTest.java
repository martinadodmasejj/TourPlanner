package TEST;

import BL.Exceptions.ReportGeneratorException;
import BL.ReportGenerator;
import DAL.Exceptions.ModelOperationException;
import DAL.Model;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class ReportGeneratorTest {

    //Arrange all tests



    @Test
    public void calculateStatsTourLogs() throws ReportGeneratorException, ModelOperationException {
        //Arrange
        ReportGenerator reportGenerator = new ReportGenerator();
        Model model = Model.getModelInstance();
        List<Tour> tourList;
        List<TourLog> logList;
        tourList = model.getTours();
        String goalTour = "";
        for(Tour tour : tourList){
            logList = model.getAllTourLogs(tour.getTourName());
            if (!logList.isEmpty()){
                goalTour = tour.getTourName();
                break;
            }
        }
        //Act
        boolean isEmpty = true;
        HashMap<String,String> results = reportGenerator.calculateStatsTourLogs(goalTour);
        if (Double.valueOf(results.get("average Duration")) > 0){
            isEmpty = false;
        }
        // Assert
        Assert.assertEquals(false,isEmpty);
    }

    @Test
    public void calculateStatsTourLogsEmpty() throws ReportGeneratorException, ModelOperationException {
        //Arrange
        ReportGenerator reportGenerator = new ReportGenerator();
        Model model = Model.getModelInstance();
        List<Tour> tourList;
        List<TourLog> logList;
        tourList = model.getTours();
        String goalTour = "";
        for(Tour tour : tourList){
            logList = model.getAllTourLogs(tour.getTourName());
            if (logList.isEmpty()){
                goalTour = tour.getTourName();
                break;
            }
        }
        //Act
        boolean isEmptyStats = false;
        HashMap<String,String> results = reportGenerator.calculateStatsTourLogs(goalTour);
        if (results.isEmpty()){
            isEmptyStats = true;
        }
        // Assert
        Assert.assertEquals(true,isEmptyStats);
    }

}
