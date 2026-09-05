package clover;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box containing a speaker's image and message.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a dialog box with the supplied text and avatar image.
     *
     * @param dialogText the message to display
     * @param avatarImage the avatar image to display beside the message
     */
    private DialogBox(String dialogText, Image avatarImage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        dialog.setText(dialogText);
        displayPicture.setImage(avatarImage);
    }

    /**
     * Flips the dialog box so its image is on the left and text is on the right.
     */
    private void flip() {
        ObservableList<Node> temporaryChildren = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(temporaryChildren);
        getChildren().setAll(temporaryChildren);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a right-aligned dialog box for a user message.
     *
     * @param dialogText the user's message
     * @param avatarImage the user's avatar image
     * @return the dialog box to display
     */
    public static DialogBox getUserDialog(String dialogText, Image avatarImage) {
        return new DialogBox(dialogText, avatarImage);
    }

    /**
     * Creates a left-aligned dialog box for a Clover response.
     *
     * @param dialogText Clover's response
     * @param avatarImage Clover's avatar image
     * @return the flipped dialog box to display
     */
    public static DialogBox getCloverDialog(String dialogText, Image avatarImage) {
        return getCloverDialog(dialogText, avatarImage, null);
    }

    /**
     * Creates a left-aligned Clover dialog with styling for the command that produced it.
     *
     * @param dialogText Clover's response
     * @param avatarImage Clover's avatar image
     * @param commandType the type of command that produced the response
     * @return the styled, flipped dialog box to display
     */
    public static DialogBox getCloverDialog(String dialogText, Image avatarImage, String commandType) {
        var dialogBox = new DialogBox(dialogText, avatarImage);
        dialogBox.flip();
        dialogBox.changeDialogStyle(commandType);
        return dialogBox;
    }

    /**
     * Applies a CSS class for command types that have a dedicated reply style.
     */
    private void changeDialogStyle(String commandType) {
        if (commandType == null) {
            return;
        }

        switch (commandType) {
            case "ToDoCommand":
            case "DeadlineCommand":
            case "EventCommand":
                dialog.getStyleClass().add("add-label");
                break;
            case "MarkCommand":
                dialog.getStyleClass().add("marked-label");
                break;
            case "DeleteCommand":
                dialog.getStyleClass().add("delete-label");
                break;
            default:
                break;
        }
    }
}
