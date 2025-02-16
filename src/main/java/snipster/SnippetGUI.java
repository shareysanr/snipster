package snipster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class SnippetGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button createSnippetButton = new Button("Create Snippet");
        createSnippetButton.setOnAction(e -> createSnippetPage(primaryStage));

        Button goToReadSnippet = new Button("View Snippets");
        goToReadSnippet.setOnAction(e -> showSnippetsPage(primaryStage));

        Button viewFoldersButton = new Button("View Folders");
        viewFoldersButton.setOnAction(e -> viewFoldersPage(primaryStage));

        Button searchSnippetButton = new Button("Search Snippet");
        searchSnippetButton.setOnAction(e -> searchSnippetPage(primaryStage));

        Button updateSnippetButton = new Button("Update Snippet");
        updateSnippetButton.setOnAction(e -> updateSnippetPage(primaryStage));

        Button deleteSnippetButton = new Button("Delete Snippet");
        deleteSnippetButton.setOnAction(e -> deleteSnippetPage(primaryStage));

        Button exportSnippetButton = new Button("Export Snippets");
        exportSnippetButton.setOnAction(e -> exportSnippetsPage(primaryStage));

        VBox homeLayout = new VBox(10);
        homeLayout.getChildren().addAll(createSnippetButton, goToReadSnippet, viewFoldersButton,
            searchSnippetButton ,updateSnippetButton, deleteSnippetButton, exportSnippetButton);

        Scene homeScene = new Scene(homeLayout, 300, 300);

        primaryStage.setTitle("Home Page");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void exportSnippetsPage(Stage primaryStage) {
        Button exportButton = new Button("Export Snippets");

        Label fileLabel = new Label("Enter File Name:");
        TextField fileField = new TextField();

        Label output = new Label();
        exportButton.setOnAction(e -> {
            String fileName = fileField.getText().trim();
            if (fileName.isEmpty() || !fileName.toLowerCase().endsWith(".csv")) {
                output.setText("Invalid file Name");
            } else {
                SnippetRepository.exportSnippets(fileName);
                output.setText("Snippets exported to " + fileName);
            }
            fileField.setText("");
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(fileLabel, fileField, exportButton, output, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void searchSnippetPage(Stage primaryStage) {
        Label queryLabel = new Label("Enter Query:");
        TextField queryField = new TextField();

        Button searchButton = new Button("Search");
        Label output = new Label();

        ObservableList<Snippet> snippets = FXCollections.observableArrayList();
        ListView<Snippet> listView = new ListView<>(snippets);

        searchButton.setOnAction(e -> {
            String query = queryField.getText().trim();
            if (query.isEmpty()) {
                output.setText("Query Field Missing");
            } else {
                try (LuceneIndexer indexer = LuceneIndexer.getInstance()) {
                    List<Snippet> snippetList = indexer.searchSnippets(query);
                    snippets.setAll(snippetList);
                    output.setText("Snippets shown above");
                } catch (Exception ex) {
                    System.out.println("Error searching for snippet");
                    ex.printStackTrace();
                }
            }
        });

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Snippet s = listView.getSelectionModel().getSelectedItem();
                if (s != null) {
                    showSnippetDetails(primaryStage, s);
                }
            }
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(queryLabel, queryField, searchButton, listView, output, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }
        
    private void showSnippetsPage(Stage primaryStage) {
        List<Snippet> snippetList = SnippetRepository.readSnippets();
        ObservableList<Snippet> snippets = FXCollections.observableArrayList(snippetList);
        ListView<Snippet> listView = new ListView<>(snippets);

        Button doubleClickButton = new Button("Double Click");
        doubleClickButton.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                System.out.println("Double clicked button");
            }
        });

        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Snippet s = listView.getSelectionModel().getSelectedItem();
                if (s != null) {
                    showSnippetDetails(primaryStage, s);
                }
            }
        });

        Button printLuceneButton = new Button("Print Lucene Index");
        printLuceneButton.setOnAction(e -> {
            try (LuceneIndexer indexer = LuceneIndexer.getInstance()){
                //new LuceneIndexer("index").printAllSnippets();
                indexer.printAllSnippets();
            } catch (IOException ex) {
                System.out.println("Error printing Lucene index");
                ex.printStackTrace();
            }
        });

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(listView, doubleClickButton, printLuceneButton, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void viewFoldersPage(Stage primaryStage) {
        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void showSnippetDetails(Stage primaryStage, Snippet snippet) {
        Label titleLabel = new Label("Title:\n" + snippet.getTitle());
        Label codeLabel = new Label("Code:\n" + snippet.getCode());
        Label tagsLabel = new Label("Tags:\n" + snippet.getTags());

        Button backButton = new Button("Back to Snippets");
        backButton.setOnAction(e -> showSnippetsPage(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, codeLabel, tagsLabel, backButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
    }

    private void createSnippetPage(Stage primaryStage) {
        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();

        Label codeLabel = new Label("Code: ");
        TextArea codeArea = new TextArea();
        codeArea.setWrapText(false);

        Label tagsLabel = new Label("Tags: ");
        TextField tagsField = new TextField();

        Button submit = new Button("Submit");
        Label output = new Label("");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));
        
        submit.setOnAction(e -> {
            String title = titleField.getText().trim();
            String code = codeArea.getText().trim();
            String tags = tagsField.getText().trim();
    
            if (title.isEmpty() || code.isEmpty() || tags.isEmpty()) {
                output.setText("At least one field is missing");
            } else {
                SnippetRepository.insertSnippet(title, code, tags);
                output.setText("Snippet added successfully");
                titleField.setText("");
                codeArea.setText("");
                tagsField.setText("");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleField,
            codeLabel, codeArea,
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
        TextArea codeArea = new TextArea();
        codeArea.setWrapText(false);

        Label tagsLabel = new Label("Tags: ");
        TextField tagsField = new TextField();

        Button submit = new Button("Update");
        Label output = new Label("");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> start(primaryStage));
        
        submit.setOnAction(e -> {
            String idString = idField.getText().trim();
            String title = titleField.getText().trim();
            String code = codeArea.getText().trim();
            String tags = tagsField.getText().trim();
    
            if (idString.isEmpty() || title.isEmpty() || code.isEmpty() || tags.isEmpty()) {
                output.setText("At least one field is missing");
            } else {
                int id = Integer.parseInt(idString);
                SnippetRepository.updateSnippet(id, title, code, tags);
                output.setText("Snippet updated successfully");
                idField.setText("");
                titleField.setText("");
                codeArea.setText("");
                tagsField.setText("");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(idLabel, idField,
            titleLabel, titleField,
            codeLabel, codeArea,
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
