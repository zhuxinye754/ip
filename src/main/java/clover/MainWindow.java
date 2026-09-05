package clover;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Clover clover;
    private Image userImage = new Image(getClass().getResourceAsStream("/images/curiousKitty.jpeg"));
    private Image cloverImage = new Image(getClass().getResourceAsStream("/images/gentlemenCat.jpeg"));

    /**
     * Connects the scroll pane to the dialog container after FXML injection.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Clover instance.
     *
     * @param cloverInstance the Clover instance that generates responses
     */
    public void setClover(Clover cloverInstance) {
        clover = cloverInstance;
    }

    /**
     * Adds dialogs for the user's input and Clover's response, then clears the input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = clover.getResponse(input);
        String commandType = clover.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCloverDialog(response, cloverImage, commandType)
        );
        userInput.clear();
    }
}
