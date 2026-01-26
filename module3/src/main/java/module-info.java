module com.example.module3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.module3 to javafx.fxml;
    exports com.example.module3;
}