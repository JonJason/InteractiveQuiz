package application.controller;

import java.util.ArrayList;
import java.util.Optional;

import application.model.Question;
import application.model.Quiz;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ButtonBar.ButtonData;

public class AdminQuizTabController extends BaseController {
	
	private Quiz quiz;
	private ObservableList<Question> questions;
	private ObservableList<String> topics;
	private ObservableList<String> schools;

	public AdminQuizTabController() {

		super("adminquiztab");
	}
	
	@FXML
	private void showQuizSetupDialog() {
		
		AdminQuizSetupController setupController = new AdminQuizSetupController();
		setupController.setTopics(topics);
		setupController.setQuestions(questions);
		setupController.setSchools(schools);
		
		setupController.setQuiz(quiz);
		
		Dialog<Quiz> dialog = createQuizDialog(setupController);
		
		Optional<Quiz> result = dialog.showAndWait();
		result.ifPresent(quiz -> {
			Storage.saveQuiz(quiz);
		});
	}
	
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
	}

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
	}
	
	public void setSchools(ObservableList<String> schools) {
		this.schools = schools;
	}
	
	public Dialog<Quiz> createQuizDialog(AdminQuizSetupController setupController) {
		
		Dialog<Quiz> dialog = new Dialog<Quiz>();

		ButtonType okButtonType = new ButtonType("Save Changes", ButtonData.OK_DONE);
		dialog.setTitle("Setup Quiz");
		dialog.setDialogPane((DialogPane) setupController.getRoot());
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
		
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return setupController.getQuiz();
			}
		    return null;
		});
		
		return dialog;
		
	}
}
