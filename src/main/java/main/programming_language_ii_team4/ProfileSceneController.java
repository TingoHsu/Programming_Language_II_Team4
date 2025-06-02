package main.programming_language_ii_team4;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileSceneController implements Initializable {
    private final UserDB userDB = new UserDB();

    @FXML
    private ComboBox<String> backgroundBox;

    @FXML
    private JFXRadioButton femaleBtn;

    @FXML
    private ToggleGroup genderBtnGroup;

    @FXML
    private Label genderLabel;

    @FXML
    private JFXRadioButton maleBtn;

    @FXML
    private TextField nameChangeField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField passwordChangeField;

    @FXML
    private Label passwordLabel;

    @FXML
    void backBtnOnAction(ActionEvent event) {
        try {
            userDB.updateColor();
            MainStage.changeScene("MainScene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteAccBtnOnAction(ActionEvent event) {
        final boolean[] answer = {true};
        final int[] count = {1};
        while (true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("確認");
            alert.setHeaderText("確認");
            alert.setContentText("你" + "真".repeat(count[0]) + "的要刪除帳號嗎?");

            ButtonType yesBtn = new ButtonType("是");
            ButtonType noBtn = new ButtonType("否");

            alert.getButtonTypes().setAll(yesBtn, noBtn);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesBtn) {
                    count[0]++;
                } else if (response == noBtn) {
                    answer[0] = false;
                }
            });
            if (count[0] > 5 || !answer[0]) break;
        }
        if (count[0] > 5) {
            userDB.delete(User.getName());
            User.clearAll();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("帳號刪除");
            alert.setHeaderText("帳號刪除");
            alert.setContentText("帳號成功刪除");
            alert.showAndWait();
            try {
                MainStage.changeScene("LoginScene.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void nameChangeBtnOnAction(ActionEvent event) {
        try {
            userDB.setName(User.getName(), nameChangeField.getText());
            User.setName(nameChangeField.getText());
            nameLabel.setText(User.getName());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("訊息");
            alert.setHeaderText("訊息");
            alert.setContentText("名稱更改成功!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("錯誤");
            alert.setHeaderText("錯誤");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void passwordChangeBtnOnAction(ActionEvent event) {
        try {
            userDB.setPassword(User.getPassword(), passwordChangeField.getText());
            User.setPassword(passwordChangeField.getText());
            passwordLabel.setText(User.getPassword());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("訊息");
            alert.setHeaderText("訊息");
            alert.setContentText("密碼更改成功!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("錯誤");
            alert.setHeaderText("錯誤");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void genderChangeBtnOnAction(ActionEvent event) {
        if (!checkGenderSelection().equals(User.getGender())) {
            try {
                userDB.setGender(User.getName(), checkGenderSelection());
                User.setGender(checkGenderSelection());
                String gender = (checkGenderSelection().equals("male")) ? "男" : (checkGenderSelection().equals("female")) ? "女" : null;
                genderLabel.setText(gender);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("訊息");
                alert.setHeaderText("訊息");
                alert.setContentText("性別更改成功!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("錯誤");
                alert.setHeaderText("錯誤");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    String checkGenderSelection ()  {
        if (maleBtn.isSelected()) {
            return "male";
        } else if (femaleBtn.isSelected()) {
            return "female";
        } else {
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(User.getName());
        passwordLabel.setText(User.getPassword());
        if (User.getGender().equals("male")) {
            genderLabel.setText("男");
            maleBtn.setSelected(true);
        } else if (User.getGender().equals("female")) {
            genderLabel.setText("女");
            femaleBtn.setSelected(true);
        }
        backgroundBox.getItems().addAll("白色", "黑寶", "天空藍", "薰衣草紫", "杏色", "淺藍漸層", "淺紫漸層", "淺棕漸層");
        String color = "";
        switch (User.getBackground()) {
            case "white": color = "白色"; break;
            case "dark": color = "黑寶"; break;
            case "skyblue": color = "天空藍"; break;
            case "apricot": color = "杏色"; break;
            case "lavender_purple": color = "薰衣草紫"; break;
            case "light_blue_gradient": color = "淺藍漸層"; break;
            case "light_purple_gradient": color = "淺紫漸層"; break;
            case "light_brown_gradient": color = "淺棕漸層"; break;
        }
        backgroundBox.setPromptText(color);
        backgroundBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            String selectedColor="";
            switch (newValue) {
                case "白色": selectedColor = "white"; break;
                case "黑寶": selectedColor = "dark"; break;
                case "天空藍": selectedColor = "skyblue"; break;
                case "杏色": selectedColor = "apricot"; break;
                case "薰衣草紫": selectedColor = "lavender_purple"; break;
                case "淺藍漸層": selectedColor = "light_blue_gradient"; break;
                case "淺紫漸層": selectedColor = "light_purple_gradient"; break;
                case "淺棕漸層": selectedColor = "light_brown_gradient"; break;
            }
            User.setBackground(selectedColor);
        });
    }
}
