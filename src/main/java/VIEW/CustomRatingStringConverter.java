package VIEW;

import javafx.util.converter.IntegerStringConverter;

public class CustomRatingStringConverter extends IntegerStringConverter {

    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return null;
    }

    @Override
    public Integer fromString(String string) {
        try {
            int number = converter.fromString(string);
            if (number < 0){
                return 0;
            }
            else if (number > 5){
                return 0;
            }
            return number;
        } catch (NumberFormatException e) {
            showAlert(e);
        }
        return null;
    }

    private void showAlert(Exception e){
        System.out.println(e.getMessage());
    }

}
