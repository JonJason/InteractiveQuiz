package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class AdminHomeController extends BaseController {

	@FXML
	Tab topicsTab;
	
	@FXML
	Tab questionsTab;
	
	@FXML
	Tab quizTab;

	public AdminHomeController() {

		loadFXML("adminhome");
		
		renderTabs();

	}
	
	private void renderTabs() {
		topicsTab.setContent(new AdminTopicsTabController().getRoot());
		questionsTab.setContent(new AdminQuestionsTabController().getRoot());
		quizTab.setContent(new AdminQuizTabController().getRoot());
	}
}
