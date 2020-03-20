package magitUI;

import DataStructures.Tree;
import DataStructures.TreeNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import magitObjects.Commit;
import magitObjects.GitObject;

import java.io.File;

public class CommitDetailsController {

    private Commit commit;
    private MagitController mainController;
    @FXML private TabPane tabPane;
    @FXML private TextField Sha1Txt;
    @FXML private TextField commitMessageTxt;
    @FXML private TextField createdByTxt;
    @FXML private TextField creationDateTxt;
    @FXML private TextField firstPrecedingCommitTxt;
    @FXML private TextField secondtPrecedingCommitTxt;
    @FXML private ScrollPane newFilesScrollTab1;
    @FXML private ScrollPane changedFilesScrollTab1;
    @FXML private ScrollPane deletedFilesScrollTab1;
    @FXML private ScrollPane newFilesScrollTab2;
    @FXML private ScrollPane changedFilesScrollTab2;
    @FXML private ScrollPane deletedFilesScrollTab2;
    @FXML private Tab secondPrecedingCommitTab;
    @FXML private Tab firstPrecedingCommitTab;
    @FXML private Button showCommitFilesBtn;

    @FXML
    void showCommitFiles(ActionEvent event) {

        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/showAllHeadFilesScene.fxml"));
            Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

            //create new stage and set it to the controller
            Stage showCommitFilesStage = new Stage();
            showCommitFilesStage.setTitle("Show commit files");
            showCommitFilesStage.setScene(new Scene(root1));
            showCommitFilesStage.initModality(Modality.APPLICATION_MODAL);
            showCommitFilesStage.setResizable(false);

            ShowAllHeadFilesController controller = fxmlLoader.getController();
            Tree gitObjectTreeToPrint = mainController.getMagitEngine().getCurrentRepo().initializeGitObjectTreeForCommit(commit.getSha1());
            VBox box = new VBox();

            mainController.printTree(gitObjectTreeToPrint.getRoot(), box);
            controller.setFilesToScroll(box);
            mainController.setSceneDesign(showCommitFilesStage.getScene(), true);
            showCommitFilesStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show commit files", "Error", e.getMessage());
        }
    }


    public void setCommit(Commit commit) {
        this.commit = commit;
    }



    public void initializeData(){
        setSha1Txt();
        setCommitMessageTxt();
        setCreatedByTxt();
        setCreationDateTxt();
        setFirstPrecedingCommitTxt();
        setSecondtPrecedingCommitTxt();
        initializeOpenChangesScrollPane(firstPrecedingCommitTab);
        initializeOpenChangesScrollPane(secondPrecedingCommitTab);
    }

    private void setSecondtPrecedingCommitTxt() {
        secondtPrecedingCommitTxt.setText(commit.getSecondParentSha1().equals("null") ? "None" : commit.getSecondParentSha1());
        secondtPrecedingCommitTxt.setTooltip(new Tooltip(secondtPrecedingCommitTxt.getText()));
    }

    private void setFirstPrecedingCommitTxt() {
        firstPrecedingCommitTxt.setText(commit.getFirstParentSha1().equals("null") ? "None" : commit.getFirstParentSha1());
        firstPrecedingCommitTxt.setTooltip(new Tooltip(firstPrecedingCommitTxt.getText()));
    }

    private void setCreationDateTxt() {
        creationDateTxt.setText(commit.getmCreatedDate());
        creationDateTxt.setTooltip(new Tooltip(commit.getmCreatedDate()));
    }

    private void setCreatedByTxt() {
        createdByTxt.setText(commit.getmCreatedBy());
        createdByTxt.setTooltip(new Tooltip(commit.getmCreatedBy()));
    }

    private void setCommitMessageTxt() {
        commitMessageTxt.setText(commit.getmMessage());
        commitMessageTxt.setTooltip(new Tooltip(commit.getmMessage()));
    }

    private void setSha1Txt() {
        Sha1Txt.setText(commit.getSha1());
        Sha1Txt.setTooltip(new Tooltip(commit.getSha1()));
    }

    private void initializeOpenChangesScrollPane(Tab precedingCommitTab){
        showDeletedFiles(precedingCommitTab);
        showNewFiles(precedingCommitTab);
        showChangedFiles(precedingCommitTab);
    }
    private void showDeletedFiles(Tab precedingCommitTab) {
        String repoPath = mainController.getMagitEngine().getRepositoryLocation() + File.separator;
        if(precedingCommitTab.getText().equals("First Preceding Commit")
                && commit.getCommitChangesToFirstPrecedingCommit() != null){
            VBox vBox = new VBox();
            // getCommitChangesToFirstPrecedingCommit return relative path, complete it to the full path in the repo
            for(String filePath : commit.getCommitChangesToFirstPrecedingCommit().getDeletedFiles()){
                //String fileFullPath = repoPath + filePath.substring(mainController.getMagitEngine().getCurrentRepo().getRepoFolderName().length()+1);
                String fileFullPath = repoPath + filePath;
                Label deletedFilePath = new Label(fileFullPath);
                vBox.getChildren().add(deletedFilePath);
            }
            deletedFilesScrollTab1.setContent(vBox);
        }
        else if(precedingCommitTab.getText().equals("Second Preceding Commit")
                && commit.getCommitChangesToSecondPrecedingCommit() != null){
            VBox vBox = new VBox();
            for(String filePath : commit.getCommitChangesToSecondPrecedingCommit().getDeletedFiles()){
                //String fileFullPath = repoPath + filePath.substring(mainController.getMagitEngine().getCurrentRepo().getRepoFolderName().length()+1);
                String fileFullPath = repoPath + filePath;
                Label deletedFilePath = new Label(fileFullPath);
                vBox.getChildren().add(deletedFilePath);
            }
            deletedFilesScrollTab2.setContent(vBox);
        }
    }

    private void showNewFiles(Tab precedingCommitTab) {
        String repoPath = mainController.getMagitEngine().getRepositoryLocation() + File.separator;
        if(precedingCommitTab.getText().equals("First Preceding Commit")
                && commit.getCommitChangesToFirstPrecedingCommit() != null){
            VBox vBox = new VBox();
            for(String filePath : commit.getCommitChangesToFirstPrecedingCommit().getNewFiles()){
                String fileFullPath = repoPath + filePath;
                Label newFilePath = new Label(fileFullPath);
                vBox.getChildren().add(newFilePath);
            }
            newFilesScrollTab1.setContent(vBox);
        }
        else if(precedingCommitTab.getText().equals("Second Preceding Commit")
                && commit.getCommitChangesToSecondPrecedingCommit() != null){
            VBox vBox = new VBox();
            for(String filePath : commit.getCommitChangesToSecondPrecedingCommit().getNewFiles()){
                String fileFullPath = repoPath + filePath;
                Label newFilePath = new Label(fileFullPath);
                vBox.getChildren().add(newFilePath);
            }
            newFilesScrollTab2.setContent(vBox);
        }
    }

    private void showChangedFiles(Tab precedingCommitTab) {
        String repoPath = mainController.getMagitEngine().getRepositoryLocation() + File.separator;
        if(precedingCommitTab.getText().equals("First Preceding Commit")
                && commit.getCommitChangesToFirstPrecedingCommit() != null){
            VBox vBox = new VBox();
            for(String filePath : commit.getCommitChangesToFirstPrecedingCommit().getChangedFiles()){
                String fileFullPath = repoPath + filePath;
                Label changedFilePath = new Label(fileFullPath);
                vBox.getChildren().add(changedFilePath);
            }
            changedFilesScrollTab1.setContent(vBox);
        }
        else if(precedingCommitTab.getText().equals("Second Preceding Commit")
                && commit.getCommitChangesToSecondPrecedingCommit() != null){
            VBox vBox = new VBox();
            for(String filePath : commit.getCommitChangesToSecondPrecedingCommit().getChangedFiles()){
                String fileFullPath = repoPath + filePath;
                Label changedFilePath = new Label(fileFullPath);
                vBox.getChildren().add(changedFilePath);
            }
            changedFilesScrollTab2.setContent(vBox);
        }
    }

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
}
