package snipster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.List;
import java.util.ArrayList;

public class SnippetGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button createSnippetButton = new Button("Create Snippet");
        createSnippetButton.setOnAction(e -> createSnippetPage(primaryStage));

        Button goToReadSnippet = new Button("View Snippets");
        goToReadSnippet.setOnAction(e -> showSnippetsPage(primaryStage));

        Button searchSnippetButton = new Button("Search Snippet");
        searchSnippetButton.setOnAction(e -> searchSnippetPage(primaryStage));

        Button updateSnippetButton = new Button("Update Snippet");
        updateSnippetButton.setOnAction(e -> updateSnippetPage(primaryStage));

        Button deleteSnippetButton = new Button("Delete Snippet");
        deleteSnippetButton.setOnAction(e -> deleteSnippetPage(primaryStage));

        VBox homeLayout = new VBox(10);
        homeLayout.getChildren().addAll(createSnippetButton, goToReadSnippet,
            searchSnippetButton ,updateSnippetButton, deleteSnippetButton);

        Scene homeScene = new Scene(homeLayout, 300, 300);

        primaryStage.setTitle("Home Page");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void searchSnippetPage(Stage primaryStage) {
        Label idLabel = new Label("Enter Snippet ID:");
        TextField idField = new TextField();

        Button searchButton = new Button("Search");
        Label output = new Label();

        searchButton.setOnAction(e -> {
            String idString = idField.getText().trim();
            if (idString.isEmpty()) {
                output.setText("ID field is missing");
            } else {
                int id = Integer.parseInt(idString);
                String title = SnippetRepository.readSnippet(id);
                if (title == null) {
                    output.setText("Snippet not found for id: " + id);
                } else {
                    output.setText("Title: " + title);
                }
                idField.setText("");
            }
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(idLabel, idField, searchButton, output, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }
        
    private void showSnippetsPage(Stage primaryStage) {
        List<Snippet> snippetList = SnippetRepository.readSnippets();
        ObservableList<Snippet> snippets = FXCollections.observableArrayList(snippetList);
        ListView<Snippet> listView = new ListView<>(snippets);

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(listView, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void createSnippetPage(Stage primaryStage) {
        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();

        Label codeLabel = new Label("Code: ");
        TextField codeField = new TextField();

        Label tagsLabel = new Label("Tags: ");
        TextField tagsField = new TextField();

        Button submit = new Button("Submit");
        Label output = new Label("");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));
        
        submit.setOnAction(e -> {
            String title = titleField.getText().trim();
            String code = codeField.getText().trim();
            String tags = tagsField.getText().trim();
    
            if (title.isEmpty() || code.isEmpty() || tags.isEmpty()) {
                output.setText("At least one field is missing");
            } else {
                SnippetRepository.insertSnippet(title, code, tags);
                output.setText("Snippet added successfully");
                titleField.setText("");
                codeField.setText("");
                tagsField.setText("");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleField,
            codeLabel, codeField,
            tagsLabel, tagsField,
            submit, output,
            backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }


    private void updateSnippetPage(Stage primaryStage) {
        Label idLabel = new Label("ID: ");
        TextField idField = new TextField();

        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();

        Label codeLabel = new Label("Code: ");
        TextField codeField = new TextField();

        Label tagsLabel = new Label("Tags: ");
        TextField tagsField = new TextField();

        Button submit = new Button("Update");
        Label output = new Label("");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));
        
        submit.setOnAction(e -> {
            String idString = idField.getText().trim();
            String title = titleField.getText().trim();
            String code = codeField.getText().trim();
            String tags = tagsField.getText().trim();
    
            if (idString.isEmpty() || title.isEmpty() || code.isEmpty() || tags.isEmpty()) {
                output.setText("At least one field is missing");
            } else {
                int id = Integer.parseInt(idString);
                SnippetRepository.updateSnippet(id, title, code, tags);
                output.setText("Snippet updated successfully");
                idField.setText("");
                titleField.setText("");
                codeField.setText("");
                tagsField.setText("");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(idLabel, idField,
            titleLabel, titleField,
            codeLabel, codeField,
            tagsLabel, tagsField,
            submit, output,
            backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void deleteSnippetPage(Stage primaryStage) {
        Label idLabel = new Label("ID: ");
        TextField idField = new TextField();

        Button submit = new Button("Delete");
        Label output = new Label("");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        submit.setOnAction(e -> {
            String idString = idField.getText().trim();
            if (idString.isEmpty()) {
                output.setText("ID field is missing");
            } else {
                int id = Integer.parseInt(idString);
                SnippetRepository.deleteSnippet(id);
                output.setText("Snippet deleted successfully");
                idField.setText("");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(idLabel, idField,
            submit, output,
            backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}
