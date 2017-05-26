package banco.fxml;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
public class HomeController implements Initializable {
    @FXML
    Button btnEmpleados,btnSucursales,btnClientes,btnCuentas,btnTransacciones;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEmpleados.setOnAction(listener);
        btnSucursales.setOnAction(listener);
        btnClientes.setOnAction(listener);
        btnCuentas.setOnAction(listener);
        btnTransacciones.setOnAction(listener);
    }
    EventHandler<ActionEvent> listener = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event) {
            // EMPLEADOS
            if(event.getSource() == btnEmpleados){
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource("Empleado.fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // SUCURSALES
            if(event.getSource() == btnSucursales){
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource("Sucursales.fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*if(event.getSource() == btnClientes){
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource(".fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            /*if(event.getSource() == btnCuentas){
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource(".fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            /*if(event.getSource() == btnTransacciones){
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource(".fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    };
}
