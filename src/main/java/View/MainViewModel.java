package View;

import BusinessLayer.*;
import BusinessLayer.Exceptions.*;
import BusinessLayer.Exporting.JsonExporter;
import BusinessLayer.Exporting.PDFExporter;
import Datatypes.Tour;
import Datatypes.TourLog;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import javafx.beans.value.ChangeListener;
import org.mockito.internal.matchers.Any;


public class MainViewModel {

    private TourListManager tourListManager;
    private TourLogManager tourLogManager;
    private final Logger log;
    PDFExporter pdfExporter=null;

    private ObservableList<String> tourList;
    private ObservableList<TourLog> tourLogs;
    private ObservableList<String> tourRatings;
    private ObjectProperty<ObservableList<String>> tourListView;

    MapApiHttpHandler mapApiHttpHandler = new MapApiHttpHandler();
    JsonExporter jsonExporter = new JsonExporter();

    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty routeInformation = new SimpleStringProperty("");
    private final StringProperty fromDestination = new SimpleStringProperty("");
    private final StringProperty toDestination = new SimpleStringProperty("");
    private final StringProperty searchField = new SimpleStringProperty("");

    private final StringProperty logAuthor = new SimpleStringProperty("");
    private final StringProperty logReport = new SimpleStringProperty("");
    private final StringProperty logDistance = new SimpleStringProperty("");
    private final StringProperty logDuration = new SimpleStringProperty("");



    private final StringProperty logSpeed = new SimpleStringProperty("");
    private final StringProperty logRemarks = new SimpleStringProperty("");
    private final StringProperty logJoule = new SimpleStringProperty("");
    private final StringProperty logWeather = new SimpleStringProperty("");

    private final ObjectProperty<javafx.scene.image.Image> tourImage = new SimpleObjectProperty<>();
    private final ObjectProperty<ObservableList<TourLog>> tourLogsTable = new SimpleObjectProperty<>();
    private final ObjectProperty<SelectionModel> tourListSelectionProperty = new SimpleObjectProperty<>();

    private String selectedListItem=null;
    private TourLog selectedLog=null;
    private int selectedRating=0;
    private Tab selectedTab=null;

    public MainViewModel() throws TourLogManagerException, TourListManagerException, MapApiHandlerException, JsonExporterException {
        Configurator.initialize(null, "TourPlannerLog4j.conf.xml");
        log = LogManager.getLogger(MainViewModel.class);
        tourListManager = new TourListManager();
        tourLogManager = new TourLogManager();
        tourList = FXCollections.observableArrayList(tourListManager.getTours());
        tourListView = new SimpleObjectProperty<>(tourList);
        tourRatings = FXCollections.observableArrayList(new ArrayList<>(List.of("1","2","3","4","5")));
    }


    public ChangeListener<String> getSelectedTourChange(){
        return (observableValue,oldValue,newValue)->{
            selectedListItem = newValue;
        };
    }

    public ChangeListener<TourLog> getSelectedTourLogChange(){
        return (observableValue,oldValue,newValue)->{
            selectedLog = newValue;
        };
    }

    public ChangeListener<Tab> getSelectedTabChange(){
        return (observableValue,oldValue,newValue)->{
            selectedTab = newValue;
        };
    }


    public ChangeListener<String> getSelectedRatingChange(){
        return (observableValue,oldValue,newValue)->{
            selectedRating = Integer.valueOf(newValue);

        };
    }

    public StringProperty tourNameProperty() {
        System.out.println("VM: init tourName field");
        return tourName;
    }

    public StringProperty tourDistanceProperty() {
        System.out.println("VM: init tourDistance field");
        return tourDistance;
    }

    public StringProperty tourDescriptionProperty() {
        System.out.println("VM: init tourDescription field");
        return tourDescription;
    }

    public StringProperty routeInformationProperty() {
        System.out.println("VM: init routeInformation field");
        return routeInformation;
    }

    public StringProperty fromDestinationProperty(){
        System.out.println("VM: init fromDestination field");
        return fromDestination;
    }

    public StringProperty toDestinationProperty(){
        System.out.println("VM: init toDestination field");
        return toDestination;
    }

    public StringProperty searchFieldProperty(){
        System.out.println("VM: init search field");
        return searchField;
    }


    public StringProperty logAuthorProperty() {
        return logAuthor;
    }


    public StringProperty logReportProperty() {
        return logReport;
    }


    public StringProperty logDistanceProperty() {
        return logDistance;
    }

    public StringProperty logDurationProperty() {
        return logDuration;
    }


    public StringProperty logSpeedProperty() {
        return logSpeed;
    }



    public StringProperty logRemarksProperty() {
        return logRemarks;
    }



    public StringProperty logJouleProperty() {
        return logJoule;
    }


    public StringProperty logWeatherProperty() {
        return logWeather;
    }

    public ObjectProperty<javafx.scene.image.Image> tourImageProperty(){
        System.out.println("Image init");
        return tourImage;
    }

    public ObjectProperty<ObservableList<TourLog>> tourLogsTableProperty() {
        System.out.println("TableView init");
        return tourLogsTable;
    }

    public ObjectProperty<SelectionModel> tourListSelectionProperty(){
        return tourListSelectionProperty;
    }

    public ObservableList<String> getTourRatingsList(){
        return tourRatings;
    }

    public ObservableList<String> getTourList() {
        return tourList;
    }



    public void addTour() throws TourListManagerException, MapApiHandlerException {
        Tour newTour = new Tour();
        newTour.setTourName(tourName.getValue());
        newTour.setTourDescription(tourDescription.getValue());
        newTour.setRouteInformation(routeInformation.getValue());
        newTour.setTourTo(toDestination.getValue());
        newTour.setTourFrom(fromDestination.getValue());
        tourListManager.addTour(newTour);
        tourList.add(newTour.getTourName());
        log.debug("MVM Tour Insertion");
        if (newTour.getTourTo()!=null && newTour.getTourFrom()!=null){
            saveTourPicture(newTour.getTourTo(),newTour.getTourFrom(), newTour.getTourName());
        }
    }

    public void deleteTour() throws TourListManagerException {
        File file = new File("src/main/resources/View/pictures/"+selectedListItem+".jpg");
        file.delete();
        tourListManager.deleteTour(selectedListItem);
        tourList.remove(selectedListItem);
        log.debug("MVVM Tour Deletion");
    }

    public void updateTour() throws TourListManagerException, MapApiHandlerException {
        if(selectedListItem == null){
            return;
        }
        String tourName=this.tourName.getValue();
        String tourDescription=this.tourDescription.getValue();
        String routeInformation=this.routeInformation.getValue();
        double tourDistance= Double.parseDouble(this.tourDistance.getValue());
        String from = this.fromDestination.get();
        String to = this.toDestination.get();
        tourListManager.updateTour(selectedListItem,tourDescription,tourName,routeInformation,tourDistance,from,to);
        saveTourPicture(from,to,tourName);
        if (tourName.equals(selectedListItem)){
            return;
        }
        int curr_pos=tourList.indexOf(selectedListItem);
        tourList.set(curr_pos,tourName);
        log.debug("MVVM Tour Updated");
    }

    private void saveTourPicture(String from,String to,String tourName) throws MapApiHandlerException {
        //Image srcGif = new Image("file:src/main/resources/View/loading_2.gif");
        //tourImage.set(srcGif);
        log.debug("MVVM send TourPicture request");
        Image resultImage = mapApiHttpHandler.sendImageRequest(from,to,tourName);
        tourImage.set(resultImage);
        log.debug("Set Picture from Http Request");
    }

    private void displayTourPicture(String tourName) {
        Image resultImage = new Image("file:src/main/resources/View/pictures/"+tourName+".jpg");
        tourImage.set(resultImage);
        log.debug("Set Picture from File system");
    }

    public void displayTourAttributes() throws TourListManagerException {
        if (selectedListItem==null ){
            return;
        }
        Tour tourDetails=tourListManager.getTourAttributes(selectedListItem);
        if(tourDetails!=null) {
            tourName.set(tourDetails.getTourName());
            tourDistance.set(String.valueOf(tourDetails.getTourDistance()));
            tourDescription.set(tourDetails.getTourDescription());
            routeInformation.set(tourDetails.getRouteInformation());
            fromDestination.set(tourDetails.getTourFrom());
            toDestination.set(tourDetails.getTourTo());
            displayTourPicture(tourDetails.getTourName());
        }
        log.info("display TourAttributes on UI");
    }


    public void exportPdf() throws PDFExporterException {
        if (pdfExporter == null){
            pdfExporter = new PDFExporter();
        }
        if (selectedListItem == null){
            pdfExporter.exportToursPdf();
        }
        else {
            pdfExporter.exportTourPdf(selectedListItem);
        }
    }

    public void searchTours() throws TourListManagerException {
        List<String> tours=tourListManager.fullTextSearch(searchField.get());
        if(searchField.get().isEmpty()){
            tours = tourListManager.getTours();
            tourList.clear();
            tourList.addAll(tours);
            return;
        }
        tourList.clear();
        tourList.addAll(tours);
    }

    public void addTourLog() throws TourLogManagerException {
        if (selectedListItem != null){
            tourLogManager.addTourLog(selectedListItem);

            getAllTourLogs();
        }
    }

    public void deleteTourLog() throws TourLogManagerException {
        if (selectedLog != null && selectedListItem != null){
            tourLogManager.deleteTourLog(selectedLog.getTimestamp());
            getAllTourLogs();
        }
    }

    public void getAllTourLogs() throws TourLogManagerException {
        if (selectedListItem != null){
            tourLogs=FXCollections.observableArrayList(tourLogManager.getAllTourLogs(selectedListItem));
            tourLogsTable.set(tourLogs);
        }
    }

    public void displayTourLog() throws TourLogManagerException {
        if(selectedTab == null || !selectedTab.textProperty().get().equals("Logs")){
            return;
        }
        if (selectedLog != null){
            TourLog tourLog = tourLogManager.getTourLog(selectedLog.getTimestamp());
            if (tourLog == null){
                return;
            }
            else {
                logAuthor.set(tourLog.getAuthor());
                logReport.set(tourLog.getLogReport());
                logDistance.set(String.valueOf(tourLog.getTraveledDistance()));
                logDuration.set(String.valueOf(tourLog.getDuration()));
                logRemarks.set(tourLog.getRemarks());
                logJoule.set(String.valueOf(tourLog.getJoule()));
                logWeather.set(tourLog.getWeather());
                logSpeed.set(String.valueOf(tourLog.getAvgSpeed()));
            }
        }
    }

    public void updateTourLogAuthor(String value) throws TourLogManagerException {
        selectedLog.setAuthor(value);
        tourLogManager.updateTourLog(selectedLog);
    }
    public void updateTourLogReport(String value) throws TourLogManagerException {
        selectedLog.setLogReport(value);
        tourLogManager.updateTourLog(selectedLog);
    }

    public void updateTourLogDistance(Double value) throws TourLogManagerException {
        selectedLog.setTraveledDistance(value);
        tourLogManager.updateTourLog(selectedLog);
    }

    public void updateTourLogTime(Double value) throws TourLogManagerException {
        selectedLog.setDuration(value);
        tourLogManager.updateTourLog(selectedLog);
    }

    public void updateTourLogRating(Integer value) throws TourLogManagerException {
        selectedLog.setRating(value);
        tourLogManager.updateTourLog(selectedLog);
    }
    public void updateTourLogAvgSpeed(Double value) throws TourLogManagerException {
        selectedLog.setAvgSpeed(value);
        tourLogManager.updateTourLog(selectedLog);
    }

    public void updateTourLogRemarks(String value) throws TourLogManagerException {
        selectedLog.setRemarks(value);
        tourLogManager.updateTourLog(selectedLog);
    }
    public void updateTourLogWeather(String value) throws TourLogManagerException {
        selectedLog.setWeather(value);
        tourLogManager.updateTourLog(selectedLog);
    }
    public void updateTourLogJoule(Integer value) throws TourLogManagerException {
        selectedLog.setJoule(value);
        tourLogManager.updateTourLog(selectedLog);
    }


    public void exportJson() throws JsonExporterException {
        jsonExporter.exportJson(selectedListItem);
    }


}
