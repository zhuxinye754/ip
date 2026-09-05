package clover;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * A chat message containing text and an accompanying avatar image.
 */
public class DialogBox extends HBox {
    private Label text;
    private ImageView displayPicture;

    /**
     * Creates a dialog box with the supplied text and avatar image.
     *
     * @param s the message to display
     * @param i the avatar image to display beside the message
     */
    public DialogBox(String s, Image i) {
        text = new Label(s);
        displayPicture = new ImageView(i);

        text.setWrapText(true);
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        this.setAlignment(Pos.TOP_RIGHT);
        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Flips the dialog box so its image is on the left and text is on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> temporaryChildren = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(temporaryChildren);
        this.getChildren().setAll(temporaryChildren);
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
