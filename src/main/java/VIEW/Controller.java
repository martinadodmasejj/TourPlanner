package VIEW;


import BL.Exceptions.*;
import DATATYPES.TourLog;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private final Logger log;
    public MainViewModel viewModel = new MainViewModel();

    public ListView tourList;
    public Tab attributesTab;
    public TextField tourName;
    public TextField tourDistance;
    public JFXTextArea searchField;
    public TextArea tourDescription;
    public TextArea routeInformation;
    public TextField fromDestination;
    public TextField toDestination;
    public ImageView tourImage;

    public TabPane selectionTab;
    public Tab logTab;

    public TableView tourLogsTable;
    public TableColumn dateColumn;
    public TableColumn reportColumn;
    public TableColumn distanceColumn;
    public TableColumn timeColumn;
    public TableColumn ratingColumn;
    public TableColumn avgSpeedColumn;
    public TableColumn remarksColumn;
    public TableColumn weatherColumn;
    public TableColumn jouleColumn;
    public TableColumn authorColumn;


    public Controller() throws TourListManagerException, TourLogManagerException, MapApiHandlerException {
        System.out.println("Controller generated");
        Configurator.initialize(null, "TourPlannerLog4j.conf.xml");
        log = LogManager.getLogger(Controller.class);

    }


    @FXML
    public void addTour(ActionEvent actionEvent) throws TourListManagerException, MapApiHandlerException {
        System.out.println("Controller generate new Tour");
        viewModel.addTour();
    }

    @FXML
    public void deleteTour(ActionEvent actionEvent) throws TourListManagerException {
        System.out.println("Controller deleting Tour ");
        viewModel.deleteTour();

    }

    @FXML
    public void updateTour(ActionEvent actionEvent) throws TourListManagerException, MapApiHandlerException {
        System.out.println("Controller updating Tour");
        viewModel.updateTour();

    }

    @FXML
    public void displayTourInfo(Event event) throws TourListManagerException, TourLogManagerException {
        displayTourDetails(event);
        getAllTourLogs(event);
    }

    @FXML
    public void displayTourDetails(Event actionEvent) throws TourListManagerException {
        System.out.println("Showing Details for selected Element");
        viewModel.displayTourAttributes();
    }


    @FXML
    public void generateTourReport(ActionEvent actionEvent) throws ReportGeneratorException {
        viewModel.generateTourReport();
    }

    @FXML
    public void generateSummaryReport(ActionEvent actionEvent) throws ReportGeneratorException {
        viewModel.generateTourSummaryReport();
    }


    @FXML
    public void searchTours(ActionEvent actionEvent) throws TourListManagerException {
        viewModel.searchTours();
    }

    @FXML
    public void addTourLog(ActionEvent actionEvent) throws TourLogManagerException {
        viewModel.addTourLog();
    }

    @FXML
    public void deleteTourLog(ActionEvent actionEvent) throws TourLogManagerException {
        viewModel.deleteTourLog();
    }

    @FXML
    private void getAllTourLogs(Event event) throws TourLogManagerException {
        viewModel.getAllTourLogs();
    }

    @FXML
    public void updateTourLogAuthor(TableColumn.CellEditEvent<TourLog, String> event) throws TourLogManagerException {
        viewModel.updateTourLogAuthor(event.getNewValue());
    }

    @FXML
    public void updateTourLogReport(TableColumn.CellEditEvent<TourLog, String> event) throws TourLogManagerException {
        viewModel.updateTourLogReport(event.getNewValue());
    }

    @FXML
    public void updateTourLogDistance(TableColumn.CellEditEvent<TourLog, Double> event) throws TourLogManagerException {
        viewModel.updateTourLogDistance(event.getNewValue());
    }

    @FXML
    public void updateTourLogTime(TableColumn.CellEditEvent<TourLog, Double> event) throws TourLogManagerException {
        viewModel.updateTourLogTime(event.getNewValue());
    }

    @FXML
    public void updateTourLogRating(TableColumn.CellEditEvent<TourLog, Integer> event) throws TourLogManagerException {
        viewModel.updateTourLogRating(event.getNewValue());
    }

    @FXML
    public void updateTourLogAvgSpeed(TableColumn.CellEditEvent<TourLog, Double> event) throws TourLogManagerException {
        viewModel.updateTourLogAvgSpeed(event.getNewValue());
    }

    @FXML
    public void updateTourLogRemarks(TableColumn.CellEditEvent<TourLog, String> event) throws TourLogManagerException {
        viewModel.updateTourLogRemarks(event.getNewValue());
    }

    @FXML
    public void updateTourLogWeather(TableColumn.CellEditEvent<TourLog, String> event) throws TourLogManagerException {
        viewModel.updateTourLogWeather(event.getNewValue());
    }

    @FXML
    public void updateTourLogJoule(TableColumn.CellEditEvent<TourLog, Integer> event) throws TourLogManagerException {
        viewModel.updateTourLogJoule(event.getNewValue());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeLogTable();
        initializeEditableTable();
        tourLogsTable.itemsProperty().bindBidirectional(viewModel.tourLogsTableProperty());
        tourLogsTable.getSelectionModel().selectedItemProperty().addListener(viewModel.getSelectedTourLogChange());

        tourList.setItems(viewModel.getTourList());
        tourList.getSelectionModel().selectedItemProperty().addListener(viewModel.getSelectedTourChange());

        searchField.textProperty().bindBidirectional(viewModel.searchFieldProperty());
        tourName.textProperty().bindBidirectional(viewModel.tourNameProperty());
        tourDistance.textProperty().bindBidirectional(viewModel.tourDistanceProperty());
        tourDescription.textProperty().bindBidirectional(viewModel.tourDescriptionProperty());
        routeInformation.textProperty().bindBidirectional(viewModel.routeInformationProperty());
        fromDestination.textProperty().bindBidirectional(viewModel.fromDestinationProperty());
        toDestination.textProperty().bindBidirectional(viewModel.toDestinationProperty());
        tourImage.imageProperty().bindBidirectional(viewModel.tourImageProperty());
    }

    private void initializeLogTable(){
        dateColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("date"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<TourLog,Integer>("rating"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<TourLog,Double>("duration"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("traveledDistance"));
        reportColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("logReport"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("author"));
        avgSpeedColumn.setCellValueFactory(new PropertyValueFactory<TourLog,Double>("avgSpeed"));
        remarksColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("remarks"));
        weatherColumn.setCellValueFactory(new PropertyValueFactory<TourLog,String>("weather"));
        jouleColumn.setCellValueFactory(new PropertyValueFactory<TourLog,Integer>("joule"));

    }

    private void initializeEditableTable(){
        reportColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ratingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        timeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        distanceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        avgSpeedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomDoubleStringConverter()));
        remarksColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        weatherColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        jouleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
    }






}
