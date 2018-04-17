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
	Storage storage = new Storage();
	int questionSize;
	
	@BeforeEach
	void setUp() throws FileNotFoundException, ClassNotFoundException, IOException {
		ArrayList<Question> questions = storage.loadQuestions();
		
		questionSize = questions.size();
		
		Question question;
		question = new Question();
		question.setQuestionText("what?");
		question.setPicture("flower.jpg");
		question.addTopic("general");
		question.addTopic("uk");
		question.addAnswer("a");
		question.addAnswer("b");
		question.addAnswer("c");
		question.addAnswer("d");
		question.setCorrectAnswerIndex(3);
		
		questions.add(question);
		
		question = new Question();
		question.setQuestionText("what the?");
		question.setPicture("boys.jpg");
		question.addTopic("not general");
		question.addTopic("united kingdom");
		question.addAnswer("e");
		question.addAnswer("f");
		question.addAnswer("g");
		question.addAnswer("h");
		question.setCorrectAnswerIndex(1);
		
		questions.add(question);
		storage.saveQuestions(questions);
		
	}
	
	@AfterEach
	void cleanUp() throws FileNotFoundException, ClassNotFoundException, IOException {
		ArrayList<Question> questions = storage.loadQuestions();
		questions.remove(questions.size() - 1);
		questions.remove(questions.size() - 1);
		storage.saveQuestions(questions);
	}

	@Test
	void saveAndLoadQuestion() throws FileNotFoundException, ClassNotFoundException, IOException {

		ArrayList<Question> loadedQuestions = storage.loadQuestions();
		assertEquals(loadedQuestions.get(0).getQuestionText(), loadedQuestions.get(0).getQuestionText());
		assertEquals(loadedQuestions.get(1).getQuestionText(), loadedQuestions.get(1).getQuestionText());
		
	}

}
