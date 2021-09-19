package DATATYPES;


public class TourLog {

    String logReport;
    double traveledDistance;
    double duration;
    int rating;
    double avgSpeed;
    String author;
    String remarks;
    int joule;
    String weather;
    String timestamp;

    public TourLog(){
        this.logReport = "";
        this.traveledDistance = 0.0;
        this.duration = 0.0;
        this.rating = 0;
        this.avgSpeed = 0.0;
        this.author = "anonymous";
        this.remarks = "";
        this.joule = 0;
        this.weather = "";
    }


    public TourLog(String logReport, double traveledDistance, double duration, int rating,
                   double avgSpeed, String author, String specialRemarks, int joule, String weather,String timestamp) {
        this.logReport = logReport;
        this.traveledDistance = traveledDistance;
        this.duration = duration;
        this.rating = rating;
        this.avgSpeed = avgSpeed;
        this.author = author;
        this.remarks = specialRemarks;
        this.joule = joule;
        this.weather = weather;
        this.timestamp = timestamp;
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
