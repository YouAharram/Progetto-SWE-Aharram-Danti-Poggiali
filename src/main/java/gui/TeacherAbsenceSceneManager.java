package gui;

import javafx.collections.FXCollections; 
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
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
import java.util.Iterator;

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
    public void showAbsences() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            return;
        }

        Iterator<Student> studentIterator;
        Iterator<Absence> absenceIterator;
        try {
            studentIterator = teacherController.getStudentsByClass(schoolClass);
            absenceIterator = teacherController.getAbsencesByClassInDate(schoolClass, selectedDate);
        } catch (StudentDaoException | AbsenceDaoException | DaoConnectionException | SchoolClassDaoException e) {
            e.printStackTrace();
            return;
        }

        ObservableList<StudentAbsenceInfo> absenceData = FXCollections.observableArrayList();

        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            String justification = "";
            String presenceStatus = "Present";

            while (absenceIterator.hasNext()) {
                Absence absence = absenceIterator.next();
                if (absence.getStudent().equals(student)) {
                    justification = absence.isJustified() ? "Yes" : "No";
                    presenceStatus = "Absent";
                    break;
                }
            }

            StudentAbsenceInfo info = new StudentAbsenceInfo(
                student.getName(),
                student.getSurname(),
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
        private String firstName;
        private String lastName;
        private String presence;
        private String justification;

        public StudentAbsenceInfo(String firstName, String lastName, String presence, String justification) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.presence = presence;
            this.justification = justification;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPresence() {
            return presence;
        }

        public String getJustification() {
            return justification;
        }
    }

    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        presenceColumn.setCellValueFactory(new PropertyValueFactory<>("presence"));
        justificationColumn.setCellValueFactory(new PropertyValueFactory<>("justification"));
    }

    public static void setTeacherAbsence(SchoolClass schoolClass) {
        TeacherAbsenceSceneManager.schoolClass = schoolClass;
    }
}
