package TEST;

import DATATYPES.Tour;
import DATATYPES.TourLog;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

public class TourLogTest {
    // Arrange
    String logReport="testReport";
    double traveledDistance=111;
    int rating = 5;
    double duration = 20.11;
    double avgSpeed= 10.1;
    String author = "testAuthor";
    String remarks = "testRemarks";
    int joule = 1000;
    String weather = "testWeather";
    String timestamp = "19.09.2021";

    // Act
    TourLog tourLog = new TourLog(logReport,traveledDistance,duration,rating,avgSpeed
    ,author,remarks,joule,weather,timestamp);

    @Test
    public void testLogReport(){
        Assert.assertEquals(logReport,tourLog.getLogReport());
    }

    @Test
    public void testTraveledDistance(){
        Assert.assertEquals(String.valueOf(traveledDistance),String.valueOf(tourLog.getTraveledDistance()));
    }

    @Test
    public void testRating(){
        Assert.assertEquals(rating,tourLog.getRating());
    }

    @Test
    public void testAvgSpeed(){
        Assert.assertEquals(String.valueOf(avgSpeed),String.valueOf(tourLog.getAvgSpeed()));
    }

    @Test
    public void testDuration(){
        Assert.assertEquals(String.valueOf(duration),String.valueOf(tourLog.getDuration()));
    }

    @Test
    public void testAuthor(){
        Assert.assertEquals(author,tourLog.getAuthor());
    }

    @Test
    public void testRemarks(){
        Assert.assertEquals(remarks,tourLog.getRemarks());
    }

    @Test
    public void testJoule(){
        Assert.assertEquals(joule,tourLog.getJoule());
    }

    @Test
    public void testTimestamp(){
        Assert.assertEquals(timestamp,tourLog.getTimestamp());
    }

    @Test
    public void testWeather(){
        Assert.assertEquals(weather,tourLog.getWeather());
    }
}
