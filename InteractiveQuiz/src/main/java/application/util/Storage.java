package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import application.model.*;

public class Storage {

	final private String root = "/data";
	private URI questionsUri;
	
	public Storage() {
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
	
	private void initAllData() throws URISyntaxException, IOException {
		File folder = new File(getClass().getResource(root).toURI());
		File file = new File(folder, "questions.dat");
		if (!file.exists()) {
			file.createNewFile();
			write(file, new ArrayList<Question>());
		}
		questionsUri = file.toURI();
		
	}

	public void write(File file, Object obj) {

		try {
			ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( file ) );
            out.writeObject(obj);
            out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Object read(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		Object sc;
        ObjectInputStream in = new ObjectInputStream( new FileInputStream( file ) );
        sc = (Object)in.readObject();
        in.close();
		return sc;
	}
	
	public void saveQuestions(ArrayList<Question> questions) {
		write(new File(questionsUri), questions);
	}
	
	public ArrayList<Question> loadQuestions() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArrayList<Question> questions = (ArrayList<Question>) read( new File(questionsUri));
		return questions;
	}
}
