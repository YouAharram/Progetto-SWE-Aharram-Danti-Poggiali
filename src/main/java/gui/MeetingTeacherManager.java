package gui;

import java.io.IOException;

import domainModel.Meeting;
import domainModel.MeetingAvailability;
import businessLogic.TeacherController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class MeetingTeacherManager {
	
	private static TeacherController teacherController;
    @FXML
    private Button btnBack; // Bottone per tornare indietro
    private Stage stage;     // Riferimento alla finestra (Stage)
    private Scene scene;     // Riferimento alla scena
    private Parent root;     // Riferimento alla root della scena caricata
    
    @FXML
    private TableView<MeetingAvailability> availableMeetingsTable; // Tabella per le disponibilità
    @FXML
    private TableColumn<MeetingAvailability, String> availabilityDateColumn; // Colonna per la data
    @FXML
    private TableColumn<MeetingAvailability, String> availabilityTimeColumn; // Colonna per l'ora
    
    @FXML
    private TableView<Meeting> bookedMeetingsTable; // Tabella per i colloqui prenotati
    @FXML
    private TableColumn<Meeting, String> bookedDateColumn; // Colonna per la data
    @FXML
    private TableColumn<Meeting, String> bookedTimeColumn; // Colonna per l'ora
    
    @FXML
    public void switchToPreviousScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/TeacherInterface.fxml"));
        root = loader.load();
        stage = (Stage) btnBack.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Metodo per inizializzare le tabelle con i dati
    @FXML
    public void initialize() {
        // Inizializza la tabella delle disponibilità
//        availabilityDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
//        availabilityTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
//        
//        // Inizializza la tabella dei colloqui prenotati
//        bookedDateColumn.setCellValueFactory(cellData -> cellData.getValue().getDateProperty());
//        bookedTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
        
        // Carica i dati per entrambe le tabelle
        loadAvailableMeetings();
        loadBookedMeetings();
    }

    // Carica le disponibilità dalla logica di business e le mostra nella tabella
    public void loadAvailableMeetings() {
        try {
            // Ottieni le disponibilità tramite il TeacherController
            ObservableList<MeetingAvailability> availableMeetings = FXCollections.observableArrayList();
            teacherController.getMeetingAvailabilities().forEachRemaining(availableMeetings::add);
            
            // Aggiungi i dati alla tabella
            availableMeetingsTable.setItems(availableMeetings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Carica i colloqui prenotati dalla logica di business e li mostra nella tabella
    public void loadBookedMeetings() {
        try {
            // Ottieni i colloqui prenotati tramite il TeacherController
            ObservableList<Meeting> bookedMeetings = FXCollections.observableArrayList();
            teacherController.getBookedMeetings().forEachRemaining(bookedMeetings::add);
            
            // Aggiungi i dati alla tabella
            bookedMeetingsTable.setItems(bookedMeetings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Metodo per aggiungere una disponibilità
    public void addAvailability() {
        // Implementazione futura per aggiungere una disponibilità
    }

    // Metodo per eliminare una disponibilità
    public void deleteAvailability() {
        // Implementazione futura per rimuovere una disponibilità
    }
    
    // Metodo setter per iniettare il TeacherController
    public static void setTeacherController(TeacherController teacherController) {
        MeetingTeacherManager.teacherController = teacherController;
    }
}
