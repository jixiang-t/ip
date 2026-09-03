package gnaix;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main Gnaix GUI.
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

    private Gnaix gnaix;

    /**
     * Connects the controller to the Gnaix application.
     *
     * @param gnaix Gnaix application instance
     */
    public void setGnaix(Gnaix gnaix) {
        this.gnaix = gnaix;
    }

    /**
     * Handles user input submitted through the GUI.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        // We will connect this to Gnaix next.
        userInput.clear();
    }
}
