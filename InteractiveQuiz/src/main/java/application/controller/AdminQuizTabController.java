package application.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import application.model.Question;
import application.model.Quiz;
import application.model.Statistic;
import application.model.StatisticData;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBar.ButtonData;

public class AdminQuizTabController extends BaseController {

	@FXML
	private Label quizNameLabel;

	@FXML
	private Label quizDateLabel;
	
	@FXML
	private Label totalCorrectLabel;
	
	@FXML
	private Label totalIncorrectLabel;
	
	@FXML
	private Label totalGivenUpLabel;
	
	@FXML
	private ChoiceBox<String> quizChoiceBox;

	@FXML
	private ChoiceBox<String> schoolChoiceBox;

	@FXML
	private BarChart<String, Integer> statisticBarChart;
	
	@FXML
	private GridPane statisticGridPane;
	
	@FXML
	private Label noStatisticLabel;

	private Quiz quiz;
	private ObservableList<String> statQuizNames;
	private ObservableList<String> statSchools;
	private HashMap<String, HashMap<String, Statistic>> statistics;

	private ObservableList<Question> questions;
	private ObservableList<String> topics;
	private ObservableList<String> schools;

	private ObservableList<XYChart.Data<String, Integer>> correctData;
	private ObservableList<XYChart.Data<String, Integer>> incorrectData;
	private ObservableList<XYChart.Data<String, Integer>> givenUpData;

	private XYChart.Series<String, Integer> correctSeries;
	private XYChart.Series<String, Integer> incorrectSeries;
	private XYChart.Series<String, Integer> givenUpSeries;

	private QuizHomeController quizHomeController;

	public AdminQuizTabController(Quiz quiz) {

		super("adminquiztab");

		this.quiz = quiz;

		try {
			statistics = Storage.loadStatistics();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		renderQuiz();

		renderChart();

		renderChoiceBoxes();
		
		if (statistics.keySet().isEmpty()) {
			((GridPane) getRoot()).getChildren().remove(statisticGridPane);
		} else {
			((GridPane) getRoot()).getChildren().remove(noStatisticLabel);
		}
	}

	@FXML
	private void showQuizSetupDialog(ActionEvent e) {

		AdminQuizSetupController setupController = new AdminQuizSetupController();
		setupController.setTopics(topics);
		setupController.setQuestions(questions);
		setupController.setSchools(schools);

		setupController.setQuiz(quiz);

		Dialog<Quiz> dialog = createQuizDialog(setupController);

		Optional<Quiz> result = dialog.showAndWait();
		result.ifPresent(quiz -> {
			Storage.saveQuiz(quiz);
			quizHomeController.updateQuizData();
			renderQuiz();
		});
	}
	
	@FXML
	private void exportSummary(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Generate Report File");
		fc.getExtensionFilters().addAll(
//				new ExtensionFilter("CSV", "*.csv"),
				new ExtensionFilter("Text", "*.txt"));
		File file = fc.showSaveDialog(getRoot().getScene().getWindow());

		if (file == null) {
			return;
		}

		String extension = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
		writeReport(file, extension);
	}
	
	@FXML
	private void resetStatistics(ActionEvent e) {
		
		Alert confirmationAlert = AlertThrower.createAlert("Reset Confirmation", 
				"Are you sure you want to reset the Statistics?", null, "confirm");
		
		Optional<ButtonType> answer = confirmationAlert.showAndWait();
		if (answer.get() == ButtonType.OK){
			Storage.clearStatistics();
			((GridPane) getRoot()).getChildren().remove(statisticGridPane);
			((GridPane) getRoot()).add(noStatisticLabel, 0, 2);
		}
	}

	public void renderQuiz() {
		quizNameLabel.setText(quiz.getName());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        if (quiz.getDate() == null) {
        	quizDateLabel.setText("Not Set Yet");
        } else {
    		quizDateLabel.setText(formatter.format(quiz.getDate()));
        }
	}

	private void renderChart() {
		correctData = FXCollections.observableArrayList();
		incorrectData = FXCollections.observableArrayList();
		givenUpData = FXCollections.observableArrayList();

		for (int i = 0; i < 10; i++) {
			correctData.add(new XYChart.Data<String, Integer>(Integer.toString(i + 1),0));
			incorrectData.add(new XYChart.Data<String, Integer>(Integer.toString(i + 1),0));
			givenUpData.add(new XYChart.Data<String, Integer>(Integer.toString(i + 1),0));
		}

		correctSeries = new XYChart.Series<String, Integer>(correctData);
		incorrectSeries = new XYChart.Series<String, Integer>(incorrectData);
		givenUpSeries = new XYChart.Series<String, Integer>(givenUpData);

		statisticBarChart.getData().add(incorrectSeries);
		statisticBarChart.getData().add(givenUpSeries);
		statisticBarChart.getData().add(correctSeries);

		correctSeries.setName("Correctly Answered");
		incorrectSeries.setName("Incorrectly Answered");
		givenUpSeries.setName("Given Up");

	}

	public void renderChoiceBoxes() {
		statQuizNames = FXCollections.observableArrayList();
		quizChoiceBox.setItems(statQuizNames);

		statSchools = FXCollections.observableArrayList();
		schoolChoiceBox.setItems(statSchools);

		quizChoiceBox.setOnAction(e -> {
			String lastValue = schoolChoiceBox.getValue();

			statSchools.clear();
			statistics.get(quizChoiceBox.getValue()).keySet().forEach(school -> {
				statSchools.add(school);
			});

			if (statSchools.contains(lastValue)) {
				schoolChoiceBox.setValue(lastValue);
			} else if (statQuizNames.size() > 0) {
				schoolChoiceBox.setValue(statSchools.get(0));
			}
		});

		schoolChoiceBox.setOnAction(e -> {
			renderStatistic(statistics.get(quizChoiceBox.getValue()).get(schoolChoiceBox.getValue()).getData());
		});

		statQuizNames.clear();
		statistics.keySet().forEach(quizName -> {
			statQuizNames.add(quizName);
		});

		if (statQuizNames.size() > 0) {
			quizChoiceBox.setValue(statQuizNames.get(0));
		}
	}

	public void renderStatistic(ArrayList<StatisticData> dataList) {

		int totalCorrect = 0;
		int totalIncorrect = 0;
		int totalGivenUp = 0;

		for (int i = 0; i < dataList.size(); i++) {
			
			totalCorrect += dataList.get(i).getAnsweredCorrectly();
			correctData.set(i,new XYChart.Data<String, Integer>(
					Integer.toString(i + 1),
					dataList.get(i).getAnsweredCorrectly())
			);

			totalIncorrect += dataList.get(i).getAnsweredIncorrectly();
			incorrectData.set(i,new XYChart.Data<String, Integer>(
					Integer.toString(i + 1),
					dataList.get(i).getAnsweredIncorrectly())
			);

			totalGivenUp += dataList.get(i).getGivenUpTime();
			givenUpData.set(i,new XYChart.Data<String, Integer>(
					Integer.toString(i + 1),
					dataList.get(i).getGivenUpTime())
			);
		}
		
		int total = totalCorrect + totalIncorrect + totalGivenUp;

		totalCorrectLabel.setText(Integer.toString(totalCorrect) + 
				" (" + String.format("%.2f", (float) Math.round(
						totalCorrect * 10000.0f / total) / 100) + "%)");
		totalIncorrectLabel.setText(Integer.toString(totalIncorrect) + 
				" (" + String.format("%.2f", (float) Math.round(
						totalIncorrect * 10000.0f / total) / 100) + "%)");
		totalGivenUpLabel.setText(Integer.toString(totalGivenUp) + 
				" (" + String.format("%.2f", (float) Math.round(
						totalGivenUp * 10000.0f / total) / 100) + "%)");
	}

	public void setQuestions(ObservableList<Question> questions) {
		this.questions = questions;
	}

	public void setTopics(ObservableList<String> topics) {
		this.topics = topics;
	}

	public void setSchools(ObservableList<String> schools) {
		this.schools = schools;
	}

	public Dialog<Quiz> createQuizDialog(AdminQuizSetupController setupController) {

		Dialog<Quiz> dialog = new Dialog<Quiz>();

		ButtonType okButtonType = new ButtonType("Save Changes", ButtonData.OK_DONE);
		dialog.setTitle("Setup Quiz");
		dialog.setDialogPane((DialogPane) setupController.getRoot());
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return setupController.getQuiz();
			}
		    return null;
		});

		return dialog;

	}
	
	private void writeReport(File file, String extension) {
		switch (extension) {

		// csv
//		case (".csv"):
//			try {
//				PrintWriter out = new PrintWriter(file);
//
//				for (int i = 0; i < summaries.size(); i++) {
//					out.println(summaries.get(i).toCSV());
//				}
//
//	            out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			break;

		// txt
		case (".txt"):
			try {
				PrintWriter out = new PrintWriter(file);

				String tab1 = "    ";
				String tab2 = tab1 + tab1;
				
				statistics.keySet().forEach(quizName -> {
					// QUIZ HEADER
					out.println("[ " + quizName + " ]");
					statistics.get(quizName).keySet().forEach(school -> {
						
						// SCHOOL HEADER
						out.println();
						out.println(school + " :");
						
						// calculation
						ArrayList<StatisticData> dataList = statistics.get(quizName).get(school).getData();
						ArrayList<Question> questionList = statistics.get(quizName).get(school).getQuestions();
						
						int 	easyQ 			= 0;
						float 	maxPCorrect 	= 0;
						int 	missunderstoodQ	= 0;
						float 	maxPIncorrect 	= 0;
						int 	hardQ 			= 0;
						float 	maxPGivenUp 	= 0;
						
						int totalCorrect 	= 0;
						int totalIncorrect 	= 0;
						int totalGivenUp 	= 0;
						
						for (int i = 0; i < dataList.size(); i++) {
							
							StatisticData data = dataList.get(i);
							Question question = questionList.get(i);

							int correct = data.getAnsweredCorrectly();
							int incorrect = data.getAnsweredIncorrectly();
							int givenUp = data.getGivenUpTime();
							int totalEncountered = correct + incorrect + givenUp;
							
							float pCorrect = (float) Math.round(
									correct * 10000.0f / totalEncountered) / 100;
							float pIncorrect = (float) Math.round(
									incorrect * 10000.0f / totalEncountered) / 100;
							float pGivenUp = (float) Math.round(
									givenUp * 10000.0f / totalEncountered) / 100;

							// each data printing business
							out.println();
							out.print(tab1 + Integer.toString(i + 1) + ". ");
							out.println(question.getText());
							
							out.println();
							out.print(tab2 + "Correct\t\t: " + correct);
							out.println(String.format(" (%.2f%%)", pCorrect));
							out.print(tab2 + "Incorrect\t: " + incorrect);
							out.println(String.format(" (%.2f%%)", pIncorrect));
							out.print(tab2 + "Given Up\t: " + givenUp);
							out.println(String.format(" (%.2f%%)", pGivenUp));
							out.println(tab2 + "Total\t\t: " + totalEncountered);
							
							// calculation business
							totalCorrect += correct;
							totalIncorrect += incorrect;
							totalGivenUp += givenUp;
							
							if (pCorrect > maxPCorrect) {
								maxPCorrect = pCorrect;
								easyQ = i;
							}
							
							if (pIncorrect > maxPIncorrect) {
								maxPIncorrect = pIncorrect;
								missunderstoodQ = i;
							}
							
							if (pGivenUp > maxPGivenUp) {
								maxPGivenUp = pGivenUp;
								hardQ = i;
							}
						}
						
						int total = totalCorrect + totalIncorrect + totalGivenUp;
						
						float pTotalCorrect = (float) Math.round(
								totalCorrect * 10000.0f / total) / 100;
						float pTotalIncorrect = (float) Math.round(
								totalIncorrect * 10000.0f / total) / 100;
						float pTotalGivenUp = (float) Math.round(
								totalGivenUp * 10000.0f / total) / 100;
						
						out.println();
						out.println(tab1 + "School Performance Summary :");
						
						out.println();
						out.print(tab2 + "Correct\t\t: " + totalCorrect);
						out.println(String.format(" (%.2f%%)", pTotalCorrect));
						out.print(tab2 + "Incorrect\t: " + totalIncorrect);
						out.println(String.format(" (%.2f%%)", pTotalIncorrect));
						out.print(tab2 + "Given Up\t: " + totalGivenUp);
						out.println(String.format(" (%.2f%%)", pTotalGivenUp));
						out.println(tab2 + "Total\t\t: " + total);
						
						out.println();
						out.print(tab2 + "Question most often answered correctly\t\t: ");
						out.println(questionList.get(easyQ).getText());
						out.print(tab2 + "Question most often answered incorrectly\t: ");
						out.println(questionList.get(missunderstoodQ).getText());
						out.print(tab2 + "Question most often be given up\t\t\t: ");
						out.println(questionList.get(hardQ).getText());
					});
					out.println();
					out.println();
				});

	            out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		default:
			break;
		}
		
	}

	public void setQuizHomeController(QuizHomeController quizHomeController) {
		this.quizHomeController = quizHomeController;		
	}
}
