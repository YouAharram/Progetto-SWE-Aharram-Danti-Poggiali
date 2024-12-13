package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.ParentController;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Teacher;
import domainModel.TeachingAssignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ParentMeetingSceneController {
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Button back;
    private static ParentController parentController;
    
    @FXML
    private ComboBox<String> teacherComboBox;

    @FXML
    private TableView<MeetingAvailability> availabilityTable;

    @FXML
    private TableColumn<MeetingAvailability, String> dateColumn;

    @FXML
    private TableColumn<MeetingAvailability, String> timeColumn;
    
    @FXML
    private TableColumn<MeetingAvailability, String> statusColumn;
    
    @FXML
    private TableColumn<MeetingAvailability, Void> bookColumn;

    @FXML
    private TableView<Meeting> appointmentsTable;

    @FXML
    private TableColumn<Meeting, String> teacherColumn;

    @FXML
    private TableColumn<Meeting, String> appointmentDateColumn;

    @FXML
    private TableColumn<Meeting, String> appointmentTimeColumn;

    private final ObservableList<String> teachers = FXCollections.observableArrayList();
    private final ObservableList<MeetingAvailability> availabilities = FXCollections.observableArrayList();
    private final ObservableList<Meeting> meetings = FXCollections.observableArrayList();
    


    public void initialize() {
        // Configura le colonne della tabella Disponibilità
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configura la colonna "Prenota" per contenere i bottoni
        bookColumn.setCellFactory(col -> new TableCell<MeetingAvailability, Void>() {
            private final Button bookButton = new Button("Prenota");
            {
                bookButton.setOnAction(event -> {
                    MeetingAvailability availability = getTableView().getItems().get(getIndex());
                    bookAppointment(availability); // Prenotazione colloquio
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    MeetingAvailability availability = getTableView().getItems().get(getIndex());
                    if (availability.isBooked()) {
                        bookButton.setDisable(false);
                    } else {
                        bookButton.setDisable(true);
                    }
                    setGraphic(bookButton);
                }
            }
        });

        // Popola la tabella con i dati
        availabilityTable.setItems(availabilities);

        // Configura le colonne della tabella Appuntamenti
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        appointmentsTable.setItems(meetings);

        // Popola il ComboBox
        loadTeachers();
        teacherComboBox.setItems(teachers);

        // Listener per il ComboBox
        teacherComboBox.setOnAction(event -> {
            String selectedTeacher = teacherComboBox.getValue();
            if (selectedTeacher != null) {
                loadAvailabilities(selectedTeacher);
            }
        });

        // Carica gli appuntamenti già fissati
        loadAppointments();
    }

    @FXML
    public void switchToParentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
        stage = (Stage) back.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private void loadTeachers() {
    	List<Teacher> teachersList = new ArrayList<>();
    	try {
			parentController.getTeachings().forEachRemaining(teaching -> teachersList.add(teaching.getTeacher()));
		} catch (TeachingAssignmentDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StudentDaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        teachers.addAll(teachersList.stream().map(teacher -> teacher.getSurname() + " " + teacher.getName()).toList()); 
    }

    private void loadAvailabilities(String teacher) {

    }

    private void loadAppointments() {
 
    }

    private void bookAppointment(MeetingAvailability availability) {

    }

	public static void setParentController(ParentController parentController) {
		ParentMeetingSceneController.parentController = parentController;
	}
}
