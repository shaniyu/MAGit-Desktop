package magitUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;


import java.util.ArrayList;

public class DeleteBranchController {

    private MagitController magitController;
    private Stage stage;
    @FXML private ChoiceBox<String> branchToDeleteSpinner;
    @FXML private Button deleteBranchBtn;

    @FXML
    void tryDeleteBranch(ActionEvent event) {

        String branchToDelete = branchToDeleteSpinner.getSelectionModel().getSelectedItem();
        magitController.tryDeleteBranch(branchToDelete);
        stage.close();
    }

    public void setMainController(MagitController magitController) {
        this.magitController = magitController;
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void setBranchToChoiceBox(ArrayList<String> branchesOnCommit) {

        //We don't want to add the head branch to the deleteBranch spinner.
        String headBranchName = magitController.getMagitEngine().getCurrentRepo().getHeadBranch().getName();

        for(String branchName : branchesOnCommit){

            if(!branchName.equals(headBranchName)){
                branchToDeleteSpinner.getItems().add(branchName);
            }
        }
    }
}
