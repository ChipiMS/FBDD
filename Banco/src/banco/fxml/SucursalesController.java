package banco.fxml;

import banco.Sucursal;
import database.MySQL;
import database.dao.SucursalDAO;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
public class SucursalesController implements Initializable {
    @FXML
    Button btnAgregar,btnModificar,btnBorrar;
    @FXML
    GridPane actions;
    @FXML
    TextField txtNombre,txtCiudad,txtDireccion;
    @FXML
    TableView<Sucursal> table;
    Boolean agregando = false;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        SucursalDAO sucursaldao = new SucursalDAO(db.getConnection());
        
        TableColumn nombres = new TableColumn("Número");
        nombres.setCellValueFactory(new PropertyValueFactory("numSucursal"));
        
        TableColumn apellidos = new TableColumn("Nombre");
        apellidos.setCellValueFactory(new PropertyValueFactory("nombreSucursal"));
        
        TableColumn telefono = new TableColumn("Ciudad");
        telefono.setCellValueFactory(new PropertyValueFactory("ciudad"));
        
        TableColumn domicilio = new TableColumn("Dirección");
        domicilio.setCellValueFactory(new PropertyValueFactory("direccion"));
        
        table.getColumns().addAll(nombres,apellidos,telefono,domicilio);
        table.setItems(sucursaldao.findAll());
        table.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = table.getSelectionModel().getSelectedItem();
                btnModificar.setDisable(false);
                btnBorrar.setDisable(false);
                txtNombre.setText(g.getNombreSucursal());
                txtCiudad.setText(g.getCiudad());
                txtDireccion.setText(g.getDireccion());
                actions.setVisible(true);
                agregando = false;
            }
        });
        btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(agregando){
                    if(txtNombre.getText().trim().length() > 0 && txtCiudad.getText().trim().length() > 0 && txtDireccion.getText().trim().length() > 0){
                        sucursaldao.insert(new Sucursal(txtNombre.getText(),txtCiudad.getText(),txtDireccion.getText()));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            table.setItems(sucursaldao.findAll());
                            agregando = false;
                            actions.setVisible(false);
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();
                        
                    }
                }
                else{
                    btnModificar.setDisable(true);
                    btnBorrar.setDisable(true);
                    txtNombre.setText("");
                    txtCiudad.setText("");
                    txtDireccion.setText("");
                    actions.setVisible(true);
                    agregando = true;
                }
            }
        });
        btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = table.getSelectionModel().getSelectedItem();
                if(txtNombre.getText().trim().length() > 0 && txtCiudad.getText().trim().length() > 0 && txtDireccion.getText().trim().length() > 0){
                    g.setNombreSucursal(txtNombre.getText());
                    g.setCiudad(txtCiudad.getText());
                    g.setDireccion(txtDireccion.getText());
                    if(sucursaldao.update(g)){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Sucursal modificado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            table.setItems(sucursaldao.findAll());
                            actions.setVisible(false);
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                }
                else{
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = table.getSelectionModel().getSelectedItem();
                if(sucursaldao.delete(g.getNumSucursal())){
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("Sucursal borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if(respuesta.get() == ButtonType.OK){
                        table.setItems(sucursaldao.findAll());
                        actions.setVisible(false);
                    }
                }
                else{
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
    }    
}
