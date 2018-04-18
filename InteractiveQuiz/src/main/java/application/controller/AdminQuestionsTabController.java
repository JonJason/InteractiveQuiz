package application.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

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

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
		topicComboBox.setItems(this.topics);
	}
}
