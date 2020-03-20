package magitUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import magitObjects.Commit;

public class CommitMergeController {
    private MagitController mainController;
    private String branchToMergeWith;
    private Stage myStage;

    @FXML private Button commitTheMergeBtn;
    @FXML private TextField commitMessageTxt;
    @FXML private Label mergeTitle;

    @FXML void commitTheMerge(ActionEvent event) {
        String commitMessage;
        if (commitMessageTxt.getText().isEmpty())
        {
            // use the prompt text, default for merge branches
            commitMessage = commitMessageTxt.getPromptText();
        }
        else
        {
            commitMessage = commitMessageTxt.getText();
        }
        try
        {
            Commit commitOfTheirs = mainController.getMagitEngine().getCurrentRepo().getCommitOfBranch(branchToMergeWith);
            String sha1OfTheirs = commitOfTheirs.getSha1();

            mainController.getMagitEngine().createCommit(commitMessage, sha1OfTheirs);
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION,
                    "Merge",
                    "Please Notice!",
                    "Merge head with branch " + branchToMergeWith+ " was done successfully.");
            mainController.clearTextFieldsAndSpinners();
            mainController.createCommitsGraphAndShowIt(null);
            myStage.close(); // close this window
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Merge", "Error",
                    "Commit the merge failed.\n" +
                            UserMessages.ERROR_CREATE_COMMIT + "\n" + e.getMessage());
            myStage.close();
        }
    }

    public void setBranchToMergeWith(String branchToMergeWith) {
        this.branchToMergeWith = branchToMergeWith;
        setCommitMessagePrompt(); // set the prompt
        setTitle();
    }

    public void setCommitMessagePrompt()
    {
        if (! branchToMergeWith.isEmpty())
        {
            commitMessageTxt.setPromptText("Merge head with branch " + branchToMergeWith);
        }
    }
    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }
    public void setMainController(MagitController mainController)
    {
        this.mainController = mainController;
    }
    public void setTitle()
    {
        mergeTitle.setText("Merging branch " + branchToMergeWith + " to head.");
    }
}
