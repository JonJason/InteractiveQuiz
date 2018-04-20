package application.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Statistic implements Serializable {
	
	final private String quizName;
	final private String school;
	final private ArrayList<StatisticData> data;
	
	public Statistic(String quizName, String school) {
		this.quizName = quizName;
		this.school = school;
		
		data = new ArrayList<StatisticData>(10);
		
		for(int i = 0;i < 10;i++) {
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
	
	public String toString() {
		return getClass().getSimpleName() + " [" +
		"quizName=" + quizName + ", " +
		"school=" + school + ", " +
		"data=" + data + "]";
	}
	
}
