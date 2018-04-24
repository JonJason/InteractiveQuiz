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

public class AdminSchoolsTabController extends BaseController {
	
	@FXML
	private ListView<String> schoolsListView;
	
	private ObservableList<String> schools;

	public AdminSchoolsTabController() {

		super("adminschoolstab");
		
	}
	
	@FXML
	private void showAddSchoolDialog(ActionEvent e) {
		
		TextInputDialog dialog = new TextInputDialog();
		
		dialog.setTitle("New School");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Please enter school:");
		ButtonType addButtonType = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().set(0, addButtonType);
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(school -> {
			if (!school.equals("") ) {
				
				if(schools.contains(school)) {
					AlertThrower.showAlert("Duplicate Detected", "School already exists", 
							"school wasn't added", "warning");
					return;
				}
				
				schools.add(school);
				Storage.saveSchools( new ArrayList<String>(schools));
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", "no school added", "warning");
			}
		});
		
	}
	
	@FXML
	private void showEditSchoolDialog(ActionEvent e) {
		String selectedSchool = schoolsListView.getSelectionModel().getSelectedItem();
		if (selectedSchool == null) {
			AlertThrower.showAlert("No school Selected", "No school Selected", "Please select a school that you want to edit", "warning");
			return;
		}
		
		TextInputDialog dialog = new TextInputDialog(schoolsListView.getSelectionModel().getSelectedItem());
		dialog.setTitle("Edit School");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		dialog.setContentText("Please enter school:");
		ButtonType addButtonType = new ButtonType("Edit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().set(0, addButtonType);
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(school -> {
			if (!school.equals("") ) {
				if (!school.equals(selectedSchool)) {
					
					if(schools.contains(school)) {
						AlertThrower.showAlert("Duplicate Detected", "School already exists", 
								"school wasn't changed", "warning");
						return;
					}
					
					schools.set(schoolsListView.getSelectionModel().getSelectedIndex(), school);
					Storage.saveSchools( new ArrayList<String>(schools));	
				}
			} else {
				AlertThrower.showAlert("Invalid Input", "input was empty", 
						"school wasn't changed", "warning");
			}
		});
		
	}
	
	@FXML
	private void showDeleteSchoolDialog(ActionEvent e) {
		String selectedSchool = schoolsListView.getSelectionModel().getSelectedItem();
		if (selectedSchool == null) {
			AlertThrower.showAlert("No school Selected", "No school Selected", 
					"Please select a school that you want to delete", "warning");
			return;
		}
		
		Alert confirmationAlert = AlertThrower.createAlert("Delete Confirmation", 
				"Are you sure you want to delete: " + selectedSchool, null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			schools.remove(selectedSchool);
			Storage.saveSchools( new ArrayList<String>(schools));
		}
	}
	
	public void setSchools(ObservableList<String> schools) {
		this.schools = schools;
		schoolsListView.setItems(this.schools);
	}

}
