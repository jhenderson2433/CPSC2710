module module6 {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.au.cpsc.part1 to javafx.fxml;
    exports edu.au.cpsc.part1;
    exports edu.au.cpsc.part2;
    opens edu.au.cpsc.part2 to javafx.fxml;
}
