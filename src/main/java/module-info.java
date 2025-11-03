module com.example.scrmbl {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.scrmbl to javafx.fxml;
    exports com.example.scrmbl;
}