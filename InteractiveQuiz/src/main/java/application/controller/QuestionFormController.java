package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.model.Question;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class QuestionFormController extends BaseController {

	@FXML
	TextField titleTextField;

	@FXML
	TextArea textTextArea;

	@FXML
	ImageView pictureImageView;

	@FXML
	Button uploadButton;

	@FXML
	Label pictureLabel;

	@FXML
	TextField answerTextField1;

	@FXML
	TextField answerTextField2;

	@FXML
	TextField answerTextField3;

	@FXML
	TextField answerTextField4;

	@FXML
	ToggleButton correctAnswerButton1;

	@FXML
	ToggleButton correctAnswerButton2;

	@FXML
	ToggleButton correctAnswerButton3;

	@FXML
	ToggleButton correctAnswerButton4;

	@FXML
	ListView<String> providerTopicsListView;

	@FXML
	ListView<String> attachedTopicsListView;

	@FXML
	Button addTopicButton;

	@FXML
	Button removeTopicButton;

	private Question question;
	private ObservableList<String> providerTopics;
	private ObservableList<String> attachedTopics;
	private String imgPath;
	private ArrayList<ToggleButton> correctButtons = new ArrayList<ToggleButton>(4);

	public QuestionFormController() {

		loadFXML("questionform");

		correctButtons.add(correctAnswerButton1);
		correctButtons.add(correctAnswerButton2);
		correctButtons.add(correctAnswerButton3);
		correctButtons.add(correctAnswerButton4);
	}

	@FXML
	private void addTopic(ActionEvent e) {
		String selectedTopic = providerTopicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", "Please select a topic that you want to add", "warning");
			return;
		}

		if (!attachedTopics.contains(selectedTopic)) {
			attachedTopics.add(selectedTopic);
		}
	}

	@FXML
	private void removeTopic(ActionEvent e) {
		String selectedTopic = attachedTopicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", "Please select a topic that you want to add", "warning");
			return;
		}

		attachedTopics.remove(selectedTopic);
	}

	@FXML
	private void uploadPicture(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
   	         new ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File source = chooser.showOpenDialog(((Button) e.getSource()).getScene().getWindow());
        try {
            imgPath = Storage.storeAndGetImage(source);
            pictureLabel.setText(source.getName());
            pictureImageView.setImage(new Image(imgPath));
        } catch (URISyntaxException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@FXML
	private void correctButtonPressed(ActionEvent e) {
		ToggleButton toggleButton = (ToggleButton) e.getSource();
		if (toggleButton.isSelected()) {
			correctButtons.forEach((button) -> {
				if (!button.equals(toggleButton)) {
					button.setSelected(false);
				}
			});
		};
	}

	public void setTopics(ObservableList<String> topics) {
		providerTopics = topics;
		providerTopicsListView.setItems(providerTopics);
	}

	private void parseData() {

		// title
		if (!titleTextField.getText().equals("")) {
			question.setTitle(titleTextField.getText());
		}

		// text
		if (!textTextArea.getText().equals("")) {
			question.setText(textTextArea.getText());
		}

		if (imgPath != null) {
			question.setPicture(imgPath);
		}

		// answers
		question.setAnswer(0, answerTextField1.getText());
		question.setAnswer(1, answerTextField2.getText());
		question.setAnswer(2, answerTextField3.getText());
		question.setAnswer(3, answerTextField4.getText());

		int correctIndex = -1;
		for (int i = 0; i < correctButtons.size();i++) {
			if (correctButtons.get(i).isSelected()) {
				correctIndex = i;
			}
		}
		question.setCorrectAnswerIndex(correctIndex);
		question.setTopics(new ArrayList<String>(attachedTopics));
	}

	private void serializeData() {

		// title
		if (question.getTitle() != null) {
			titleTextField.setText(question.getTitle());
		}

		// text
		if (question.getText() != null) {
			textTextArea.setText(question.getText());
		}

		if (question.getPicture() != null) {
			imgPath = question.getPicture();
            pictureImageView.setImage(new Image(imgPath));
		}

		// answers
		answerTextField1.setText(question.getAnswer(0));
		answerTextField2.setText(question.getAnswer(1));
		answerTextField3.setText(question.getAnswer(2));
		answerTextField4.setText(question.getAnswer(3));

		if (question.getCorrectAnswerIndex() != -1) {
			correctButtons.get(question.getCorrectAnswerIndex()).setSelected(true);
		}
	}

	public void setQuestion(Question question) {
		this.question = question;
		attachedTopics = FXCollections.observableArrayList(this.question.getTopics());
		attachedTopicsListView.setItems(attachedTopics);
		
		serializeData();
	}

	public Question getQuestion() {
		parseData();
		return question;
	}
}
