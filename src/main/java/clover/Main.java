package clover;

import java.io.IOException;

import clover.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Clover using FXML.
 */
public class Main extends Application {
    private Clover clover = new Clover();

    /**
     * Loads and displays the main FXML view.
     *
     * @param stage the primary window provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            Scene scene = new Scene(anchorPane);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setClover(clover);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
