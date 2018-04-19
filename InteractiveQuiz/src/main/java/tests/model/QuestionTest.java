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
		question.addTopic("bro");
		question.addTopic("sis");
		ArrayList<String> topics = question.getTopics();
		assertFalse(topics.isEmpty());
		assertTrue(topics.contains("bro"));
		assertTrue(topics.contains("bro"));
		assertEquals(topics.get(1), "sis");
		assertEquals(topics.size(), 2);
	}

	@Test
	void addAnswers() {
		question.addAnswer("bro");
		question.addAnswer("sis");
		ArrayList<String> answers = question.getAnswers();
		assertFalse(answers.isEmpty());
		assertTrue(answers.contains("bro"));
		assertTrue(answers.contains("bro"));
		assertEquals(answers.get(1), "sis");
		assertEquals(answers.size(), 2);
	}

	@Test
	void setCorrectAnswer() {
		question.addAnswer("bro");
		question.addAnswer("sis");
		question.addAnswer("yow");
		question.addAnswer("dude");
		question.setCorrectAnswerIndex(3);
		assertEquals(question.getCorrectAnswerIndex(), 3);
		assertEquals(question.getAnswer(question.getCorrectAnswerIndex()), "dude");
		assertNotEquals(question.getAnswer(question.getCorrectAnswerIndex()), "yow");
	}

}
