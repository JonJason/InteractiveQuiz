package application.controller;

import java.util.ArrayList;
import java.util.Optional;

import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class AdminTopicsTabController extends BaseController {
	
	@FXML
	private Button addTopicButton;
	
	@FXML
	private Button editTopicButton;
	
	@FXML
	private Button deleteTopicButton;
	
	@FXML
	private ListView<String> topicsListView;
	
	private ObservableList<String> topics;

	public AdminTopicsTabController() {

		loadFXML("admintopicstab");
		
	}
	
	@FXML
	private void showAddTopicDialog(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("New Topic");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Please enter topic:");
		ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().set(0, addButtonType);
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(topic -> {
			if (!topic.equals("") ) {
				topics.add(topic);
				Storage.saveTopics( new ArrayList<String>(topics));
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", "no topic added", "warning");
			}
		});
		
	}
	
	@FXML
	private void showEditTopicDialog(ActionEvent e) {
		String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", "Please select a topic that you want to edit", "warning");
			return;
		}
		
		TextInputDialog dialog = new TextInputDialog(topicsListView.getSelectionModel().getSelectedItem());
		dialog.setTitle("New Topic");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Please enter topic:");
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(topic -> {
			if (!topic.equals("") ) {
				topics.set(topicsListView.getSelectionModel().getSelectedIndex(), topic);
				if (!topic.equals(selectedTopic)) {
					Storage.saveTopics( new ArrayList<String>(topics));	
				}
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", "topic wasn't changed", "warning");
			}
		});
		
	}
	
	@FXML
	private void showDeleteTopicDialog(ActionEvent e) {
		String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", "Please select a topic that you want to delete", "warning");
			return;
		}
		
		Alert confirmationAlert = AlertThrower.createAlert("Delete Confirmation", "Are you sure you want to delete: " + selectedTopic, null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			topics.remove(selectedTopic);
			Storage.saveTopics( new ArrayList<String>(topics));
		}
	}
	
	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
		topicsListView.setItems(this.topics);
	}

}
