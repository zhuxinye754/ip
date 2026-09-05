package clover;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Displays Clover's first JavaFX window.
 */
public class Main extends Application {

    /**
     * Configures and displays the primary application window.
     *
     * @param stage the primary window provided by JavaFX
     */
    @Override
    public void start(Stage stage) {
        Label helloWorld = new Label("Hello World!");
        Scene scene = new Scene(helloWorld);

        stage.setScene(scene);
        stage.show();
    }
}
