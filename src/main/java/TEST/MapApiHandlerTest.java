package TEST;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class MapApiHandlerTest {
    //Arrange
    String testResponse = "{\n" +
            "\t\"route\": {\n" +
            "\t\t\"hasTollRoad\": true,\n" +
            "\t\t\"hasBridge\": false,\n" +
            "\t\t\"boundingBox\": {\n" +
            "\t\t\t\"lr\": {\n" +
            "\t\t\t\t\"lng\": 16.373583,\n" +
            "\t\t\t\t\"lat\": 47.069527\n" +
            "\t\t\t},\n" +
            "\t\t\t\"ul\": {\n" +
            "\t\t\t\t\"lng\": 15.281363,\n" +
            "\t\t\t\t\"lat\": 48.192204\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"distance\": 120.009\n" +
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
        Assert.assertEquals("47.069527",String.valueOf(lrLat));
        Assert.assertEquals("16.373583",String.valueOf(lrLng));

        Assert.assertEquals("48.192204",String.valueOf(ulLat));
        Assert.assertEquals("15.281363",String.valueOf(ulLng));
    }

    @Test
    public void testDistanceConversion() {
        //Act
        JSONObject jsonObject = new JSONObject(testResponse);
        JSONObject route = jsonObject.getJSONObject("route");
        Double distanceValue = route.getDouble("distance");

        //Assert
        Assert.assertEquals("120.009",String.valueOf(distanceValue));
    }



}
