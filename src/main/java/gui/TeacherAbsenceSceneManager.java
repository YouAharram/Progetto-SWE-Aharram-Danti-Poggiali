package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import domainModel.SchoolClass;
import domainModel.Student;
import exceptions.AbsenceDaoException;
import exceptions.DaoConnectionException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import domainModel.Absence;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.TeacherController;

public class TeacherAbsenceSceneManager {

    @FXML private DatePicker datePicker;
    @FXML private TableView<StudentAbsenceInfo> studentTableView;
    @FXML private TableColumn<StudentAbsenceInfo, String> firstNameColumn;
    @FXML private TableColumn<StudentAbsenceInfo, String> lastNameColumn;
    @FXML private TableColumn<StudentAbsenceInfo, String> presenceColumn;
    @FXML private TableColumn<StudentAbsenceInfo, String> justificationColumn;
    @FXML private Button showButton;
    @FXML private Button back;

    private static TeacherController teacherController;
    private static SchoolClass schoolClass;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static void setSchoolClass(SchoolClass schoolClass) {
        TeacherAbsenceSceneManager.schoolClass = schoolClass;
    }

    public static void setTeacherController(TeacherController teacherController) {
        TeacherAbsenceSceneManager.teacherController = teacherController;
    }
    

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        justificationColumn.setCellValueFactory(new PropertyValueFactory<>("justification"));

        presenceColumn.setCellValueFactory(cellData -> cellData.getValue().presenceProperty());

        presenceColumn.setCellFactory(column -> new TableCell<>() {
            private final Button presenceButton = new Button();

            @Override
            protected void updateItem(String presence, boolean empty) {
                super.updateItem(presence, empty);

                if (empty || presence == null) {
                    setGraphic(null);
                    return;
                }

                StudentAbsenceInfo currentInfo = getTableView().getItems().get(getIndex());
                presenceButton.setText(currentInfo.getPresence());
                presenceButton.setStyle(currentInfo.getPresence().equals("Present")
                        ? "-fx-background-color: green; -fx-text-fill: white;"
                        : "-fx-background-color: red; -fx-text-fill: white;");
                presenceButton.setOnAction(event -> {
                    if (currentInfo.getPresence().equals("Absent")) {
                        return;
                    }
                    String newState = "Absent";
                    currentInfo.setPresence(newState);
                    presenceButton.setText(newState);
                    presenceButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    LocalDate selectedDate = datePicker.getValue();
                    if (selectedDate != null && newState.equals("Absent")) {
                        addAbsence(currentInfo.getStudent(), selectedDate);
                    }
                    getTableView().refresh();
                });

                setGraphic(presenceButton);
            }
        });

    }

    @FXML
    public void showAbsences() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            HandlerError.showError("Please select a date");
            return;
        }

        Iterator<Student> studentIterator;
        List<Absence> absenceList = new ArrayList<>();
        
        try {
            studentIterator = teacherController.getStudentsByClass(schoolClass);
            teacherController.getAbsencesByClassInDate(schoolClass, selectedDate)
            		.forEachRemaining(absenceList::add);
        } catch (StudentDaoException | AbsenceDaoException | DaoConnectionException | SchoolClassDaoException e) {
            HandlerError.showError(e.getMessage());
            return;
        }

        ObservableList<StudentAbsenceInfo> absenceData = FXCollections.observableArrayList();

        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            String justification = "";
            String presenceStatus = "Present"; 
            for (Absence absence : absenceList) {
                if (absence.getStudent().equals(student)) {
                    justification = absence.isJustified() ? "Yes" : "No";
                    presenceStatus = "Absent";
                }
            }
            StudentAbsenceInfo info = new StudentAbsenceInfo(
                student,
                presenceStatus,
                justification
            );
            absenceData.add(info);
        }

        studentTableView.setItems(absenceData);

    }


    public void switchToTeacherScene() throws IOException{
    	        root = FXMLLoader.load(getClass().getResource("../TeacherInterface.fxml"));
    	        stage = (Stage) back.getScene().getWindow();
    	        scene = new Scene(root);
    	        stage.setScene(scene);
    	        stage.show();
   }


    public static class StudentAbsenceInfo {
        private final Student student;
        private final StringProperty presence;
        private final StringProperty justification;

        public StudentAbsenceInfo(Student student, String presence, String justification) {
            this.student = student;
            this.presence = new SimpleStringProperty(presence);
            this.justification = new SimpleStringProperty(justification);
        }

        public Student getStudent() {
            return student;
        }

        public String getFirstName() {
            return student.getName();
        }

        public String getLastName() {
            return student.getSurname();
        }

        public String getPresence() {
            return presence.get();
        }

        public void setPresence(String presence) {
            this.presence.set(presence);
        }

        public StringProperty presenceProperty() {
            return presence;
        }

        public String getJustification() {
            return justification.get();
        }
        
    }


    private void addAbsence(Student student, LocalDate date) {
    	try {
			teacherController.assignAbsenceToStudentInDate(student, date);
		} catch (AbsenceDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
    	showAbsences();
    	
    }

    public static void setTeacherAbsence(SchoolClass schoolClass) {
        TeacherAbsenceSceneManager.schoolClass = schoolClass;
    }
}
