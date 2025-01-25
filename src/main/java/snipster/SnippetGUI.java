package snipster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class SnippetGUI extends Application {

    private String snippetEdit = null;

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Input Snippet");
        TextField textField = new TextField();

        Button button = new Button("Enter Snippet Title");
        Button removeButton = new Button("Remove Selected");
        Button editButton = new Button("Edit selected");

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

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, button, outputLabel, listView, removeButton, editButton);

        Scene scene = new Scene(layout, 350, 400);

        primaryStage.setTitle("Snippet GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
