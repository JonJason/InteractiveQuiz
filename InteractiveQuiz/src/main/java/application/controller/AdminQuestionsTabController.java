package application.controller;

import java.util.ArrayList;
import java.util.Optional;

import application.model.Question;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.FXCollections;
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
		Question newQuestion = new Question();
		
		if (topicsComboBox.getSelectionModel().getSelectedItem() != null) {
			newQuestion.addTopic(topicsComboBox.getSelectionModel().getSelectedItem());
		}
		
		formController.setQuestion(newQuestion);
		
		Dialog<Question> dialog = createQuestionDialog(formController, "new");
		
		Optional<Question> result = dialog.showAndWait();
		result.ifPresent(question -> {
			if (!question.getTitle().equals("")) {
				questions.add(question);
				filterQuestion(topicsComboBox.getSelectionModel().getSelectedItem());
				Storage.saveQuestions(new ArrayList<Question>(questions));
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", "no topic added", "warning");
			}
		});
	}

	@FXML
	private void showEditQuestionDialog(ActionEvent e) {
		Question selectedQuestion = questionsListView.getSelectionModel().getSelectedItem();
		if (selectedQuestion == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", "Please select a topic that you want to edit", "warning");
			return;
		}
		
		QuestionFormController formController = new QuestionFormController();
		formController.setTopics(topics);
		formController.setQuestion(selectedQuestion);
		
		Dialog<Question> dialog = createQuestionDialog(formController, "edit");
		
		Optional<Question> result = dialog.showAndWait();
		result.ifPresent(question -> {
			if (!question.getTitle().equals("")) {
				filterQuestion(topicsComboBox.getSelectionModel().getSelectedItem());
				Storage.saveQuestions( new ArrayList<Question>(questions));
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", "topic wasn't changed", "warning");
			}
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
			filterQuestion(topicsComboBox.getSelectionModel().getSelectedItem());
			Storage.saveQuestions( new ArrayList<Question>(questions));
		}
	}
	
	@FXML
	private void onTopicChange(ActionEvent e) {
		filterQuestion(topicsComboBox.getSelectionModel().getSelectedItem());
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
		
		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return formController.getQuestion();
			}
		    return null;
		});
		
		return dialog;
		
	}
	
	private void filterQuestion(String topic) {
		System.out.println(topic);
		System.out.println(questions.size());

		if (topic == null) {
			questionsListView.setItems(questions);
			return;
		}

		ObservableList<Question> subentries = FXCollections.observableArrayList();
		for (Question question : questions) {
			
			if (question.getTopics().contains(topic)) {
				subentries.add(question);
			}
			
		}
		questionsListView.setItems(subentries);
	}
}
