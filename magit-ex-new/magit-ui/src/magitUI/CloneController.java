package magitUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class CloneController {
    @FXML private Button openDirChooser;
    @FXML private Label errorLable;
    @FXML private TextField chosenRepoToClone;
    @FXML private Button loadRepoDirBtn;
    @FXML private TextField chosenDirPathForRepo;
    @FXML private Button cloneBtn;
    @FXML private Label errorLableOfRepo;
    @FXML private Label errorLableOfNewRepo;
    @FXML private TextField newRepoName;

    private String newRepoNameString;
    private Stage primaryStage;
    private MagitController mainController; // the controller of the main app - "magit" controller

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML void chooseRepoToClone(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedFolder = dirChooser.showDialog(primaryStage);

        if (selectedFolder == null)
        {
            return;
        }
        else
        {
            String absolutePath = selectedFolder.getAbsolutePath();
            chosenRepoToClone.setText(absolutePath);
            errorLableOfRepo.setText(""); // hide error, if there is a message inside.
        }

    }

    @FXML void chooseWhereToClone(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedFolder = dirChooser.showDialog(primaryStage);

        if (selectedFolder == null)
        {
            return;
        }
        else
        {
            String absolutePath = selectedFolder.getAbsolutePath();
            chosenDirPathForRepo.setText(absolutePath);
            errorLableOfNewRepo.setText(""); // hide error, if there is a message inside.
        }
    }

    @FXML void tryToClone(ActionEvent event) {
        String pathOfRepoToCloneFrom = chosenRepoToClone.getText();
        String pathOfNewRepo = chosenDirPathForRepo.getText();
        boolean canClone = true;

        if (pathOfRepoToCloneFrom.isEmpty())
        {
            errorLableOfRepo.setText(UserMessages.EMPTY_PATH);
            canClone = false;
        }
        else if (!mainController.getMagitEngine().isPathAlreadyARepo(pathOfRepoToCloneFrom))
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Clone repository", "Please Notice!", pathOfRepoToCloneFrom + " is not a magit repository.");
            canClone = false;
        }
        if (pathOfNewRepo.isEmpty())
        {
            errorLableOfNewRepo.setText(UserMessages.EMPTY_PATH);
            canClone = false;
        }
        int lastFileSeperator = pathOfRepoToCloneFrom.lastIndexOf(File.separator);
        String newDir = pathOfRepoToCloneFrom.substring(lastFileSeperator +1);
        if (new File(pathOfNewRepo + File.separator + newDir).exists())
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Clone repository", "Please Notice!", pathOfNewRepo + File.separator + newDir + " already exists.\nCan't clone to there.");
            canClone = false;
        }
        if (newRepoName.getText().isEmpty())
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Clone repository", "Error", UserMessages.EMPTY_NAME_);
            canClone = false;
        }
        else if (newRepoName.getText().contains(File.separator) || newRepoName.getText().contains("/"))
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Clone repository", "Error", UserMessages.ILEGAL_CHARS_IN_NAME);
            canClone = false;
        }
        else
        {
            newRepoNameString = newRepoName.getText();
        }

        if (canClone)
        {
            try {
                mainController.getMagitEngine().clone(pathOfRepoToCloneFrom, pathOfNewRepo, newDir, newRepoNameString);
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Clone repository", "Please Notice!","Repository cloned and loaded!\nRepository from " +pathOfRepoToCloneFrom +" was cloned and loaded successfully.");
                // update the properties
                mainController.updateRepoProperties();
                mainController.createCommitsGraphAndShowIt(null);
                mainController.clearTextFieldsAndSpinners();
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Clone repository", "Error", UserMessages.CANT_CLONE + "\n" + e.getMessage());
            }
            finally {
                primaryStage.close();
            }
        }
    }
}
