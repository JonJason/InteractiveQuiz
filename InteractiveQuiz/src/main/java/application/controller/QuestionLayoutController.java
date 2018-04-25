package application.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import application.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class QuestionLayoutController extends BaseController {
	
	@FXML
	private Label questionLabel;

	@FXML
	private StackPane pictureStackPane;
	
	@FXML
	private ImageView pictureImageView;

	@FXML
	private RadioButton answerRadioButton1;

	@FXML
	private RadioButton answerRadioButton2;

	@FXML
	private RadioButton answerRadioButton3;

	@FXML
	private RadioButton answerRadioButton4;
	
	@FXML
	private ToggleGroup answerGroup;
	
	private ArrayList<RadioButton> answers;
	
	private Question question;
	private ArrayList<Integer> order;
	static private Integer[] baseOrder = { 0, 1, 2, 3};
	
	public QuestionLayoutController(Question question) {
		super("questionlayout");
		
		this.question = question;
		
		renderQuestion();
		
		shuffleAnswers();
		
		renderAnswers();
	}
	
	private void renderQuestion() {
		questionLabel.setText(question.getText());
		if (question.getPicture() != null) {
			pictureImageView.setImage(new Image(question.getPicture()));
		} else {
			pictureStackPane.setVisible(false);
			pictureStackPane.setManaged(false);
		}
	}
	
	private void shuffleAnswers() {

		long seed = System.nanoTime();
		order = new ArrayList<Integer>(Arrays.asList(baseOrder));
		Collections.shuffle(order, new Random(seed));
		
	}
	
	private void renderAnswers() {
		answers = new ArrayList<RadioButton>(4);
		answers.add(answerRadioButton1);
		answers.add(answerRadioButton2);
		answers.add(answerRadioButton3);
		answers.add(answerRadioButton4);
		
		for (int i = 0; i < answers.size(); i++) {
			answers.get(i).setText(question.getAnswer(order.get(i).intValue()));
		}
	}
	
	public boolean isAnswerSelected() {
		return answerGroup.getSelectedToggle() != null;
	}
	
	public boolean isAnswerCorrect() {
		
		order.get(answers.indexOf(answerGroup.getSelectedToggle()));
		
		return order.get(answers.indexOf(answerGroup.getSelectedToggle())).equals(question.getCorrectAnswerIndex());
	}
	
	public String getCorrectAnswer() {
		return question.getCorrectAnswer();
	}
	
}
