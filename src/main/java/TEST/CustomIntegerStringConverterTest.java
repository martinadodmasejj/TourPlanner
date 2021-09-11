package TEST;

import VIEW.CustomIntegerStringConverter;
import org.junit.Assert;
import org.junit.Test;

public class CustomIntegerStringConverterTest {
    CustomIntegerStringConverter converter;

    public CustomIntegerStringConverterTest(){
        //Arrange
        converter = new CustomIntegerStringConverter();
    }

    @Test
    public void ConvertIntegerToString(){
        //Act
        String testNumber = converter.toString(123);
        //Assert
        Assert.assertEquals("123",testNumber);
    }

    @Test
    public void ConvertStringToInteger(){
        //Act
        int testNumber = converter.fromString("123");
        //Assert
        Assert.assertEquals(123,testNumber);
    }

    @Test
    public void ConvertIncorrectStringToInteger(){
        //Act
        Integer testNumber = converter.fromString("testNumber");
        //Assert
        Assert.assertEquals(null,testNumber);
    }

}


