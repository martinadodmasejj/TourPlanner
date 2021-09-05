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
        String tourTest="TestTour";
        List<String> tourList;
        //Act
        //dataModelBackend.addTour(tourTest);
        tourList=dataModelBackend.getTours();
        dataModelBackend.deleteTour(tourTest); // Cleanup
        //Assert
        Assert.assertEquals(tourTest,tourList.get(tourList.size()-1));
    }

    @Test
    public void testRandomGeneration() {
        //Arrange
        Model dataModelBackend = Model.getTestModelInstance();
        String randomName="";
        String generatedName = "";
        Boolean matches=false;
        //Act
        for (int i=0;i<100;i++){
            generatedName = dataModelBackend.generateTourRandomName();
            if(randomName.equals(generatedName)){
                matches=true;
                break;
            }

        }
        //Assert
        Assert.assertEquals(false,matches);
    }

/* todo after improving LocalTourList */
    @Test
    public void testTourUpdate() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        String tourTest="TestTour";
        Tour tourDetails;
        //Act
        //dataModelBackend.addTour(tourTest);
       // dataModelBackend.updateTour(tourTest,"testDescription"
            //    ,"NewName","TestRoute",100);
        tourDetails=dataModelBackend.getTourDetails(0,"NewName");
        dataModelBackend.deleteTour("NewName");
        //Assert
        Assert.assertEquals("NewName",tourDetails.getTourName());
        Assert.assertEquals("testDescription",tourDetails.getTourDescription());
        Assert.assertEquals("TestRoute",tourDetails.getRouteInformation());
        Assert.assertEquals("100.0",String.valueOf(tourDetails.getTourDistance()));
    }

    @Test
    public void testGetTourRoute() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        String tourTest="TestTour";
        //Act
        //dataModelBackend.addTour(tourTest);
        //dataModelBackend.updateTourRoute(tourTest,"Wien","Berlin");
        Tour tourDetails = dataModelBackend.getTourDetails(0,tourTest);
        dataModelBackend.deleteTour(tourTest);
        //Assert
        Assert.assertEquals("Wien",tourDetails.getTourFrom());
        Assert.assertEquals("Berlin",tourDetails.getTourTo());
    }

    @Test
    public void testTourDistanceUpdate() throws ModelOperationException {
        //Arrange
        Model dataModelBackend = Model.getModelInstance();
        String tourTest="TestTour";
        //Act
        //dataModelBackend.addTour(tourTest);
        dataModelBackend.updateDistance("TestTour",100.69);
        Tour tourDetails = dataModelBackend.getTourDetails(0,tourTest);
        dataModelBackend.deleteTour(tourTest);
        //Assert
        Assert.assertEquals(String.valueOf(tourDetails.getTourDistance()),"100.69");
    }


}
