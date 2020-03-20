package graph;//package node;

import DataStructures.CommitChanges;
import Exceptions.BranchAlreadyInUseException;
import Exceptions.CommitTreeException;
import Exceptions.IlegalHeadFileException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import magitEngine.MagitEngine;
import magitObjects.Commit;
import magitUI.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
//import node.CommitDetailsController;

public class CommitNodeController {

    private Commit commit;
    private MagitController magitController;
    @FXML private Label commitTimeStampLabel;
    @FXML private Label messageLabel;
    @FXML private Label committerLabel;
    @FXML private Circle CommitCircle;
    @FXML private Label commitSha1Label;
    @FXML private Label branchTopLabel;

    @FXML
    void showContextMenu(ContextMenuEvent event) {

        // Create ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        //Create createBranchHere menu option
        MenuItem createBranchHereMenuItem = new MenuItem("Create branch here");
        createBranchHereMenuItem.setOnAction((e) -> createBranchOnSpecificCommit(commit.getSha1()));
        //Create showCommitDetails menu option
        MenuItem showCommitDetailsMenuItem = new MenuItem("Show commit details");
        showCommitDetailsMenuItem.setOnAction((e) -> showCommitsWindow());
        //Create resetHeadBranc menu option
        MenuItem resetHeadBranchToThisCommit = new MenuItem("Reset head branch to this commit");
        resetHeadBranchToThisCommit.setOnAction((e)-> resetHeadBranchToSpecificCommit());
        //Create mergeBranchOnThisCommitToHead menu option
        MenuItem mergeBranchOnThisCommitToHead = new MenuItem("Merge branch on this commit to head branch");
        mergeBranchOnThisCommitToHead.setOnAction((e)-> mergeBranchOnThisCommitToHead());
        //Create resetHeadBranc menu option
        MenuItem deleteBranchOnThisCommit = new MenuItem("Delete branch on this commit");
        deleteBranchOnThisCommit.setOnAction((e)-> deleteBranchOnThisCommit());

        MenuItem boldAllCommitsUntilThisOneOnTheGraph = new MenuItem("Mark all commits of this branch");
        boldAllCommitsUntilThisOneOnTheGraph.setOnAction((e)-> magitController.createCommitsGraphAndShowIt(commit));
        //Add menu option to context menu
        contextMenu.getItems().addAll(
                createBranchHereMenuItem,
                showCommitDetailsMenuItem,
                resetHeadBranchToThisCommit,
                mergeBranchOnThisCommitToHead,
                deleteBranchOnThisCommit,
                boldAllCommitsUntilThisOneOnTheGraph);

        contextMenu.show(CommitCircle, event.getScreenX(), event.getScreenY());
    }

    private void deleteBranchOnThisCommit() {

        ArrayList<String> branchesOnCommit = magitController.getMagitEngine().getAllBranchesOnCommit(commit.getSha1());

        if(branchesOnCommit.size() == 0){
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Delete branch", "Please Notice!", "There aren't any branches pointing on this commit");
        }
        else if(branchesOnCommit.size() == 1){

            String branchToDelete = branchesOnCommit.get(0);
            String headBranchName = magitController.getMagitEngine().getCurrentRepo().getHeadBranch().getName();
            if(branchToDelete.equals(headBranchName)){
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Delete branch", "Please Notice!", "You can't delete the head branch!");
            }
            else{
                magitController.tryDeleteBranch(branchToDelete);
            }

        }
        else //branchesOnCommit.size() > 1
        {
            try{
                // use new controller and scene to choose branch to merge with
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../magitUI/fxml/chooseBranchToDelete.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage chooseBranchToDeleteStage = new Stage();
                chooseBranchToDeleteStage.setTitle("Delete branch");
                chooseBranchToDeleteStage.setScene(new Scene(root1));
                chooseBranchToDeleteStage.initModality(Modality.APPLICATION_MODAL);
                chooseBranchToDeleteStage.setResizable(false);

                DeleteBranchController deleteBranchController = fxmlLoader.getController();
                deleteBranchController.setMainController(magitController); // pass this controller to the new controller
                deleteBranchController.setPrimaryStage(chooseBranchToDeleteStage);

                deleteBranchController.setBranchToChoiceBox(branchesOnCommit);
                magitController.setSceneDesign(chooseBranchToDeleteStage.getScene(), true);
                chooseBranchToDeleteStage.show();
            }
            catch (Exception e){
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Delete branch", "Error", "Couldn't delete chosen branch");
            }
        }
    }

    private void mergeBranchOnThisCommitToHead() {

        ArrayList<String> branchesOnCommit = magitController.getMagitEngine().getAllBranchesOnCommit(commit.getSha1());

        if(branchesOnCommit.size() == 0){
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", "There aren't any branches pointing on this commit");
        }
        else if(branchesOnCommit.size() == 1){

            String branchToMergeWith = branchesOnCommit.get(0);
            magitController.tryToMerge(branchToMergeWith);

        }
        else //branchesOnCommit.size() > 1
        {
            try{
                // use new controller and scene to choose branch to merge with
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../magitUI/fxml/chooseBranchToMergeWithScene.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage chooseBranchToMergeWithStage = new Stage();
                chooseBranchToMergeWithStage.setTitle("Merge branches");
                chooseBranchToMergeWithStage.setScene(new Scene(root1));
                chooseBranchToMergeWithStage.initModality(Modality.APPLICATION_MODAL);
                chooseBranchToMergeWithStage.setResizable(false);

                MergeBranchToHeadController mergeBranchToHeadController = fxmlLoader.getController();
                mergeBranchToHeadController.setMainController(magitController); // pass this controller to the new controller
                mergeBranchToHeadController.setPrimaryStage(chooseBranchToMergeWithStage);

                mergeBranchToHeadController.setBranchToChoiceBox(branchesOnCommit);
                magitController.setSceneDesign(chooseBranchToMergeWithStage.getScene(), true);
                chooseBranchToMergeWithStage.show();
            }
            catch (Exception e){
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Merge", "Error", "Couldn't merge chosen branch to head branch");
            }

        }
    }

    private void resetHeadBranchToSpecificCommit(){

        MagitEngine magitEngine= magitController.getMagitEngine();
        String commitSha1 = commit.getSha1();

        try{
            if (!magitEngine.isThereOpenChangesInRepository()) {
                magitEngine.resetBranchSha1(commitSha1);
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head branch", "Please Notice!", "Reset passed successfully, you have checked out to the commit "+ commitSha1);
                magitController.updateBranchesList();
                magitController.createCommitsGraphAndShowIt(null);
            }
            else {
                ResetHeadController resetHeadController = new ResetHeadController();
                resetHeadController.setMainController(magitController);
                if (resetHeadController.handleOpenedChangesOnReset(commitSha1) == true )
                {
                    // close the reset head window if the reset was done
                    magitController.updateBranchesList();
                }
            }
        }

        catch (IlegalHeadFileException e1)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Reset head branch", "Error","Reset head, " + e1.getMessage());
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Reset head branch", "Error","Reset head, " + e.getMessage());
        }
    }

    private void createBranchOnSpecificCommit(String sha1) {

        try {
            ArrayList<String> remoteBranchesPointedByCommit = magitController.getMagitEngine().getAllRemoteBranchesOnCommit(commit.getSha1());

            //There are one or more remote branches that are pointing to this commit,
            // so we will give the user an option to create the branch as RTB on any of the RBs
            if (remoteBranchesPointedByCommit.size() > 0) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Add new branch");
                alert.setHeaderText("");
                alert.setContentText("This commit is pointed by a remote branch,\nWould you like to track this branch and create a remote tracking branch?");

                //creating the buttons
                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");
                // put buttons on dialog window
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                // show dialog and wait for response
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() != null && option.get() == buttonTypeYes) {
                    String remoteBranchToTrack = remoteBranchesPointedByCommit.get(0);
                    String newBranchName = remoteBranchToTrack.substring(remoteBranchToTrack.lastIndexOf(File.separator) +1);
                    magitController.getMagitEngine().addNewBranchOnSpecificCommit(newBranchName, commit);
                    magitController.performAddRTB(remoteBranchesPointedByCommit);

                } else {
                    // user wants to create a local branch
                    getBranchNameFromUserAndCreateLocalBranchForCommit();
                }
            }
            //There are no remote branches pointing to this commit, ask the user for a name for a local branch (not RTB)
            else{ //remoteBranchesPointedByCommit.size() == 0
                // user wants to create a local branch
                getBranchNameFromUserAndCreateLocalBranchForCommit();
            }
        }
        catch (IlegalHeadFileException e1)
        {
            // might be thrown by isThereAnyCommitInTheRepository, when head isn't valid
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error","Add new branch " + e1.getMessage());
        }
        catch (CommitTreeException e)
        {
            // might be thrown by addBranchToCommitTree, when head node isn't in the tree
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error","Add new branch " + e.getMessage());
        }
        catch (Exception e3)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch","Error", "Couldn't create branch.\n " + e3.getMessage());
        }

    }

    public void getBranchNameFromUserAndCreateLocalBranchForCommit() throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../magitUI/fxml/createLocalBranchForCommitScene.fxml"));
        Parent root = (Parent)fxmlLoader.load();

        // set the primary stage to be the stage of the new window
        CreateLocalBranchForCommitController createLocalBranchForCommitController = fxmlLoader.getController();

        //create new stage and set it to the controller
        Stage createLocalBranchForCommitStage = new Stage();
        createLocalBranchForCommitStage.setTitle("Create new local branch");
        createLocalBranchForCommitStage.setScene(new Scene(root));
        createLocalBranchForCommitStage.initModality(Modality.APPLICATION_MODAL);
        createLocalBranchForCommitStage.setResizable(false);
        createLocalBranchForCommitController.setMainController(magitController);
        createLocalBranchForCommitController.setCommit(commit);
        createLocalBranchForCommitController.setPrimaryStage(createLocalBranchForCommitStage);
        magitController.setSceneDesign(createLocalBranchForCommitStage.getScene(), true);
        createLocalBranchForCommitStage.show();
    }

    @FXML
    void showCommitSha1(MouseEvent event) {
        // when we pass the commit circle with mouse, show its sha1
        commitSha1Label.setVisible(true);
    }

    @FXML
    void hideCommitSha1(MouseEvent ecirvent) {
        // hide sha1 when mouse left the commit circle
        commitSha1Label.setVisible(false);
    }

    //@FXML
    // when user chooses "show commit details" from context menu when clicking on commit circle
    // commit circle is clicked, open a window to perform everything on this commit
    public void showCommitsWindow() {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../magitUI/fxml/commitDetailsScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();

            // set the primary stage to be the stage of the new window
            CommitDetailsController commitDetailsController = fxmlLoader.getController();

            //create new stage and set it to the controller
            Stage commitDetailsStage = new Stage();
            commitDetailsStage.setTitle("Commit Details");
            commitDetailsController.setMainController(magitController);
            commitDetailsStage.setScene(new Scene(root, 400,700));
            commitDetailsStage.initModality(Modality.APPLICATION_MODAL);
            commitDetailsController.setCommit(commit);
            calcualteChangesBetweenCommitAndPrecedingCommits();
            commitDetailsController.initializeData();
            magitController.setSceneDesign(commitDetailsStage.getScene(), true);
            commitDetailsStage.show();
        }
        catch (Exception e){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show commit details", "Error", e.getMessage());
        }
    }

    // This function compares a commit to its parents and creates the diffs lists objects
    private void calcualteChangesBetweenCommitAndPrecedingCommits() throws Exception{
        String pathToSpreadTo = magitController.getMagitEngine().getRepositoryLocation() + File.separator + ".magit" + File.separator +"temp";
        // spread those commits to temp folder under magit folder
        if(commit.getFirstParentSha1() != null && !commit.getFirstParentSha1().equals("null")){
            if(commit.getCommitChangesToFirstPrecedingCommit() == null){
                commit.setCommitChangesToFirstPrecedingCommit(new CommitChanges());
                //Calculate changes only if CommitChangesToFirstPrecedingCommit is null.
                //If it wasn't null then it means we already calculated the changes at some point,
                //and these changes won't change because a commit can't changes
                magitController.getMagitEngine().calculateChangesBetween2Commits(
                        commit, commit.getFirstParentSha1(), commit.getCommitChangesToFirstPrecedingCommit(), pathToSpreadTo, "temp");
            }
        }
        if(commit.getSecondParentSha1() != null && !commit.getSecondParentSha1().equals("null")){
            if(commit.getCommitChangesToSecondPrecedingCommit() == null){
                commit.setCommitChangesToSecondPrecedingCommit(new CommitChanges());
                //Calculate changes only if CommitChangesToSecondPrecedingCommit is null.
                //If it wasn't null then it means we already calculated the changes at some point,
                //and these changes won't change because a commit can't changes
                magitController.getMagitEngine().calculateChangesBetween2Commits(
                        commit, commit.getSecondParentSha1(), commit.getCommitChangesToSecondPrecedingCommit(), pathToSpreadTo, "temp");
            }
        }
    }


    public void setCommitTimeStamp(String timeStamp) {
        commitTimeStampLabel.setText(timeStamp);
        commitTimeStampLabel.setTooltip(new Tooltip(timeStamp));
    }

    public void setCommitter(String committerName) {
        committerLabel.setText(committerName);
        committerLabel.setTooltip(new Tooltip(committerName));
    }

    public void setCommitMessage(String commitMessage) {
        messageLabel.setText(commitMessage);
        messageLabel.setTooltip(new Tooltip(commitMessage));
    }

    public void setCommitSha1Message(String commitSha1) {
        commitSha1Label.setText(commitSha1);
        commitSha1Label.setTooltip(new Tooltip(commitSha1));
    }

    public void setCommitBranchesTop(String branchesNames)
    {
        branchTopLabel.setText(branchesNames);
        branchTopLabel.setTooltip(new Tooltip(branchesNames));
    }

    public int getCircleRadius() {
        return (int)CommitCircle.getRadius();
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public void setMainController(MagitController magitController) {
        this.magitController = magitController;
    }

    public void setHeadColor()
    {
        CommitCircle.setFill(Paint.valueOf("Yellow"));
    }

    public void boldCircle()
    {
        CommitCircle.setFill(Paint.valueOf("Orange"));
    }

}
