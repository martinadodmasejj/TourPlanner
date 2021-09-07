package VIEW;

import BL.*;
import BL.Exceptions.*;
import BL.ReportGenerator;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import javafx.beans.value.ChangeListener;


public class MainViewModel {

    private TourListManager tourListManager;
    private TourLogManager tourLogManager;
    private final Logger log;
    ReportGenerator reportGenerator =null;

    private ObservableList<Tour> tourList;
    private ObservableList<TourLog> tourLogs;

    MapApiHandler mapApiHandler = new MapApiHandler();

    private final StringProperty tourName = new SimpleStringProperty("");
    private final StringProperty tourDistance = new SimpleStringProperty("");
    private final StringProperty tourDescription = new SimpleStringProperty("");
    private final StringProperty routeInformation = new SimpleStringProperty("");
    private final StringProperty fromDestination = new SimpleStringProperty("");
    private final StringProperty toDestination = new SimpleStringProperty("");
    private final StringProperty searchField = new SimpleStringProperty("");

    private final ObjectProperty<javafx.scene.image.Image> tourImage = new SimpleObjectProperty<>();
    private final ObjectProperty<ObservableList<TourLog>> tourLogsTable = new SimpleObjectProperty<>();

    private Tour selectedListItem=null;
    private TourLog selectedLog=null;
    private Tab selectedTab=null;

    public MainViewModel() throws TourLogManagerException, TourListManagerException, MapApiHandlerException {
        Configurator.initialize(null, "TourPlannerLog4j.conf.xml");
        log = LogManager.getLogger(MainViewModel.class);
        tourListManager = new TourListManager();
        tourLogManager = new TourLogManager();
        tourList = FXCollections.observableArrayList(tourListManager.getTours());
    }


    public ChangeListener<Tour> getSelectedTourChange(){
        return (observableValue,oldValue,newValue)->{
            selectedListItem = newValue;
        };
    }

    public ChangeListener<TourLog> getSelectedTourLogChange(){
        return (observableValue,oldValue,newValue)->{
            selectedLog = newValue;
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


    public ObjectProperty<javafx.scene.image.Image> tourImageProperty(){
        System.out.println("Image init");
        return tourImage;
    }

    public ObjectProperty<ObservableList<TourLog>> tourLogsTableProperty() {
        System.out.println("TableView init");
        return tourLogsTable;
    }

    public ObservableList<Tour> getTourList() {
        return tourList;
    }

    private void deleteTourPicture(){
        File file = new File("src/main/resources/View/pictures/"+selectedListItem+".jpg");
        file.delete();
    }

    public void addTour() throws TourListManagerException, MapApiHandlerException {
        Tour newTour = new Tour();
        newTour.setTourName(tourName.getValue());
        newTour.setTourDescription(tourDescription.getValue());
        newTour.setRouteInformation(routeInformation.getValue());
        newTour.setTourTo(toDestination.getValue());
        newTour.setTourFrom(fromDestination.getValue());
        tourListManager.addTour(newTour);
        tourList.add(newTour);
        log.debug("MVM Tour Insertion");
        if (newTour.getTourTo()!=null && newTour.getTourFrom()!=null){
            saveTourPicture(newTour);
        }
    }

    public void deleteTour() throws TourListManagerException {
        deleteTourPicture();
        tourListManager.deleteTour(selectedListItem);
        tourList.remove(selectedListItem);
        log.debug("MVVM Tour Deletion");
    }

    public void updateTour() throws TourListManagerException, MapApiHandlerException {
        if(selectedListItem == null){
            return;
        }
        String oldName = selectedListItem.getTourName();
        String tourName=this.tourName.getValue();
        String tourDescription=this.tourDescription.getValue();
        String routeInformation=this.routeInformation.getValue();
        double tourDistance= Double.parseDouble(this.tourDistance.getValue());
        String from = this.fromDestination.get();
        String to = this.toDestination.get();
        selectedListItem.setTourName(this.tourName.getValue());
        selectedListItem.setTourDescription(this.tourDescription.getValue());
        selectedListItem.setRouteInformation(this.routeInformation.getValue());
        selectedListItem.setTourFrom(this.fromDestination.get());
        selectedListItem.setTourTo(this.toDestination.get());
        tourListManager.updateTour(oldName,tourDescription,tourName,routeInformation,tourDistance,from,to);
        saveTourPicture(selectedListItem);
        if (tourName.equals(selectedListItem)){
            return;
        }
        int curr_pos=tourList.indexOf(selectedListItem);
        tourList.set(curr_pos,selectedListItem);
        log.debug("MVVM Tour Updated");

    }

    private void saveTourPicture(Tour newTour) throws MapApiHandlerException, TourListManagerException {
        log.debug("MVVM send TourPicture request");
        Double distance = mapApiHandler.sendImageRequest(newTour.getTourFrom(),newTour.getTourTo(),newTour.getTourName());
        tourListManager.updateTourDistance(newTour.getTourName(),distance);
        newTour.setTourDistance(distance);
        Image resultImage = new Image("file:src/main/resources/View/pictures/"+tourName+".jpg");
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
        tourName.set(selectedListItem.getTourName());
        tourDistance.set(String.valueOf(selectedListItem.getTourDistance()));
        tourDescription.set(selectedListItem.getTourDescription());
        routeInformation.set(selectedListItem.getRouteInformation());
        fromDestination.set(selectedListItem.getTourFrom());
        toDestination.set(selectedListItem.getTourTo());
        displayTourPicture(selectedListItem.getTourName());
        log.info("display TourAttributes on UI");
    }


    public void generateTourReport() throws ReportGeneratorException {
        if (reportGenerator == null){
            reportGenerator = new ReportGenerator();
        }
        reportGenerator.generateTourReport(selectedListItem);
    }

    public void generateTourSummaryReport() throws ReportGeneratorException{
        if (reportGenerator == null){
            reportGenerator = new ReportGenerator();
        }
        reportGenerator.genereateTourSummaryReport();
    }

    public void searchTours() throws TourListManagerException {
        List<Tour> tours=tourListManager.fullTextSearch(searchField.get());
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
            tourLogManager.addTourLog(selectedListItem.getTourName());
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
            tourLogs=FXCollections.observableArrayList(tourLogManager.getAllTourLogs(selectedListItem.getTourName()));
            tourLogsTable.set(tourLogs);
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

}
