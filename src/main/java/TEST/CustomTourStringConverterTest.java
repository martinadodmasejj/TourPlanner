package TEST;

import BL.Exceptions.MapApiHandlerException;
import BL.Exceptions.TourListManagerException;
import BL.Exceptions.TourLogManagerException;
import DATATYPES.Tour;
import VIEW.Controller;
import VIEW.CustomTourConverter;
import com.jfoenix.controls.JFXListCell;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;

public class CustomTourStringConverterTest {

    private CustomTourConverter converter;
    private ObservableList<Tour> tourObservableList;
    private List<Tour> tourList;

    public CustomTourStringConverterTest() {
        // Arrange
        converter = new CustomTourConverter();
    }

    @Test
    public void testTourToStringConversion(){
        //Act
        String testTourName = "testTour";
        String actualTourName = converter.toString(new Tour(testTourName));
        //Assert
        Assert.assertEquals(testTourName,actualTourName);
    }
}
