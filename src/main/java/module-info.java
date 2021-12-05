module com.example.snake_v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires neuroph.core;


    opens com.example.snake_v2 to javafx.fxml;
    exports com.example.snake_v2;
}