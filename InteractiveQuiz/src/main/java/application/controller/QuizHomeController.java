package application.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class QuizHomeController extends BaseController {

	@FXML
	private BorderPane borderPane;
	
	@FXML
	private GridPane homeGridPane;
	
	@FXML
	private Label quizTitleLabel;
	
	private Quiz quiz;

	public QuizHomeController() {

		super("quizhome");

		try {
			quiz = Storage.loadQuiz();
			
			updateQuizData();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@FXML
	private void startQuiz(ActionEvent e) {

		ChoiceDialog<String> dialog = new ChoiceDialog<String>(
				quiz.getSchools().get(0), quiz.getSchools());
		dialog.setTitle("School");
		dialog.setHeaderText("Pick your School");
		dialog.setContentText("Choose your school:");

		Optional<String> result = dialog.showAndWait();

		result.ifPresent(school -> {
			showQuizLayout(quiz, school);
		});
	}
	
	@FXML
	private void openAdmin(ActionEvent e) {
		

		Dialog<Pair<String, String>> dialog = createLoginDialog();

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			System.out.println(usernamePassword.getKey().equals("admin"));
			System.out.println(usernamePassword.getValue().equals("team_7"));
			if (!usernamePassword.getKey().equals("admin") || 
					!usernamePassword.getValue().equals("team_7")) {
				AlertThrower.showAlert("Invalid Login", "Wrong Username or Password", 
						"Please enter the correct username and password", "warning");
			} else {

		    	Window owner = ((Button) e.getSource()).getScene().getWindow();

		        Stage newStage = new Stage();
		        newStage.setTitle("Quiz Administrator");
		        
		        AdminHomeController adminHomeController = new AdminHomeController(quiz);
		        
		        adminHomeController.setQuizHomeController(this);

		        newStage.setScene(new Scene(adminHomeController.getRoot(), 735, 500));

		        newStage.initOwner(owner);
		        
		        newStage.setOnCloseRequest(event -> {

		            Alert closeConfirmation = AlertThrower.createCloseConfrimationAlert(newStage);

		            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		            if (!ButtonType.OK.equals(closeResponse.get())) {
		                event.consume();
		            }
		        });
		        
		        newStage.show();
			}
		});
        
	}
	
	private void showQuizLayout(Quiz quiz, String school) {
		try {
			Statistic statistic = Storage.loadStatistic(quiz.getName(), school);
			statistic.cloneQuestions(quiz.getQuestions());
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			statistic.setDateString(formatter.format(quiz.getDate()));
			
			QuizLayoutController quizLayoutController = 
					new QuizLayoutController(quiz, school, statistic);
			quizLayoutController.setQuizHomeController(this);
			
			showView(quizLayoutController.getRoot());
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

		grid.add(new Label("Username :"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password :"), 0, 1);
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
	
	public void showHome() {
		showView(homeGridPane);
	}

	public void showView(Parent root) {
		borderPane.setCenter(root);
	}

	public void updateQuizData() {
		
		if (quiz.getName() != null) {
			quizTitleLabel.setText(quiz.getName());	
		}
	}

}
