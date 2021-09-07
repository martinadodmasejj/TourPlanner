package TEST;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class MapApiHandlerTest {
    //Arrange
    String testResponse = "{\n" +
            "\t\"route\": {\n" +
            "\t\t\"boundingBox\": {\n" +
            "\t\t\t\"lr\": {\n" +
            "\t\t\t\t\"lng\": 20.123555,\n" +
            "\t\t\t\t\"lat\": 69.343466\n" +
            "\t\t\t},\n" +
            "\t\t\t\"ul\": {\n" +
            "\t\t\t\t\"lng\": 42.123123,\n" +
            "\t\t\t\t\"lat\": 50.145577\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"distance\": 200.333\n" +
            "    }\n" +
            "}";
    @Test
    public void testLatLngConversion() {
        //Act
        JSONObject jsonObject = new JSONObject(testResponse);
        JSONObject route = jsonObject.getJSONObject("route");
        JSONObject boundingBox = route.getJSONObject("boundingBox");
        JSONObject ul = boundingBox.getJSONObject("ul");
        JSONObject lr = boundingBox.getJSONObject("lr");
        Double ulLat = ul.getDouble("lat");
        Double ulLng = ul.getDouble("lng");
        Double lrLat = lr.getDouble("lat");
        Double lrLng = lr.getDouble("lng");

        //Assert
        Assert.assertEquals("69.343466",String.valueOf(lrLat));
        Assert.assertEquals("20.123555",String.valueOf(lrLng));

        Assert.assertEquals("50.145577",String.valueOf(ulLat));
        Assert.assertEquals("42.123123",String.valueOf(ulLng));
    }

    @Test
    public void testDistanceConversion() {
        //Act
        JSONObject jsonObject = new JSONObject(testResponse);
        JSONObject route = jsonObject.getJSONObject("route");
        Double distanceValue = route.getDouble("distance");

        //Assert
        Assert.assertEquals("200.333",String.valueOf(distanceValue));
    }



}
