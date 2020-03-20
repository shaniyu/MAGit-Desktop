package magitUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import magitEngine.MagitEngine;
import magitObjects.Repository;


public class loadRepoFromPathController implements Initializable{

    private Stage primaryStage;
    private MagitController mainController; // the controller of the main app - "magit" controller

    @FXML
    private Label errorLable;

    @FXML
    private TextField chosenDirPathForRepo;
    @FXML
    private Button browseBtn;
    @FXML
    public void initialize(){
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    @FXML
    private void openDirChooser(ActionEvent event) {

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
            errorLable.setText(""); // hide error, if there is a message inside.
        }


    }

    @FXML
    private void tryToLoadRepoFromDir(ActionEvent event)
    {
        String pathToLoadRepoFrom = chosenDirPathForRepo.getText();
        MagitEngine engine = mainController.getMagitEngine();

        if (pathToLoadRepoFrom.isEmpty())
        {
            errorLable.setText(UserMessages.EMPTY_PATH);
        }
        else
        {
               //  if this repository is loaded already, we will tell the user
            if (engine.getRepositoryLocation().equals(pathToLoadRepoFrom))
            {
                errorLable.setText(UserMessages.ALREADY_ON_THIS_REPO);
            }
            else if ( ! engine.isPathAlreadyARepo(pathToLoadRepoFrom))
                errorLable.setText(UserMessages.NOT_A_REPO);
            else
            {
                try {
                    // call empty c'tor of current repository to avoid null pointer exception
                    if(engine.getCurrentRepo() == null){
                        engine.setCurrentRepo(new Repository());
                    }
                    // set the repository
                    engine.getCurrentRepo().loadRepositoryFromPath(pathToLoadRepoFrom);
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Load repository from path", "Please Notice!", "Repository from " +pathToLoadRepoFrom +" was loaded successfully.");
                    // update the properties
                    mainController.updateRepoProperties();

                    mainController.createCommitsGraphAndShowIt(null);

                    mainController.clearTextFieldsAndSpinners();
                    primaryStage.close();
                }
                catch (Exception e){
                    errorLable.setText(e.getMessage());
                }
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
