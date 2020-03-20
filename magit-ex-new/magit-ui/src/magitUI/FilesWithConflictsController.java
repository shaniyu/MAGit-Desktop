package magitUI;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import magitObjects.Commit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FilesWithConflictsController implements Initializable {
    private int numberOfFilesToSolve;
    private SimpleBooleanProperty isMoreFiles;
    private ArrayList<String> filesPath;
    private MagitController mainController;
    private String branchToMergeWith;
    private Stage myStage;

    @FXML private ScrollPane filesWithConflictsScroll;
    @FXML private TextField commitMessageTxt;
    @FXML private Button createCommitAfterConflicts;

    @FXML private void createCommitAfterConflicts()
    {
        // When all the filesPath that had conflicts are solved and saced to WC we can commit
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
    public void setFilesWithConflictsScroll()
    {
        VBox box = new VBox();
        for (String file : filesPath)
        {
            Label fileLbl = new Label(file);
            fileLbl.setOnMouseClicked((e)->{
                dealWithFileConflicts(file);});
            box.getChildren().add(fileLbl);
        }
        filesWithConflictsScroll.setContent(box);
    }

    public void dealWithFileConflicts(String filePath)
    {
        // open the solverConflictForFileScene window
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/solverConflictForFileScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            SolveConflicToFileController solveConflictsController = fxmlLoader.getController();
            Stage solveConflictsOfFileStage = new Stage();
            solveConflictsOfFileStage.setTitle("Solve conflicts");
            solveConflictsOfFileStage.setScene(new Scene(root));
            solveConflictsOfFileStage.initModality(Modality.APPLICATION_MODAL);
            solveConflictsController.setMyStage(solveConflictsOfFileStage);
            solveConflictsController.setMainController(mainController);
            solveConflictsController.setFilesController(this);
            solveConflictsController.setFileToSolvePath(filePath);
            setTheScrollsOfThreeFilesAndDeleteButton(filePath, solveConflictsController);
            mainController.setSceneDesign(solveConflictsOfFileStage.getScene(), true);
            solveConflictsOfFileStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Merge", "Error", e.getMessage());
        }
    }
    private void setTheScrollsOfThreeFilesAndDeleteButton(String filePath, SolveConflicToFileController solveConflictsController) throws IOException
    {
        Boolean shouldDisplayDeleteButton = false;
        String repoLocation =  mainController.getMagitEngine().getRepositoryLocation();
        String pathInHead = repoLocation+ File.separator +filePath;
        String pathInTheirs = repoLocation + File.separator
                + ".magit" + File.separator + "theirs" + File.separator + filePath;
        String pathInAncenstor = repoLocation + File.separator
                + ".magit" + File.separator + "ancenstor" + File.separator + filePath;

        String ancenstorContent;
        String headContent;
        String theirsContent;

        // get all the files ( head, ancenstor, theirs) content and use it
        if ( !new File(pathInAncenstor).exists())
        {
            ancenstorContent = "";
        }
        else
        {
            ancenstorContent = mainController.getMagitEngine().getFileContent(pathInAncenstor);
        }
        solveConflictsController.setFileInAncenstorTxt(ancenstorContent);

        if ( !new File(pathInHead).exists())
        {
            headContent = "";
            shouldDisplayDeleteButton = true;
        }
        else
        {
            headContent = mainController.getMagitEngine().getFileContent(pathInHead);
        }
        solveConflictsController.setFileInHeadTxt(headContent);

        if (! new File(pathInTheirs).exists())
        {
            theirsContent = "";
            shouldDisplayDeleteButton = true;
        }
        else
        {
            theirsContent = mainController.getMagitEngine().getFileContent(pathInTheirs);
        }
        solveConflictsController.setFileInTheirsTxtAndDeleteButton(theirsContent, shouldDisplayDeleteButton);
        // empty text area for editing
        solveConflictsController.initializeTextAreaForSolveConflict();
    }

    public void setNumberOfFilesToSolve(int numberOfFilesToSolve) {
        this.numberOfFilesToSolve = numberOfFilesToSolve;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // enable the commit button when there are no filesPath to solve conflicts
        createCommitAfterConflicts.disableProperty().bind(isMoreFiles);
    }

    public FilesWithConflictsController()
    {
        isMoreFiles = new SimpleBooleanProperty(true);
    }

    public void updateFilesWithConflictsNumber()
    {
        numberOfFilesToSolve -- ;
        if (numberOfFilesToSolve == 0)
        {
            isMoreFiles.set(false);
        }
    }

    // the SolveConflicToFileController call it
    public void removeFileFromList(String filePath)
    {
        // remove file from the list
        filesPath.remove(filePath);
        setFilesWithConflictsScroll();
        updateFilesWithConflictsNumber();
    }
    public void setFilesPath(ArrayList<String> filesPath) {
        this.filesPath = filesPath;
        setNumberOfFilesToSolve(filesPath.size());
        setFilesWithConflictsScroll();
    }

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }
    public void setCommitMessagePrompt() {
        if (! branchToMergeWith.isEmpty())
        {
            commitMessageTxt.setPromptText("Merge head with branch " + branchToMergeWith);
        }
    }

    public void setBranchToMergeWith(String branchToMergeWith) {
        this.branchToMergeWith = branchToMergeWith;
        setCommitMessagePrompt();
    }
}
