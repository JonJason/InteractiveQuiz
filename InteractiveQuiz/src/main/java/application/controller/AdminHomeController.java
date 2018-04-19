package application.controller;

import java.io.IOException;

import application.model.Question;
import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class AdminHomeController extends BaseController {
	
	@FXML
	private Tab quizTab;
	
	@FXML
	private Tab questionsTab;

	@FXML
	private Tab topicsTab;

	@FXML
	private Tab schoolsTab;

	private AdminQuizTabController quizController;
	private AdminQuestionsTabController questionsController;
	private AdminTopicsTabController topicsController;
	private AdminSchoolsTabController schoolsController;
	
	protected ObservableList<Question> questions;
	protected ObservableList<String> topics;
	protected ObservableList<String> schools;

	public AdminHomeController() {

		loadFXML("adminhome");
		
		initTabs();
		
		try {
			questions = FXCollections.observableArrayList(Storage.loadQuestions());
			topics = FXCollections.observableArrayList(Storage.loadTopics());
			schools = FXCollections.observableArrayList(Storage.loadSchools());

			bindData();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTabs() {
		quizController = new AdminQuizTabController();
		questionsController = new AdminQuestionsTabController();
		topicsController = new AdminTopicsTabController();
		schoolsController = new AdminSchoolsTabController();

		quizTab.setContent(quizController.getRoot());
		questionsTab.setContent(questionsController.getRoot());
		topicsTab.setContent(topicsController.getRoot());
		schoolsTab.setContent(schoolsController.getRoot());
	}
	
	private void bindData() {
		quizController.setQuestions(questions);
		quizController.setTopics(topics);
		questionsController.setQuestions(questions);
		questionsController.setTopics(topics);
		topicsController.setTopics(topics);
		schoolsController.setSchools(schools);
	}
}
