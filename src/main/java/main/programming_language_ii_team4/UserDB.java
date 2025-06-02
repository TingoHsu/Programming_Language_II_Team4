package main.programming_language_ii_team4;

import java.sql.*;

public class UserDB {
    private final String server = "jdbc:mysql://140.119.19.73:3315/";
    private final String database = "TG04"; // change to your own database
    private final String url = server + database + "?useSSL=false";
    private final String dbUser = "TG04";// change to your own username
    private final String dbPassword = "XFvnCp";// change to your own password

    private Connection conn;

    public UserDB() {
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPassword);

            //Write the sql query to create a table with 3 columns: id (as primarykey), username, and password_hash
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" + "id INT AUTO_INCREMENT PRIMARY KEY, " + "username VARCHAR(255) UNIQUE, " + "password_hash VARCHAR(255), " + "gender VARCHAR(10))";

            conn.createStatement().execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password, String gender) throws Exception {
        if (username.isEmpty()) throw new UserError("帳號名稱不能為空白");
        if (password.length() != 8) throw new PasswordError("密碼應為8個字");
        if (!password.matches(".*[a-zA-Z].*")) throw new PasswordError("密碼必須至少包含1個字母");
        if (!checkUserExist(username) && gender == null) throw new GenderError("尚未選擇性別");

        String hash = customHash(password);

        //write the sql query to insert user information into the table
        //hint: use PreparedStatement and executeUpdate() to complete the insertion

        String query = "INSERT INTO users (username, password_hash, gender, color) VALUES (?, ?, ?, \"white\")";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hash);
            stmt.setString(3, gender);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                throw new UserError("用戶名稱已存在");
            }
            throw new Exception("資料庫錯誤");
        }
    }

    public void login(String username, String password) throws Exception {
        //write the sql query to select the password hash for the given username
        //hint: use PreparedStatement to perform the lookup, and name the ResultSet asrs for use in the code below
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        //check if the username exists
        if (!rs.next()) throw new UserError("找不到使用者");
        String dbHash = rs.getString("password_hash");
        String inputHash = customHash(password);
        if (!dbHash.equals(inputHash)) throw new PasswordError("密碼錯誤");
        rs.close();
        stmt.close();
    }

    public void setName(String oldName, String newName) throws Exception {
        if (newName.isEmpty()) throw new UserError("帳號名稱不能為空白");
        if (checkUserExist(newName)) throw new UserError("用戶名稱已存在");
        String query = "UPDATE users SET username = ? WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newName);
            stmt.setString(2, oldName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setPassword(String oldPassword, String newPassword) throws Exception {
        if (newPassword.length() != 8) throw new PasswordError("密碼應為8個字");
        if (!newPassword.matches(".*[a-zA-Z].*")) throw new PasswordError("密碼必須至少包含1個字母");
        String query = "UPDATE users SET password_hash = ? WHERE password_hash = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customHash(newPassword));
            stmt.setString(2, customHash(oldPassword));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setGender(String name, String newGender) throws Exception {
        if (newGender == null) throw new GenderError("尚未選擇性別");
        String query = "UPDATE users SET gender = ? WHERE username = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newGender);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String name) {
        String query = "DELETE FROM users WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String customHash(String pw){
        StringBuilder sb = new StringBuilder(pw);
        for (int i=0; i<sb.length(); i++) {
            char ch = sb.charAt(i);
            if (Character.isLetter(ch)){
                if (ch == 'z' || ch == 'Z') {
                    sb.setCharAt(i, (char) ((int) ch - 25));
                } else {
                    sb.setCharAt(i, (char) ((int) ch + 1));
                }
            }
        }
        int sum1=0, sum2=0;
        for (int i=0; i<sb.length(); i++) {
            char ch = sb.charAt(i);
            if (Character.isLetter(ch)) {
                int pos = (int) ch - 64;
                while (pos > 0) {
                    int digit = pos % 10;
                    sum1 += digit;
                    pos /= 10;
                }
            }
        }
        for (int i=0; i<Integer.toString(sum1).length(); i++) {
            while (sum1>0) {
                int digit = sum1%10;
                sum2+=digit;
                sum1/=10;
            }
        }
        sb.append(sum2);
        return sb.toString();
    }

    public boolean checkUserExist (String name) {
        boolean exist;
        String query = "SELECT username FROM users WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            exist = rs.next();
            stmt.close();
            rs.close();
            return exist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserGender(String name) {
        String gender = "";
        String query = "SELECT gender FROM users WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                gender = rs.getString("gender");
            }
            stmt.close();
            rs.close();
            return gender;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUpper() {
        String query = "SELECT * FROM clothes WHERE TempMin < ? AND TempMax >= ? AND style = ? AND type = \"top\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, User.getTemperature());
            stmt.setDouble(2, User.getTemperature());
            stmt.setString(3, User.getStyle());
            ResultSet rs = stmt.executeQuery();
            String clothing="";
            while (rs.next()) {
                if (rs.getString("Gender") != null) {
                    if (rs.getString("Gender").equals(User.getGender())) {
                        clothing = rs.getString("clothing_item");
                        break;
                    }
                } else {
                    clothing = rs.getString("clothing_item");
                    break;
                }
            }
            stmt.close();
            rs.close();
            return clothing;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getLower() {
        String query = "SELECT * FROM clothes WHERE TempMin < ? AND TempMax >= ? AND style = ? AND type = \"bottom\"";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, User.getTemperature());
            stmt.setDouble(2, User.getTemperature());
            stmt.setString(3, User.getStyle());
            ResultSet rs = stmt.executeQuery();
            String clothing="";
            while (rs.next()) {
                if (rs.getString("Gender") != null) {
                    if (rs.getString("Gender").equals(User.getGender())) {
                        clothing = rs.getString("clothing_item");
                        break;
                    }
                } else {
                    clothing = rs.getString("clothing_item");
                    break;
                }
            }
            stmt.close();
            rs.close();
            return clothing;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getShoes() {
        String query = "SELECT * FROM OutfitRules WHERE TempMin < ? AND TempMax >= ? AND RainMin <= ? AND RainMax >= ? AND style = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, User.getTemperature());
            stmt.setDouble(2, User.getTemperature());
            stmt.setLong(3, User.getPrecipitationProb());
            stmt.setLong(4, User.getPrecipitationProb());
            stmt.setString(5, User.getStyle());
            ResultSet rs = stmt.executeQuery();
            String shoes="";
            while (rs.next()) {
                if (rs.getString("Gender") != null) {
                    if (rs.getString("Gender").equals(User.getGender())) {
                        shoes = rs.getString("Shoes");
                        break;
                    }
                } else {
                    shoes = rs.getString("Shoes");
                    break;
                }
            }
            stmt.close();
            rs.close();
            return shoes;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAccessory() {
        String query = "SELECT * FROM OutfitRules WHERE TempMin < ? AND TempMax >= ? AND RainMin <= ? AND RainMax >= ? AND style = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, User.getTemperature());
            stmt.setDouble(2, User.getTemperature());
            stmt.setLong(3, User.getPrecipitationProb());
            stmt.setLong(4, User.getPrecipitationProb());
            stmt.setString(5, User.getStyle());
            ResultSet rs = stmt.executeQuery();
            String accessory="";
            while (rs.next()) {
                if (rs.getString("Gender") != null) {
                    if (rs.getString("Gender").equals(User.getGender())) {
                        accessory = rs.getString("Accessory");
                        break;
                    }
                } else {
                        accessory = rs.getString("Accessory");
                        break;
                }
            }
            stmt.close();
            rs.close();
            return accessory;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getColor() {
        String color = "";
        String query = "SELECT Color FROM users WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, User.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                color = rs.getString("Color");
            }
            stmt.close();
            rs.close();
            return color;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateColor() {
        String query = "UPDATE users SET Color = ? WHERE username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, User.getBackground());
            stmt.setString(2, User.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

class UserError extends Exception {
    public UserError(String Error){
        super(Error);
    }
}
class PasswordError extends Exception {
    public PasswordError(String Error){
        super(Error);
    }
}
class GenderError extends Exception {
    public GenderError(String Error){
        super(Error);
    }
}
class InputError extends Exception {
    public InputError(String Error){
        super(Error);
    }
}
class LocationNotFoundError extends Exception {
    public LocationNotFoundError(String Error){
        super(Error);
    }
}
class ToggleButtonError extends Exception {
    public ToggleButtonError(String Error){
        super(Error);
    }
}