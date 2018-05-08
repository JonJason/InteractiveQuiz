package application.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import application.model.Question;
import application.model.Quiz;
import application.model.Statistic;
import application.model.StatisticData;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	private ArrayList<Integer> score;
	Timeline timeline;
	
	private ArrayList<Integer> order;
	
	private ArrayList<Integer> baseOrder;
	
	public QuizLayoutController(Quiz quiz, String school, Statistic statistic) {
		
		super("quizlayout");

		this.quiz = quiz;
		this.school = school;
		this.statistic = statistic;

		while(this.statistic.getData().size() < quiz.getQuestions().size()) {
			this.statistic.getData().add(new StatisticData());
		}

		baseOrder = new ArrayList<Integer>();
		score = new ArrayList<Integer>();
		
		for (int i = 0; i < quiz.getQuestions().size(); i++) {
			baseOrder.add(new Integer(i));
			score.add(new Integer(0));
		}
		
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
			ImageView icon = new ImageView(getClass().getResource("/img/check.png").toString());
			icon.setFitHeight(50);
			icon.setFitWidth(50);
			dialog.setGraphic(icon);
			stopTimer();
			dialog.showAndWait();
			
			noteResult(2); // correct
			nextQuestion();
			
		} else {
			Alert dialog = AlertThrower.createAlert("Incorrect!", 
					"Oops! The Correct Answer is " + 
					questionLayoutController.getCorrectAnswer(), null, "info");
			ImageView icon = new ImageView(getClass().getResource("/img/cancel.png").toString());
			icon.setFitHeight(50);
			icon.setFitWidth(50);
			dialog.setGraphic(icon);
			stopTimer();
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
			Alert dialog = AlertThrower.createAlert("That's Too Bad!", 
					"The Correct Answer is " + 
					questionLayoutController.getCorrectAnswer(), null, "info");
			ImageView icon = new ImageView(getClass().getResource("/img/pass.png").toString());
			icon.setFitHeight(50);
			icon.setFitWidth(50);
			dialog.setGraphic(icon);
			stopTimer();
			dialog.showAndWait();
			
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
			stopTimer();
			quizHomeController.showHome();
		}
	}
	
	private void shuffleQuestions() {
		
		long seed = System.nanoTime();
		order = (ArrayList<Integer>) baseOrder.clone();
		Collections.shuffle(order, new Random(seed));
		
	}
	
	public void renderLayout() {
		titleLabel.setText(quiz.getName());
		schoolLabel.setText(school);
	}
	
	private void resetQuiz() {
		position = 0;

		for(int i = 0; i < score.size(); i++) {
			score.set(i, new Integer(0));
		}
		
		shuffleQuestions();
		
		stopTimer();

		showQuestion();
	}
	
	private void noteResult(int result) {
		// default 0, incorrect 1, correct 2
		score.set(position, new Integer(result));
		
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
		
		Storage.saveStatistic(statistic);
		
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
				"/" + Integer.toString(order.size()));
		
		Question question = quiz.getQuestion(order.get(position).intValue());
		questionLayoutController = new QuestionLayoutController(question);
		questionBorderPane.setCenter(questionLayoutController.getRoot());
		
		restartTimer();
	}
	
	private void stopTimer() {
		if (timeline != null) {
    		timeline.stop();
    	}
	}
	
	private void restartTimer() {
		seconds = 30;
    	timerLabel.setText("0:" + Integer.toString(seconds));
    	
    	if (timeline != null) {
    		timeline.stop();
    	} else {
        	
    		timeline = new Timeline(new KeyFrame(
    	        Duration.millis(1000),
    	        ae ->  {
    	        	seconds--;
    	        	timerLabel.setText("0:" + Integer.toString(seconds));
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
		for(int i = 0; i < score.size(); i++) {
			if (score.get(i).equals(0)) {
				passed++;
				
			} else if(score.get(i).equals(1)) {
				incorrect++;
				
			} else if (score.get(i).equals(2)) {
				correct++;
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
