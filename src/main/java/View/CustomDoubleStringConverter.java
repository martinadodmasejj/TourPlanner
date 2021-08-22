package View;

import javafx.util.converter.DoubleStringConverter;

public class CustomDoubleStringConverter extends DoubleStringConverter {
    private final DoubleStringConverter converter = new DoubleStringConverter();

    @Override
    public String toString(Double object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return null;
    }

    @Override
    public Double fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return null;
    }

    private void showAlert(Exception e){
        System.out.println(e.getMessage());
    }
}
