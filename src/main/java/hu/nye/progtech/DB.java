package hu.nye.progtech;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/highscore"; // Az adatbázis nevével helyettesítsd
    private static final String USER = "root"; // A MySQL felhasználóneveddel helyettesítsd
    private static final String PASSWORD = ""; // A MySQL jelszavaddal helyettesítsd

    public static void main(String[] args) {
        readDataFromUsersTable();
        insertDataToUsernamesTable(1, 100, "John Doe");
    }

    public static void readDataFromUsersTable() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM usernames";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String userName = resultSet.getString("user_name");
                        System.out.println("ID: " + id + ", User Name: " + userName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void insertDataToUsernamesTable(int id, int points, String name) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO usernames (ID, points, name) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, points);
                preparedStatement.setString(3, name);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Adatok sikeresen beszúrva.");
                } else {
                    System.out.println("Adatok beszúrása sikertelen.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
