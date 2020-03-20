package magitUI;

import Utils.Files.FilesOperations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SolveConflicToFileController {
    private Stage myStage;
    private FilesWithConflictsController filesController;
    private MagitController mainController;

    @FXML private TextArea fileOnCurrentTxt;
    @FXML private TextArea fileInAncenstorTxt;
    @FXML private TextArea fileInTheirsTxt;
    @FXML private TextField filePath;
    @FXML private Button saveFileToWcBtn;
    @FXML private TextArea textAreaForSolveConflict;
    @FXML private Button deleteFileFromWCBtn;

    @FXML void saveConflictedFileToWC(ActionEvent event) throws IOException {
        // save the content of the solved file to the WC and then update the FilesWithConflictsController
        // ADD: save file to WC
        if(textAreaForSolveConflict.getText().equals("")){
            Alerts.showAlertMessage(Alert.AlertType.ERROR, "Solve Conflict", "Error", "You can't save an empty file");
        }
        else{
            String filePathToSolve = mainController.getMagitEngine().getRepositoryLocation() + File.separator + filePath.getText();
            File updatedFile = new File (filePathToSolve);
            if ( ! updatedFile.getParentFile().exists())
            {
                updatedFile.getParentFile().mkdirs();
            }
            if (! updatedFile.exists())
            {
                updatedFile.createNewFile();
            }
            // now the file exists on WC, write to it
            FilesOperations.writeTextToFile(textAreaForSolveConflict.getText(),filePathToSolve);

            // remove it from the list of files with conflicts
            filesController.removeFileFromList(filePath.getText());
            myStage.close();
        }
    }

    @FXML
    void deleteConflictedFileToWC(ActionEvent event) {

        // delete the solved file from the WC and then update the FilesWithConflictsController
        String filePathToSolve = mainController.getMagitEngine().getRepositoryLocation() + File.separator + filePath.getText();
        File fileToDelete = new File (filePathToSolve);
        if (fileToDelete.exists()) {
            if (!fileToDelete.delete()) {
                Alerts.showAlertMessage(Alert.AlertType.ERROR, "Delete File", "Error", "Couldn't delete file " + filePath + " From the working copy");
                //Don't want to close the solveConflict stage yet, maybe the user would want to solve the conflict in some other way
            } else {
                // remove it from the list of files with conflicts
                filesController.removeFileFromList(filePath.getText());
                myStage.close();
            }
        }
        else{
            filesController.removeFileFromList(filePath.getText());
            myStage.close();
        }
    }

    public void setMyStage(Stage stage)
    {
        myStage = stage;
    }

    public void setFileToSolvePath(String fileToSolveConflict)
    {
        filePath.setText(fileToSolveConflict);
    }

    public void setFileInHeadTxt(String textOfFile)
    {
        fileOnCurrentTxt.setText(textOfFile);
    }

    public void setFileInTheirsTxtAndDeleteButton(String textOfFile, Boolean shouldDisplayDeleteButton)
    {
        fileInTheirsTxt.setText(textOfFile);
        deleteFileFromWCBtn.setVisible(shouldDisplayDeleteButton);
    }

    public void setFileInAncenstorTxt(String textOfFile)
    {
        fileInAncenstorTxt.setText(textOfFile);
    }

    private String getFileContent(String filePathToShow) throws IOException
    {
        File fileToRead = new File(filePathToShow);
        if (fileToRead.exists())
        {
            String textOfFile = mainController.getMagitEngine().getFileContent(filePathToShow);
            return textOfFile;
        }
        return null;
    }

    public void initializeTextAreaForSolveConflict()
    {
        textAreaForSolveConflict.setText("");
    }

    public void setFilesController(FilesWithConflictsController filesController) {
        this.filesController = filesController;
    }
    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
}
