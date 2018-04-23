package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Statistic implements Serializable {
	
	final private String quizName;
	final private String school;
	final private ArrayList<StatisticData> data;
	private ArrayList<Question> questions;
	private String dateString;

	public Statistic(String quizName, String school, int dataSize) {
		this.quizName = quizName;
		this.school = school;

		data = new ArrayList<StatisticData>();
		
		for(int i = 0;i < dataSize;i++) {
			data.add(new StatisticData());
		}
	}
	
	public String getQuizName() {
		return quizName;
	}
	
	public String getSchool() {
		return school;
	}
	
	public void incCorrect(int i) {
		data.get(i).incCorrect();
	}
	
	public void incIncorrect(int i) {
		data.get(i).incIncorrect();
	}
	
	public void incGivenupTime(int i) {
		data.get(i).incGivenupTime();
	}
	
	public ArrayList<StatisticData> getData() {
		return data;
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public void cloneQuestions(ArrayList<Question> questions) {
		this.questions = (ArrayList<Question>) questions.clone();
	}
	
	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	public String toString() {
		return getClass().getSimpleName() + " [" +
		"quizName=" + quizName + ", " +
		"school=" + school + ", " +
		"data=" + data + "]";
	}
	
}
