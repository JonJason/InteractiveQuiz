package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import application.model.*;
import javafx.scene.image.Image;

public class Storage {

	private static String quizFilename = "quiz.dat";
	private static String questionsFilename = "questions.dat";
	private static String topicsFilename = "topics.dat";
	private static String schoolsFilename = "schools.dat";
	
	public static void setup() {
		try {
			initAllData();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void initAllData() throws URISyntaxException, IOException {
		File quizFile = new File(quizFilename);
		if (!quizFile.exists()) {
			quizFile.createNewFile();
			write(quizFilename, new Quiz());
		}
		
		File questionsFile = new File(questionsFilename);
		if (!questionsFile.exists()) {
			questionsFile.createNewFile();
			write(questionsFilename, new ArrayList<Question>());
		}
		
		File topicsFile = new File(topicsFilename);
		if (!topicsFile.exists()) {
			topicsFile.createNewFile();
			write(topicsFilename, new ArrayList<String>());
		}
		
		File schoolsFile = new File(schoolsFilename);
		if (!schoolsFile.exists()) {
			schoolsFile.createNewFile();
			write(schoolsFilename, new ArrayList<String>());
		}
		
	}

	public static void write(String name, Object obj) {

		try {
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( name ) );
            out.writeObject(obj);
            out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object read(String name) throws FileNotFoundException, IOException, ClassNotFoundException {
		Object sc;
        ObjectInputStream in = new ObjectInputStream( new FileInputStream( name ) );
        sc = (Object)in.readObject();
        in.close();
		return sc;
	}
	
	public static void saveQuiz(Quiz quiz) {
		write(quizFilename, quiz);
	}
	
	public static Quiz loadQuiz() throws FileNotFoundException, ClassNotFoundException, IOException {
        Quiz quiz = (Quiz) read(quizFilename);
		return quiz;
	}
	
	public static void saveQuestions(ArrayList<Question> questions) {
		write(questionsFilename, questions);
	}
	
	public static ArrayList<Question> loadQuestions() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Question> questions = (ArrayList<Question>) read(questionsFilename);
		return questions;
	}
	
	public static void saveTopics(ArrayList<String> topics) {
		write(topicsFilename, topics);
	}
	
	public static ArrayList<String> loadTopics() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<String> topics = (ArrayList<String>) read(topicsFilename);
		return topics;
	}
	
	public static void saveSchools(ArrayList<String> schools) {
		write(schoolsFilename, schools);
	}
	
	public static ArrayList<String> loadSchools() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<String> schools = (ArrayList<String>) read(schoolsFilename);
		return schools;
	}
	
	public static String storeAndGetImage(File source) throws IOException, URISyntaxException {
		File folder = new File(Storage.class.getResource("/img").toURI());
		
		String filename = source.getName();
		Path dest = Paths.get(folder.toPath().toString(), filename);
		String extension = filename.substring(filename.lastIndexOf("."), filename.length());
		String name = filename.substring(0, filename.lastIndexOf("."));
		
		int i = 0;
		while (new File(dest.toString()).exists()) {
			dest = Paths.get(folder.toPath().toString(), name + "(" + Integer.toString(i) + ")" + extension);
			i++;
		}
		Files.copy(source.toPath(), dest);
		return dest.toString().substring(
				dest.toString().lastIndexOf("\\img\\"), 
				dest.toString().length());
	}
}
