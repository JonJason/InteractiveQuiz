package application.controller;

import java.util.Optional;

import application.model.Question;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;

public class AdminQuestionsTabController extends BaseController {
	
	@FXML
	private ComboBox<String> topicComboBox;
	
	@FXML
	private Button resetFilterButton;
	
	private ObservableList<String> topics;

	public AdminQuestionsTabController() {

		loadFXML("adminquestionstab");

	}

	@FXML
	private void resetFilter(ActionEvent e) {
		topicComboBox.setValue(null);
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
			System.out.println(question);
		});
	}

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
		topicComboBox.setItems(this.topics);
	}
}
