package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.model.*;

public class Storage {

	private static String questionsFilename = "questions.dat";
	
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
		File file = new File(questionsFilename);
		if (!file.exists()) {
			file.createNewFile();
			write(questionsFilename, new ArrayList<Question>());
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
	
	public static void saveQuestions(ArrayList<Question> questions) {
		write(questionsFilename, questions);
	}
	
	public static ArrayList<Question> loadQuestions() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Question> questions = (ArrayList<Question>) read(questionsFilename);
		return questions;
	}
}
