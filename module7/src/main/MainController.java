import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class MainController {

    @FXML
    private ListView<String> workoutList;

    private ObservableList<String> workouts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        workoutList.setItems(workouts);
    }

    @FXML
    private void handleAddWorkout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_workout.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Add Workout");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRemoveWorkout() {
        String selected = workoutList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            workouts.remove(selected);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    public void addWorkout(String workout) {
        workouts.add(workout);
    }
}