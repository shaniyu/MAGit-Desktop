package magitUI;

import Exceptions.BranchAlreadyInUseException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import magitObjects.Commit;

import java.io.File;

public class CreateLocalBranchForCommitController {

    private MagitController magitController;
    private Commit commit;
    private Stage stage;

    @FXML
    private TextField localBranchForCommitNameTxt;


    @FXML
    private Button createLocalBranchForCommitBtn;

    @FXML
    void tryToCreateLocalBranchForCommit(ActionEvent event) throws Exception{
        String newBranchName = localBranchForCommitNameTxt.getText();
        if (newBranchName.isEmpty())
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.EMPTY_NAME_);
            localBranchForCommitNameTxt.setText("");
        }
        else if (newBranchName.equals("HEAD") || newBranchName.equals("commits"))
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.NOT_VALID_BRANCH_NAME);
            localBranchForCommitNameTxt.setText("");
        }
        else if (newBranchName.contains(File.separator) || newBranchName.contains("/"))
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.ILEGAL_CHARS_IN_NAME);
            localBranchForCommitNameTxt.setText("");
        }
        else{
            //If the branch already exists we'll catch it
            createALocalBranchOnSpecificCommit(localBranchForCommitNameTxt.getText());
        }
    }

    private void createALocalBranchOnSpecificCommit(String newBranchName) throws Exception
    {
        try{
            magitController.getMagitEngine().addNewBranchOnSpecificCommit(newBranchName, commit);
            magitController.clearTextFieldsAndSpinners();
            magitController.updateBranchesList();

            // offer to checkout only if the commit isn't null and no open changes
            magitController.offerToCheckoutToNewBranchOrNotiFySuccess(newBranchName);
            // update visual graph
            magitController.createCommitsGraphAndShowIt(null);
            stage.close();
        }
        catch (BranchAlreadyInUseException e2){
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error", e2.getMessage());
        }
    }

    public void setMainController(MagitController magitController) {
        this.magitController = magitController;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

}
