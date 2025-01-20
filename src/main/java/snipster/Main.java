package snipster;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnector.connect()){
            System.out.println("Connected to the database.");
        } catch (Exception e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }
    
}
