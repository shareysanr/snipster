package snipster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SnippetGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Input Snippet");
        TextField textField = new TextField();

        Button button = new Button("Enter Snippet Title");
        Label outputLabel = new Label();

        button.setOnAction(e -> {
            String input = textField.getText();
            outputLabel.setText("Snippet Title: " + input);
            textField.setText("");
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField, button, outputLabel);

        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Snippet GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
