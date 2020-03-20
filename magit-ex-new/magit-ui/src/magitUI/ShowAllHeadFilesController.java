package magitUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ShowAllHeadFilesController {

    @FXML
    private ScrollPane filesField;

    public void setFilesToScroll(VBox boxWithFilesLabels)
    {
        filesField.setContent(boxWithFilesLabels);
    }
}
