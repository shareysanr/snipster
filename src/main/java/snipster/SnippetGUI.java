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

    private String snippetEdit = null;

    @Override
    public void start(Stage primaryStage) {
        Button goToSnippetManagement = new Button("Manage Snippets");
        goToSnippetManagement.setOnAction(e -> showSnippetManagementPage(primaryStage));

        Button goToReadSnippet = new Button("View Snippets");
        goToReadSnippet.setOnAction(e -> showSnippetsPage(primaryStage));

        Button createSnippetButton = new Button("Create Snippet");
        createSnippetButton.setOnAction(e -> createSnippetPage(primaryStage));

        Button updateSnippetButton = new Button("Update Snippet");
        updateSnippetButton.setOnAction(e -> updateSnippetPage(primaryStage));

        VBox homeLayout = new VBox(10);
        homeLayout.getChildren().addAll(goToSnippetManagement, goToReadSnippet, createSnippetButton, updateSnippetButton);

        Scene homeScene = new Scene(homeLayout, 300, 300);

        primaryStage.setTitle("Home Page");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    private void showSnippetManagementPage(Stage primaryStage) {
        Label label = new Label("Input Snippet");
        TextField textField = new TextField();

        Button button = new Button("Enter Snippet Title");
        Button removeButton = new Button("Remove Selected");
        Button editButton = new Button("Edit selected");
        Button clearAllButton = new Button("Clear All Snippets");
        Button homeButton = new Button("Back to Home");

        homeButton.setOnAction(e -> start(primaryStage));

        Label outputLabel = new Label();
        

        ObservableList<String> snippets = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(snippets);

        button.setOnAction(e -> {
            String input = textField.getText().trim();
            if (input.equals("")) {
                outputLabel.setText("Enter a valid snippet title");
            } else {
                if (snippetEdit == null) {
                    snippets.add(input);
                    outputLabel.setText("Snippet Title: " + input);
                } else {
                    int idx = snippets.indexOf(snippetEdit);
                    snippets.set(idx, input);
                    outputLabel.setText("Snippet updated to: " + input);
                }
                
            }
            textField.setText("");
        });

        removeButton.setOnAction(e -> {
            String selectedSnippet = listView.getSelectionModel().getSelectedItem();
            if (selectedSnippet != null) {
                snippets.remove(selectedSnippet);
                outputLabel.setText("Snippet removed");
            } else {
                outputLabel.setText("No snippet selected");
            }
        });

        editButton.setOnAction(e -> {
            String selectedSnippet = listView.getSelectionModel().getSelectedItem();
            if (selectedSnippet != null) {
                textField.setText(selectedSnippet);
                snippetEdit = selectedSnippet;
                outputLabel.setText("Editing snippet: " + selectedSnippet);
            } else {
                outputLabel.setText("No snippet selected");
            }
        });

        clearAllButton.setOnAction(e -> {
            snippets.clear();
            snippetEdit = null;
            outputLabel.setText("All snippets cleared.");
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, button, outputLabel, 
            listView, removeButton, editButton, homeButton, clearAllButton);

        Scene scene = new Scene(layout, 400, 400);

        primaryStage.setTitle("Snippet GUI");
        primaryStage.setScene(scene);
    }

    private void showSnippetsPage(Stage primaryStage) {
        List<Integer> ids = SnippetRepository.readSnippetIds();
        List<String> titles = SnippetRepository.readSnippetTitles();

        List<String> snippetList = new ArrayList<>();
        for (int i = 0; i < Math.min(ids.size(), titles.size()); i++) {
            snippetList.add(ids.get(i) + " - " + titles.get(i));
        }

        ObservableList<String> snippets = FXCollections.observableArrayList(snippetList);
        ListView<String> listView = new ListView<>(snippets);

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

    public static void main(String[] args) {
        launch(args);
    }
    
}
