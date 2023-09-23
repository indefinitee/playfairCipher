module com.example.appmain {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.appmain to javafx.fxml;
    exports com.example.appmain;
}