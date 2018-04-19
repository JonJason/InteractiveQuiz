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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ButtonBar.ButtonData;

public class AdminQuestionsTabController extends BaseController {

	@FXML
	private ComboBox<String> topicsComboBox;
	
	@FXML
	private ListView<Question> questionsListView;
	
	@FXML
	private Button resetFilterButton;
	
	private ObservableList<String> topics;
	private ObservableList<Question> questions;

	public AdminQuestionsTabController() {

		loadFXML("adminquestionstab");

	}

	@FXML
	private void resetFilter(ActionEvent e) {
		topicsComboBox.setValue(null);
	}
	
	@FXML
	private void showAddQuestionDialog() {
		
		QuestionFormController formController = new QuestionFormController();
		formController.setTopics(topics);
		
		Dialog<Question> dialog = new Dialog<Question>();
		
		dialog.setTitle("New Question");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.getDialogPane().setContent(formController.getRoot());
		ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
		
		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == addButtonType) {
		        return formController.getQuestion();
		    }
		    return null;
		});
		
		Optional<Question> result = dialog.showAndWait();
		result.ifPresent(question -> {
			questions.add(question);
			Storage.saveQuestions(new ArrayList<Question>(questions));
		});
	}
	
	@FXML
	private void showDeleteQuestionDialog(ActionEvent e) {
		Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No question Selected", "No question Selected", "Please select a question that you want to delete", "warning");
			return;
		}
		
		Alert confirmationAlert = AlertThrower.createAlert("Delete Confirmation", "Are you sure you want to delete: " + selectedQuestion.getTitle(), null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			questions.remove(selectedQuestion);
			Storage.saveQuestions( new ArrayList<Question>(questions));
		}
	}

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
		topicsComboBox.setItems(this.topics);
	}
	
	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
		questionsListView.setItems(this.questions);
		
		questionsListView.setCellFactory(param -> new ListCell<Question>() {
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
}
