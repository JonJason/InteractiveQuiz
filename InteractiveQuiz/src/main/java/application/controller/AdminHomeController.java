package application.controller;

import java.io.IOException;

import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class AdminHomeController extends BaseController {

	@FXML
	private Tab topicsTab;
	
	@FXML
	private Tab questionsTab;
	
	@FXML
	private Tab quizTab;

	private AdminTopicsTabController topicsController;
	private AdminQuestionsTabController questionsController;
	private AdminQuizTabController quizController;
	protected ObservableList<String> topics;

	public AdminHomeController() {

		loadFXML("adminhome");
		
		initTabs();
		
		try {
			topics = FXCollections.observableArrayList(Storage.loadTopics());

			bindData();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTabs() {
		topicsController = new AdminTopicsTabController();
		questionsController = new AdminQuestionsTabController();
		quizController = new AdminQuizTabController();
		
		topicsTab.setContent(topicsController.getRoot());
		questionsTab.setContent(questionsController.getRoot());
		quizTab.setContent(quizController.getRoot());
	}
	
	private void bindData() {
		topicsController.setTopics(topics);
		questionsController.setTopics(topics);
	}
}
