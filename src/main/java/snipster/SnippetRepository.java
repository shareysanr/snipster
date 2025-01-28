package snipster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SnippetRepository {
    public static void createTable() {
        String sql = """
                        CREATE TABLE IF NOT EXISTS snippets (
                            id INTEGER PRIMARY KEY AUTOINCREMENT, 
                            title TEXT NOT NULL, 
                            code TEXT NOT NULL, 
                            tags TEXT
                        )
                    """;
        try (Connection conn = DatabaseConnector.connect();
            Statement stmt = conn.createStatement()){

            System.out.println("Connected to the database.");
            stmt.execute(sql);
            System.out.println("Table created or already exists");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static void insertSnippet(String title, String code, String tags) {
        String sql = "INSERT INTO snippets (title, code, tags) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, code);
            pstmt.setString(3, tags);
            pstmt.executeUpdate();
            System.out.println("Snippet inserted");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }
    
    public static void readSnippets() {
        String sql = "SELECT * FROM snippets";
        try (Connection conn = DatabaseConnector.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Code: " + rs.getString("code"));
                System.out.println("Tags: " + rs.getString("tags"));
                System.out.println("-----------");
            }
            System.out.println("Snippets read");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static List<String> readSnippetTitles() {
        String sql = "SELECT title FROM snippets";
        List<String> titles = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch (Exception e) {
            System.out.println("Error reading snippets");
            e.printStackTrace();
        }
        return titles;
    }

    public static void updateSnippet(int id, String title, String code, String tags) {
        String sql = "UPDATE snippets SET title = ?, code = ?, tags = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, code);
            pstmt.setString(3, tags);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Snippet updated");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static void deleteSnippet(int id) {
        String sql = "DELETE FROM snippets WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Snippet deleted");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static void clearTable() {
        String sql = "DELETE FROM snippets";
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table cleared");
        } catch (Exception e) {
            System.out.println("Error clearing table.");
            e.printStackTrace();
        }
    }
}
