package gui;

import businessLogic.TeacherController;
import businessLogic.TeacherController.MeetingAlreadyBookedException;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import exceptions.DaoConnectionException;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.TeacherDaoException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TeacherMeetingManager {

    private static TeacherController teacherController;

    @FXML
    private Button btnBack;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<MeetingAvailability> availableMeetingsTable;
    @FXML
    private TableView<Meeting> bookedMeetingsTable;
    @FXML
    private TableColumn<MeetingAvailability, String> availabilityDateColumn;
    @FXML
    private TableColumn<MeetingAvailability, String> availabilityTimeColumn;
    @FXML
    private TableColumn<Meeting, String> bookedDateColumn;
    @FXML
    private TableColumn<Meeting, String> bookedTimeColumn;
    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<Integer> btnHour;
    @FXML
    private ChoiceBox<Integer> btnMinute;
    @FXML
    private TableColumn<Meeting, String> studentNameColumn;

    @FXML
    private TableColumn<Meeting, String> parentNameColumn;


    @FXML
    private Button addAvailabilityButton;

    private ObservableList<MeetingAvailability> availabilityList = FXCollections.observableArrayList();

    @FXML
    public void switchToPreviousScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/TeacherInterface.fxml"));
        root = loader.load();
        stage = (Stage) btnBack.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        availabilityDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        availabilityTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour().toString()));

        bookedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetingAvailability().getDate().toString()));
        bookedTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetingAvailability().getHour().toString()));

        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getParent().getStudent().getSurname() + " " + cellData.getValue().getParent().getStudent().getName()));
        parentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getParent().getSurname() + " " + cellData.getValue().getParent().getName()));

        loadAvailableMeetings();
        loadBookedMeetings();

        for (int i = 8; i < 12; i++) {
            btnHour.getItems().add(i);
        }
        for (int i = 0; i < 60; i += 15) {
            btnMinute.getItems().add(i);
        }
    }


    public void loadAvailableMeetings() {
        ObservableList<MeetingAvailability> availableMeetings = FXCollections.observableArrayList();
        try {
            teacherController.getMeetingAvailabilities().forEachRemaining(availableMeetings::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        availableMeetingsTable.setItems(availableMeetings);
    }

    public void loadBookedMeetings() {
        ObservableList<Meeting> bookedMeetings = FXCollections.observableArrayList();
        try {
            teacherController.getBookedMeetings().forEachRemaining(bookedMeetings::add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bookedMeetingsTable.setItems(bookedMeetings);
    }

    public void addAvailability() {
        LocalDate date = datePicker.getValue();
        Integer hour = btnHour.getValue();
        Integer minute = btnMinute.getValue();

        if (date == null || hour == null || minute == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore di Inserimento");
            alert.setHeaderText("Campo mancante");
            alert.setContentText("Devi inserire la data, l'ora e i minuti.");
            alert.showAndWait();
            return;
        }

        LocalTime localTime = LocalTime.of(hour, minute);
        
        try {
            teacherController.addNewMeetingAvailabilityInDate(date, localTime);
        } catch (TeacherDaoException | MeetingAvailabilityDaoException | DaoConnectionException e) {
        	HandlerError.showError(e.getMessage());
        	return;
        }

        MeetingAvailability newAvailability = new MeetingAvailability(teacherController.getTeacher(), date, localTime, false);

        availabilityList.add(newAvailability);
        loadAvailableMeetings();
    }

    public void deleteAvailability() {
        MeetingAvailability selectedAvailability = availableMeetingsTable.getSelectionModel().getSelectedItem();

        if (selectedAvailability == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Eliminazione Disponibilità");
            alert.setHeaderText("Nessuna disponibilità selezionata");
            alert.setContentText("Seleziona una disponibilità da eliminare.");
            alert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Conferma Eliminazione");
        confirmationAlert.setHeaderText("Sei sicuro di voler eliminare questa disponibilità?");
        confirmationAlert.setContentText("Data: " + selectedAvailability.getDate() + "\nOrario: " + selectedAvailability.getHour());
        if (confirmationAlert.showAndWait().isEmpty() || !confirmationAlert.getResult().getButtonData().isDefaultButton()) {
            return;
        }

        try {
            try {
				teacherController.deleteMeetingAvailability(selectedAvailability);
			} catch (MeetingAlreadyBookedException e) {
				e.printStackTrace();
			} catch (TeacherDaoException e) {
				e.printStackTrace();
			}

            availabilityList.remove(selectedAvailability);
            availableMeetingsTable.setItems(availabilityList);

        } catch (DaoConnectionException | MeetingAvailabilityDaoException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Errore");
            errorAlert.setHeaderText("Impossibile eliminare la disponibilità");
            errorAlert.setContentText("Si è verificato un errore durante l'eliminazione. Riprova più tardi.");
            errorAlert.showAndWait();
        }
        loadAvailableMeetings();
    }


    public static void setTeacherController(TeacherController teacherController) {
        TeacherMeetingManager.teacherController = teacherController;
    }
}
