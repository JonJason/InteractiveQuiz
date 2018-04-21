package application;

import java.util.Optional;

import application.controller.QuizHomeController;
import application.util.AlertThrower;
import application.util.Storage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Quiz Application");

        initRootController(primaryStage);
    }
    
    public void initRootController(Stage primaryStage) {
        primaryStage.setScene(new Scene(new QuizHomeController().getRoot()));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/style/MainScene.css").toExternalForm());

        primaryStage.setOnCloseRequest(event -> {

            Alert closeConfirmation = AlertThrower.createCloseConfrimationAlert(primaryStage);

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get())) {
                event.consume();
            }
            
        });
        
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
    	// setup storage
    	Storage.setup();
    	
        launch(args);
    }
}
