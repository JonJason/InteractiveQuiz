package application.controller;

import application.model.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class QuestionsListWidgetController extends BaseController {

	@FXML
	private ComboBox<String> topicsComboBox;
	
	@FXML
	private ListView<Question> questionsListView;
	
	@FXML
	private Button resetFilterButton;
	
	private ObservableList<Question> questions;

	public QuestionsListWidgetController() {
		super("questionslistwidget");

	}

	@FXML
	private void resetFilter(ActionEvent e) {
		topicsComboBox.setValue(null);
	}
	
	@FXML
	private void onTopicChange(ActionEvent e) {
		updateFilteredQuestions();
	}
	
	public String getSelectedTopic() {
		return topicsComboBox.getSelectionModel().getSelectedItem();
	}
	
	public Question getSelectedQuestion() {
		return questionsListView.getSelectionModel().getSelectedItem();
	}

	public void setTopics(ObservableList<String> topics) {
		topicsComboBox.setItems(topics);
	}
	
	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
		questionsListView.setItems(this.questions);
		
		questionsListView.setCellFactory(param -> new ListCell<Question>() {
		    @Override
		    protected void updateItem(Question question, boolean empty) {
		        super.updateItem(question, empty);

		        if (empty || question == null || question.getText() == null) {
		            setText(null);
		        } else {
		            setText(question.getText());
		        }
		    }
		});
	}

	public void updateFilteredQuestions() {

		String topic = topicsComboBox.getSelectionModel().getSelectedItem();
		
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
