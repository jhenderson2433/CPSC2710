module com.example.module4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.module4 to javafx.fxml;
    exports com.example.module4;
}