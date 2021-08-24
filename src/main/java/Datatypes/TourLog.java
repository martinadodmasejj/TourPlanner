package Datatypes;

import com.jfoenix.controls.JFXSlider;

public class TourLog {

    String logReport;
    double traveledDistance;
    double duration;
    String date;
    int rating;
    JFXSlider ratingSlider;
    double avgSpeed;
    String author;
    String remarks;
    int joule;
    String weather;
    String timestamp;

    private void initRatingSlider(int rating){
        this.ratingSlider = new JFXSlider();
        this.ratingSlider.setValue(rating);
        this.ratingSlider.setMax(5);
        this.ratingSlider.setMin(0);
        this.ratingSlider.setMajorTickUnit(1);
        this.ratingSlider.setMinorTickCount(1);
        this.ratingSlider.setSnapToTicks(true);
        this.ratingSlider.setBlockIncrement(1);
    }

    public TourLog(){
        this.logReport = "";
        this.traveledDistance = 0.0;
        this.duration = 0.0;
        this.date = "";
        this.rating = 0;
        initRatingSlider(rating);
        this.avgSpeed = 0.0;
        this.author = "anonymous";
        this.remarks = "";
        this.joule = 0;
        this.weather = "";
    }


    public TourLog(String logReport, double traveledDistance, double duration, String date, int rating,
                   double avgSpeed, String author, String specialRemarks, int joule, String weather) {
        this.logReport = logReport;
        this.traveledDistance = traveledDistance;
        this.duration = duration;
        this.date = date;
        this.rating = rating;
        this.avgSpeed = avgSpeed;
        initRatingSlider(rating);
        this.author = author;
        this.remarks = specialRemarks;
        this.joule = joule;
        this.weather = weather;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogReport() {
        return logReport;
    }

    public void setLogReport(String logReport) {
        this.logReport = logReport;
    }

    public double getTraveledDistance() {
        return traveledDistance;
    }

    public void setTraveledDistance(double traveledDistance) {
        this.traveledDistance = traveledDistance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDate () {
        return date;
    }

    public void setDate (String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String specialRemarks) {
        this.remarks = specialRemarks;
    }

    public int getJoule() {
        return joule;
    }

    public void setJoule(int joule) {
        this.joule = joule;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

}
