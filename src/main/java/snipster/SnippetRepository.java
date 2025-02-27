package snipster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LuceneIndexer indexer = LuceneIndexer.getInstance()) {
            pstmt.setString(1, title);
            pstmt.setString(2, code);
            pstmt.setString(3, tags);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("THIS IS THE ID IN INSERT SNIPPET: " + id);
                indexer.indexSnippet(id, title, code, tags);
            }

            System.out.println("Snippet inserted");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static String readSnippet(int id) {
        String sql = "SELECT title FROM snippets WHERE id = ?";

        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("title");
            }
        } catch (Exception e) {
            System.out.println("Error reading ID");
            e.printStackTrace();
        }
        return null;
    }
    
    public static void printSnippets() {
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

    public static List<Snippet> readSnippets() {
        String sql = "SELECT id, title, code, tags FROM snippets";
        List<Snippet> snippets = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String code = rs.getString("code");
                String tags = rs.getString("tags");
                snippets.add(new Snippet(id, title, code, tags));
            }
        } catch (Exception e) {
            System.out.println("Error reading snippets.");
            e.printStackTrace();
        }
        return snippets;
    }

    public static void updateSnippet(int id, String title, String code, String tags) {
        String sql = "UPDATE snippets SET title = ?, code = ?, tags = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            LuceneIndexer indexer = LuceneIndexer.getInstance()) {
            pstmt.setString(1, title);
            pstmt.setString(2, code);
            pstmt.setString(3, tags);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();

            indexer.updateIndex(id, title, code, tags);

            System.out.println("Snippet updated");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static void deleteSnippet(int id) {
        String sql = "DELETE FROM snippets WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             LuceneIndexer indexer = LuceneIndexer.getInstance()) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            indexer.deleteIndex(id);

            System.out.println("Snippet deleted");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }

    public static void clearTable() {
        String sql = "DELETE FROM snippets";
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             LuceneIndexer indexer = LuceneIndexer.getInstance()) {
            stmt.executeUpdate(sql);

            indexer.clearIndex();

            System.out.println("Table cleared");
        } catch (Exception e) {
            System.out.println("Error clearing table.");
            e.printStackTrace();
        }
    }

    public static List<Snippet> importCSVSnippets(String file){
        List<Snippet> snippets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line = reader.readLine();

            while (line != null) {
                List<String> parts = splitLine(line);

                int id = Integer.parseInt(parts.get(0)); 
                String title = parts.get(1);
                String code = parts.get(2);
                code = code.replace("\\n", "\n");
                String tags = parts.get(3);

                snippets.add(new Snippet(id, title, code, tags));
                line = reader.readLine();
            }

            System.out.println("Snippets imported");
        } catch (Exception e) {
            System.out.println("Error importing snippets");
            e.printStackTrace();
        }
    
        return snippets;
    }

    private static List<String> splitLine(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        Boolean startPart = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"' && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                current.append(c);
                i++;
            } else if (c == '\"') {
                startPart = !startPart;
            } else if (c == ',' && !startPart) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        parts.add(current.toString());

        return parts;
    }

    public static void exportSnippets(String filePath) {
        String sql = "SELECT id, title, code, tags FROM snippets";

        try (Connection conn = DatabaseConnector.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            
            writer.write("ID,Title,Code,Tags");
            writer.newLine();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                title = title.replace("\"", "\"\"");
                String code = rs.getString("code");
                code = code.replace("\n", "\\n");
                code = code.replace("\"", "\"\"");
                String tags = rs.getString("tags");
                tags = tags.replace("\"", "\"\"");

                writer.write("\"" + id + "\",\"" + title + "\",\"" + code + "\",\"" + tags + "\"");
                writer.newLine();
            }

            System.out.println("Snippets exported to " + filePath);

        } catch (Exception e) {
            System.out.println("Error exporting snippets");
            e.printStackTrace();
        }
    }

}
