package BL;


import BL.Exceptions.MapApiHandlerException;
import BL.Exceptions.TourListManagerException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class MapApiHandler {
    String key;
    private final Logger log;
    TourListManager tourListManager;

    public MapApiHandler() throws  MapApiHandlerException {
        key = "sbA2AG4PAtKsucb54CDBLp8YOsxS8oL1";
        Configurator.initialize(null, "TourPlannerLog4j.conf.xml");
        log = LogManager.getLogger(MapApiHandler.class);
        try {
            tourListManager = new TourListManager();
        } catch (TourListManagerException e) {
            throw new MapApiHandlerException("could not get tourListManager Interface",e);
        }
    }


    private String sendFromToRequest(String from, String to) throws MapApiHandlerException {

        String requestUrl = "http://open.mapquestapi.com/directions/v2/route" +
                "?key=" + key + "&from=" + from + "&to=" + to;
        URI uri = null;
        try {
            uri = new URI(requestUrl);
        } catch (URISyntaxException e) {
            throw new MapApiHandlerException("could not generate URI for http request",e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .headers("Content-Type", "application/json")
                .GET()
                .build();

        log.trace("Sent 1st Async Request TourRoute");
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new MapApiHandlerException("String could not be read during http request",e);
        } catch (InterruptedException e) {
            throw new MapApiHandlerException("from to http request was interrupted",e);
        }

        return response.body();
    }

    public Image sendImageRequest(String from,String to,String tourName) throws MapApiHandlerException {

        String resp = sendFromToRequest(from,to);
        JSONObject jsonObject = new JSONObject(resp);
        JSONObject route = jsonObject.getJSONObject("route");
        String sessionId = route.getString("sessionId");
        JSONObject boundingBox = route.getJSONObject("boundingBox");
        JSONObject ul = boundingBox.getJSONObject("ul");
        JSONObject lr = boundingBox.getJSONObject("lr");
        Double ulLat = ul.getDouble("lat");
        Double ulLng = ul.getDouble("lng");
        Double lrLat = lr.getDouble("lat");
        Double lrLng = lr.getDouble("lng");
        Double distanceValue = route.getDouble("distance");
        try {
            tourListManager.updateTourDistance(tourName,distanceValue);
        } catch (TourListManagerException e) {
            throw new MapApiHandlerException("could not update Tour Distance",e);
        }

        if(true) {

            String requestUrl = "https://www.mapquestapi.com/staticmap/v5/map?key=" + key + "&size=1000,400" +
                    "&defaultMarker=marker-purple-sm&zoom=20&session=" + sessionId + "&boundingBox=" + ulLat + "," + ulLng + "," + lrLat + "," + lrLng;

            URI uri ;
            HttpRequest request;
            try {
                uri = new URI(requestUrl);
                request = HttpRequest.newBuilder()
                        .uri(uri)
                        .headers("Content-Type", "image/jpeg")
                        .GET()
                        .build();



                log.info("Send ImageRequest for TourImage");
                HttpResponse<InputStream> response = HttpClient.newBuilder()
                        .build()
                        .send(request, HttpResponse.BodyHandlers.ofInputStream());

                log.info("Get Image Response");
                BufferedImage image= ImageIO.read(response.body());
                File outfile = new File("src/main/resources/View/pictures/"+tourName+".jpg");
                ImageIO.write(image, "jpg", outfile);
                log.debug("Save image to file");
                return SwingFXUtils.toFXImage(image,null);

            } catch (URISyntaxException | IOException | InterruptedException e) {
                throw new MapApiHandlerException("could not send http request successfully to display image",e);
            }

        }
        else {

            String requestUrl = "https://www.mapquestapi.com/staticmap/v5/map?key=" + key + "&size=520,350" +
                    "&defaultMarker=marker-red-lg&zoom=11&session=" + sessionId + "&boundingBox=" + ulLat + "," + ulLng + "," + lrLat + "," + lrLng;
            URI uri = null;
            try {
                uri = new URI(requestUrl);
            } catch (URISyntaxException e) {
                throw new MapApiHandlerException("could not generate URI for http request for pdf export",e);
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .headers("Content-Type", "image/jpeg")
                    .GET()
                    .build();

            log.trace("Get Async Image Response");
            log.info("Send Async ImageRequest for ReportGenerator");
        }
        return  null;
    }

}
