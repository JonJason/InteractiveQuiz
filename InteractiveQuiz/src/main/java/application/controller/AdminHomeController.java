package application.controller;

import java.io.IOException;
import java.util.ArrayList;

import application.model.Question;
import application.model.Quiz;
import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

	private QuizHomeController quizHomeController;

	public AdminHomeController(Quiz quiz) {

		super("adminhome");
		
		try {
			this.quiz = quiz;
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
		
		questions.addListener(new ListChangeListener<Question>() {

			@Override
			public void onChanged(Change<? extends Question> c) {
				while (c.next()) {
					if (c.wasPermutated()) {
						// permutated
					} else if (c.wasUpdated()) {
						// updated
					} else {
						boolean exists = false;
						for (Question removedQuestion : c.getRemoved()) {
							exists = quiz.getQuestions().contains(removedQuestion);
							quiz.getQuestions().remove(removedQuestion);
							Storage.saveQuiz(quiz);
						}
						
						if (exists && c.wasReplaced()) {
							for (Question addedQuestion : c.getAddedSubList()) {
								System.out.println(addedQuestion.getPicture());
								quiz.getQuestions().add(addedQuestion);
								Storage.saveQuiz(quiz);
							}
						}
					}
				}
			}
		});
	}

	public void setQuizHomeController(QuizHomeController quizHomeController) {
		this.quizHomeController = quizHomeController;
		quizController.setQuizHomeController(quizHomeController);
		
	}
}
