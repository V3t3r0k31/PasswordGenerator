module com.example.passwordgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;


    opens com.example.passwordgenerator to javafx.fxml;
    exports com.example.passwordgenerator;
}