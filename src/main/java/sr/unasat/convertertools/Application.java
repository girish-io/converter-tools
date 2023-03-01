package sr.unasat.convertertools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sr.unasat.convertertools.constants.ApplicationInfo;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("layout/converter.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);

        stage.setTitle(ApplicationInfo.WINDOW_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}