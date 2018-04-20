package application.controller;

import java.io.IOException;
import java.util.Optional;

import application.model.Quiz;
import application.model.Statistic;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class QuizHomeController extends BaseController {

	@FXML
	private Button startButton;
	
	@FXML
	private Button adminButton;

	public QuizHomeController() {

		loadFXML("quizhome");

	}
	
	@FXML
	private void startQuiz(ActionEvent e) {

		try {
			Quiz quiz = Storage.loadQuiz();

			ChoiceDialog<String> dialog = new ChoiceDialog<String>(quiz.getSchools().get(0), quiz.getSchools());
			dialog.setTitle("School");
			dialog.setHeaderText("Pick your School");
			dialog.setContentText("Choose your school:");

			Optional<String> result = dialog.showAndWait();

			result.ifPresent(school -> {
				showQuizLayout(quiz, school);
			});
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void openAdmin(ActionEvent e) {
		

		Dialog<Pair<String, String>> dialog = createLoginDialog();

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			if (usernamePassword.getKey() == "admin" && usernamePassword.getValue() == "team7") {
				AlertThrower.showAlert("Invalid Login", "Wrong Username or Password", 
						"Please enter the correct username and password", "warning");
			} else {

		    	Window owner = ((Button) e.getSource()).getScene().getWindow();

		        Stage newStage = new Stage();
		        newStage.setTitle("Quiz Administrator");

		        newStage.setScene(new Scene(new AdminHomeController().getRoot(), 670, 400));

		        newStage.initOwner(owner);
		        newStage.show();
			}
		});
        
	}
	
	private void showQuizLayout(Quiz quiz, String school) {
		try {
			Statistic statistic = Storage.loadStatistic(quiz.getName(), school);
			System.out.println(statistic);
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	private Dialog<Pair<String, String>> createLoginDialog() {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);
		
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> username.requestFocus());
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});
		
		return dialog;
	}

}
