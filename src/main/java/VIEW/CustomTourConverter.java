package VIEW;

import DATATYPES.Tour;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

public class CustomTourConverter extends StringConverter<Tour> {
    private final ListCell<Tour> cell;
    public CustomTourConverter(ListCell<Tour> cell) {
        this.cell = cell ;
    }

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
