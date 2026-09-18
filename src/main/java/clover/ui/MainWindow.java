package clover.ui;

import clover.Clover;
import clover.command.CommandResponseStyle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
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
    private Image cloverImage = new Image(getClass().getResourceAsStream("/images/clover-forest-sprite.png"));

    /**
     * Connects the scroll pane to the dialog container after FXML injection.
     */
    @FXML
    public void initialize() {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, this::handleScroll);
    }

    /**
     * Scrolls the chat log when the pointer is over it.
     *
     * @param event the mouse-wheel or trackpad scroll event
     */
    private void handleScroll(ScrollEvent event) {
        double scrollableHeight = dialogContainer.getHeight() - scrollPane.getViewportBounds().getHeight();
        if (scrollableHeight <= 0 || event.getDeltaY() == 0) {
            return;
        }

        double newVerticalValue = scrollPane.getVvalue() - event.getDeltaY() / scrollableHeight;
        scrollPane.setVvalue(Math.clamp(newVerticalValue, 0, 1));
        event.consume();
    }

    /**
     * Injects the Clover instance.
     *
     * @param cloverInstance the Clover instance that generates responses
     */
    public void setClover(Clover cloverInstance) {
        clover = cloverInstance;
        dialogContainer.getChildren().add(DialogBox.getCloverDialog(
                "Welcome to Clover's study grove.\n"
                        + "What learning quest shall we tend today?", cloverImage));
        scrollToLatestMessage();
    }

    /**
     * Adds dialogs for the user's input and Clover's response, then clears the input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = clover.getResponse(input);
        CommandResponseStyle responseStyle = clover.getResponseStyle();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getCloverDialog(addForestSpriteReaction(response, responseStyle), cloverImage, responseStyle)
        );
        scrollToLatestMessage();
        userInput.clear();
        userInput.requestFocus();
    }

    /**
     * Adds a context-appropriate forest-sprite introduction to GUI responses.
     * The command result follows the introduction so it remains easy to read.
     *
     * @param response the command result to display
     * @param responseStyle the visual style selected for the result
     * @return the response with a BunBun reaction when one is appropriate
     */
    static String addForestSpriteReaction(String response, CommandResponseStyle responseStyle) {
        return switch (responseStyle) {
            case TASK_ADDED -> "A new study quest has taken root! " + response;
            case TUTOREE_ADDED -> "A new learning companion has arrived in the grove. " + response;
            case TASK_MARKED -> "The grove celebrates your progress! " + response;
            case TASK_DELETED -> "This trail has been cleared from our grove. " + response;
            case ERROR -> response;
            case STANDARD -> addStandardForestSpriteIntroduction(response);
        };
    }

    /**
     * Adds a forest-sprite introduction to standard command responses where the content reveals the action.
     *
     * @param response the standard command result to display
     * @return the response with a context-appropriate introduction
     */
    private static String addStandardForestSpriteIntroduction(String response) {
        return response;
    }

    /** Scrolls to the newest message after JavaFX updates the chat layout. */
    private void scrollToLatestMessage() {
        Platform.runLater(() -> scrollPane.setVvalue(1));
    }
}
