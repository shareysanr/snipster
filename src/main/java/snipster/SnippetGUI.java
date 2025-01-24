package snipster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SnippetGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Input Snippet");
        TextField textField = new TextField();

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, textField);

        Scene scene = new Scene(layout, 300, 200);

        primaryStage.setTitle("Snippet GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
