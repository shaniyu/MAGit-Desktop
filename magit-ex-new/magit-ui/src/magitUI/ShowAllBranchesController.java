package magitUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;


public class ShowAllBranchesController {


    @FXML
    private ScrollPane branchesField;

    public void setBranchesToScroll(VBox boxWithBranchesLables)
    {
        branchesField.setContent(boxWithBranchesLables);
    }
}
