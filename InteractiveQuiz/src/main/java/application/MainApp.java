package application;

import application.controller.QuizHomeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Quiz Application");

        initRootController(primaryStage);
    }
    
    public void initRootController(Stage primaryStage) {
        primaryStage.setScene(new Scene(new QuizHomeController().getRoot()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
