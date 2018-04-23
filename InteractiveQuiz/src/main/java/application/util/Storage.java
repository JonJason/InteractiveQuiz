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
import java.util.HashMap;

import application.model.*;
import javafx.scene.image.Image;

public class Storage {

	private static String quizFilename = "quiz.dat";
	private static String questionsFilename = "questions.dat";
	private static String topicsFilename = "topics.dat";
	private static String schoolsFilename = "schools.dat";
	private static String statisticsFilename = "statistics.dat";
	
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
		
		File statisticsFile = new File(statisticsFilename);
		if (!statisticsFile.exists()) {
			statisticsFile.createNewFile();
			write(statisticsFilename, new HashMap<String, HashMap<String, Statistic>>());
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
	
	public static void saveStatistic(Statistic s) {
		try {
			HashMap<String, HashMap<String, Statistic>> statistics = loadStatistics();
			if (statistics.get(s.getQuizName()) == null) {
				HashMap<String, Statistic> quizStats = new HashMap<String, Statistic>();
				quizStats.put(s.getSchool(), s);
				statistics.put(s.getQuizName(), quizStats);
			}
			
			statistics.get(s.getQuizName()).put(s.getSchool(), s);
			
			write(statisticsFilename, statistics);
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Statistic loadStatistic(String quizName, String school) throws FileNotFoundException, ClassNotFoundException, IOException {
		HashMap<String, HashMap<String, Statistic>> statistics = loadStatistics();
		if (statistics.get(quizName) == null || statistics.get(quizName).get(school) == null) {
			return null;
		}
		return statistics.get(quizName).get(school);
	}
	
	public static HashMap<String, HashMap<String, Statistic>> loadStatistics() throws FileNotFoundException, IOException, ClassNotFoundException {
		return (HashMap<String, HashMap<String, Statistic>>) read(statisticsFilename);
	}
	
	public static String storeAndGetImage(File source) throws IOException, URISyntaxException {
		File folder = new File(Storage.class.getResource("/img").toURI());
		
		String filename = source.getName();
		Path dest = Paths.get(folder.toPath().toString(), filename);
		String extension = filename.substring(filename.lastIndexOf("."), filename.length());
		String name = filename.substring(0, filename.lastIndexOf("."));
		
		int i = 0;
		String finalName = name;
		while (new File(dest.toString()).exists()) {
			finalName = name + "(" + Integer.toString(i) + ")";
			dest = Paths.get(folder.toPath().toString(), finalName + extension);
			i++;
		}
		Files.copy(source.toPath(), dest);
		return Storage.class.getResource("/img/" + finalName + extension).toString();
	}

	public static void clearStatistics() {
		File statisticsFile = new File(statisticsFilename);
		if (statisticsFile.exists()) {
			write(statisticsFilename, new HashMap<String, HashMap<String, Statistic>>());
		}
	}
}
