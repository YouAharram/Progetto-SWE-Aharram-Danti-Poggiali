package gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.TeacherController;
import businessLogic.TeacherController.IllegalReportAccessException;
import domainModel.DisciplinaryReport;
import domainModel.Student;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.DisciplinaryReportException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeacherDaoException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TeacherDisciplinaryReportManager {

	private static TeacherController teacherController;
	private static TeachingAssignment teachingAssignment;
	
	@FXML
	private TableView<ObservableList<String>> tvDisciplinaryrReport;
	
	@FXML
	private TextArea taDesciption;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private ComboBox<Student> cbStudents;
	private TableColumn<ObservableList<String>, String> studentColumn;
	private TableColumn<ObservableList<String>, String> teacherColumn;
	private TableColumn<ObservableList<String>, String> descriptionColumn;
	private TableColumn<ObservableList<String>, String> dateColumn;
	@FXML
	private Button btnBack;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public void initialize() {
		Iterator<Student> students = null;
		try {
			students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}

		List<Student> listStudents = new ArrayList<Student>();
		students.forEachRemaining(listStudents::add);
				cbStudents.setItems(FXCollections.observableArrayList(listStudents));
				cbStudents.setConverter(new StringConverter<Student>() {
					@Override
					public String toString(Student student) {
						return student != null ? student.getName() + " " + student.getSurname() : "";
					}

					@Override
					public Student fromString(String string) {
						return null;
					}
				});
	}
	
	@SuppressWarnings("unchecked")
	public void getDisciplinaryReport() {
		studentColumn = new TableColumn<>("Student");
		teacherColumn = new TableColumn<>("Teacher");
		descriptionColumn = new TableColumn<>("Description");
		dateColumn = new TableColumn<>("date");

		tvDisciplinaryrReport.getColumns().addAll(studentColumn, teacherColumn, descriptionColumn, dateColumn);
		
		
		Iterator<DisciplinaryReport> disciplinaryReports = null;
		try {
			disciplinaryReports = teacherController.getStudentDisciplinaryReports(cbStudents.getValue());
		} catch (DisciplinaryReportException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

		while (disciplinaryReports.hasNext()) {
		    DisciplinaryReport disciplinaryReport = disciplinaryReports.next();

		    ObservableList<String> row = FXCollections.observableArrayList();
		    row.add(disciplinaryReport.getStudent().getName() + " " + disciplinaryReport.getStudent().getSurname());
		    row.add(disciplinaryReport.getTeacher().getName() + "  " + disciplinaryReport.getTeacher().getSurname());
		    row.add(disciplinaryReport.getDescription());
		    row.add(String.valueOf(disciplinaryReport.getDate()));
		    data.add(row);
		}

		tvDisciplinaryrReport.setItems(data);

		studentColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(0)));
		teacherColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(1)));
		descriptionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(2)));
		dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(3)));	
	}
	
	public void addDisciplinaryReport() {
		try {
			teacherController.assignDisciplinaryReportToStudentInDate(cbStudents.getValue(), taDesciption.getText(), datePicker.getValue());
		} catch (DisciplinaryReportException | DaoConnectionException | StudentDaoException | TeacherDaoException e) {
			HandlerError.showError(e.getMessage());
		}
	}
	
	public void deleteDisciplinaryReport() {
		Iterator<DisciplinaryReport> disciplinaryReports = null;
		try {
			disciplinaryReports = teacherController.getStudentDisciplinaryReports(cbStudents.getValue());
		} catch (DisciplinaryReportException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		List<DisciplinaryReport> disciplinaryRepostsList = new ArrayList<>();
		disciplinaryReports.forEachRemaining(disciplinaryRepostsList::add);
		
		try {
			teacherController.deleteDisciplinaryReport(disciplinaryRepostsList.get(tvDisciplinaryrReport.getSelectionModel().getSelectedIndex()));
		} catch (IllegalReportAccessException | DisciplinaryReportException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}
	}
	
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();	}
	
	protected static void setController(TeacherController teacherController) {
		TeacherDisciplinaryReportManager.teacherController = teacherController;
	}
	
	protected static void setTeachingAssignment(TeachingAssignment teachingAssignment) {
		TeacherDisciplinaryReportManager.teachingAssignment = teachingAssignment;
	}
}
