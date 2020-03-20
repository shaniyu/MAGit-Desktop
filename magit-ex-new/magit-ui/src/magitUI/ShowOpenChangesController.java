package magitUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class ShowOpenChangesController {

    private MagitController mainController; // the controller of the main app - "magit" controller
    private Stage primaryStage;

    @FXML private ScrollPane newFilesScroll;
    @FXML private ScrollPane changedFilesScroll;
    @FXML private ScrollPane deletedFilesScroll;

    public void setMainController(MagitController controller)
    {
        this.mainController = controller;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setChangedFiles(){

        setDeletedFiles();
        setNewFiles();
        setChangedFile();
    }

    private void setDeletedFiles() {

        VBox vBox = new VBox();
        for(String filePath : mainController.getMagitEngine().getDeletedFiles()){
//            String fileFullPath = mainController.getMagitEngine().getRepositoryLocation()+ File.separator + filePath.substring(mainController.getMagitEngine().getCurrentRepo().getRepoFolderName().length()+1);
            String fileFullPath = mainController.getMagitEngine().getRepositoryLocation()+ File.separator + filePath;
            Label deletedFilePath = new Label(fileFullPath);
            vBox.getChildren().add(deletedFilePath);
        }
        deletedFilesScroll.setContent(vBox);
    }

    private void setNewFiles() {
        VBox vBox = new VBox();
        for(String filePath : mainController.getMagitEngine().getNewFiles()){
            Label newFilePath = new Label(filePath);
            vBox.getChildren().add(newFilePath);
        }
        newFilesScroll.setContent(vBox);
    }

    private void setChangedFile() {
        VBox vBox = new VBox();
        for(String filePath : mainController.getMagitEngine().getChangedFiles()){
            Label changedFilePath = new Label (filePath);
            vBox.getChildren().add(changedFilePath);
        }
        changedFilesScroll.setContent(vBox);
    }
}
