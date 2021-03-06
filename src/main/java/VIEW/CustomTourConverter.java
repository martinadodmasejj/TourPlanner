package VIEW;

import DATATYPES.Tour;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

import java.util.List;

public class CustomTourConverter extends StringConverter<Tour> {
    private final ListCell<Tour> cell;
    public CustomTourConverter(ListCell<Tour> cell) {
        this.cell = cell ;
    }
    public CustomTourConverter() { cell = null; }

    @Override
    public String toString(Tour tour) {
        return tour.getTourName();
    }

    @Override
    public Tour fromString(String string) {
        Tour tour = cell.getItem();
        tour.setTourName(string);
        return tour;
    }
}
