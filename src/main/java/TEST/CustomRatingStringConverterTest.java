package TEST;

import VIEW.CustomRatingStringConverter;
import org.junit.Assert;
import org.junit.Test;

public class CustomRatingStringConverterTest {
    CustomRatingStringConverter converter;

    public CustomRatingStringConverterTest(){
        //Arrange
        converter = new CustomRatingStringConverter();
    }

    @Test
    public void testWorkingRating(){
        //Act
        int number = converter.fromString("5");
        //Assert
        Assert.assertEquals(5,number);
    }

    @Test
    public void testRatingOver5(){
        //Act
        int number = converter.fromString("124");
        //Assert
        Assert.assertEquals(0,number);
    }

    @Test
    public void testNegativeRating(){
        //Act
        int number = converter.fromString("-124");
        //Assert
        Assert.assertEquals(0,number);
    }



}
