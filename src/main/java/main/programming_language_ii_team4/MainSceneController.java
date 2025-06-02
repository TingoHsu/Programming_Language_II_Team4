package main.programming_language_ii_team4;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {
    private JSONObject weatherData;

    @FXML
    private JFXToggleButton casualBtn;

    @FXML
    private TextField cityInput;

    @FXML
    private Label cityNameLabel;

    @FXML
    private Label conditionLabel;

    @FXML
    private JFXToggleButton formalBtn;

    @FXML
    private TextField lowerColorField;

    @FXML
    private Label precipitationProbLabel;

    @FXML
    private JFXToggleButton sportsBtn;

    @FXML
    private ToggleGroup styleBtnGroup;

    @FXML
    private Label temperatureLabel;

    @FXML
    private TextField upperColorField;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label windspeedLabel;

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
            alert.setTitle("錯誤");
            alert.setHeaderText("錯誤");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        try {
            weatherData = WeatherApp.getWeatherData(input);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("錯誤");
            alert.setHeaderText("錯誤");
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

        User.setWeatherCondition(weatherCondition);

        double temperature = (double) weatherData.get("temperature");
        temperatureLabel.setText(temperature + " C");
        User.setTemperature(temperature);

        conditionLabel.setText(weatherCondition);

        long precipitationProb = (long) weatherData.get("precipitation_probability") ;
        precipitationProbLabel.setText(precipitationProb + "%");
        User.setPrecipitationProb(precipitationProb);

        double windspeed = (double) weatherData.get("windspeed");
        windspeedLabel.setText(windspeed + " km/h");
        User.setWindspeed(windspeed);

        cityNameLabel.setText(cityInputCapitalize(input));
    }

    @FXML
    void profileBtnOnAction(ActionEvent event) {
        try {
            String style = getStyleSelection();
            String upperColor = upperColorField.getText();
            String lowerColor = lowerColorField.getText();
            User.setStyle(style);
            User.setUpperColor(upperColor);
            User.setLowerColor(lowerColor);
            MainStage.changeScene("ProfileScene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recommendBtnOnAction(ActionEvent event) {
        try {
            String style = getStyleSelection();
            if (style.isEmpty()) throw new ToggleButtonError("請選擇風格");
            String upperColor = upperColorField.getText();
            String lowerColor = lowerColorField.getText();
            User.setStyle(style);
            User.setUpperColor(upperColor);
            User.setLowerColor(lowerColor);
            MainStage.changeScene("RecommendScene.fxml");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("錯誤");
            alert.setHeaderText("錯誤");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private String getStyleSelection() {
        if (casualBtn.isSelected()) {
            return "casual";
        } else if (formalBtn.isSelected()) {
            return "formal";
        } else if (sportsBtn.isSelected()) {
            return "sports";
        } else {
            return "";
        }
    }

    private String cityInputCapitalize(String cityName) {
        cityName = cityName.toLowerCase();
        String[] words = cityName.split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        upperColorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                upperColorField.setText(oldValue);
            }
        });
        lowerColorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 3) {
                lowerColorField.setText(oldValue);
            }
        });

        if (!User.getCityInput().isEmpty()) {
            switch (User.getWeatherCondition()) {
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

            temperatureLabel.setText(User.getTemperature() + " C");

            conditionLabel.setText(User.getWeatherCondition());

            precipitationProbLabel.setText(User.getPrecipitationProb() + "%");

            windspeedLabel.setText(User.getWindspeed() + " km/h");

            cityNameLabel.setText(cityInputCapitalize(User.getCityInput()));
        }
        if (!User.getStyle().isEmpty()) {
            switch (User.getStyle()) {
                case "casual": casualBtn.setSelected(true); break;
                case "formal": formalBtn.setSelected(true); break;
                case "sports": sportsBtn.setSelected(true); break;
                default:
            }
        }
        if (!User.getUpperColor().isEmpty()) {upperColorField.setText(User.getUpperColor());}
        if (!User.getLowerColor().isEmpty()) {lowerColorField.setText(User.getLowerColor());}
    }
}
