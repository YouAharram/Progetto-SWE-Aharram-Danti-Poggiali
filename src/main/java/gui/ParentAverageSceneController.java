package gui;

import java.io.IOException;
import java.util.Iterator;

import DaoExceptions.DaoConnectionException;
import DaoExceptions.GradeDaoException;
import DaoExceptions.StudentDaoException;
import DaoExceptions.TeachingAssignmentDaoException;
import businessLogic.ParentController;
import domainModel.TeachingAssignment;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import strategyForGrade.ArithmeticGradeAverageStrategy;
import strategyForGrade.GeometricGradeAverageStrategy;
import strategyForGrade.GradeAverageStrategy;
import strategyForGrade.WeightedGradeAverageStrategy;

public class ParentAverageSceneController {
	@FXML
	private Button back;
	
	@FXML
	private TableView<SubjectAverage> tableView;
	
	@FXML
	private TableColumn<SubjectAverage, String> subjectColumn;
	
	@FXML
	private TableColumn<SubjectAverage, Double> averageColumn;
	
	@FXML
	private ChoiceBox<String> strategyChoiceBox;
	
	@FXML
	private Label totalAverageLabel;
	
	@FXML
	private Button showButton;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static ParentController parentController;

	public static void setController(ParentController parentController) {
		ParentAverageSceneController.parentController = parentController;
	}

	public void switchToParentScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
		stage = (Stage) back.getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void initialize() {
		strategyChoiceBox.getItems().addAll("Arithmetic", "Geometric", "Weighted");
		subjectColumn.setCellValueFactory(cellData -> cellData.getValue().getSubject());
		averageColumn.setCellValueFactory(cellData -> cellData.getValue().getAverage().asObject());
		showButton.setOnAction(event -> updateTableWithStrategy());
	}

	public void updateTableWithStrategy() {
		String selectedStrategy = strategyChoiceBox.getValue();

		if (selectedStrategy == null) {
			return;
		}

		GradeAverageStrategy strategy = getStrategyFromChoice(selectedStrategy);

		Iterator<TeachingAssignment> teachingIterator = null;
		try {
			teachingIterator = parentController.getTeachings();
		} catch (TeachingAssignmentDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}

		ObservableList<SubjectAverage> data = FXCollections.observableArrayList();

		while (teachingIterator.hasNext()) {
			TeachingAssignment teaching = teachingIterator.next();

			double average = 0;
			try {
				average = parentController.calculateTeachingGradeAverage(teaching, strategy);
			} catch (GradeDaoException | DaoConnectionException | StudentDaoException
					| TeachingAssignmentDaoException e) {
				HandlerError.showError(e.getMessage());
			}

			data.add(new SubjectAverage(teaching.getSubject(), average));
		}

		tableView.setItems(data);

		double totalAverage = 0;
		try {
			totalAverage = parentController.calculateTotalGradeAverage(strategy);
		} catch (GradeDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
		}
		totalAverageLabel.setText("Total Average: " + totalAverage);
	}

	private GradeAverageStrategy getStrategyFromChoice(String strategyChoice) {
		switch (strategyChoice) {
		case "Arithmetic":
			return new ArithmeticGradeAverageStrategy();
		case "Geometric":
			return new GeometricGradeAverageStrategy();
		case "Weighted":
			return new WeightedGradeAverageStrategy();
		default:
			return null;
		}
	}

	public static class SubjectAverage {

		private final StringProperty subject;
		private final DoubleProperty average;

		public SubjectAverage(String subject, double average) {
			this.subject = new SimpleStringProperty(subject);
			this.average = new SimpleDoubleProperty(average);
		}

		public StringProperty getSubject() {
			return subject;
		}

		public DoubleProperty getAverage() {
			return average;
		}
	}

}
