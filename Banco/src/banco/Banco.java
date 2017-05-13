package banco;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class Banco extends Application {
    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader();
        Parent fxmlHome = null;
        try{
            fxmlHome = loader.load(getClass().getResource("fxml/Home.fxml"));
        }catch(IOException e){
            e.printStackTrace();
        }
        
        StackPane root = new StackPane();
        root.getChildren().add(fxmlHome);
        
        Scene scene = new Scene(root, 500, 500);
        
        primaryStage.setTitle("Banco");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
