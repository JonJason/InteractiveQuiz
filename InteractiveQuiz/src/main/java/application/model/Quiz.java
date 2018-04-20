package application.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Quiz implements Serializable  {
	
	private Date date;
	private String name;
	final private ArrayList<Question> questions;
	final private ArrayList<String> schools;
	
	public Quiz() {
		questions = new ArrayList<Question>();
		schools = new ArrayList<String>();
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Question> getQuestions() {
		return questions;
	}
	
	public void setQuestions(ArrayList<Question> questions) {
		this.questions.clear();
		this.questions.addAll(questions);
	}
	
	public ArrayList<String> getSchools() {
		return schools;
	}

	public void setSchools(ArrayList<String> schools) {
		this.schools.clear();
		this.schools.addAll(schools);
	}
}
