package application.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import application.model.Question;
import application.model.Quiz;
import application.model.Statistic;
import application.util.AlertThrower;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class QuizLayoutController extends BaseController {
	
	@FXML
	private Label titleLabel;
	
	@FXML
	private Label schoolLabel;
	
	@FXML
	private Label positionLabel;
	
	@FXML
	private Label timerLabel;
	
	@FXML
	private BorderPane questionBorderPane;
	
	private QuestionLayoutController questionLayoutController;
	
	private QuizHomeController quizHomeController;
	
	private Quiz quiz;
	private String school;
	private Statistic statistic;
	
	private int position = 0;
	private int seconds = 0;
	private int[] score = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	Timeline timeline;
	
	private ArrayList<Integer> order;
	
	static private Integer[] baseOrder = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	public QuizLayoutController(Quiz quiz, String school, Statistic statistic) {
		
		super("quizlayout");

		this.quiz = quiz;
		this.school = school;
		this.statistic = statistic;
		
		renderLayout();
		
		shuffleQuestions();

		showQuestion();
	}
	
	@FXML
	private void submitAnswer(ActionEvent e) {
		if (!questionLayoutController.isAnswerSelected()) {
			AlertThrower.showAlert("No Answer Selected", "Please choose your Answer", 
					null, "warning");
			return;
		}
		
		if (questionLayoutController.isAnswerCorrect()) {
			Alert dialog = AlertThrower.createAlert("Correct!", 
					"You are Correct!", null, "info");
			dialog.showAndWait();
			
			noteResult(2); // correct
			nextQuestion();
			
		} else {
			Alert dialog = AlertThrower.createAlert("Incorrect!", 
					"Oops! The Correct Answer is " + 
					questionLayoutController.getCorrectAnswer(), null, "info");
			dialog.showAndWait();

			noteResult(1); // incorrect
			nextQuestion();
		};
	}
	
	@FXML
	private void giveUp(ActionEvent e) {
		
		Alert confirmationAlert = AlertThrower.createAlert("Give Up?", 
				"Are you sure you want to give up on this one? ", null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			noteResult(0); // pass
			nextQuestion();
		}
		
	}
	
	@FXML
	private void onResetButtonClicked(ActionEvent e) {
		
		Alert confirmationAlert = AlertThrower.createAlert("Reset Quiz?", 
				"Are you sure you want to Reset the Quiz? ", null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			resetQuiz();
		}
	}
	
	@FXML
	private void onQuitButtonClicked(ActionEvent e) {
		
		Alert confirmationAlert = AlertThrower.createAlert("Quit Quiz?", 
				"Are you sure you want to quit the quiz? ", null, "confirm");
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			quizHomeController.showHome();
		}
	}
	
	private void shuffleQuestions() {
		
		long seed = System.nanoTime();
		order = new ArrayList<Integer>(Arrays.asList(baseOrder));
		Collections.shuffle(order, new Random(seed));
		
	}
	
	public void renderLayout() {
		titleLabel.setText(quiz.getName());
		schoolLabel.setText(school);
	}
	
	private void resetQuiz() {
		position = 0;

		for(int i = 0; i < score.length; i++) {
			score[i] = 0;
		}
		
		shuffleQuestions();

		showQuestion();
	}
	
	private void noteResult(int result) {
		// default 0
		// incorrect 1
		// correct 2
		score[position] = result;
		
		switch (result) {
		
		case(0):
			statistic.incGivenupTime(order.get(position).intValue());
			break;
		
		case(1):
			statistic.incIncorrect(order.get(position).intValue());
			break;
		
		case(2):
			statistic.incCorrect(order.get(position).intValue());
			break;
		
		}
		
		
	}
	
	private void nextQuestion() {
		position++;
		
		if (position < order.size()) {
			showQuestion();
		} else {
			showSummary();
		}
		
	}
	
	private void showQuestion() {
		
		positionLabel.setText(Integer.toString(position + 1) + 
				" / " + Integer.toString(order.size()));
		
		Question question = quiz.getQuestion(order.get(position).intValue());
		questionLayoutController = new QuestionLayoutController(question);
		questionBorderPane.setCenter(questionLayoutController.getRoot());
		
		restartTimer();
	}
	
	private void restartTimer() {
		seconds = 30;
    	timerLabel.setText("0 : " + Integer.toString(seconds));
    	
    	if (timeline != null) {
    		timeline.stop();
    	} else {
        	
    		timeline = new Timeline(new KeyFrame(
    	        Duration.millis(1000),
    	        ae ->  {
    	        	seconds--;
    	        	timerLabel.setText("0 : " + Integer.toString(seconds));
    	        }
    	    ));
    		
    		timeline.setCycleCount(seconds);
    		
    	}
		
		timeline.setOnFinished((e) -> {

			nextQuestion();
			
		});
		
		timeline.play();
	}
	
	private void showSummary() {
		int correct = 0;
		int incorrect = 0;
		int passed = 0;
		for(int i = 0; i < score.length; i++) {
			switch(score[i]) {
			case(0):
				passed++;
				break;
			
			case(1):
				incorrect++;
				break;
			
			case(2):
				correct++;
				break;
				
			}
		}
		
		QuizSummaryController quizSummaryController = 
				new QuizSummaryController(correct, incorrect, passed);
		
		quizSummaryController.setQuizHomeController(quizHomeController);
		
		quizHomeController.showView(quizSummaryController.getRoot());
	}
	
	public void setQuizHomeController(QuizHomeController quizHomeController) {
		this.quizHomeController = quizHomeController;
	}
	
}
