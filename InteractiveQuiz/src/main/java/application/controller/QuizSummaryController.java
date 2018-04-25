package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class QuizSummaryController extends BaseController {
	
	@FXML
	private Label scoreLabel;
	
	@FXML
	private Label resultLabel;
	
	@FXML
	private Label correctLabel;
	
	@FXML
	private Label incorrectLabel;
	
	@FXML
	private Label passedLabel;
	
	final private int correct;
	final private int incorrect;
	final private int passed;
	final private int total;
	private QuizHomeController quizHomeController;

	public QuizSummaryController(int correct, int incorrect, int passed) {
		super("quizsummary");
		
		this.correct = correct;
		this.incorrect = incorrect;
		this.passed = passed;
		total = correct + incorrect + passed;
		
		renderSummary();
	}
	
	@FXML
	private void showHome() {
		quizHomeController.showHome();
	}
	
	private void renderSummary() {
		scoreLabel.setText("You got " + Integer.toString(Math.round(correct * 100 / total)) + "!");
		resultLabel.setText(Integer.toString(Math.round(correct * 100 / total)));
		correctLabel.setText(Integer.toString(correct));
		incorrectLabel.setText(Integer.toString(incorrect));
		passedLabel.setText(Integer.toString(passed));
	}
	
	public void setQuizHomeController(QuizHomeController quizHomeController) {
		this.quizHomeController = quizHomeController;
	}
	
}
