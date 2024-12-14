package gui;

import businessLogic.TeacherController;
import businessLogic.TeacherController.MeetingAlreadyBookedException;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
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

import DaoExceptions.DaoConnectionException;
import DaoExceptions.MeetingAvailabilityDaoException;
import DaoExceptions.TeacherDaoException;

public class MeetingTeacherManager {

    private static TeacherController teacherController;

    @FXML
    private Button btnBack; // Bottone per tornare indietro
    private Stage stage; // Riferimento alla finestra (Stage)
    private Scene scene; // Riferimento alla scena
    private Parent root; // Riferimento alla root della scena caricata

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
    private DatePicker datePicker; // Campo per la selezione della data

    @FXML
    private ChoiceBox<Integer> btnHour; // ChoiceBox per l'ora
    @FXML
    private ChoiceBox<Integer> btnMinute; // ChoiceBox per i minuti

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
        // Imposta la colonna della data e dell'orario per la tabella delle disponibilità
        availabilityDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        availabilityTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHour().toString()));

        // Imposta la colonna della data e dell'orario per la tabella dei colloqui prenotati
        bookedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetingAvailability().getDate().toString()));
        bookedTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMeetingAvailability().getHour().toString()));

        // Carica le disponibilità e i colloqui prenotati
        loadAvailableMeetings();
        loadBookedMeetings();

        // Popola le ChoiceBox con le ore (0-23) e i minuti (0-59)
        for (int i = 0; i < 24; i++) {
            btnHour.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            btnMinute.getItems().add(i);
        }
    }

    public void loadAvailableMeetings() {
        ObservableList<MeetingAvailability> availableMeetings = FXCollections.observableArrayList();
        try {
            teacherController.getMeetingAvailabilities().forEachRemaining(availableMeetings::add);
        } catch (Exception e) {
            e.printStackTrace(); // Gestisci errori qui
        }
        availableMeetingsTable.setItems(availableMeetings);
    }

    public void loadBookedMeetings() {
        ObservableList<Meeting> bookedMeetings = FXCollections.observableArrayList();
        try {
            teacherController.getBookedMeetings().forEachRemaining(bookedMeetings::add);
        } catch (Exception e) {
            e.printStackTrace(); // Gestisci errori qui
        }
        bookedMeetingsTable.setItems(bookedMeetings);
    }

    public void addAvailability() {
        LocalDate date = datePicker.getValue();
        Integer hour = btnHour.getValue();
        Integer minute = btnMinute.getValue();

        // Verifica che la data, l'ora e i minuti siano selezionati
        if (date == null || hour == null || minute == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore di Inserimento");
            alert.setHeaderText("Campo mancante");
            alert.setContentText("Devi inserire la data, l'ora e i minuti.");
            alert.showAndWait();
            return;
        }

        // Crea l'oggetto LocalTime utilizzando l'ora e i minuti selezionati
        LocalTime localTime = LocalTime.of(hour, minute);
        
        try {
            teacherController.addNewMeetingAvailabilityInDate(date, localTime);
        } catch (TeacherDaoException | MeetingAvailabilityDaoException | DaoConnectionException e) {
        	HandlerError.showError(e.getMessage());
        	return;
        }

        // Crea una nuova disponibilità
        MeetingAvailability newAvailability = new MeetingAvailability(teacherController.getTeacher(), date, localTime, false);

        // Aggiungi la nuova disponibilità alla lista
        availabilityList.add(newAvailability);
        loadAvailableMeetings();
    }

    public void deleteAvailability() {
        // Ottieni l'elemento selezionato nella tabella
        MeetingAvailability selectedAvailability = availableMeetingsTable.getSelectionModel().getSelectedItem();

        if (selectedAvailability == null) {
            // Mostra un alert se non è stato selezionato alcun elemento
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Eliminazione Disponibilità");
            alert.setHeaderText("Nessuna disponibilità selezionata");
            alert.setContentText("Seleziona una disponibilità da eliminare.");
            alert.showAndWait();
            return;
        }

        // Conferma l'eliminazione
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Conferma Eliminazione");
        confirmationAlert.setHeaderText("Sei sicuro di voler eliminare questa disponibilità?");
        confirmationAlert.setContentText("Data: " + selectedAvailability.getDate() + "\nOrario: " + selectedAvailability.getHour());
        if (confirmationAlert.showAndWait().isEmpty() || !confirmationAlert.getResult().getButtonData().isDefaultButton()) {
            // Se l'utente non conferma, esci dal metodo
            return;
        }

        try {
            // Rimuovi la disponibilità dal database
            try {
				teacherController.deleteMeetingAvailability(selectedAvailability);
			} catch (MeetingAlreadyBookedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TeacherDaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Rimuovi la disponibilità dalla lista locale e aggiorna la tabella
            availabilityList.remove(selectedAvailability);
            availableMeetingsTable.setItems(availabilityList);

        } catch (DaoConnectionException | MeetingAvailabilityDaoException e) {
            // Gestisci eventuali errori durante l'eliminazione dal database
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
        MeetingTeacherManager.teacherController = teacherController;
    }
}
