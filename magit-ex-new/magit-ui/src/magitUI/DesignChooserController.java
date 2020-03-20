package magitUI;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import magitUI.fxml.AppCss.CssStyle;


public class DesignChooserController {

    private Stage myStage;
    private Scene myScene;
    private MagitController mainController;
    @FXML private ComboBox<String> designSpinner;
    @FXML private Button saveDesignsBtn;

    @FXML
    void performSaveDesignsToApp(ActionEvent event){
        String designChosen = designSpinner.getSelectionModel().getSelectedItem();
        if (designChosen == null)
        {
            // nothing was chosen, keep the existing design
            myStage.close();
        }
        else if(designChosen.equals("cold"))
        {
            //set game design enum
            mainController.setGameStyle(CssStyle.magit1);
            // update this page
            myScene.getStylesheets().clear();
            myScene.getStylesheets().add("magitUI/fxml/AppCss/magit1.css");
            //update main page
            mainController.getMainScene().getStylesheets().clear();
            mainController.getMainScene().getStylesheets().add("magitUI/fxml/AppCss/magit1.css");
        }
        else if(designChosen.equals("blue"))
        {
            mainController.setGameStyle(CssStyle.magit2);

            myScene.getStylesheets().clear();
            myScene.getStylesheets().add("magitUI/fxml/AppCss/magit2.css");

            mainController.getMainScene().getStylesheets().clear();
            mainController.getMainScene().getStylesheets().add("magitUI/fxml/AppCss/magit2.css");
        }
        else if(designChosen.equals("purple"))
        {
            mainController.setGameStyle(CssStyle.magit3);

            myScene.getStylesheets().clear();
            myScene.getStylesheets().add("magitUI/fxml/AppCss/magit3.css");

            mainController.getMainScene().getStylesheets().clear();
            mainController.getMainScene().getStylesheets().add("magitUI/fxml/AppCss/magit3.css");
        }
        else if(designChosen.equals("default"))
        {
            // reset the css
            mainController.setGameStyle(CssStyle.defaultive);
            myScene.getStylesheets().clear();
            mainController.getMainScene().getStylesheets().clear();
        }
    }

    public void setMyStage(Stage myStage) {
        this.myStage = myStage;
        myScene = myStage.getScene();
        // also set the combo-box with values
        designSpinner.setItems(FXCollections.observableArrayList("cold", "blue", "purple", "default"));
    }

    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
}
