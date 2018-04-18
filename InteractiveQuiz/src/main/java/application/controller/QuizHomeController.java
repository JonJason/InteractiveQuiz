package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

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
		System.out.println("Show the QUIZZZZ!!!!");
	}
	
	@FXML
	private void openAdmin(ActionEvent e) {

    	Window owner = ((Button) e.getSource()).getScene().getWindow();

        Stage newStage = new Stage();
        newStage.setTitle("Quiz Administrator");

        newStage.setScene(new Scene(new AdminHomeController().getRoot()));

        newStage.initOwner(owner);
        newStage.show();
        
	}

}
