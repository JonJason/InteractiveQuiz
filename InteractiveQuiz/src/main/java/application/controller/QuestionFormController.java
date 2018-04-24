package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
	private TextArea textTextArea;

	@FXML
	private ImageView pictureImageView;

	@FXML
	private Label pictureLabel;
	
	@FXML
	private Button deletePictureButton;

	@FXML
	private TextField answerTextField1;

	@FXML
	private TextField answerTextField2;

	@FXML
	private TextField answerTextField3;

	@FXML
	private TextField answerTextField4;

	@FXML
	private ToggleButton correctAnswerButton1;

	@FXML
	private ToggleButton correctAnswerButton2;

	@FXML
	private ToggleButton correctAnswerButton3;

	@FXML
	private ToggleButton correctAnswerButton4;

	@FXML
	private ListView<String> providerTopicsListView;

	@FXML
	private ListView<String> attachedTopicsListView;

	private ObservableList<String> providerTopics;
	private ObservableList<String> attachedTopics;
	private String imgPath;
	private ArrayList<ToggleButton> correctButtons = new ArrayList<ToggleButton>(4);

	public QuestionFormController() {

		super("questionform");

		correctButtons.add(correctAnswerButton1);
		correctButtons.add(correctAnswerButton2);
		correctButtons.add(correctAnswerButton3);
		correctButtons.add(correctAnswerButton4);
	}

	@FXML
	private void addTopic(ActionEvent e) {
		String selectedTopic = providerTopicsListView.getSelectionModel().getSelectedItem();
		if (selectedTopic == null) {
			AlertThrower.showAlert("No topic Selected", "No topic Selected", 
					"Please select a topic that you want to add", "warning");
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
			AlertThrower.showAlert("No topic Selected", "No topic Selected", 
					"Please select a topic that you want to remove", "warning");
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
            deletePictureButton.setDisable(false);
        } catch (URISyntaxException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@FXML
	private void deletePicture(ActionEvent e) {

		imgPath = null;
		pictureLabel.setText(null);
		pictureImageView.setImage(new Image(getClass().getResource("/img/placeholder.png").toString()));
		deletePictureButton.setDisable(true);
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

	private Question parseData() {
		
		Question question = new Question();

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
		
		return question;
	}

	private void serializeData(Question question) {

		// text
		if (question.getText() != null) {
			textTextArea.setText(question.getText());
		}

		if (question.getPicture() != null) {
			imgPath = question.getPicture();
            pictureImageView.setImage(new Image(imgPath));
            deletePictureButton.setDisable(false);
		} else {
            deletePictureButton.setDisable(true);
		}

		// answers
		answerTextField1.setText(question.getAnswer(0));
		answerTextField2.setText(question.getAnswer(1));
		answerTextField3.setText(question.getAnswer(2));
		answerTextField4.setText(question.getAnswer(3));

		// correct answer index
		if (question.getCorrectAnswerIndex() != -1) {
			correctButtons.get(question.getCorrectAnswerIndex()).setSelected(true);
		}
		
		// topics
		attachedTopics = FXCollections.observableArrayList(question.getTopics());
		attachedTopicsListView.setItems(attachedTopics);
	}

	public void setTopics(ObservableList<String> topics) {
		providerTopics = topics;
		providerTopicsListView.setItems(providerTopics);
	}

	public void fillForm(Question question) {
		
		serializeData(question);
	}

	public Question getQuestion() {
		return parseData();
	}
}
