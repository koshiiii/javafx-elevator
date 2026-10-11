package sk.koshi.elevator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElevatorApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ElevatorApplication.class.getResource("elevator-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 920, 660);

        stage.setTitle("JavaFX výťah");
        stage.setMinWidth(820);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
