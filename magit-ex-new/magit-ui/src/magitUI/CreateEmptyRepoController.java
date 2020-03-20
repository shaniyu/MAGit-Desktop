package magitUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import magitEngine.MagitEngine;

import java.io.File;
import java.io.IOException;

public class CreateEmptyRepoController {

    private Stage primaryStage;

    private String pathToCreateEmptyRepo;

    private MagitController mainController;

    @FXML
    private Button openDirChooser;

    @FXML
    private Label errorLable;

    @FXML
    private TextField chosenDirPathForRepo;

    @FXML
    private Button createEmptyRepoBtn;

    @FXML
    private TextField folderNameTxtFld;

    @FXML
    private TextField repositoryName;

    @FXML
    void openDirChooser(ActionEvent event) {

        DirectoryChooser dirChooser = new DirectoryChooser();
        File selectedFolder = dirChooser.showDialog(primaryStage);

        if (selectedFolder == null) {
            return;
        }

        String absolutePath = selectedFolder.getAbsolutePath();
        chosenDirPathForRepo.setText(absolutePath);
    }

    @FXML
    void tryToCreateEmptyRepo(ActionEvent event) {

        if(chosenDirPathForRepo.getText().isEmpty()){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.EMPTY_PATH);
        }
        else if(folderNameTxtFld.getText().isEmpty()){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.EMPTY_FOLDER_NAME);
        }
        else if(folderNameTxtFld.getText().contains("\\") || folderNameTxtFld.getText().contains("/")){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.INVALID_FOLDER_NAME);
        }
        else if (MagitEngine.isPathAlreadyARepo(chosenDirPathForRepo.getText() + File.separator + folderNameTxtFld.getText())) {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.ALREADY_A_REPO);
        }
        else if (repositoryName.getText().isEmpty()){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.EMPTY_REPO_NAME);
        }
        else if (repositoryName.getText().contains(File.separator) || repositoryName.getText().contains("/"))
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", UserMessages.ILEGAL_CHARS_IN_NAME);
        }
        else{
            try
            {
                pathToCreateEmptyRepo = chosenDirPathForRepo.getText() + File.separator + folderNameTxtFld.getText();
                mainController.getMagitEngine().createEmptyRepository(pathToCreateEmptyRepo, repositoryName.getText());
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Create empty repository", "Please notice!", UserMessages.REPO_CREATED);
                mainController.updateRepoProperties();
                mainController.createCommitsGraphAndShowIt(null);
                mainController.clearTextFieldsAndSpinners();
                primaryStage.close();
            }
            catch (IOException e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", e.getMessage());
            }
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
}
