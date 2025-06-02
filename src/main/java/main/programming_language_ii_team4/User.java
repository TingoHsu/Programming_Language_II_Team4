package main.programming_language_ii_team4;

public class User {
    private static String name="";
    private static String password="";
    private static String gender="";
    private static String cityInput="";
    private static String style="";
    private static String upperColor="";
    private static String lowerColor="";
    private static String weatherCondition="";
    private static double temperature=26;
    private static long precipitationProb=0;
    private static double windspeed=0;
    public static String background="";

    public static String getName() {
        return name;
    }

    public static String getPassword() {
        return password;
    }

    public static String getGender() {
        return gender;
    }

    public static String getCityInput() {
        return cityInput;
    }

    public static String getStyle() {
        return style;
    }

    public static String getUpperColor() {
        return upperColor;
    }

    public static String getLowerColor() {
        return lowerColor;
    }

    public static String getWeatherCondition() {
        return weatherCondition;
    }

    public static double getTemperature() {
        return temperature;
    }

    public static long getPrecipitationProb() {
        return precipitationProb;
    }

    public static double getWindspeed(){
        return windspeed;
    }

    public static String getBackground() {
        return background;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static void setGender(String gender) {
        User.gender = gender;
    }

    public static void setCityInput(String cityInput) {
        User.cityInput = cityInput;
    }

    public static void setStyle(String style) {
        User.style = style;
    }

    public static void setUpperColor(String upperColor) {
        User.upperColor = upperColor;
    }

    public static void setLowerColor(String lowerColor) {
        User.lowerColor = lowerColor;
    }

    public static void setWeatherCondition(String weatherCondition) {
        User.weatherCondition = weatherCondition;
    }

    public static void setTemperature(double temperature) {
        User.temperature = temperature;
    }

    public static void setPrecipitationProb(long precipitationProb) {
        User.precipitationProb = precipitationProb;
    }

    public static void setWindspeed(double windspeed) {
        User.windspeed = windspeed;
    }

    public static void setBackground (String background) {
        User.background = background;
    }

    public static void clearAll() {
        User.name = "";
        User.password = "";
        User.gender = "";
        User.cityInput = "";
        User.style = "";
        User.upperColor = "";
        User.lowerColor = "";
        User.weatherCondition = "多雲";
        User.temperature = 26;
        User.precipitationProb = 0;
        User.windspeed = 0;
        User.background = "";
    }


}
