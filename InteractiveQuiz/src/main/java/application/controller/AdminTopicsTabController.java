package application.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class AdminTopicsTabController extends BaseController {
	
	@FXML
	Button addTopicButton;
	
	@FXML
	Button editTopicButton;
	
	@FXML
	Button deleteTopicButton;
	
	@FXML
	ListView<String> topicsListView;
	
	ObservableList<String> topics;

	public AdminTopicsTabController() {

		loadFXML("admintopicstab");
		
		try {
			topics = FXCollections.observableArrayList(Storage.loadTopics());
			topicsListView.setItems(topics);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@FXML
	private void showAddTopicDialog(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("New Topic");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Please enter topic:");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(topic -> {
			if (!topic.equals("") ) {
				topics.add(topic);
				Storage.saveTopics( new ArrayList<String>(topics));
			}
		});
		
	}
	
	@FXML
	private void showEditTopicDialog(ActionEvent e) {
		String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "Please select a topic that you want to edit", null, "warning");
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
				AlertThrower.showAlert("Invalid Input", "input was empty, topic wasn't changed", null, "warning");
			}
		});
		
	}
	
	@FXML
	private void showDeleteConfirmationDialog(ActionEvent e) {
		String selectedTopic = topicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "Please select a topic that you want to delete", null, "warning");
			return;
		}
		
		Alert confirmationAlert = AlertThrower.createAlert("Delete Confirmation", "Are you sure you want to delete: " + selectedTopic, null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			topics.remove(topicsListView.getSelectionModel().getSelectedIndex());
			Storage.saveTopics( new ArrayList<String>(topics));
		}
	}

}
