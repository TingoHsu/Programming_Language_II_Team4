package main.programming_language_ii_team4;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!User.getCityInput().isEmpty()) {
            cityInput.setText(User.getCityInput());
            searchBtnOnAction(new ActionEvent());
        }
    }

    private JSONObject weatherData;

    @FXML
    private TextField cityInput;

    @FXML
    private JFXButton logoutBtn;

    @FXML
    private JFXButton profileBtn;

    @FXML
    private Label cityNameLabel;

    @FXML
    private Label conditionLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label precipitationProbLabel;

    @FXML
    private Label windspeedLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private ImageView weatherIcon;

    @FXML
    void logoutBtnOnAction(ActionEvent event) {
        try {
            User.clearAll();
            MainStage.changeScene("LoginScene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchBtnOnAction(ActionEvent event) {
        String input = cityInput.getText();
        User.setCityInput(input);

        try {
            if (input.replaceAll("\\s", "").isEmpty()) {
                throw new InputError("城市名稱不能為空白");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getName());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        try {
            weatherData = WeatherApp.getWeatherData(input);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(e.getClass().getName());
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        String weatherCondition = (String) weatherData.get("weather_condition");

        switch (weatherCondition) {
            case "晴朗":
                Image clearImage = new Image(imagePathBuilder("clear.png"));
                weatherIcon.setImage(clearImage);
                break;
            case "多雲":
                Image cloudyImage = new Image(imagePathBuilder("cloudy.png"));
                weatherIcon.setImage(cloudyImage);
                break;
            case "降雨":
                Image rainImage = new Image(imagePathBuilder("rain.png"));
                weatherIcon.setImage(rainImage);
                break;
            case "降雪":
                Image snowImage = new Image(imagePathBuilder("snow.png"));
                weatherIcon.setImage(snowImage);
                break;
        }

        double temperature = (double) weatherData.get("temperature");
        temperatureLabel.setText(temperature + " C");

        conditionLabel.setText(weatherCondition);

        long precipitationProb = (long) weatherData.get("precipitation_probability") ;
        precipitationProbLabel.setText(precipitationProb + "%");

        double windspeed = (double) weatherData.get("windspeed");
        windspeedLabel.setText(windspeed + " km/h");

        cityNameLabel.setText(cityInputCapitalize(input));
    }

    @FXML
    void profileBtnOnAction(ActionEvent event) {
        try {
            MainStage.changeScene("ProfileScene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String cityInputCapitalize(String cityName) {
        cityName = cityName.toLowerCase();
        String[] words = cityName.split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return capitalized.toString().trim();
    }

    private String imagePathBuilder(String fileName) {
        return Objects.requireNonNull(getClass().getResource("/images/" + fileName)).toExternalForm();
    }

}
