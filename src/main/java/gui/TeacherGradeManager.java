package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.SchoolClassDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.TeacherController;
import businessLogic.TeacherController.InvalidGradeValueException;
import domainModel.Grade;
import domainModel.Homework;
import domainModel.SchoolClass;
import domainModel.Student;
import domainModel.TeachingAssignment;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import strategyForGrade.ArithmeticGradeAverageStrategy;
import strategyForGrade.GeometricGradeAverageStrategy;
import strategyForGrade.GradeAverageStrategy;
import strategyForGrade.WeightedGradeAverageStrategy;

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
	public void initialize() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException, 
			GradeDaoException, TeachingAssignmentDaoException {
		cbStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					handleChoiceBoxChange(newValue);
				} catch (GradeDaoException | DaoConnectionException | StudentDaoException
						| TeachingAssignmentDaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Popolamento degli studenti nella ComboBox
		Iterator<Student> students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		students.forEachRemaining(Liststudents::add);

		cbWeight.getItems().addAll(2, 4, 6); // Pesi fissi per la ComboBox

		students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		while (students.hasNext()) {
			Student student = students.next();
			Iterator<Grade> gradesPerStudent = teacherController.getAllStudentGradesByTeaching(student,
					teachingAssignment);
			gradesPerStudent.forEachRemaining(gradesList::add);
		}

		// Popolamento dei voti nella ComboBox
		double grade = 1;
		
		for (int i = 1; i <= 20; i++) {
			cbGrades.getItems().add(grade);
			grade += 0.5;
		}

		// Popolamento degli studenti nella ComboBox
		cbStudents.setItems(FXCollections.observableArrayList(Liststudents));
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

		
		
		List<GradeAverageStrategy> strategies = new ArrayList<>();
		strategies.add(new ArithmeticGradeAverageStrategy("Aritmetic average"));
		strategies.add(new GeometricGradeAverageStrategy("Geometric average"));
		strategies.add(new WeightedGradeAverageStrategy("Weighted average"));
		
		cbStrategy.setItems(FXCollections.observableArrayList(strategies));
	

		cbStrategy.setConverter(new StringConverter<GradeAverageStrategy>() {
			
			@Override
			public String toString(GradeAverageStrategy object) {
				// TODO Auto-generated method stub
				return object != null ? object.getName(): "";

			}
			
			@Override
			public GradeAverageStrategy fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	public void showGrades() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
		ConfigureColumnForGrade();
		Iterator<Student> students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		int rowindex = 0;
		while (students.hasNext()) {
			int c = 1;// Ad esempio, 5 studenti
			Student student = students.next();
			System.out.println(student.getName());
			ObservableList<String> row = rows.get(rowindex);
			rowindex++;
			row.set(0, String.valueOf(student.getName() + " " + student.getSurname()));
			Iterator<Grade> gradesPerStudent = teacherController.getAllStudentGradesByTeaching(student,
					teachingAssignment);
			while (gradesPerStudent.hasNext()) { // Ad esempio, 10 voti per studente
				row.set(c, String.valueOf(gradesPerStudent.next().getValue()));
				c++;
//				row.set(c, String.valueOf(gradesPerStudent.next().getValue()));
			}
		}
	}

	public void addGrade() 
			throws GradeDaoException, InvalidGradeValueException, DaoConnectionException, StudentDaoException {
		Double gradeValue = cbGrades.getValue();
		String description = taDescription.getText();
		Student student = cbStudents.getValue();
		LocalDate date = datePicker.getValue();

		// Assegna il voto
		teacherController.assignGradeToStudentInDate(gradeValue, description, teachingAssignment, student, date);
	}

	public void deleteGrade() throws GradeDaoException, DaoConnectionException {
		// Logica per eliminare un voto (da implementare)
		teacherController.deleteGrade(gradeSelected);
		//System.out.println(gradeSelected.getId());
	}

	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/TeacherInterface.fxml"));
		stage = (Stage) btnBack.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();	}

	public void editGrade() throws GradeDaoException, DaoConnectionException {
		// Logica per modificare un voto esistente (da implementare)
		teacherController.editGradeDescription(gradeSelected, taDescription.getText());
		teacherController.editGradeValue(gradeSelected, cbGrades.getValue());
		teacherController.editGradeWeight(gradeSelected, cbWeight.getValue());
	}

	
	public void getAverage() throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException
	{
		Double avg = teacherController.calculateStudentTeachingGradeAverage(cbStudents.getValue(), teachingAssignment, cbStrategy.getValue());
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

	private void handleChoiceBoxChange(Student newValue) throws GradeDaoException, DaoConnectionException, StudentDaoException, TeachingAssignmentDaoException {
		gradeTable.getItems().clear();
		gradeTable.getColumns().clear();

		int size = 5;
		TableColumn<ObservableList<String>, String> studentColumn = new TableColumn<>("Student");
		TableColumn<ObservableList<String>, String> valueColumn = new TableColumn<>("Value");
		TableColumn<ObservableList<String>, String> weightColumn = new TableColumn<>("weght");
		TableColumn<ObservableList<String>, String> dateColumn = new TableColumn<>("date");
		TableColumn<ObservableList<String>, String> descriptionColumn = new TableColumn<>("description:");

		gradeTable.getColumns().addAll(studentColumn, valueColumn, weightColumn, dateColumn, descriptionColumn);
		
		Iterator<Grade> grades = teacherController.getAllStudentGradesByTeaching(newValue, teachingAssignment);
		
		// Popolamento della tabella gradeTable
		ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

		while (grades.hasNext()) {
		    Grade grade = grades.next();

		    ObservableList<String> row = FXCollections.observableArrayList();
		    row.add(grade.getStudent().getName()); // Supponendo che getName() restituisca il nome dello studente
		    row.add(String.valueOf(grade.getValue()));
		    row.add(String.valueOf(grade.getWeight()));
		    row.add(grade.getDate().toString());
		    row.add(grade.getDescription());

		    data.add(row);
		}

		gradeTable.setItems(data);

		// Imposta le celle per ogni colonna
		studentColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(0)));
		valueColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(1)));
		weightColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(2)));
		dateColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(3)));
		descriptionColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().get(4)));

		

	}
	private void ConfigureColumnForGrade() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
// Svuota i dati del modello
		gradeTable.getItems().clear();
// Rimuove tutte le colonne
		gradeTable.getColumns().clear();

		int size = getMaxNumberOfGrades() + 1; // Numero di colonne necessarie

		for (int i = 0; i < size; i++) {
			TableColumn<ObservableList<String>, String> column;
			if (i == 0) {
				column = new TableColumn<>("Student: ");
			} else {
				column = new TableColumn<>("Grade " + (i));
			}

			final int colIndex = i; // Indice di colonna

			// Imposta la cella come elemento della lista corrispondente all'indice
			column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));

			// Rendi la cella modificabile
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(event -> event.getRowValue().set(colIndex, event.getNewValue()));
			gradeTable.getColumns().add(column);
		}


		System.out.println(numberOfStudents);
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

	private int getMaxNumberOfGrades() throws StudentDaoException, DaoConnectionException, SchoolClassDaoException,
			GradeDaoException, TeachingAssignmentDaoException {
		Iterator<Student> students = teacherController.getStudentsByClass(teachingAssignment.getSchoolClass());
		int maxNumberOfGrades = 0;
		while (students.hasNext()) {
			numberOfStudents++;
			Iterator<Grade> gradesPerStudent = teacherController.getAllStudentGradesByTeaching(students.next(),
					teachingAssignment);
			;
			List<Grade> grades = new ArrayList<Grade>();
			gradesPerStudent.forEachRemaining(grades::add);
			if (grades.size() > maxNumberOfGrades) {
				maxNumberOfGrades = grades.size();
			}
		}
		System.out.println("max: " + maxNumberOfGrades);
		return maxNumberOfGrades;
	}
}