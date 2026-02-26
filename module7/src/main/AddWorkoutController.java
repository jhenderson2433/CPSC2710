import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddWorkoutController {

    @FXML
    private TextField exerciseField;

    @FXML
    private TextField setsField;

    @FXML
    private TextField repsField;

    @FXML
    private TextArea notesArea;

    @FXML
    private void handleSave() {
        String workout = exerciseField.getText() + " - " +
                setsField.getText() + " sets x " +
                repsField.getText() + " reps";

        Stage stage = (Stage) exerciseField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) exerciseField.getScene().getWindow();
        stage.close();
    }
}