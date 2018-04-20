package application.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class BaseController {
	
	private Parent root;
	
	public BaseController(String name) {
		loadFXML(name);
	}

	/**
	 * load fxml view and init controller.
	 * 
	 * @param name - fxml-file's name.
	 */
	private void loadFXML(String name) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + name + ".fxml"));
        fxmlLoader.setController(this);
        try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Parent getRoot() {
		return root;
	}
}
