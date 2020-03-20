package magitUI;

import Exceptions.IlegalHeadFileException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import magitEngine.MagitEngine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResetHeadController implements Initializable {

    private MagitController mainController; // the controller of the main app - "magit" controller
    private Stage primaryStage;
    @FXML
    private ChoiceBox<String> sha1ForResetSpinner;
    @FXML
    void perfornShowCommitsOnSpinner(MouseEvent event) {
    }

    @FXML
    void resetHeadToCommit(ActionEvent event) {
        MagitEngine engine = mainController.getMagitEngine();

        String chosenSha1 = sha1ForResetSpinner.getValue();
        if (chosenSha1== null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head branch", "Please Notice!", UserMessages.CHOOSE_SHA1);
        }
        else
        {
            try
            {
                if (!engine.isThereOpenChangesInRepository()) {
                    engine.resetBranchSha1(chosenSha1);
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head branch", "Please Notice!", "Reset passed successfully, you have checked out to the commit "+chosenSha1);
                    mainController.updateBranchesList();
                    mainController.createCommitsGraphAndShowIt(null);
                    primaryStage.close();
                }
                else {
                     if (handleOpenedChangesOnReset(chosenSha1) == true )
                     {
                         // close the reset head window if the reset was done
                         mainController.updateBranchesList();
                         primaryStage.close();
                     }
                }
            }
            catch (IlegalHeadFileException e1)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Error", "Reset head", e1.getMessage());
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Error", "Reset head", e.getMessage());
            }

        }
    }

    //returns true if reset was performed
    public boolean handleOpenedChangesOnReset(String sha1FromUser)
    {
        // create a costume dialog with yes and no button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset head");
        alert.setHeaderText(UserMessages.OPEN_CHANGES + "\n" + UserMessages.DEAL_WITH_OPEN_CHANGES_RESET_HEAD);
        alert.setContentText(UserMessages.RESET_OPEN_CHANGES_WARNING);

        //creating the buttons
        ButtonType buttonTypeCancelReset = new ButtonType("Cancel reset");
        ButtonType buttonTypeDeleteChanges = new ButtonType("Reset anyway");

        // put buttons on dialog window
        alert.getButtonTypes().setAll(buttonTypeCancelReset, buttonTypeDeleteChanges);

        // show dialog and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeCancelReset){
            // don't reset
            return false;
        } else if (result.get() == buttonTypeDeleteChanges) {
            return resetAnywayAndDeleteChanges(sha1FromUser);
        } else {
            // user chose CANCEL or closed the dialog
            return false;
        }
    }

    //returns true if reset was performed
    public boolean resetAnywayAndDeleteChanges(String sha1ToReset)
    {
        try
        {
            mainController.getMagitEngine().resetBranchSha1(sha1ToReset);
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head branch", "Please Notice!","Reset passed successfully, you have checked out to the commit " + sha1ToReset);
            return true;
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Error", "Error in reset head to" + sha1ToReset, e.getMessage());
            return false;
        }
    }

    public void setSha1ToChoiceBox(ArrayList<String> sha1List)
    {
        sha1ForResetSpinner.getItems().addAll(sha1List);
    }

    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setMainController(MagitController controller)
    {
        this.mainController = controller;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
