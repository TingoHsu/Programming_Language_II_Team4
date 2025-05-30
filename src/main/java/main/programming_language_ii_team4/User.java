package main.programming_language_ii_team4;

public class User {
    private static String name;
    private static String password;
    private static String gender;

    public static String getName() {
        return name;
    }

    public static String getPassword() {
        return password;
    }

    public static String getGender() {
        return gender;
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
}
