package magitUI;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class Alerts {

    public static void showAlertMessage(Alert.AlertType alertType, String title, String header, String message){
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
