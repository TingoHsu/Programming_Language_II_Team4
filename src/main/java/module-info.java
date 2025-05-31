module main.programming_language_ii_team4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.jfoenix;
    requires json.simple;


    opens main.programming_language_ii_team4 to javafx.fxml;
    exports main.programming_language_ii_team4;
}