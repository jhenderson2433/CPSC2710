module com.example.module5 {
    requires javafx.controls;
    requires javafx.fxml;

    // Part 1
    exports edu.au.cpsc.miscstyle;
    opens edu.au.cpsc.miscstyle to javafx.fxml;

    // Part 2
    exports edu.au.cpsc.launcher;
    opens edu.au.cpsc.launcher to javafx.fxml;
}
