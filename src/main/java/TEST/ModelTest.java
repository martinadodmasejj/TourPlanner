package TEST;

import DAL.Exceptions.ModelOperationException;
import DAL.Model;
import DATATYPES.Tour;
import org.junit.Assert;
import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;

import java.util.List;

//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;

public class ModelTest {

    @Test
    public void testToursCreation() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        Tour testTour = new Tour("tourTest");
        List<Tour> tourList;
        //Act
        dataModelBackend.addTour(testTour);
        tourList=dataModelBackend.getTours();
        dataModelBackend.deleteTour(testTour);// Cleanup
        boolean tourExists = false;
        for(Tour tour : tourList){
            if (tour.getTourName().equals("tourTest")){
                tourExists = true;
            }
        }
        //Assert
        Assert.assertEquals(true,tourExists);
    }



    @Test
    public void testTourUpdate() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        Tour testTour = new Tour("tourTest");
        //Act
        dataModelBackend.addTour(testTour);
        testTour.setTourName("tourTestUpdate");
        testTour.setTourDescription("testDescription");
        dataModelBackend.updateTour("tourTest",testTour);
        Tour resultTour = dataModelBackend.getTourDetails(0,"tourTestUpdate");
        dataModelBackend.deleteTour(testTour);
        //Assert
        Assert.assertEquals("testDescription",resultTour.getTourDescription());
    }


    @Test
    public void testTourDistanceUpdate() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        Tour testTour= new Tour("TestTour");
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
        Model dataModelBackend = Model.getModelInstance();
        Tour testTour= new Tour("TestTour");
        testTour.setTourTo("Wien");
        testTour.setTourFrom("Berlin");
        //Act
        dataModelBackend.addTour(testTour);
        Tour tourDetails = dataModelBackend.getTourDetails(0,"TestTour");
        dataModelBackend.deleteTour(testTour);
        //
        Assert.assertEquals("Wien",tourDetails.getTourTo());
        Assert.assertEquals("Berlin",tourDetails.getTourFrom());

    }


}
