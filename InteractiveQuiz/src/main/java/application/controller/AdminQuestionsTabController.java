package application.controller;

import java.util.ArrayList;
import java.util.Optional;

import application.model.Question;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class AdminQuestionsTabController extends BaseController {
	
	private ObservableList<String> topics;
	private ObservableList<Question> questions;
	final private QuestionsListWidgetController questionsListWidgetController;

	public AdminQuestionsTabController() {

		super("adminquestionstab");
		
		questionsListWidgetController = new QuestionsListWidgetController();
		((GridPane) this.getRoot()).add(questionsListWidgetController.getRoot(), 0, 0);
	}
	
	@FXML
	private void showAddQuestionDialog() {
		
		QuestionFormController formController = new QuestionFormController();
		formController.setTopics(topics);
		Question newQuestion = new Question();
		
		if (questionsListWidgetController.getSelectedTopic() != null) {
			newQuestion.addTopic(questionsListWidgetController.getSelectedTopic());
		}
		
		formController.fillForm(newQuestion);
		
		Dialog<Question> dialog = createQuestionDialog(formController, "new");
		
		Optional<Question> result = dialog.showAndWait();
		result.ifPresent(question -> {
			if (!question.getText().equals("")) {
				
				if(hasDuplicate(question, -1)) {
					AlertThrower.showAlert("Duplicate Detected", "Question already exists", 
							"question wasn't added", "warning");
					return;
				}
				
				questions.add(question);
				questionsListWidgetController.updateFilteredQuestions();
				Storage.saveQuestions(new ArrayList<Question>(questions));
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", 
						"no Question added", "warning");
			}
		});
	}

	@FXML
	private void showEditQuestionDialog(ActionEvent e) {
		Question selectedQuestion = questionsListWidgetController.getSelectedQuestion();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No Question Selected", "No Question Selected", 
					"Please select a Question that you want to edit", "warning");
			return;
		}
		
		QuestionFormController formController = new QuestionFormController();
		formController.setTopics(topics);
		formController.fillForm(selectedQuestion);
		
		Dialog<Question> dialog = createQuestionDialog(formController, "edit");
		
		Optional<Question> result = dialog.showAndWait();
		result.ifPresent(question -> {
			System.out.println(selectedQuestion.getText());
			System.out.println(selectedQuestion.getPicture());
			if (!question.getText().equals("")) {
				int selectedIndex = questions.indexOf(selectedQuestion);
				
				if(hasDuplicate(question, selectedIndex)) {
					AlertThrower.showAlert("Duplicate Detected", "Question already exists", 
							"Question wasn't changed", "warning");
					return;
				}
				System.out.println("Yow");
				questions.set(selectedIndex, question);
				
				questionsListWidgetController.updateFilteredQuestions();
				Storage.saveQuestions( new ArrayList<Question>(questions));
			} else {
				AlertThrower.showAlert("Invalid Input", "Input was empty", 
						"Question wasn't changed", "warning");
			}
		});
		
	}
	
	@FXML
	private void showDeleteQuestionDialog(ActionEvent e) {
		Question selectedQuestion = questionsListWidgetController.getSelectedQuestion();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No Question Selected", "No Question Selected", 
					"Please select a Question that you want to delete", "warning");
			return;
		}
		
		Alert confirmationAlert = AlertThrower.createAlert("Delete Confirmation", "Are you sure you want to delete: " + selectedQuestion.getText(), null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			questions.remove(selectedQuestion);
			questionsListWidgetController.updateFilteredQuestions();
			Storage.saveQuestions(new ArrayList<Question>(questions));
		}
	}

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
		questionsListWidgetController.setTopics(this.topics);
	}
	
	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
		questionsListWidgetController.setQuestions(this.questions);
	}
	
	public Dialog<Question> createQuestionDialog(QuestionFormController formController, String type) {
		
		Dialog<Question> dialog = new Dialog<Question>();
		
		String okText = "New";
		
		switch (type) {
		case("new"):
			okText = "New";
			dialog.setTitle("New Question");
			break;
		
		case("edit"):
			okText = "Edit";
			dialog.setTitle("Edit Question");
			break;
			
		default:
		}
		
		ButtonType okButtonType = new ButtonType(okText, ButtonData.OK_DONE);
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.getDialogPane().setContent(formController.getRoot());
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
		
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return formController.getQuestion();
			}
		    return null;
		});
		
		return dialog;
		
	}

	private boolean hasDuplicate(Question question, int selectedIndex) {
		
		for (int i = 0; i < questions.size(); i++) {
			
			if (questions.get(i).getText().equals(question.getText()) && i != selectedIndex) {
				return true;
			}
			
		}
		
		return false;
	}
}
