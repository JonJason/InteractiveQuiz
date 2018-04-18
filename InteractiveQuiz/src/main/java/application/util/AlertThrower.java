package application.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
}
