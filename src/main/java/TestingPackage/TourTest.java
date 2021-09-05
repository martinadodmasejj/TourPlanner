package TestingPackage;

import Datatypes.Tour;
import org.junit.Assert;
import org.junit.Test;

public class TourTest {
    // Arrange
    int tourID=1;
    String tourName="testName";
    double tourDistance=111;
    String tourDescription="testDescription";
    String routeInformation="testRoute";
    String from = "Wien";
    String to = "Graz";
    // Act
    Tour tour=new Tour(tourID,tourDistance,tourName,tourDescription,routeInformation,from,to);

    @Test
    public void createTourIDTest(){
        //Assert
        Assert.assertEquals(tourID,tour.getTourID());
    }
    @Test
    public void createTourNameTest(){
        Assert.assertEquals(tourName,tour.getTourName());
    }
    @Test
    public void createTourDescriptionTest(){
        Assert.assertEquals(tourDescription,tour.getTourDescription());
    }
    @Test
    public void createTourRouteInfoTest(){
        Assert.assertEquals(routeInformation,tour.getRouteInformation());
    }
    @Test
    public void createTourRouteFrom(){
        Assert.assertEquals(from,tour.getTourFrom());
    }
    @Test
    public void createTourRouteTo(){
        Assert.assertEquals(to,tour.getTourTo());
    }

}
