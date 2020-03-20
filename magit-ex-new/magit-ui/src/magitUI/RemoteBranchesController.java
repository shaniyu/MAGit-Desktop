package magitUI;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class RemoteBranchesController {
    private MagitController mainController;
    private Stage myStage;

    @FXML private ScrollPane remoteBranchesScroll;

    public void setRemoteBranchesScroll(VBox remoteBranchesLabels) {
        this.remoteBranchesScroll.setContent(remoteBranchesLabels);
    }

    public void addSSelectedRTBranch(String remoteBranchName)
    {
        try
        {
            mainController.getMagitEngine().addRTBranch(remoteBranchName);
            String newBranchName = remoteBranchName.substring(remoteBranchName.lastIndexOf(File.separator)+1);
            myStage.close();

            mainController.clearTextFieldsAndSpinners();
            mainController.updateBranchesList();
            // update visual graph
            mainController.createCommitsGraphAndShowIt(null);

            // offer to checkout only if the commit isn't null and no open changes
            mainController.offerToCheckoutToNewBranchOrNotiFySuccess(newBranchName);
        }
        catch (Exception e)
        {
            // should never get here
            Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Add remote tracking branch", "Error", e.getMessage());
        }
    }

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }

    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
    }

}
