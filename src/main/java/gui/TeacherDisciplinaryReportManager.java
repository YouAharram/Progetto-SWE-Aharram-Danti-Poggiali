package gui;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.DisciplinaryReportException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeacherDaoException;
import businessLogic.TeacherController;
import businessLogic.TeacherController.IllegalReportAccessException;
import domainModel.DisciplinaryReport;
import domainModel.Student;
import domainModel.TeachingAssignment;
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
	public void initialize() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException {
		Iterator<Student> students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		List<Student> listStudents = new ArrayList<Student>();
		students.forEachRemaining(listStudents::add);
		// Popolamento degli studenti nella ComboBox
				cbStudents.setItems(FXCollections.observableArrayList(listStudents));
				cbStudents.setConverter(new StringConverter<Student>() {
					@Override
					public String toString(Student student) {
						return student != null ? student.getName() + " " + student.getSurname() : "";
					}

					@Override
					public Student fromString(String string) {
						// Non necessario, ma se necessario implementare la logica per trovare lo
						// studente
						return null;
					}
				});
	}
	
	@SuppressWarnings("unchecked")
	public void getDisciplinaryReport() throws DisciplinaryReportException, DaoConnectionException, StudentDaoException
	{
		studentColumn = new TableColumn<>("Student");
		teacherColumn = new TableColumn<>("Teacher");
		descriptionColumn = new TableColumn<>("Description");
		dateColumn = new TableColumn<>("date");

		tvDisciplinaryrReport.getColumns().addAll(studentColumn, teacherColumn, descriptionColumn, dateColumn);
		
		
		Iterator<DisciplinaryReport> disciplinaryReports = teacherController.getStudentDisciplinaryReports(cbStudents.getValue());
		
		// Popolamento della tabella gradeTable
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

		while (disciplinaryReports .hasNext()) {
		    DisciplinaryReport disciplinaryReport = disciplinaryReports.next();

		    ObservableList<String> row = FXCollections.observableArrayList();
		    row.add(disciplinaryReport.getStudent().getName()+" "+disciplinaryReport.getStudent().getSurname()); // Supponendo che getName() restituisca il nome dello studente
		    row.add(disciplinaryReport.getTeacher().getName()+"  "+disciplinaryReport.getTeacher().getSurname());
		    row.add(disciplinaryReport.getDescription());
		    row.add(String.valueOf(disciplinaryReport.getDate()));
		    data.add(row);
		}

		tvDisciplinaryrReport.setItems(data);

		// Imposta le celle per ogni colonna
		studentColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(0)));
		teacherColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(1)));
		descriptionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(2)));
		dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(3)));	
	}
	
	public void addDisciplinaryReport() throws DisciplinaryReportException, DaoConnectionException, StudentDaoException, TeacherDaoException {
		teacherController.assignDisciplinaryReportToStudentInDate(cbStudents.getValue(), taDesciption.getText(), datePicker.getValue());
	}
	
	public void deleteDisciplinaryReport() throws DisciplinaryReportException, DaoConnectionException, StudentDaoException, IllegalReportAccessException {
		Iterator<DisciplinaryReport> disciplinaryReports = teacherController.getStudentDisciplinaryReports(cbStudents.getValue());
		List<DisciplinaryReport> disciplinaryRepostsList = new ArrayList<>();
		disciplinaryReports.forEachRemaining(disciplinaryRepostsList::add);
		
		teacherController.deleteDisciplinaryReport(disciplinaryRepostsList.get(tvDisciplinaryrReport.getSelectionModel().getSelectedIndex()));
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
