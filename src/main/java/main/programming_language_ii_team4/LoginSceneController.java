package main.programming_language_ii_team4;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class LoginSceneController {
    private final UserDB userDB = new UserDB();
    private String gender;

    @FXML
    private JFXButton LoginBtn;

    @FXML
    private JFXButton enrollBtn;

    @FXML
    private JFXRadioButton femaleBtn;

    @FXML
    private ToggleGroup genderBtnGroup;

    @FXML
    private JFXRadioButton maleBtn;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void enrollBtnOnAction(ActionEvent event) {
        String name = usernameField.getText();
        String pw = passwordField.getText();
        try {
            checkGenderSelection();
            userDB.register(name, pw, gender);
            Alert registerAlert = new Alert(Alert.AlertType.INFORMATION);
            registerAlert.setTitle("Message");
            registerAlert.setContentText("註冊成功!");
            registerAlert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getName());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void loginBtnOnAction(ActionEvent event) {
        String name = usernameField.getText();
        String pw = passwordField.getText();
        try {
            userDB.login(name, pw);
            User.setName(name);
            User.setPassword(pw);
            User.setGender(gender);
            MainStage.changeScene("MainScene.fxml");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getName());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    void checkGenderSelection ()  {
        if (maleBtn.isSelected()) {
            gender = "male";
        } else if (femaleBtn.isSelected()) {
            gender = "female";
        } else {
            gender = null;
        }
    }
}
