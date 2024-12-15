package gui;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.ParentController;
import businessLogic.ParentController.AlreadyBookedMeetingException;
import domainModel.Meeting;
import domainModel.MeetingAvailability;
import domainModel.Teacher;
import exceptions.DaoConnectionException;
import exceptions.MeetingAvailabilityDaoException;
import exceptions.MeetingDaoException;
import exceptions.ParentDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import exceptions.TeachingAssignmentDaoException;
import javafx.beans.property.SimpleStringProperty;
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
    private static ParentController parentController;
    
    @FXML
    private Button backButton;
    
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
    private TableView<Meeting> meetingsTable;

    @FXML
    private TableColumn<Meeting, String> teacherColumn;

    @FXML
    private TableColumn<Meeting, String> meetingDateColumn;

    @FXML
    private TableColumn<Meeting, String> meetingTimeColumn;

    private final ObservableList<String> teachers = FXCollections.observableArrayList();
    private final ObservableList<MeetingAvailability> availabilities = FXCollections.observableArrayList();
    private final ObservableList<Meeting> meetings = FXCollections.observableArrayList();
    


    public void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("hour"));        
        statusColumn.setCellValueFactory(cellData -> {
            boolean isBooked = cellData.getValue().isBooked(); // Ottiene il valore boolean
            String status = isBooked ? "booked" : "available"; // Converte in stringa
            return new SimpleStringProperty(status);          // Ritorna una proprietà osservabile
        });


        // Configura la colonna "Prenota" per contenere i bottoni
        bookColumn.setCellFactory(col -> new TableCell<MeetingAvailability, Void>() {
            private final Button bookButton = new Button("Book");
            {
                bookButton.setOnAction(event -> {
                    MeetingAvailability availability = getTableView().getItems().get(getIndex());
                    bookMeeting(availability); // Prenotazione colloquio
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
                        bookButton.setDisable(true);
                    } else {
                        bookButton.setDisable(false);
                    }
                    setGraphic(bookButton);
                }
            }
        });

        availabilityTable.setItems(availabilities);
        

        teacherColumn.setCellValueFactory(cellData -> {
            Meeting meeting = cellData.getValue();  // Ottieni l'oggetto Meeting
            if (meeting != null && meeting.getMeetingAvailability() != null) {
                Teacher teacher = meeting.getMeetingAvailability().getTeacher();
                return new SimpleStringProperty(teacher.getSurname() + " " + teacher.getName());
            }
            return null; 
        });

        meetingDateColumn.setCellValueFactory(cellData -> {
            Meeting meeting = cellData.getValue();  // Ottieni l'oggetto Meeting
            if (meeting != null && meeting.getMeetingAvailability() != null) {
                return new SimpleStringProperty(meeting.getMeetingAvailability().getDate().toString());
            }
            return null; 
        });

        meetingTimeColumn.setCellValueFactory(cellData -> {
            Meeting meeting = cellData.getValue();  // Ottieni l'oggetto Meeting
            if (meeting != null && meeting.getMeetingAvailability() != null) {
                return new SimpleStringProperty(meeting.getMeetingAvailability().getHour().toString());
            }
            return null;
        });

        
        meetingsTable.setItems(meetings);
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
        loadMeetings();
    }

    public void switchToParentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
        stage = (Stage) backButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private void loadTeachers() {
    	List<Teacher> teachersList = getAllTeachers();
        teachers.addAll(teachersList.stream()
        		.map(teacher -> teacher.getSurname() + " " + teacher.getName()).toList()); 
    }

	private List<Teacher> getAllTeachers() {
		List<Teacher> teachersList = new ArrayList<>();
		try {
			parentController.getTeachings().forEachRemaining(teaching -> teachersList.add(teaching.getTeacher()));
		} catch (TeachingAssignmentDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		return teachersList;
	}

    private void loadAvailabilities(String teacherNameSurname) {
    	availabilities.clear();
    	List<Teacher> teachers = getAllTeachers();
    	Iterator<MeetingAvailability> availabilitiesIterator = null;
    	for (Teacher teacherElement : teachers) {
			if ((teacherElement.getSurname() + " " + teacherElement.getName()).equals(teacherNameSurname)) {
				try {
					availabilitiesIterator = parentController.getAllMeetingsAvaialabilityByTeacher(teacherElement);
				} catch (TeacherDaoException | MeetingAvailabilityDaoException | DaoConnectionException e) {
					HandlerError.showError(e.getMessage());
				}
			}
		}
    	if (availabilitiesIterator != null) {
        	availabilitiesIterator.forEachRemaining(availability -> availabilities.add(availability));
    	}
    	else {
    		HandlerError.showError("No teachers for student selected");
    	}

    }

    private void loadMeetings() {
    	meetings.clear();
    	try {
			parentController.getAllMyMeetings().forEachRemaining(meeting -> meetings.add(meeting));
			meetingsTable.setItems(meetings);
		} catch (MeetingDaoException | ParentDaoException | TeacherDaoException | MeetingAvailabilityDaoException
				| DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}
    	
    }

    private void bookMeeting(MeetingAvailability availability) {
    	try {
			parentController.bookAMeeting(availability);
	        String selectedTeacher = teacherComboBox.getValue();
	        if (selectedTeacher != null) {
	            loadAvailabilities(selectedTeacher);
	        }
	        loadMeetings();
	        loadAvailabilities(selectedTeacher);
		} catch (AlreadyBookedMeetingException | MeetingAvailabilityDaoException | MeetingDaoException
				| ParentDaoException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}

    }
    

	public static void setController(ParentController parentController) {
		ParentMeetingSceneController.parentController = parentController;
	}
	
}
