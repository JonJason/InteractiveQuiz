package application.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import application.model.Question;
import application.model.Quiz;
import application.util.AlertThrower;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AdminQuizSetupController extends BaseController {
	
	@FXML
	private TextField nameTextField;
	
	@FXML
	private DatePicker datePicker;
	
	@FXML
	private GridPane questionsGridPane;
	
	@FXML
	private ListView<String> providerSchoolsListView;
	
	@FXML
	private ListView<String> attachedSchoolsListView;
	
	@FXML
	private ListView<Question> attachedQuestionsListView;

	private Quiz quiz;
	private ObservableList<Question> questions;
	private ObservableList<Question> attachedQuestions;
	private ObservableList<String> schools;
	private ObservableList<String> attachedSchools;
	
	private QuestionsListWidgetController questionsListWidgetController;
	
	public AdminQuizSetupController() {
		
		loadFXML("adminquizsetup");
		
		questionsListWidgetController = new QuestionsListWidgetController();
		questionsGridPane.add(questionsListWidgetController.getRoot(), 0, 0);
	}
	
	@FXML
	private void addQuestion(ActionEvent e) {
		Question selectedQuestion = questionsListWidgetController.getSelectedQuestion();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No question Selected", "No question Selected", 
					"Please select a question that you want to add", "warning");
			return;
		}

		if (!attachedQuestions.contains(selectedQuestion)) {
			attachedQuestions.add(selectedQuestion);
		}
		
	}
	
	@FXML
	private void removeQuestion(ActionEvent e) {
		Question selectedQuestion = attachedQuestionsListView.getSelectionModel().getSelectedItem();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No question Selected", "No question Selected", 
					"Please select a question that you want to remove", "warning");
			return;
		}

		attachedQuestions.remove(selectedQuestion);
	}
	
	@FXML
	private void addSchool(ActionEvent e) {
		String selectedSchool = providerSchoolsListView.getSelectionModel().getSelectedItem();
		if (selectedSchool == null) {
			AlertThrower.showAlert("No school Selected", "No school Selected", 
					"Please select a school that you want to add", "warning");
			return;
		}

		if (!attachedSchools.contains(selectedSchool)) {
			attachedSchools.add(selectedSchool);
		}
	}
	
	@FXML
	private void removeSchool(ActionEvent e) {
		String selectedSchool = attachedSchoolsListView.getSelectionModel().getSelectedItem();
		if (selectedSchool == null) {
			AlertThrower.showAlert("No school Selected", "No school Selected", 
					"Please select a school that you want to remove", "warning");
			return;
		}

		attachedSchools.remove(selectedSchool);
	}
	
	private void parseData() {

		// name
		if (nameTextField.getText() != null) {
			quiz.setName(nameTextField.getText());
		}

		// date
		if (datePicker.getValue() != null) {
			LocalDate localDate = datePicker.getValue();
			Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
			Date date = Date.from(instant);
			quiz.setDate(Date.from(instant));
		}
		
		// schools
		quiz.setSchools(new ArrayList<String>(attachedSchools));
		
		// questions
		quiz.setQuestions(new ArrayList<Question>(attachedQuestions));
		
	}

	private void serializeData() {

		// name
		if (quiz.getName() != null) {
			nameTextField.setText(quiz.getName());
		}

		// date
		if (quiz.getDate() != null) {
			Date date = quiz.getDate();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			datePicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)));
		}
		
		// schools
		attachedSchools = FXCollections.observableArrayList(quiz.getSchools());
		attachedSchoolsListView.setItems(attachedSchools);
		
		// questions
		attachedQuestions = FXCollections.observableArrayList(quiz.getQuestions());
		attachedQuestionsListView.setItems(attachedQuestions);
		
		attachedQuestionsListView.setCellFactory(param -> new ListCell<Question>() {
		    @Override
		    protected void updateItem(Question question, boolean empty) {
		        super.updateItem(question, empty);

		        if (empty || question == null || question.getTitle() == null) {
		            setText(null);
		        } else {
		            setText(question.getTitle());
		        }
		    }
		});
		
	}
	
	public Quiz getQuiz() {
		parseData();
		return quiz;
	}
	
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
		
		serializeData();
	}
	
	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
		questionsListWidgetController.setQuestions(this.questions);
	}

	public void setTopics(ObservableList<String> topics) {
		questionsListWidgetController.setTopics(topics);
	}

	public void setSchools(ObservableList<String> schools) {
		this.schools = schools;
		providerSchoolsListView.setItems(this.schools);
	}
}
