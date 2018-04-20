package application.controller;

import java.io.IOException;

import application.model.Question;
import application.model.Quiz;
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

	private Quiz quiz;
	private ObservableList<Question> questions;
	private ObservableList<String> topics;
	private ObservableList<String> schools;

	public AdminHomeController() {

		super("adminhome");
		
		try {
			quiz = Storage.loadQuiz();
			questions = FXCollections.observableArrayList(Storage.loadQuestions());
			topics = FXCollections.observableArrayList(Storage.loadTopics());
			schools = FXCollections.observableArrayList(Storage.loadSchools());
			
			initTabs();

			bindData();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTabs() {
		quizController = new AdminQuizTabController(quiz);
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
		quizController.setSchools(schools);
		
		questionsController.setQuestions(questions);
		questionsController.setTopics(topics);
		
		topicsController.setTopics(topics);
		
		schoolsController.setSchools(schools);
		
	}
}
