package magitUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ShowFileContentController {

    @FXML
    private Label fileNameLbl;

    @FXML
    private TextArea fileContentTxtArea;

    public void setFileName(String fileName){
        fileNameLbl.setText(fileName + ":");
    }

    public void setFileContent(String content){
        fileContentTxtArea.setText(content);
    }

}
