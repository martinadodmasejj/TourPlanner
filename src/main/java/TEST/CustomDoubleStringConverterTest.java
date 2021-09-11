package TEST;

import VIEW.CustomDoubleStringConverter;
import org.junit.Assert;
import org.junit.Test;

public class CustomDoubleStringConverterTest {

    CustomDoubleStringConverter converter;

    public CustomDoubleStringConverterTest (){
        //Arrange
        converter = new CustomDoubleStringConverter();
    }

    @Test
    public void ConvertDoubleToString(){
        //Act
        String testNumber = converter.toString(20.32400);
        //Assert
        Assert.assertEquals("20.324",testNumber);
    }

    @Test
    public void ConvertStringToDouble(){
        //Act
        Double testNumber = converter.fromString("20.32400");
        //Assert
        Assert.assertEquals(new Double(20.32400),testNumber);
    }

    @Test
    public void ConvertIncorrectStringToDouble(){
        //Act
        Double testNumber = converter.fromString("testNumber");
        //Assert
        Assert.assertEquals(null,testNumber);
    }

}
