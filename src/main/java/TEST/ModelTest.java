package TEST;

import DAL.Exceptions.ModelOperationException;
import DAL.Model;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import org.junit.Assert;
import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;

import java.util.List;

//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;

public class ModelTest {

    Model dataModelBackend;
    Tour testTour;

    public ModelTest() throws ModelOperationException {
        //Arrange
        dataModelBackend = Model.getModelInstance();
        testTour = new Tour("TestTour");
    }

    @Test
    public void testToursCreation() throws ModelOperationException {

        //Act
        dataModelBackend.addTour(testTour);
        List<Tour> tourList=dataModelBackend.getTours();
        dataModelBackend.deleteTour(testTour);// Cleanup
        boolean tourExists = false;
        for(Tour tour : tourList){
            if (tour.getTourName().equals("TestTour")){
                tourExists = true;
            }
        }
        //Assert
        Assert.assertEquals(true,tourExists);
    }



    @Test
    public void testTourUpdate() throws ModelOperationException {
        //Act
        dataModelBackend.addTour(testTour);
        testTour.setTourName("tourTestUpdate");
        testTour.setTourDescription("testDescription");
        dataModelBackend.updateTour("TestTour",testTour);
        Tour resultTour = dataModelBackend.getTourDetails(0,"tourTestUpdate");
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("testDescription",resultTour.getTourDescription());
    }


    @Test
    public void testTourDistanceUpdate() throws ModelOperationException {
        //Act
        dataModelBackend.addTour(testTour);
        dataModelBackend.updateDistance("TestTour",10.1);
        Tour tourDetails = dataModelBackend.getTourDetails(0,"TestTour");
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("10.1",String.valueOf(tourDetails.getTourDistance()));
    }

    @Test
    public void testGetTourDetails() throws ModelOperationException{
        //Arrange
        testTour.setTourTo("Wien");
        testTour.setTourFrom("Berlin");
        //Act
        dataModelBackend.addTour(testTour);
        Tour tourDetails = dataModelBackend.getTourDetails(0,"TestTour");
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("Wien",tourDetails.getTourTo());
        Assert.assertEquals("Berlin",tourDetails.getTourFrom());
    }

    @Test
    public void testAddTourLog() throws ModelOperationException {
        //Act
        dataModelBackend.addTour(testTour);
        dataModelBackend.addTourLog(testTour.getTourName());
        List<TourLog> tourLogs = dataModelBackend.getAllTourLogs(testTour.getTourName());
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("empty",tourLogs.get(0).getLogReport());
    }

    @Test
    public void testUpdateTourLog() throws ModelOperationException {
        //Act
        dataModelBackend.addTour(testTour);
        dataModelBackend.addTourLog(testTour.getTourName());
        List<TourLog> tourLogs = dataModelBackend.getAllTourLogs(testTour.getTourName());
        tourLogs.get(0).setLogReport("updated Report");
        dataModelBackend.updateTourLog(tourLogs.get(0));
        tourLogs = dataModelBackend.getAllTourLogs(testTour.getTourName());
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("updated Report",tourLogs.get(0).getLogReport());
    }

    @Test
    public void testFullTextSearch() throws ModelOperationException {
        //Act
        dataModelBackend.addTour(testTour);
        dataModelBackend.addTourLog(testTour.getTourName());
        List<TourLog> tourLogs = dataModelBackend.getAllTourLogs(testTour.getTourName());
        tourLogs.get(0).setRemarks("broken Road");
        dataModelBackend.updateTourLog(tourLogs.get(0));
        List<Tour> tourList = dataModelBackend.fullTextSearch("broken");
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals(testTour.getTourName(),tourList.get(0).getTourName());

    }



}
