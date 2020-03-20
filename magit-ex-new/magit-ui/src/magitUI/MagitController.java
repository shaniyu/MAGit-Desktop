package magitUI;

import DataStructures.CommitChanges;
import DataStructures.MagitCommitNode;
import DataStructures.Tree;
import DataStructures.TreeNode;
import Exceptions.CommitIsNullException;
import Exceptions.CommitTreeException;
import Exceptions.IlegalHeadFileException;
import Utils.Files.FilesOperations;
import com.fxgraph.edges.Edge;
import com.fxgraph.graph.Graph;
import com.fxgraph.graph.ICell;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import graph.CommitNode;
import graph.CommitTreeLayout;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.effect.Effect;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import magitEngine.MagitEngine;
import magitEngine.TaskValue;
import magitObjects.Branch;
import magitObjects.Commit;
import magitObjects.FolderType;
import magitObjects.GitObject;
import magitUI.fxml.AppCss.CssStyle;

public class MagitController implements Initializable {

    private MagitEngine magitEngine;
    private Stage primaryStage;
    private SimpleStringProperty repoLocationProperty;
    private SimpleStringProperty repoNameProperty;
    private ObservableList<String> branchesNamesProperty = FXCollections.observableArrayList();
    private Dictionary<String, ICell> cellsDictinary;
    private Dictionary<String, Edge> edgesDictionary;
    private Graph graph;
    private CssStyle gameStyle = CssStyle.defaultive;
    private Scene mainScene;
    private HashSet<String> sha1InHistoryOfCommit;

    @FXML private TextField commitMessageTxt;
    @FXML private TextField newBranchNameField;
    @FXML private TextField user_name_text;
    @FXML private TextField repoLocationTxt;
    @FXML private TextField repoNameTxt;
    @FXML private ComboBox<String> checkoutToBranchSpinner;
    @FXML private ComboBox<String> deleteBranchSpinner;
    @FXML private ChoiceBox<String> mergeBranchesSpinner;
    @FXML private Button loadRepoFromXmlBtn;
    @FXML private Button load_repo_from_path_Btn;
    @FXML private Button createEmptyRepoBtn;
    @FXML private Button checkoutToBranchBtn;
    @FXML private Button AddBranchBtn;
    @FXML private Button cloneBtn;
    @FXML private Button pullBtn;
    @FXML private Button pushBtn;
    @FXML private Button fetchBtn;
    @FXML private Button showAllBranchesBtn;
    @FXML private Button deleteBranchBtn;
    @FXML private Button commitBtn;
    @FXML private Button showHeadFilesBtn;
    @FXML private Button resetHeadBtn;
    @FXML private Button showOpenChangesBtn;
    @FXML private Button mergeBtn;
    @FXML private Label taskMessageLabel;
    @FXML private ProgressBar taskProgressBar;
    @FXML private Label progressPercentLabel;
    @FXML private ScrollPane graphScrollPane;
    @FXML private Button designAppBtn;
    @FXML private Button saveUserNameBtn;

    public MagitController(){
        this.repoNameProperty = new SimpleStringProperty();
        this.repoLocationProperty = new SimpleStringProperty();
    };

    public void setModel(MagitEngine engine)
    {
        magitEngine = engine;
    }

    @FXML public void perfromCreateEmptyRepo(ActionEvent event) {

        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/createEmptyRepoScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();

            // set the primary stage to be the stage of the new window
            CreateEmptyRepoController createEmptyRepoController = fxmlLoader.getController();

            //create new stage and set it to the controller
            Stage createEmptyRepoStage = new Stage();
            createEmptyRepoStage.setTitle("Create an empty repository");
            createEmptyRepoStage.setScene(new Scene(root));
            createEmptyRepoStage.initModality(Modality.APPLICATION_MODAL);
            createEmptyRepoStage.setResizable(false);
            createEmptyRepoController.setMainController(this);
            createEmptyRepoController.setPrimaryStage(createEmptyRepoStage);
            setSceneDesign(createEmptyRepoStage.getScene(), true);
            createEmptyRepoStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create empty repository", "Error", e.getMessage());
        }
    }

    @FXML
    public void performLoadRepoFromPath(ActionEvent event) {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/loadRepoFromPathScene.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();

            // set the primary stage to be the stage of the new window
            loadRepoFromPathController loadRepoFromPathController = fxmlLoader.getController();

            //create new stage and set it to the controller
            Stage loadRepoFromPathStage = new Stage();
            loadRepoFromPathStage.setTitle("Load a repository");
            loadRepoFromPathStage.setScene(new Scene(root1));
            loadRepoFromPathStage.initModality(Modality.APPLICATION_MODAL);
            loadRepoFromPathStage.setResizable(false);
            loadRepoFromPathController.setPrimaryStage(loadRepoFromPathStage);
            loadRepoFromPathController.setMainController(this);
            setSceneDesign(loadRepoFromPathStage.getScene(), true);
            loadRepoFromPathStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Load repository from path", "Error", e.getMessage());
        }
    }

    @FXML
    public void performLoadRepoFromXml(ActionEvent event) {

        // open the new window that handles load repo from xml
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/loadRepoFromXmlScene.fxml"));
            Parent root1 = (Parent)fxmlLoader.load();

            // set the primary stage to be the stage of the new window
            LoadRepoFromXmlController loadRepoFromXmlController = fxmlLoader.getController();

            //create new stage and set it to the controller
            Stage loadRepoFromXmlStage = new Stage();
            loadRepoFromXmlStage.setTitle("Load a repository from xml");
            loadRepoFromXmlStage.setScene(new Scene(root1));
            loadRepoFromXmlStage.initModality(Modality.APPLICATION_MODAL);
            loadRepoFromXmlStage.setResizable(false);
            loadRepoFromXmlController.setPrimaryStage(loadRepoFromXmlStage);
            loadRepoFromXmlController.setMainController(this);
            setSceneDesign(loadRepoFromXmlStage.getScene(), true);
            loadRepoFromXmlStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Load repository from xml", "Error", e.getMessage());
        }
    }

    // is active means that the task is activated
    public void toggleTaskButtons(boolean isActive) {
        loadRepoFromXmlBtn.setDisable(isActive);
        load_repo_from_path_Btn.setDisable(isActive);
        createEmptyRepoBtn.setDisable(isActive);
        cloneBtn.setDisable(isActive);
        pushBtn.setDisable(isActive);
        pullBtn.setDisable(isActive);
        fetchBtn.setDisable(isActive);
        showAllBranchesBtn.setDisable(isActive);
        checkoutToBranchSpinner.setDisable(isActive);
        checkoutToBranchBtn.setDisable(isActive);
        AddBranchBtn.setDisable(isActive);
        deleteBranchSpinner.setDisable(isActive);
        mergeBranchesSpinner.setDisable(isActive);
        commitBtn.setDisable(isActive);
        showHeadFilesBtn.setDisable(isActive);
        resetHeadBtn.setDisable(isActive);
        showOpenChangesBtn.setDisable(isActive);
        deleteBranchBtn.setDisable(isActive);
        mergeBranchesSpinner.setDisable(isActive);
        mergeBtn.setDisable(isActive);
        designAppBtn.setDisable(isActive);
        saveUserNameBtn.setDisable(isActive);

        // show or hide the task progress components
        taskMessageLabel.setVisible(isActive);
        taskProgressBar.setVisible(isActive);
        progressPercentLabel.setVisible(isActive);
    }

    // when the task is done, reactive the buttons and spinenrs, and also the properties
    public void doWhenTaskFinish(Boolean isActive)
    {
        toggleTaskButtons(isActive);

        try
        {
            updateRepoProperties();
            // alert for success
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Load repository from xml", "Please Notice!", "Repository loaded from xml! \nYou loaded the repository in " + magitEngine.getRepositoryLocation());
           createCommitsGraphAndShowIt(null);
           clearTextFieldsAndSpinners();
        }
        catch (IOException e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Load repository from xml", "Error", e.getMessage());
        }
    }

    public void bindTaskToUIComponents(Task<TaskValue> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label, show percentage without digit after dot and append '%' to it
        // aTask.progressProperty is the only dynamic thing in here
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        // we wrote onTaskFinished, it makes unbinding
        // valueProperty will be updated when the task is done
        // unbind
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish), aTask.getValue() );
        });
    }

    // unbind all the ui components of the load repo from xml task
    public void onTaskFinished(Optional<Runnable> onFinish, TaskValue taskValue) {
        this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        if (taskValue.isTaskSuccess())
        {
            onFinish.ifPresent(Runnable::run); // call doWhenTaskFinish
        }
        else
        {
            // task failed
            toggleTaskButtons(false);
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Load repository from xml", "Error", taskValue.getException().getMessage());
        }
    }

    @FXML
    public void performAddNewBranch(ActionEvent event) {
        String newBranchName = newBranchNameField.getText();

        if (magitEngine.getCurrentRepo() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.NOT_A_REPO);
            newBranchNameField.setText("");
        }
        else
        {
            if (newBranchName.isEmpty())
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.EMPTY_NAME_);
                newBranchNameField.setText("");
            }
            else if (newBranchName.equals("HEAD") || newBranchName.equals("commits"))
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.NOT_VALID_BRANCH_NAME);
                newBranchNameField.setText("");
            }
            else if (newBranchName.contains(File.separator) || newBranchName.contains("/"))
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Add new branch", "Please Notice!", UserMessages.ILEGAL_CHARS_IN_NAME);
                newBranchNameField.setText("");
            }
            else
            {
                try
                {
                    ArrayList<String> remoteBranchesPointedByHead = magitEngine.getAllRemoteBranchesOnCommit(magitEngine.getCurrentRepo().getHeadBranch().getCommit().getSha1());
                    // ofer the user to create a RTB
                    if (remoteBranchesPointedByHead.size() > 0)
                    {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Add new branch");
                        alert.setHeaderText("");
                        alert.setContentText("The head point to a remote branch,\nWould you like to track this branch and create a remote tracking branch?");

                        //creating the buttons
                        ButtonType buttonTypeYes = new ButtonType("Yes");
                        ButtonType buttonTypeNo = new ButtonType("No");
                        // put buttons on dialog window
                        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                        // show dialog and wait for response
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get() != null && option.get() == buttonTypeYes)
                        {
                            performAddRTB(remoteBranchesPointedByHead);
                        }
                        else {
                            // user wants to create a local branch
                            createALocalBranch(newBranchName);
                        }
                    }
                    else
                    {   // the head commit doesn't point to a RB commit, create a local branch
                        createALocalBranch(newBranchName);
                    }
                }
                catch (IlegalHeadFileException e1)
                {
                    // might be thrown by isThereAnyCommitInTheRepository, when head isn't valid
                    Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error", e1.getMessage());
                    newBranchNameField.setText("");
                }
                catch (CommitTreeException e)
                {
                    // might be thrown by addBranchToCommitTree, when head node isn't in the tree
                    Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error", e.getMessage());
                    newBranchNameField.setText("");
                }
                catch (Exception e2)
                {
                    Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add new branch", "Error", "The branch "+ newBranchName + " already exists.");
                    newBranchNameField.setText("");
                }
            }
        }
    }

    @FXML
    public void performChangeUserName(ActionEvent event) {

        if(user_name_text.getText().isEmpty()){

            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Change user name", "Error", UserMessages.EMPTY_NAME);
        }
        else if(magitEngine.getUserName().equals(user_name_text.getText())){
            // do nothing
        }
        else{
            magitEngine.setUserName(user_name_text.getText());
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Change user name", "Please Notice!", "User name changed to: " + magitEngine.getUserName());
        }
    }

    @FXML
    public void performCheckoutToBranch(ActionEvent event) {
        if (magitEngine.getCurrentRepo() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout branch", "Please Notice!", UserMessages.NOT_A_REPO);
            checkoutToBranchSpinner.getSelectionModel().clearSelection();
        }
        else
        {
            if (magitEngine.getCurrentRepo().getAllOfTheBranches().size() ==1)
            {
                // it means user checks out to the same branch, do nothing
                checkoutToBranchSpinner.getSelectionModel().clearSelection();
            }
            else
            {
                String branchNameToCheckout = checkoutToBranchSpinner.getSelectionModel().getSelectedItem();
                // nothing was chosen
                if (branchNameToCheckout == null)
                    return;
                if (magitEngine.getCurrentRepo().getHeadBranch().getName().equals(branchNameToCheckout))
                {
                    // it means user checks out to the same branch, do nothing
                    checkoutToBranchSpinner.getSelectionModel().clearSelection();
                    return;
                }
                else if (magitEngine.getCurrentRepo().getBranchByName(branchNameToCheckout).getIsRemote())
                {
                    // it user wants to checkout to a RB, he can't
                    // but we offer him to create new RTB and checkout to it instead
                    handleAddRTBAndCheckout(branchNameToCheckout);
                }
                else
                {
                    try{
                        if (! magitEngine.isThereOpenChangesInRepository())
                        {
                            // no opened changes on this commit, we can checkout
                            try
                            {
                                magitEngine.checkOutToBranch(branchNameToCheckout);
                                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout to branch", "Please Notice!", "You have checked out to branch " +branchNameToCheckout);
                                checkoutToBranchSpinner.getSelectionModel().clearSelection();
                                updateBranchesList();
                                createCommitsGraphAndShowIt(null);
                            }
                            catch (IOException e)
                            {
                                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Checkout to branch", "Error", "Unable to checkout " + branchNameToCheckout + ".\n" + e.getMessage());
                            }
                        }
                        else
                        {
                            handleOpenedChangesOnCheckout(branchNameToCheckout);
                        }
                    }
                    catch (CommitIsNullException e)
                    {
                        Alerts.showAlertMessage(Alert.AlertType.ERROR, "Checkout to branch", "Error", UserMessages.CHECK_NULL_COMMIT);
                    }
                    catch (Exception e)
                    {
                        Alerts.showAlertMessage(Alert.AlertType.ERROR, "Checkout to branch", "Error", UserMessages.CHECK_OPEN_CHANGES + "\n" + e.getMessage());
                    }
                }
            }
        } // show open changes and ask the user if to commit the changes or delete them and change branch anyway
    }

    private void handleOpenedChangesOnCheckout(String branchNameToCheckout)
    {
        // create a costume dialog with yes and no button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout branch");
        alert.setHeaderText(UserMessages.OPEN_CHANGES + "\n" + UserMessages.DEAL_WITH_OPEN_CHANGES_CHECKOUT);
        alert.setContentText(UserMessages.RESET_OPEN_CHANGES_WARNING);

        //creating the buttons
        ButtonType buttonTypeCancelCheckout = new ButtonType("Cancel checkout");
        ButtonType buttonTypeDeleteChanges = new ButtonType("Checkout anyway");

        // put buttons on dialog window
        alert.getButtonTypes().setAll(buttonTypeCancelCheckout, buttonTypeDeleteChanges);

        // show dialog and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeDeleteChanges) {
            try {
                magitEngine.checkOutToBranch(branchNameToCheckout);
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout to branch", "Please Notice!", "You have checked out to branch " + branchNameToCheckout);
                checkoutToBranchSpinner.getSelectionModel().clearSelection();
                updateBranchesList();
                createCommitsGraphAndShowIt(null);
                updateBranchesList();
            } catch (Exception e) {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Checkout to branch", "Error", "Error, unable to checkout " + branchNameToCheckout + ".\n" + e.getMessage());
            }
        }
        else
        {
            // don't checkout, reset branches spinner
            checkoutToBranchSpinner.getSelectionModel().clearSelection();
        }
    }

    // create commit , not a merge commit
    @FXML
    public void performCreateCommit(ActionEvent event) {

        String newCommitSha1 = null;
        //Checking if the current repository if null
        if (magitEngine.getCurrentRepo() == null){
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Create commit", "Please Notice!", UserMessages.UNINIAITLIZED_REPO);
        }
        else{
            try {
                if (magitEngine.isThereOpenChangesInRepository()) {
                    if(commitMessageTxt.getText().isEmpty()){
                        Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create commit", "Error", UserMessages.COMMIT_MSG_EMPTY);
                    }
                    else{
                        try {
                            newCommitSha1 = magitEngine.createCommit(commitMessageTxt.getText(), null);
                            Alerts.showAlertMessage(Alert.AlertType.INFORMATION,
                                    "Create commit",
                                    "Please Notice!",
                                    "Commit " + newCommitSha1 + " was created successfully.");
                            clearTextFieldsAndSpinners();
                            createCommitsGraphAndShowIt(null);

                        } catch (Exception e) {
                            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create commit", "Error",
                                    UserMessages.ERROR_CREATE_COMMIT + "\n" + e.getMessage());
                        }
                    }
                }
                else {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Create commit", "Please Notice!", "There are no changes to commit.");
                }
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Create commit", "Error",
                        UserMessages.ERROR_CHANGES_BEFORE_COMMIT + "\n" + e.getMessage());
            }
        }
    }

    @FXML
    public void performDeleteBranch(ActionEvent event) {
        String branchToDelete = deleteBranchSpinner.getSelectionModel().getSelectedItem();
        tryDeleteBranch(branchToDelete);
    }

    public void tryDeleteBranch(String branchToDelete) {

        if (magitEngine.getCurrentRepo() == null )
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Delete branch", "Please Notice!", UserMessages.UNINIAITLIZED_REPO);
        }
        else {
            ArrayList<String> allBranchesNames = magitEngine.getAllBranchesNames();

            // nothing was chosen
            if (branchToDelete == null)
                return;
            if (allBranchesNames.size() == 1)
            {
                // this means that we only have one branch, the head, it won't be on the list anyway.
                // so do nothing
                deleteBranchSpinner.getSelectionModel().clearSelection();
                return;
            }
            if(magitEngine.getCurrentRepo().getBranchByName(branchToDelete).getIsRemote()){
                deleteBranchSpinner.getSelectionModel().clearSelection();
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Delete branch", "Error", branchToDelete + " is a remote branch.\nYou can't delete remote branches!");
            }
            else
            {
                // this branch exist, and is not the head - > should delete it
                try
                {
                    magitEngine.deleteBranch(branchToDelete);
                    // delete branch succeeded
                    deleteBranchSpinner.getSelectionModel().clearSelection();
                    updateBranchesList();
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Delete branch", "Please Notice!", "The branch " + branchToDelete + " was deleted.");
                    createCommitsGraphAndShowIt(null);
                }
                catch (Exception e)
                {
                    deleteBranchSpinner.getSelectionModel().clearSelection();
                    Alerts.showAlertMessage(Alert.AlertType.ERROR, "Delete branch", "Error", "Error, could not delete the branch " + branchToDelete + ".\n" + e.getMessage());
                }
            }
        }
    }

    @FXML
    public void performResetHeadBranch(ActionEvent event) {
        try
        {
            if (magitEngine.getCurrentRepo() == null)
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head to a commit", "Please Notice!", UserMessages.NOT_A_REPO);
            }
            else if (! magitEngine.getCurrentRepo().isThereAnyCommitInTheRepository(magitEngine.getRepositoryLocation()))
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Reset head to a commit", "Please Notice!", UserMessages.NO_COMMITS_YET);
            }
            else
            {
                // use new controller and scene for reset
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/resetHeadToCommit.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage resetHeadStage = new Stage();
                resetHeadStage.setTitle("Reset head to commit");
                resetHeadStage.setScene(new Scene(root1));
                resetHeadStage.initModality(Modality.APPLICATION_MODAL);
                resetHeadStage.setResizable(false);

                ResetHeadController controller = fxmlLoader.getController();
                controller.setMainController(this); // pass this controller to the new controller
                controller.setPrimaryStage(resetHeadStage);

                try
                {
                    ArrayList<String> allOfCommitsSha1 = magitEngine.getAllCommitsSha1();
                    controller.setSha1ToChoiceBox(allOfCommitsSha1);
                    setSceneDesign(resetHeadStage.getScene(), true);
                    resetHeadStage.show();
                }
                catch (IOException e)
                {
                    Alerts.showAlertMessage(Alert.AlertType.ERROR, "Reset head", "Error", UserMessages.ERROR_SHOW_SHA1_OF_COMMITS + "\n" + e.getMessage());
                }
            }
        }
        catch (IlegalHeadFileException e1)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Reset head", "Error", e1.getMessage());
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Reset head", "Error", e.getMessage());
        }
    }

    @FXML
    public void performShowAllBranches(ActionEvent event) {
        if (magitEngine.getCurrentRepo() == null)
        {
            // show pop up window with error
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Show all branches", "Please Notice!", UserMessages.NOT_A_REPO);
        }
        else
        {
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/showAllBranches.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage showAllBranchesStage = new Stage();
                showAllBranchesStage.setTitle("Show all branches");
                showAllBranchesStage.setScene(new Scene(root1));
                showAllBranchesStage.initModality(Modality.APPLICATION_MODAL);
                showAllBranchesStage.setResizable(false);

                ShowAllBranchesController controller = fxmlLoader.getController();
                // add the branches lables to a VB that is shown inside the scroll pane
                VBox box = new VBox();
                for (Branch b : magitEngine.getBranches())
                {
                    String branchInfoForLable = b.getName();
                    if (b.getCommit()!= null && b.getCommit().getSha1() != null)
                    {
                        branchInfoForLable += ": "+ b.getCommit().getSha1();
                    }
                    Label branchLable = new Label(branchInfoForLable);
                    if (magitEngine.isBranchTheHeadBranch(b.getName()))
                    {
                        branchLable.setTextFill(Paint.valueOf("GREEN"));
                    }
                    if (b.getIsRemote())
                    {
                        branchLable.setTextFill(Paint.valueOf("RED"));

                    }
                    box.getChildren().add(branchLable);
                }
                controller.setBranchesToScroll(box);
                setSceneDesign(showAllBranchesStage.getScene(), false);
                showAllBranchesStage.show();
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show all branches", "Error", e.getMessage());
            }
        }

    }

    @FXML
    public void performShowOpenChanges(ActionEvent event) {

        if(magitEngine.getCurrentRepo() == null){

            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Show open changes", "Please Notice!", UserMessages.UNINIAITLIZED_REPO_OPEN_CHANGES);
        }
        else{
            try{
                //calculating open changes
                magitEngine.updateAllOpenChangesOfWC();
                // use new controller and scene for reset
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/showOpenChangesScene.fxml"));
                Parent root = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage showStatus = new Stage();
                showStatus.setTitle("Open Changes");
                showStatus.setScene(new Scene(root));
                showStatus.initModality(Modality.APPLICATION_MODAL);
                //showStatus.setResizable(false);

                ShowOpenChangesController showOpenChangesController = fxmlLoader.getController();
                showOpenChangesController.setMainController(this); // pass this controller to the new controller
                showOpenChangesController.setPrimaryStage(showStatus);
                showOpenChangesController.setChangedFiles();
                setSceneDesign(showStatus.getScene(), true);
                showStatus.show();

            }
            catch (Exception e){
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show open changes", "Error", e.getMessage());
            }
        }
    }

    public void updateRepoProperties() throws IOException
    {
        repoNameProperty.set(magitEngine.getRepositoryName());
        repoLocationProperty.set(magitEngine.getRepositoryLocation());
        updateBranchesList();
    }

    public void updateBranchesList() throws IOException
    {
        branchesNamesProperty.clear();
        for (String branchName : magitEngine.getAllBranchesNames())
        {
            // add all branches except head
            if (! branchName.equals(magitEngine.getCurrentRepo().getHeadBranch().getName()))
            {
                branchesNamesProperty.add(branchName);
            }
        }

        createCommitsGraphAndShowIt(null);
    }

    @FXML
    public void initialize(){ }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize all the bindings we need
        repoLocationTxt.textProperty().bind(repoLocationProperty);
        repoNameTxt.textProperty().bind(repoNameProperty);
        checkoutToBranchSpinner.setItems(branchesNamesProperty);
        deleteBranchSpinner.setItems(branchesNamesProperty);
        mergeBranchesSpinner.setItems(branchesNamesProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MagitEngine getMagitEngine() {
        return magitEngine;
    }

    public ObservableList<String> getBranchesNamesProperty() {
        return branchesNamesProperty;
    }

    public void createCommitsGraphAndShowIt(Commit commitToBoldHistory)
    {
        // creating empty javafx graph
        graph = new Graph();
        // adding the nodes to the graph
        try {
            createCommitsForGraph(graph, commitToBoldHistory);
        }
        catch (IOException e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show commits graph", "Error", "Show commits graph failed\n" + e.getMessage());
        }
        // set the graph on the ui
        PannableCanvas canvas = graph.getCanvas();
        graphScrollPane.setContent(canvas);

    }

    // optional: send commit as commitToBoldHistory to create the graph with bold commits and edges
    // from the first commit until this commit
    // if commitToBoldHistory is null, show graph normally
    private void createCommitsForGraph(Graph graph, Commit commitToBoldHistory) throws IOException
    {
        // create empty dictionaries for the cells and the edges
        cellsDictinary = new Hashtable<>();
        edgesDictionary = new Hashtable<>();
        sha1InHistoryOfCommit = new HashSet<>(); // empty set of commits
        Model model = graph.getModel();
        graph.beginUpdate();

        if (commitToBoldHistory != null)
        {
            // means we build the graph and want to bold the commit history
            sha1InHistoryOfCommit = magitEngine.getAllSha1InHistoryOfCommit(commitToBoldHistory);
        }


        // the dictionary of all the nodes in the commits tree
        Dictionary<String, MagitCommitNode> allNodesInTree = magitEngine.getTreeNodesDictinary();
        // get all the commit nodes that are the top of a branch, from the repository commit tree
        Dictionary<String, MagitCommitNode> branchesTopNodesDictionary = magitEngine.getCurrentRepo().getAllBranchesTopNodesDictionary();
        Enumeration<String> branchTopSha1InTree = branchesTopNodesDictionary.keys();
        // add all the nodes in the tree to the graph, starting from top of each branch
        // iterate over the commits on the branches top and call recursive method to keep the work with their parents
        while(branchTopSha1InTree.hasMoreElements()) {
            String sha1Key = branchTopSha1InTree.nextElement();

            if (cellsDictinary.get(sha1Key) == null)
            {
                // cell not in the dictionary, add it
                MagitCommitNode node = allNodesInTree.get(sha1Key);
                CommitNode newCell = new CommitNode(node, this, sha1InHistoryOfCommit.contains(sha1Key));
                // add node to graph and to cellc dictionary
                model.addCell(newCell);
                cellsDictinary.put(sha1Key, newCell);

                // add edge to parent1
                if (node.getFirstParent() != null)
                {
                    CommitNode firstParentCell = recurssiveAddCellToGraph( node.getFirstParent(), model);
                    String edgeString = sha1Key+ "," + node.getFirstParent().getSha1OfNode();
                    if (edgesDictionary.get(edgeString) == null)
                    {
                        // edge does not exist, add it
                        Edge newEdge1 = new Edge(newCell, firstParentCell);
                        edgesDictionary.put(edgeString,newEdge1);
                        // add cell to graph
                        model.addEdge(newEdge1);
                    }
                }
                if (node.getCommit().equals(magitEngine.getCurrentRepo().getHeadBranch().getCommit()))
                {
                    // this is the head
                    newCell.setIsHead(true);
                }

                // add edge to parent2
                if(node.getSecondParent() != null)
                {
                    CommitNode secondParentCell =  recurssiveAddCellToGraph(node.getSecondParent(), model);
                    String edgeString = sha1Key+ "," + node.getSecondParent().getSha1OfNode();

                    if (edgesDictionary.get(edgeString) == null)
                    {
                        // edge does not exist, add it
                        Edge newEdge2 = new Edge(newCell, secondParentCell);
                        edgesDictionary.put(edgeString, newEdge2);
                        // add cell to graph
                        model.addEdge(newEdge2);
                    }
                }
            }
        }

        graph.endUpdate();
        graph.layout(new CommitTreeLayout());
        Platform.runLater(() -> {
            graph.getUseViewportGestures().set(false);
            graph.getUseNodeGestures().set(false);
        });
    }

    // this function add Cells and Edges recursively, depends on the commit tree and the dictionary cellsDictinary
    private CommitNode recurssiveAddCellToGraph(MagitCommitNode nodeToAdd, Model model)
    {
        if (cellsDictinary.get(nodeToAdd.getCommit().getSha1()) == null)
        {
            CommitNode newParent = new CommitNode(nodeToAdd, this, sha1InHistoryOfCommit.contains(nodeToAdd.getSha1OfNode()));
            model.addCell(newParent);
            cellsDictinary.put(nodeToAdd.getSha1OfNode(), newParent);

            // the parent does not exist in the graph and it is not the root, so it has more parents
            if ( ! magitEngine.getCurrentRepo().getCommitTree().getRoot().equals(nodeToAdd))
            {
                // there is first parent to add to the graph recursively
                if (nodeToAdd.getFirstParent() != null)
                {
                    MagitCommitNode myParent1 = nodeToAdd.getFirstParent();
                    CommitNode myOwnParent = recurssiveAddCellToGraph( myParent1, model);
                    // there is no edge yet to my first parent, add it now
                    addEdgeToModelAndDictionary(nodeToAdd, newParent, myParent1, myOwnParent, model);
                }

                // there is second parent to add to the graph recursively
                if (nodeToAdd.getSecondParent() != null)
                {
                    MagitCommitNode myParent2 = nodeToAdd.getSecondParent();
                    CommitNode myOwnParent = recurssiveAddCellToGraph( myParent2, model);
                    // there is no edge yet to my first parent, add it now
                    addEdgeToModelAndDictionary(nodeToAdd, newParent, myParent2, myOwnParent, model);
                }

            }
            if(nodeToAdd.getCommit().getSha1().equals(magitEngine.getCurrentRepo().getHeadBranch().getCommit().getSha1()))
            {
                newParent.setIsHead(true);
            }
            return newParent;
        }
        else
        {
            // some other cell already added this nodeToAdd as a cell, just return in
            return (CommitNode)(cellsDictinary.get(nodeToAdd.getSha1OfNode()));
        }
    }

    // this function gets two cells and their logical nodes and  might add edge to the model & dictionary, is not exist
    private void addEdgeToModelAndDictionary(MagitCommitNode child, CommitNode childCell, MagitCommitNode parent, CommitNode parentCell, Model model)
    {
        if (edgesDictionary.get(child.getSha1OfNode() + "," + parent.getSha1OfNode()) == null)
        {
            Edge newEdge = new Edge(childCell, parentCell);
            edgesDictionary.put(child.getSha1OfNode() + "," + parent.getSha1OfNode(), newEdge);
            model.addEdge(newEdge);
        }
    }
    @FXML
    public void performShowHeadFiles()
    {
        if (magitEngine.getCurrentRepo() == null)
        {
            // show pop up window with error
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Show head files", "Please Notice!", UserMessages.NOT_A_REPO);
        }
        else if (magitEngine.getCurrentRepo().getGitObjectTree() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Show head files", "Please Notice!", UserMessages.NO_HEAD_COMMIT);
        }
        else
        {
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/showAllHeadFilesScene.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage showHeadFileStage = new Stage();
                showHeadFileStage.setTitle("Show head files");
                showHeadFileStage.setScene(new Scene(root1));
                showHeadFileStage.initModality(Modality.APPLICATION_MODAL);
                showHeadFileStage.setResizable(false);

                ShowAllHeadFilesController controller = fxmlLoader.getController();
                Tree gitObjectTreeToPrint = magitEngine.getCurrentRepo().getGitObjectTree();
                VBox box = new VBox();

                printTree(gitObjectTreeToPrint.getRoot(), box);
                controller.setFilesToScroll(box);
                setSceneDesign(showHeadFileStage.getScene(), true);
                showHeadFileStage.show();
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show head files", "Error", e.getMessage());
            }
        }
    }


    // we can assume that it is possible to merge only with branch that is not the head
    @FXML
    private void performMerge()
    {
        String branchToMergeWith = mergeBranchesSpinner.getSelectionModel().getSelectedItem();
        tryToMerge(branchToMergeWith);
    }

    public void tryToMerge(String branchToMergeWith) {

        try{
            if (magitEngine.getCurrentRepo() == null)
            {
                // show pop up window with error
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", UserMessages.NOT_A_REPO);
            }
            else if(branchToMergeWith == null)
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", UserMessages.MERGE_NOT_CHOSEN);
            }
            else if( ! magitEngine.getCurrentRepo().isThereAnyCommitInTheRepository(magitEngine.getRepositoryLocation()))
            {
                // no commits in the repo, can't merge
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", UserMessages.NO_COMMITS_YET);
            }
            else if(magitEngine.getCurrentRepo().getBranchByName(branchToMergeWith).getIsRemote())
            {
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", branchToMergeWith + " is a remote branch.\nYou can't merge with remote branches!");
            }
            else
            {
                if (magitEngine.isThereOpenChangesInRepository())
                {
                    // avoid merge when there are open changes
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", UserMessages.MERGE_ON_OPEN_CHANGES);
                }
                else {
                    // Can perform merge!
                    // Step 1: find last mutual commit of two branches
                    Commit commitOfBRanchToMerge = magitEngine.getCurrentRepo().getCommitOfBranch(branchToMergeWith);
                    String currentBranchSha1 = magitEngine.getCurrentRepo().getHeadBranch().getCommit().getSha1();
                    if (commitOfBRanchToMerge.getSha1().equals(currentBranchSha1))
                    {
                        // both branches point to the same commit
                        // No need to merge
                        Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", "Both branches points to the same commit, already merged.");
                    }
                    else
                    {
                        // Need to merge- no open changes and this branch points to another commit than the head commit
                        Commit ancenstorCommit = magitEngine.getMostRecentCommonAncestor(currentBranchSha1, commitOfBRanchToMerge.getSha1());
                        if (magitEngine.mergeIsFastForward(commitOfBRanchToMerge.getSha1(), currentBranchSha1, ancenstorCommit.getSha1()))
                        {
                            magitEngine.handleFastForwardMerge(currentBranchSha1, commitOfBRanchToMerge.getSha1(), ancenstorCommit.getSha1());
                            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Merge", "Please Notice!", "Fast forward merge was done.");
                            clearTextFieldsAndSpinners();
                            createCommitsGraphAndShowIt(null);
                        }
                        else
                        {
                            // Not fast forward merge
                            // compare "theirs" with ancenstor and create a list of file which changed\created\deleted
                            CommitChanges theirsCommitChangesToAncenstor = new CommitChanges();
                            CommitChanges headCommitChangesToAncenstor = new CommitChanges();

                            HashSet<String> filesThatChangesAndMightCauseConflicts = magitEngine.createListOfChangedFilesInTwoBranches(commitOfBRanchToMerge, ancenstorCommit.getSha1(), headCommitChangesToAncenstor, theirsCommitChangesToAncenstor);

                            if (filesThatChangesAndMightCauseConflicts.size() > 0)
                            {
                                // there are files that might make a conflict
                                checkForConflictsAndHandle(filesThatChangesAndMightCauseConflicts, headCommitChangesToAncenstor, theirsCommitChangesToAncenstor, branchToMergeWith);
                            }
                            else
                            {
                                // this is not possible in out excercise, the branches must be different in some file at this point
                                // because if they are not, they have the same sha1, Aviad told us this won't happen
                            }
                        }
                    }
                }
            }
        }
        catch (IlegalHeadFileException e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Merge", "Error", e.getMessage());
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Merge", "Error", e.getMessage());
        }
    }

    private void checkForConflictsAndHandle(HashSet<String> filesThatChangesAndMightCauseConflicts, CommitChanges headCommitChangesToAncenstor,
                                            CommitChanges theirsCommitChangesToAncenstor, String branchToMergeWith) throws Exception
    {
        ArrayList<String> filesWithConflicts = magitEngine.calculateConflictedFilesListAndHandleNonConflictedFiles(filesThatChangesAndMightCauseConflicts, headCommitChangesToAncenstor, theirsCommitChangesToAncenstor, branchToMergeWith);

        // if we found conflicts on some file, open new window to deal with it
        if (filesWithConflicts.size() > 0)
        {
            openFilesWithConflictsWindow(filesWithConflicts, branchToMergeWith);
        }
        else
        {
            // we can commit now, all the files where merged without conflicts
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/commitMergeChangesScene.fxml"));
                Parent root = (Parent)fxmlLoader.load();
                CommitMergeController commitMergeController = fxmlLoader.getController();
                Stage commitMergeStage = new Stage();
                commitMergeStage.setTitle("Commit merge changes");
                commitMergeStage.setScene(new Scene(root));
                commitMergeStage.initModality(Modality.APPLICATION_MODAL);
                commitMergeStage.setResizable(false);
                commitMergeController.setMainController(this);
                commitMergeController.setMyStage(commitMergeStage);
                commitMergeController.setBranchToMergeWith(branchToMergeWith);
                setSceneDesign(commitMergeStage.getScene(), true);
                commitMergeStage.show();
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Check for conflicts", "Error", e.getMessage());
            }
        }
    }


    public void printTree(TreeNode root, VBox box){

        if(root == null){
            return;
        }
        else if(root.getmChildren() == null){ //leaf
            printTreeNodeInfo(root,box);
        }
        else{

            printTreeNodeInfo(root, box);
            for(TreeNode child : root.getmChildren()){
                printTree(child, box);
            }
        }
    }

    public void printTreeNodeInfo(TreeNode node, VBox box){

        GitObject gitObjectToPrint = node.getmNode();

        String lableStr = "Name: " + gitObjectToPrint.getmPath();
        lableStr += "\nType: " + gitObjectToPrint.getmFolderType().toString().toLowerCase();
        lableStr += "\nSHA1: " + gitObjectToPrint.getmSHA1();
        lableStr += "\nLast modified by: " + gitObjectToPrint.getmLastModifiedBy();
        lableStr += "\nLast modified date: " + gitObjectToPrint.getmLastModifiedDate()+ "\n\n";
        Label newFileLabel = new Label(lableStr);

        if(gitObjectToPrint.getmFolderType().equals(FolderType.FILE)){
            newFileLabel.setUnderline(true);
            newFileLabel.setStyle("-fx-font-weight: bold");
            //in case this is a file, we want to make the label clickable to that the user can view the file's content
            //as it was in this commit
            newFileLabel.setOnMouseClicked((x)->{showFileContent(gitObjectToPrint.getmSHA1(), gitObjectToPrint.getmPath());});
        }

        box.getChildren().add(newFileLabel);
    }

    private void showFileContent(String fileSha1, String fileName) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/showFileContentScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            ShowFileContentController showFileContentController = fxmlLoader.getController();
            Stage showFileContentStage = new Stage();
            showFileContentStage.setTitle("File content");
            showFileContentStage.setScene(new Scene(root));
            showFileContentStage.initModality(Modality.APPLICATION_MODAL);
            showFileContentStage.setResizable(false);
            //Getting file content to send it to showFileContentController
            String zipFilePath = magitEngine.getCurrentRepo().getObjectsPath() + File.separator + fileSha1 + ".zip";
            String content = FilesOperations.readAllContentFromZipFile(zipFilePath);
            showFileContentController.setFileContent(content);
            //Setting file name in showFileContentController
            showFileContentController.setFileName(fileName);
            setSceneDesign(showFileContentStage.getScene(), true);
            showFileContentStage.show();
        }
        catch (IOException e){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show file content", "Error", "Couldn't load file content");
        }
    }

    public void clearTextFieldsAndSpinners()
    {
        commitMessageTxt.setText("");
        newBranchNameField.setText("");
        mergeBranchesSpinner.getSelectionModel().clearSelection();
        checkoutToBranchSpinner.getSelectionModel().clearSelection();
    }

    private void openFilesWithConflictsWindow(ArrayList<String> filesWithConflicts, String branchToMergeWith)
    {
        try
        {
            // open the filesWithConflictsScene window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/filesWithConflictsScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            FilesWithConflictsController showConflictsController = fxmlLoader.getController();
            Stage conflictsFilesStage = new Stage();
            conflictsFilesStage.setTitle("Solve conflicts");
            conflictsFilesStage.setScene(new Scene(root));
            conflictsFilesStage.initModality(Modality.APPLICATION_MODAL);
            conflictsFilesStage.setResizable(false);
            showConflictsController.setMainController(this);
            showConflictsController.setMyStage(conflictsFilesStage);
            showConflictsController.setBranchToMergeWith(branchToMergeWith);
            showConflictsController.setFilesPath(filesWithConflicts);
            setSceneDesign(conflictsFilesStage.getScene(), true);
            conflictsFilesStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Show conflicts window", "Error", e.getMessage());
        }
    }

    @FXML
    public void performClone(ActionEvent event) {

        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/cloneScene.fxml"));
            Parent root = (Parent)fxmlLoader.load();

            // set the primary stage to be the stage of the new window
            CloneController cloneRepoController = fxmlLoader.getController();

            //create new stage and set it to the controller
            Stage cloneRepoStage = new Stage();
            cloneRepoStage.setTitle("Clone a repository");
            cloneRepoStage.setScene(new Scene(root));
            cloneRepoStage.initModality(Modality.APPLICATION_MODAL);
            cloneRepoStage.setResizable(false);
            cloneRepoController.setMainController(this);
            cloneRepoController.setPrimaryStage(cloneRepoStage);
            setSceneDesign(cloneRepoStage.getScene(), true);
            cloneRepoStage.show();
        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Clone", "Error", e.getMessage());
        }
    }

    //
    public void performAddRTB(ArrayList<String> remoteBranchesPointedByCommit) throws Exception
    {
        // user wants to add a branch when the commit points to a RB commit
        // get all the RB on the commit

        if (remoteBranchesPointedByCommit.size() == 1)
        {
            // there is only one RB on the commit, so lets create a new RTB points to this,
            // and just noitfy the user when we did it
            String remoteTrackingBranchName = remoteBranchesPointedByCommit.get(0);
            magitEngine.addRTBranch(remoteTrackingBranchName);
            clearTextFieldsAndSpinners();
            updateBranchesList();
            // update visual graph
            createCommitsGraphAndShowIt(null);

            String newBranchName = remoteTrackingBranchName.substring(remoteTrackingBranchName.lastIndexOf(File.separator)+1);
            offerToCheckoutToNewBranchOrNotiFySuccess(newBranchName);
        }
        else if (remoteBranchesPointedByCommit.size() > 1)
        {
            // there are few RB that head points to, user need to select one of them
            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/remoteBranchesScreen.fxml"));
                Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

                //create new stage and set it to the controller
                Stage chooseRBToTrackStage = new Stage();
                chooseRBToTrackStage.setTitle("Choose remote branch to track");
                chooseRBToTrackStage.setScene(new Scene(root1));
                chooseRBToTrackStage.initModality(Modality.APPLICATION_MODAL);
                chooseRBToTrackStage.setResizable(false);

                RemoteBranchesController controller = fxmlLoader.getController();
                controller.setMainController(this);
                controller.setMyStage(chooseRBToTrackStage);
                // add the branches labels to a VB that is shown inside the scroll pane
                VBox box = new VBox();
                for (String remoteBranchName: remoteBranchesPointedByCommit)
                {
                    Label remoteBranchLable = new Label(remoteBranchName);
                    // clicking one label perform add RTB of the selected RB
                    remoteBranchLable.setOnMouseClicked((e)->{controller.addSSelectedRTBranch(remoteBranchName);});
                    box.getChildren().add(remoteBranchLable);
                }

                controller.setRemoteBranchesScroll(box);
                setSceneDesign(chooseRBToTrackStage.getScene(), true);
                chooseRBToTrackStage.show();
            }
            catch (Exception e)
            {
                throw new Exception("Add RTB, " + System.lineSeparator() + e.getMessage());
            }
        }

    }

    public void offerToCheckoutToNewBranchOrNotiFySuccess(String newBranchName) throws Exception
    {
        if (magitEngine.getCurrentRepo().isThereAnyCommitInTheRepository(magitEngine.getRepositoryLocation()))
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Add new branch");
            alert.setHeaderText("Branch added!");
            alert.setContentText("Branch " + newBranchName + " was added successfully.\nWould you like to checkout to it?");

            //creating the buttons
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            // put buttons on dialog window
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            // show dialog and wait for response
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() != null && option.get() == buttonTypeYes) {
                if (magitEngine.isThereOpenChangesInRepository())
                {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION ,"Checkout to new branch", "Please Notice!", "Checkout to new branch " + UserMessages.OPEN_CHANGES_ERROR);
                }
                else
                {
                    magitEngine.checkOutToBranch(newBranchName);
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout to new branch", "Please Notice!", "Branch " + newBranchName + " was added successfully.");
                    checkoutToBranchSpinner.getSelectionModel().clearSelection();
                    updateBranchesList();
                }
            }
            else
            {

            }
        }
        else
        {
            // can't checkout to null branch commit, but show success alert
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout to new branch", "Please Notice!", "Branch " + newBranchName + " was added successfully.");
        }
    }

    private void createALocalBranch(String newBranchName) throws Exception
    {
        magitEngine.addNewBranch(newBranchName);
        clearTextFieldsAndSpinners();
        updateBranchesList();
        // update visual graph
        createCommitsGraphAndShowIt(null);
        //addBranchToGraph(newBranchName);

        // offer to checkout only if the commit isn't null and no open changes
        offerToCheckoutToNewBranchOrNotiFySuccess(newBranchName);
    }

    // we user ask to checkout to a RB, we let him creat new RTB instead, or do nothing
    private void handleAddRTBAndCheckout(String branchNameToCheckout)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout to remote branch");
        alert.setHeaderText("");
        alert.setContentText("You can't checkout to remote branch,\n" +
                "Would you like to add new remote tracking branch and checkout to it instead?");

        //creating the buttons
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No, cancel checkout");
        // put buttons on dialog window
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        // show dialog and wait for response
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() != null && option.get() == buttonTypeYes)
        {
            try
            {
                String newBranchName = magitEngine.addRTBForRBAndCheckoutToIt(branchNameToCheckout);
                // just UI updates
                clearTextFieldsAndSpinners();
                updateBranchesList();
                checkoutToBranchSpinner.getSelectionModel().clearSelection();
                updateBranchesList();
                // update visual graph
                createCommitsGraphAndShowIt(null);
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Checkout to new branch", "Please Notice!","Remote tracking branch added!\n, Branch " + newBranchName + " was created and you checked out to it.");
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Checkout to new branch", "Error", e.getMessage());
            }
        }
        else {
            // do nothing, don't checkout, and don't create a RTB
        }
    }

    @FXML public void performFetch() throws IOException
    {
        if (magitEngine.getCurrentRepo() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Fetch", "Please Notice!", UserMessages.NOT_A_REPO);
        }
        else if (!magitEngine.isRepoHasARemote())
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Fetch", "Please Notice!", UserMessages.NO_REMOTE);
        }
        else
        {
            magitEngine.fetch();
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Fetch", "Please Notice!", "You have fetched successfully from " + magitEngine.getCurrentRepo().getRemoteRepoName() + " repository.");
            clearTextFieldsAndSpinners();
            updateBranchesList();
            createCommitsGraphAndShowIt(null); // based on the logical tree that is already updated
        }
    }

    @FXML public void performPull()
    {
        if (magitEngine.getCurrentRepo() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Pull", "Please Notice!", UserMessages.NOT_A_REPO);
        }
        else if (!magitEngine.isRepoHasARemote())
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Pull", "Please Notice!", UserMessages.NO_REMOTE);
        }
        else
        {
            try
            {
                if (! magitEngine.checkHeadIsRTB())
                {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Pull", "Please Notice!", UserMessages.NOT_RTB);
                }
                else if (magitEngine.isThereOpenChangesInRepository())
                {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Pull", "Please Notice!", UserMessages.PULL_OPEN_CHANGES);
                }
                else
                {
                    magitEngine.pull();
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Pull", "Please Notice!", "Pull was done successfully.");
                    clearTextFieldsAndSpinners();
                    createCommitsGraphAndShowIt(null);
                }
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Pull", "Error", e.getMessage());
            }
        }

    }


    @FXML
    void performPush(ActionEvent event) {
        if (magitEngine.getCurrentRepo() == null)
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Push", "Please Notice!", UserMessages.NOT_A_REPO);
        }
        else if (!magitEngine.isRepoHasARemote())
        {
            Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Push", "Please Notice!", UserMessages.NO_REMOTE);
        }
        else
        {
            try {
                if (magitEngine.checkHeadIsRTB() && ! magitEngine.isRemoteBranchAndBranchInRemoteAreOnSameCommit())
                {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Push", "Please Notice!", UserMessages.REPO_BEHIND_REMOTE);
                }
                else if (magitEngine.isThereOpenChangesOnRemoteRepo())
                {
                    Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Push", "Please Notice!", UserMessages.OPEN_CHANGES_ON_REMOTE);
                }
                else
                {
                    //If the head branch isn't RTB, then the user might want to push it to the RR as a new branch (Bonus 4)
                    //Else- if the head branch is RTB then we have a fitting branch in the RR
                    boolean isBranchToPushRTB = magitEngine.checkHeadIsRTB() ? true : false;
                    // perform push
                    magitEngine.push(isBranchToPushRTB);
                    if(isBranchToPushRTB){
                        Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Push", "Please Notice!", "Push was done successfully.");
                    }
                    else{
                        Alerts.showAlertMessage(
                                Alert.AlertType.INFORMATION,
                                "Push", "",
                                "Push of new branch was done successfully."
                                        + System.lineSeparator() + "This branch is now RTB, and a local branch was created in the remote repository");
                    }
                    //updateBranchesList();
                    clearTextFieldsAndSpinners();
                    createCommitsGraphAndShowIt(null);
                }
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Push", "Error", e.getMessage());
            }
        }

    }

    // used to change css style for the magit program
    public void setGameStyle(CssStyle gameStyle) {
        this.gameStyle = gameStyle;
    }

    @FXML void performChangeCss(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/designScene.fxml"));
            Parent root1 = (Parent)fxmlLoader.load(); // this line calls initialize

            //create new stage and set it to the controller
            Stage changeDesignStage = new Stage();
            changeDesignStage.setTitle("Change design");
            changeDesignStage.setScene(new Scene(root1));
            changeDesignStage.initModality(Modality.APPLICATION_MODAL);
            changeDesignStage.setResizable(false);

            DesignChooserController controller = fxmlLoader.getController();
            controller.setMainController(this);
            controller.setMyStage(changeDesignStage);
            setSceneDesign(changeDesignStage.getScene(), true);
            changeDesignStage.show();

        }
        catch (Exception e)
        {
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Change design", "Error", e.getMessage());
        }

    }

    public void setMainScene(Scene mainScene) {
        this.mainScene = mainScene;
    }

    public Scene getMainScene() {
        return mainScene;
    }

    public void setSceneDesign(Scene s, boolean changeLabel)
    {
        if (changeLabel) // if its fine to change labels and text color
        {
            if (gameStyle.equals(CssStyle.magit1))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit1.css");
            }
            else if(gameStyle.equals(CssStyle.magit2))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit2.css");
            }
            else if(gameStyle.equals(CssStyle.magit3))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit3.css");
            }
            else
            {
                s.getStylesheets().clear(); // defaultive design
            }
        }
        else
        {
            if (gameStyle.equals(CssStyle.magit1))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit1-noLabel.css");
            }
            else if(gameStyle.equals(CssStyle.magit2))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit2-noLabel.css");
            }
            else if(gameStyle.equals(CssStyle.magit3))
            {
                s.getStylesheets().clear();
                s.getStylesheets().add("magitUI/fxml/AppCss/magit3-noLabel.css");
            }
            else
            {
                s.getStylesheets().clear(); // defaultive design
            }
        }
    }
}
