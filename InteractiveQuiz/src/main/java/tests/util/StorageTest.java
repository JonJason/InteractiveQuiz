package tests.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Question;
import application.util.Storage;

class StorageTest {
	
	ArrayList<Question> questions;
	int questionSize;
	
	@BeforeEach
	void setUp() throws FileNotFoundException, ClassNotFoundException, IOException {
		Storage.setup();
		ArrayList<Question> questions = Storage.loadQuestions();
		
		questionSize = questions.size();
		
		Question question;
		question = new Question();
		question.setText("what?");
		question.setPicture("flower.jpg");
		ArrayList<String> topics = new ArrayList<String>(); 
		topics.add("general");
		topics.add("uk");
		question.setTopics(topics);
		question.setAnswer(0, "a");
		question.setAnswer(1, "b");
		question.setAnswer(2, "c");
		question.setAnswer(3, "d");
		question.setCorrectAnswerIndex(3);
		
		questions.add(question);
		
		question = new Question();
		question.setText("what the?");
		question.setPicture("boys.jpg");
		topics = new ArrayList<String>(); 
		topics.add("not general");
		topics.add("united kingdom");
		question.setTopics(topics);
		question.setAnswer(0, "e");
		question.setAnswer(1, "f");
		question.setAnswer(2, "g");
		question.setAnswer(3, "h");
		question.setCorrectAnswerIndex(1);
		
		questions.add(question);
		Storage.saveQuestions(questions);
		
	}
	
	@AfterEach
	void cleanUp() throws FileNotFoundException, ClassNotFoundException, IOException {
		ArrayList<Question> questions = Storage.loadQuestions();
		questions.remove(questions.size() - 1);
		questions.remove(questions.size() - 1);
		Storage.saveQuestions(questions);
	}

	@Test
	void saveAndLoadQuestion() throws FileNotFoundException, ClassNotFoundException, IOException {

		ArrayList<Question> loadedQuestions = Storage.loadQuestions();
		assertEquals(loadedQuestions.get(0).getText(), loadedQuestions.get(0).getText());
		assertEquals(loadedQuestions.get(1).getText(), loadedQuestions.get(1).getText());
		
	}

}
