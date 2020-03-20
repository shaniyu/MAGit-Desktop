package magitUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import magitEngine.MagitEngine;

import java.util.ArrayList;

public class MergeBranchToHeadController {

    private Stage stage;
    private MagitController magitController;
    @FXML private ChoiceBox<String> branchForMergeSpinner;
    @FXML private Button mergeBtn;

    @FXML void mergeBranchToHead(ActionEvent event) {

        String branchToMergeWith = branchForMergeSpinner.getValue();
        magitController.tryToMerge(branchToMergeWith);
        stage.close();
    }

    public void setBranchToChoiceBox(ArrayList<String> allOfCommitsSha1) {
        branchForMergeSpinner.getItems().addAll(allOfCommitsSha1);
    }

    public void setMainController(MagitController magitController) {
        this.magitController = magitController;
    }

    public void setPrimaryStage(Stage stage){
        this.stage = stage;
    }
}
