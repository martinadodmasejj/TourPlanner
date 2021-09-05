package TEST;

import DAL.Local.LocalTourList;
import DATATYPES.Tour;
import org.junit.Assert;
import org.junit.Test;

public class LocalTourListTest {

    LocalTourList localTourList = LocalTourList.getTourListManagerInstance();

    @Test
    public void testTourAdd(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        String tour= localTourList.getToursUI().get(localTourList.getToursUI().size()-1);
        localTourList.deleteTour("testTour1");
        //Assert
        Assert.assertEquals("testTour1",tour);
    }

    @Test
    public void testGetTour(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        Tour actualTour= localTourList.getTour("testTour1");
        localTourList.deleteTour("testTour1");
        //Assert
        Assert.assertEquals("testTour1",actualTour.getTourName());
    }

    @Test
    public void testGetNonExistingTour(){
        Tour actualTour= localTourList.getTour("testTour1");
        //Assert
        Assert.assertEquals(null,actualTour);
    }

    @Test
    public void testTourDelete(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        localTourList.deleteTour("testTour1");
        Tour tour= localTourList.getTour("testTour1");
        //Assert
        Assert.assertEquals(null,tour);
    }

    @Test
    public void test_NonExisting_TourDelete(){
        //Arrange
        //Act
        boolean isSuccessful= localTourList.deleteTour("testTour1");
        //Assert
        Assert.assertEquals(false,isSuccessful);
    }

    @Test
    public void test_NonExisting_TourUpdate(){
        //Arrange
        //Act
        //boolean isSuccessful= localTourList.updateTour("testTour1","test",
          //      "newTourTest","test",111);
        //Assert
        //Assert.assertEquals(false,isSuccessful);
    }

    @Test
    public void testTourUpdate(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        //boolean isSuccessful= localTourList.updateTour("testTour1","test",
          //      "newTourTest","test",111);
        Tour updatedTour= localTourList.getTour("newTourTest");
        //Assert
        //Assert.assertEquals(true,isSuccessful);
        Assert.assertEquals("newTourTest",updatedTour.getTourName());
        Assert.assertEquals("test",updatedTour.getRouteInformation());
        Assert.assertEquals("test",updatedTour.getTourDescription());
        Assert.assertEquals("111.0",String.valueOf(updatedTour.getTourDistance()));
        localTourList.deleteTour("testTour1");
    }

    @Test
    public void testTourRouteUpdate(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        boolean isSuccessful= localTourList.updateTourRoute("testTour1","Wien","Berlin");
        Tour updatedTour= localTourList.getTour("testTour1");
        //Assert
        Assert.assertEquals(true,isSuccessful);
        Assert.assertEquals("Wien",updatedTour.getTourFrom());
        Assert.assertEquals("Berlin",updatedTour.getTourTo());
        localTourList.deleteTour("testTour1");
    }

    @Test
    public void test_NonExisting_TourRouteUpdate(){
        //Arrange
        //Act
        boolean isSuccessful= localTourList.updateTourRoute("testTour1","Wien","Berlin");
        //Assert
        Assert.assertEquals(false,isSuccessful);
    }

    @Test
    public void testDistanceUpdate(){
        //Arrange
        //Act
        localTourList.addTour(new Tour("testTour1"));
        localTourList.updateTourDistance("testTour1",100.0);
        Tour updatedTour= localTourList.getTour("testTour1");
        //Assert
        Assert.assertEquals(String.valueOf(updatedTour.getTourDistance()),"100.0");
        localTourList.deleteTour("testTour1");
    }


}
