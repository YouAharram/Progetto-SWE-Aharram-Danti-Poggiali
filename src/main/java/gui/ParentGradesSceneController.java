package gui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

import businessLogic.ParentController;
import domainModel.Grade;
import domainModel.TeachingAssignment;
import exceptions.DaoConnectionException;
import exceptions.GradeDaoException;
import exceptions.StudentDaoException;
import exceptions.TeachingAssignmentDaoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ParentGradesSceneController {

	private static ParentController parentController;

	@FXML
    private Button showButton;

    @FXML
    private TableView<Grade> tableView;

    @FXML
    private ChoiceBox<String> subjectsChoiceBox;

    @FXML
    private Button back;

    @FXML
    private TableColumn<Grade, LocalDate> dateColumn;

    @FXML
    private TableColumn<Grade, Double> valueColumn;

    @FXML
    private TableColumn<Grade, Double> weightColumn;

    @FXML
    private TableColumn<Grade, String> descriptionColumn;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private ArrayList<TeachingAssignment> teachings;

    @FXML
    public void initialize() {
        teachings = new ArrayList<>();
        populateSubjects();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("Value"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("Weight"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

    }
    

    @FXML
    public void showGrades() {
        String selectedSubject = subjectsChoiceBox.getSelectionModel().getSelectedItem();

        if (selectedSubject != null) {
            updateGradesTable(selectedSubject);
        }
    }

    private void updateGradesTable(String subjectName) {
        Iterator<TeachingAssignment> teachingsIterator = null;
        try {
            teachingsIterator = parentController.getTeachings();
        } catch (TeachingAssignmentDaoException | DaoConnectionException | StudentDaoException e) {
			HandlerError.showError(e.getMessage());
            return;
        }

        ObservableList<Grade> grades = FXCollections.observableArrayList();
        
        while (teachingsIterator != null && teachingsIterator.hasNext()) {
            TeachingAssignment teaching = teachingsIterator.next();
            if (teaching.getSubject().equals(subjectName)) {
                Iterator<Grade> gradesIterator = null;
                try {
                    gradesIterator = parentController.getGradesByTeaching(teaching);
                } catch (GradeDaoException | DaoConnectionException | StudentDaoException | TeachingAssignmentDaoException e) {
        			HandlerError.showError(e.getMessage());
                    return;
                }

                while (gradesIterator != null && gradesIterator.hasNext()) {
                    Grade grade = gradesIterator.next();
                    grades.add(grade);
                }
            }
        }

        tableView.setItems(grades);
    }

    public void populateSubjects() {
        if (parentController != null) {
            Iterator<TeachingAssignment> subjectsIterator = null;
            try {
                subjectsIterator = parentController.getTeachings();
            } catch (TeachingAssignmentDaoException | DaoConnectionException | StudentDaoException e) {
    			HandlerError.showError(e.getMessage());
            }
            subjectsChoiceBox.getItems().clear();

            while (subjectsIterator != null && subjectsIterator.hasNext()) {
                TeachingAssignment teaching = subjectsIterator.next();
                teachings.add(teaching);
                subjectsChoiceBox.getItems().add(teaching.getSubject());
            }
        }
    }

    public void switchToParentScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/ParentInterface.fxml"));
        stage = (Stage) back.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
	public static void setController(ParentController parentController) {
		ParentGradesSceneController.parentController = parentController;
		
	}


}
