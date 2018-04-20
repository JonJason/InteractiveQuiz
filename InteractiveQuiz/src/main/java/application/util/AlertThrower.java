package application.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertThrower {
	public static Alert createAlert(String title, String header, String context, String type) {
		AlertType alertType;
		switch (type) {
		case ("info"):
			alertType = AlertType.INFORMATION;
			break;
		case ("warning"):
			alertType = AlertType.WARNING;
			break;
		case ("confirm"):
			alertType = AlertType.CONFIRMATION;
			break;
		default:
			alertType = AlertType.INFORMATION;
		}
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(context);

		return alert;
	}
	
	public static void showAlert(String title, String header, String context, String type) {
		Alert alert = createAlert(title, header, context, type);
		alert.showAndWait();
	}
	

	
	public static Alert createCloseConfrimationAlert(Stage stage) {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?"
        );
        Button exitButton = (Button) alert.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Exit");
        alert.setHeaderText("Confirm Exit");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        
        return alert;
	}
}
