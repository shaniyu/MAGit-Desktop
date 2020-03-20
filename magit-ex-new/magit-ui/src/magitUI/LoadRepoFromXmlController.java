package magitUI;

import Exceptions.XmlNotValidException;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import magitEngine.MagitEngine;
import magitEngine.TaskValue;
import magitObjects.Repository;
import xmlObjects.MagitRepository;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class LoadRepoFromXmlController implements Initializable {

    private Stage primaryStage;
    private Task<TaskValue> currentRunningLoadRepoFromXmlTask;
    private MagitController mainController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private Button openFileChooser;

    @FXML
    private Label errorLable;

    @FXML
    private TextField chosenXmlPathForRepo;

    @FXML
    private Button loadXmlBtn;

    @FXML
    void openFileChooser(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);

        // xml file was chosen
        if (file != null) {

            //show chosen file and clean error lable
            String absolutePath = file.getAbsolutePath();
            chosenXmlPathForRepo.setText(absolutePath);
            errorLable.setText("");
        }

    }

    @FXML
    void tryToLoadRepoFromXml(ActionEvent event) {
        String xmlFileChosen = chosenXmlPathForRepo.getText();
        MagitEngine engine = mainController.getMagitEngine();

        if (xmlFileChosen.isEmpty())
        {
            errorLable.setText(UserMessages.EMPTY_PATH);
        }
        else
        {
            File inputFile = new File(xmlFileChosen);
            try {
                //Creating magit repository objects from the xml
                JAXBContext jaxbContext = JAXBContext.newInstance(MagitRepository.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                MagitRepository magitRepository = (MagitRepository) jaxbUnmarshaller.unmarshal(inputFile);

                // set xml validator for the engine
                engine.setXmlValidator(engine.getUserName(), magitRepository);

                if (engine.getXmlValidator().isMagitRepositoryValid()){
                    //If there is some directory or file that isn't a repository in the location then
                    //we want to inform the user and not do anything.
                    if(engine.getXmlValidator().isThereANonRepositoryDirectoryInMagitRepoPath()){
                        Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Load repo from xml", "Please Notice!", UserMessages.DIR_EXIST);
                        return;
                    }
                    //If there is already a repository in the location then we want to ask the user if he wants
                    //to override the location with the repository in the xml
                    //or continue with the repository in the location and discard xml
                    else if(engine.getXmlValidator().isThereAlreadyRepoInMagitRepoPath()){
                        handleExistingRepositoryInXmlPath(magitRepository, xmlFileChosen);
                    }
                    else{ //There is no directory in the xml path- we want to create the repository in the location
                        File repoFolder = new File(magitRepository.getLocation());

                        if(!repoFolder.mkdir()){ //if creating the repository directory fails{
                            throw new IOException("Could not create the folder " + repoFolder.getPath() + "\nCheck your permittions."); }

                        if(engine.getCurrentRepo() == null){
                            engine.setCurrentRepo(new Repository());
                        }
                        callTheMainTask(xmlFileChosen, magitRepository);

                    }
                }
            }
            catch (XmlNotValidException e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Error", "Load repository from xml",
                        "Error, could not load repository from this xml file." + "\n"+ e.getMessage());
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Error", "Load repository from xml",
                        "Error, could not load repository from this xml file." + "\n"+ e.getMessage());
            }
        }
    }

    private void handleExistingRepositoryInXmlPath(MagitRepository magitRepository, String xmlFileChosen)
    {
        // create a costume dialog with yes and no button
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Load repository from xml");
        alert.setHeaderText(UserMessages.XML_REPOSITORY_PATH_EXISTS);

        //creating the buttons
        ButtonType buttonTypeOverride = new ButtonType("Override with xml file");
        ButtonType buttonTypeLoadFromExsitingPath = new ButtonType("Discard xml and load");

        // put buttons on dialog window
        alert.getButtonTypes().setAll(buttonTypeOverride, buttonTypeLoadFromExsitingPath);

        // show dialog and wait for response
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOverride){
            try {
                String xmlRepositoryLocation = magitRepository.getLocation();

                if(mainController.getMagitEngine().getCurrentRepo() == null){
                    mainController.getMagitEngine().setCurrentRepo(new Repository());
                }
                //Clearing the existing repository from the WC, and branches + objects content
                mainController.getMagitEngine().getCurrentRepo().clearRepository(xmlRepositoryLocation);
                callTheMainTask(xmlFileChosen ,magitRepository);
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Error", "Load repository from xml",
                        "Error, could not load repository from xml " + "\n"+ e.getMessage());
                primaryStage.close();
            }
        }
        // this case won't use the task, only load repo from path
        else if (result.get() == buttonTypeLoadFromExsitingPath) {
            try {
                mainController.getMagitEngine().continueWithExistingReposotory(magitRepository.getLocation());
                Alerts.showAlertMessage(Alert.AlertType.INFORMATION, "Load repository from xml", "Please Notice!", "Repository from " + magitRepository.getLocation() +" was loaded successfully.");
                mainController.updateRepoProperties();
                mainController.createCommitsGraphAndShowIt(null);
                primaryStage.close();
            }
            catch (Exception e)
            {
                Alerts.showAlertMessage(Alert.AlertType.ERROR ,"Error", "Load repository from xml",
                        "Error, could not load repository in path " +
                                magitRepository.getLocation()+"\n"+ e.getMessage());
                primaryStage.close();
            }
        }
        else {
            // do nothing, just close the window of the handle
            return;
        }
    }
    // get ready and call the task
    private void callTheMainTask(String xmlFileChosen, MagitRepository magitRepository)
    {
        primaryStage.close(); //  close the load from xml window
        //cleanOldResults();
        // UIAdapter uiAdapter = createUIAdapter();
        // disable all the buttons
        mainController.toggleTaskButtons(true);
        String xmlPath = xmlFileChosen;
        loadRepoFromXml( xmlPath, magitRepository, () -> mainController.doWhenTaskFinish(false));
    }

    // task for load repo from xml
    public void loadRepoFromXml( String xmlFilePath, MagitRepository magitRepository, Runnable onFinish) {
        currentRunningLoadRepoFromXmlTask = new LoadRepoFromXmlTask(xmlFilePath,  mainController.getMagitEngine().getCurrentRepo(), magitRepository);
        // bind and unbind on finish
        mainController.bindTaskToUIComponents(currentRunningLoadRepoFromXmlTask, onFinish);
        new Thread(currentRunningLoadRepoFromXmlTask).start();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setMainController(MagitController mainController) {
        this.mainController = mainController;
    }
}
