package magitUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import magitEngine.MagitEngine;
import java.net.URL;

public class main extends Application {
    private MagitEngine engine;

    @Override
    public void start(Stage primaryStage) throws Exception {
        engine  = new MagitEngine();

        primaryStage.setTitle("My amazing git");

        FXMLLoader fxmlLoader = new FXMLLoader(); // load the fxml file
        URL url = getClass().getResource("fxml/AppScene.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        MagitController magitController = fxmlLoader.getController();
        magitController.setModel(engine); // put the magitEngine class obejct on the controller
        // set the primary stage of magit to the main stage of the program
        magitController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(root, 1000, 700);
        magitController.setMainScene(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(main.class); // launch calls start method
    }
}
