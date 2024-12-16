package gui;

import java.io.IOException; 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import businessLogic.TeacherController;
import businessLogic.TeacherController.InvalidGradeValueException;
import domainModel.ArithmeticGradeAverageStrategy;
import domainModel.GeometricGradeAverageStrategy;
import domainModel.Grade;
import domainModel.GradeAverageStrategy;
import domainModel.Student;
import domainModel.TeachingAssignment;
import domainModel.WeightedGradeAverageStrategy;
import exceptions.DaoConnectionException;
import exceptions.GradeDaoException;
import exceptions.SchoolClassDaoException;
import exceptions.StudentDaoException;
import exceptions.TeachingAssignmentDaoException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class TeacherGradeManager {
	private static TeacherController teacherController;
	private static TeachingAssignment teachingAssignment;
	private List<Student> Liststudents = new ArrayList<Student>();
	private List<Grade> gradesList = new ArrayList<Grade>();
	private ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();
	private Grade gradeSelected;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Button btnBack;
	
	@FXML
	private DatePicker datePicker;

	@FXML
	private TextArea taDescription;

	@FXML
	private TextArea taAverage;
	
	@FXML
	private ComboBox<Student> cbStudents;

	@FXML
	private ComboBox<Double> cbGrades;

	@FXML
	private ComboBox<Integer> cbWeight;

	@FXML
	private ComboBox<GradeAverageStrategy> cbStrategy;
	
	@FXML
	private TableView<ObservableList<String>> gradeTable;
	private int numberOfStudents = 0;

	@FXML
	public void initialize() {
		cbStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				handleChoiceBoxChange(newValue);
			}
		});

		Iterator<Student> students = null;
		try {
			students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		students.forEachRemaining(Liststudents::add);

		cbWeight.getItems().addAll(2, 4, 6);

		try {
			students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		while (students.hasNext()) {
			Student student = students.next();
			Iterator<Grade> gradesPerStudent = null;
			try {
				gradesPerStudent = teacherController.getAllStudentGradesByTeaching(student,
						teachingAssignment);
			} catch (GradeDaoException | DaoConnectionException | StudentDaoException
					| TeachingAssignmentDaoException e) {
				HandlerError.showError(e.getMessage());
			}
			gradesPerStudent.forEachRemaining(gradesList::add);
		}

		double grade = 1;
		
		for (int i = 1; i <= 20; i++) {
			cbGrades.getItems().add(grade);
			grade += 0.5;
		}

		cbStudents.setItems(FXCollections.observableArrayList(Liststudents));
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
		
		List<GradeAverageStrategy> strategies = new ArrayList<>();
		strategies.add(new ArithmeticGradeAverageStrategy("Aritmetic average"));
		strategies.add(new GeometricGradeAverageStrategy("Geometric average"));
		strategies.add(new WeightedGradeAverageStrategy("Weighted average"));
		
		cbStrategy.setItems(FXCollections.observableArrayList(strategies));
		cbStrategy.setConverter(new StringConverter<GradeAverageStrategy>() {
			
			@Override
			public String toString(GradeAverageStrategy object) {
				return object != null ? object.getName(): "";

			}
			
			@Override
			public GradeAverageStrategy fromString(String string) {
				return null;
			}
		});
	}

	public void showGrades() {
		ConfigureColumnForGrade();
		Iterator<Student> students = null;
		try {
			students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		int rowindex = 0;
		while (students.hasNext()) {
			int c = 1;
			Student student = students.next();
			ObservableList<String> row = rows.get(rowindex);
			rowindex++;
			row.set(0, String.valueOf(student.getName() + " " + student.getSurname()));
			Iterator<Grade> gradesPerStudent = null;
			try {
				gradesPerStudent = teacherController.getAllStudentGradesByTeaching(student,
						teachingAssignment);
			} catch (GradeDaoException | DaoConnectionException | StudentDaoException
					| TeachingAssignmentDaoException e) {
				HandlerError.showError(e.getMessage());
			}
			while (gradesPerStudent.hasNext()) {
				row.set(c, String.valueOf(gradesPerStudent.next().getValue()));
				c++;
			}
		}
	}

	public void addGrade() {
		Double gradeValue = cbGrades.getValue();
		String description = taDescription.getText();
		Student student = cbStudents.getValue();
		LocalDate date = datePicker.getValue();

		try {
			teacherController.assignGradeToStudentInDate(gradeValue, description, teachingAssignment, student, date);
		} catch (GradeDaoException | InvalidGradeValueException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
	}

	public void deleteGrade() {
		try {
			teacherController.deleteGrade(gradeSelected);
		} catch (GradeDaoException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}
	}

	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();	}

	public void editGrade() {
		try {
			teacherController.editGradeValue(gradeSelected, cbGrades.getValue());
			teacherController.editGradeWeight(gradeSelected, cbWeight.getValue());
		} catch (GradeDaoException | DaoConnectionException e) {
			HandlerError.showError(e.getMessage());
		}
	}

	
	public void getAverage() {
		Double avg = null;
		try {
			avg = teacherController.calculateStudentTeachingGradeAverage(cbStudents.getValue(), teachingAssignment, cbStrategy.getValue());
		} catch (GradeDaoException | DaoConnectionException | StudentDaoException | TeachingAssignmentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		taAverage.setText(String.valueOf(avg));
	}
	
	protected static void setController(TeacherController teacherController) {
		TeacherGradeManager.teacherController = teacherController;
	}

	protected static void setTeachingAssignment(TeachingAssignment teachingAssignment) {
		 TeacherGradeManager.teachingAssignment = teachingAssignment;
	}
	
	public void itemSelected() {
		gradeSelected = gradesList.get(gradeTable.getSelectionModel().getSelectedIndex());
		taDescription.setText(gradeSelected.getDescription());
		cbWeight.setValue(gradeSelected.getWeight());
		cbGrades.setValue(gradeSelected.getValue());
		}

	@SuppressWarnings("unchecked")
	private void handleChoiceBoxChange(Student newValue) {
		gradeTable.getItems().clear();
		gradeTable.getColumns().clear();

		TableColumn<ObservableList<String>, String> studentColumn = new TableColumn<>("Student");
		TableColumn<ObservableList<String>, String> valueColumn = new TableColumn<>("Value");
		TableColumn<ObservableList<String>, String> weightColumn = new TableColumn<>("weght");
		TableColumn<ObservableList<String>, String> dateColumn = new TableColumn<>("date");
		TableColumn<ObservableList<String>, String> descriptionColumn = new TableColumn<>("description:");

		gradeTable.getColumns().addAll(studentColumn, valueColumn, weightColumn, dateColumn, descriptionColumn);
		
		Iterator<Grade> grades = null;
		try {
			grades = teacherController.getAllStudentGradesByTeaching(newValue, teachingAssignment);
		} catch (GradeDaoException | DaoConnectionException | StudentDaoException | TeachingAssignmentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

		while (grades.hasNext()) {
		    Grade grade = grades.next();

		    ObservableList<String> row = FXCollections.observableArrayList();
		    row.add(grade.getStudent().getName());
		    row.add(String.valueOf(grade.getValue()));
		    row.add(String.valueOf(grade.getWeight()));
		    row.add(grade.getDate().toString());
		    row.add(grade.getDescription());

		    data.add(row);
		}

		gradeTable.setItems(data);

		studentColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(0)));
		valueColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(1)));
		weightColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(2)));
		dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(3)));
		descriptionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(4)));

		

	}
	private void ConfigureColumnForGrade() {
		gradeTable.getItems().clear();
		gradeTable.getColumns().clear();

		int size = getMaxNumberOfGrades() + 1;
		
		for (int i = 0; i < size; i++) {
			TableColumn<ObservableList<String>, String> column;
			if (i == 0) {
				column = new TableColumn<>("Student: ");
			} else {
				column = new TableColumn<>("Grade " + (i));
			}

			final int colIndex = i;

			// Imposta la cella come elemento della lista corrispondente all'indice
			column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));

			// Rendi la cella modificabile
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(event -> event.getRowValue().set(colIndex, event.getNewValue()));
			gradeTable.getColumns().add(column);
		}


		for (int r = 0; r < numberOfStudents; r++) {
			ObservableList<String> row = FXCollections.observableArrayList();
			for (int c = 0; c < size; c++) {
				row.add("");
			}
			// Aggiungi un valore per la colonna nascosta
			row.add("Hidden Value"); // Può essere lasciato vuoto o modificato in base alla logica
			rows.add(row);
		}
		gradeTable.setItems(rows);
	}

	private int getMaxNumberOfGrades() {
		Iterator<Student> students = null;
		try {
			students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		} catch (StudentDaoException | DaoConnectionException | SchoolClassDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		int maxNumberOfGrades = 0;
		while (students.hasNext()) {
			numberOfStudents++;
			Iterator<Grade> gradesPerStudent = null;
			try {
				gradesPerStudent = teacherController.getAllStudentGradesByTeaching(students.next(),
						teachingAssignment);
			} catch (GradeDaoException | DaoConnectionException | StudentDaoException
					| TeachingAssignmentDaoException e) {
				HandlerError.showError(e.getMessage());
			}
			;
			List<Grade> grades = new ArrayList<Grade>();
			gradesPerStudent.forEachRemaining(grades::add);
			if (grades.size() > maxNumberOfGrades) {
				maxNumberOfGrades = grades.size();
			}
		}
		return maxNumberOfGrades;
	}
}