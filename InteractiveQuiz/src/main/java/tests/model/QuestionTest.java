package tests.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.model.Question;

class QuestionTest {
	
	Question question;
	
	@BeforeEach
	void setUp() {
		question = new Question();
	}

	@Test
	void setQuestionText() {
		question.setText("test oy test!");
		assertEquals(question.getText(), "test oy test!");
	}

	@Test
	void setPicture() {
		question.setPicture("test oy test!");
		assertEquals(question.getPicture(), "test oy test!");
	}

	@Test
	void addTopics() {
		ArrayList<String> topics = new ArrayList<String>(); 
		topics.add("bro");
		topics.add("sis");
		question.setTopics(topics);
		ArrayList<String> currentTopics = question.getTopics();
		assertFalse(currentTopics.isEmpty());
		assertTrue(currentTopics.contains("bro"));
		assertTrue(currentTopics.contains("bro"));
		assertEquals(currentTopics.get(1), "sis");
		assertEquals(currentTopics.size(), 2);
	}

	@Test
	void addAnswers() {
		question.setAnswer(0, "bro");
		question.setAnswer(1, "sis");
		question.setAnswer(2, "yow");
		question.setAnswer(3, "dude");
		ArrayList<String> answers = question.getAnswers();
		assertFalse(answers.isEmpty());
		assertTrue(answers.contains("bro"));
		assertTrue(answers.contains("bro"));
		assertEquals(answers.get(1), "sis");
		assertEquals(answers.size(), 4);
	}

	@Test
	void setCorrectAnswer() {
		question.setAnswer(0, "bro");
		question.setAnswer(1, "sis");
		question.setAnswer(2, "yow");
		question.setAnswer(3, "dude");
		question.setCorrectAnswerIndex(3);
		assertEquals(question.getCorrectAnswerIndex(), 3);
		assertEquals(question.getAnswer(question.getCorrectAnswerIndex()), "dude");
		assertNotEquals(question.getAnswer(question.getCorrectAnswerIndex()), "yow");
	}

}
