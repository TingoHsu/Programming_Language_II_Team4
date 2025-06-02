package main.programming_language_ii_team4;

import javafx.scene.control.Alert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) throws LocationNotFoundError {
        JSONArray locationData = getLocationData(locationName);
        String urlString = getString(locationData);

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ApiError");
                alert.setContentText("無法連接到API");
                alert.showAndWait();
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());

                while (sc.hasNext()) {
                    resultJson.append(sc.nextLine());
                }

                sc.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

                JSONArray time = (JSONArray) hourly.get("time");
                
                int index = findIndexOfCurrentTime(time);

                JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
                double temperature = (double) temperatureData.get(index);

                JSONArray weathercode = (JSONArray) hourly.get("weather_code");
                String weatherCondition = convertWeatherCode((long)weathercode.get(index));

                JSONArray precipitationProbData = (JSONArray) hourly.get("precipitation_probability");
                long precipitationProbability = (long) precipitationProbData.get(index);

                JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
                double windspeed = (double) windspeedData.get(index);

                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("precipitation_probability", precipitationProbability);
                weatherData.put("windspeed", windspeed);

                return weatherData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getString(JSONArray locationData) throws LocationNotFoundError {
        if (locationData == null) {
            throw new LocationNotFoundError("找不到此城市");
        }
        JSONObject location = (JSONObject) locationData.getFirst();
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        return "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,weather_code,precipitation_probability,wind_speed_10m&timezone=Asia%2FSingapore";
    }

    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if (weathercode == 0L) {
            weatherCondition = "晴朗";
        } else if (weathercode > 0L && weathercode <= 3L) {
            weatherCondition = "多雲";
        } else if ((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)) {
            weatherCondition = "降雨";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "降雪";
        }

        return weatherCondition;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {return i;}
        }

        return 0;
    }

    private static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        return currentDateTime.format(formatter);
    }

    private static JSONArray getLocationData(String locationName) {
        locationName = locationName.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);

            if (conn.getResponseCode() != 200) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ApiError");
                alert.setContentText("無法連接到API");
                alert.showAndWait();
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());

                while (sc.hasNext()) {
                    resultJson.append(sc.nextLine());
                }

                sc.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                return (JSONArray) resultJsonObj.get("results");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
