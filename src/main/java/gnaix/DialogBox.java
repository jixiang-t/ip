package gnaix;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a chat message shown in the Gnaix GUI.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load dialog box.", e);
        }

        dialog.setText(text);
        displayPicture.setImage(image);
    }

    /**
     * Creates a dialog box for user input.
     *
     * @param text text to display
     * @param image user image
     * @return dialog box
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for Gnaix's reply.
     *
     * @param text text to display
     * @param image Gnaix image
     * @return dialog box
     */
    public static DialogBox getGnaixDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        return dialogBox;
    }
}
