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
     * @param s the message to display
     * @param i the avatar image to display beside the message
     */
    private DialogBox(String s, Image i) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(s);
        displayPicture.setImage(i);
    }

    /**
     * Flips the dialog box so its image is on the left and text is on the right.
     */
    private void flip() {
        ObservableList<Node> temporaryChildren = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(temporaryChildren);
        getChildren().setAll(temporaryChildren);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a right-aligned dialog box for a user message.
     *
     * @param s the user's message
     * @param i the user's avatar image
     * @return the dialog box to display
     */
    public static DialogBox getUserDialog(String s, Image i) {
        return new DialogBox(s, i);
    }

    /**
     * Creates a left-aligned dialog box for a Clover response.
     *
     * @param s Clover's response
     * @param i Clover's avatar image
     * @return the flipped dialog box to display
     */
    public static DialogBox getCloverDialog(String s, Image i) {
        var dialogBox = new DialogBox(s, i);
        dialogBox.flip();
        return dialogBox;
    }
}
