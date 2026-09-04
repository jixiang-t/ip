package gnaix;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main Gnaix GUI.
 */
public class MainWindow extends AnchorPane {

    private final Image userImage =
            new Image(getClass().getResourceAsStream("/images/Stewie.png"));

    private final Image gnaixImage =
            new Image(getClass().getResourceAsStream("/images/Brian.png"));

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView headerImage;

    @FXML
    private Label headerBanner;

    private Gnaix gnaix;

    /**
     * Connects the controller to the Gnaix application.
     *
     * @param gnaix Gnaix application instance.
     */
    public void setGnaix(Gnaix gnaix) {
        this.gnaix = gnaix;
    }

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        String banner = """
  ____ _   _    _    _____  __
 / ___| \\ | |  / \\  |_ _\\ \\/ /
| |  _|  \\| | / _ \\  | | \\  /
| |_| | |\\  |/ ___ \\ | | /  \\
 \\____|_| \\_/_/   \\_\\___/_/\\_\\
            """;

        headerImage.setImage(gnaixImage);
        headerBanner.setText(banner);

        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        dialogContainer.getChildren().add(
                DialogBox.getGnaixDialog(
                        "Hello! I'm Gnaix\nWhat can I do for you?",
                        gnaixImage));
    }

    /**
     * Handles a command entered by the user.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        String response = gnaix.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getGnaixDialog(response, gnaixImage)
        );

        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            sendButton.getScene().getWindow().hide();
        }
    }
}
