/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Empleado;
import database.MySQL;
import database.dao.EmpleadoDAO;
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

/**
 * FXML Controller class
 *
 * @author aldoea
 */
public class EmpleadoController implements Initializable {

    @FXML
    Button btnAgregar,btnModificar,btnBorrar;
    @FXML
    GridPane actions;
    @FXML
    TextField txtNombre,txtTelefono,txtNumSucursal;
    @FXML
    TableView<Empleado> table;
    Boolean agregando = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        EmpleadoDAO empleadodao = new EmpleadoDAO(db.getConnection());
        
        TableColumn nombres = new TableColumn("Número");
        nombres.setCellValueFactory(new PropertyValueFactory("numEmpleado"));
        
        TableColumn apellidos = new TableColumn("Nombre");
        apellidos.setCellValueFactory(new PropertyValueFactory("nombreEmpleado"));
        
        TableColumn telefono = new TableColumn("Telefono");
        telefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        
        TableColumn domicilio = new TableColumn("Sucursal");
        domicilio.setCellValueFactory(new PropertyValueFactory("numSucursal"));
        
        table.getColumns().addAll(nombres,apellidos,telefono,domicilio);
        table.setItems(empleadodao.findAll());
        table.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Empleado g = table.getSelectionModel().getSelectedItem();
                btnModificar.setDisable(false);
                btnBorrar.setDisable(false);
                txtNombre.setText(g.getNombreEmpleado());
                txtTelefono.setText(g.getTelefono());
                txtNumSucursal.setText(g.getNumSucursal());
                actions.setVisible(true);
                agregando = false;
            }
        });
        btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(agregando){
                    if(txtNombre.getText().trim().length() > 0 && txtTelefono.getText().trim().length() > 0 && txtNumSucursal.getText().trim().length() > 0){
                        empleadodao.insert(new Empleado(txtNombre.getText(),txtTelefono.getText(),txtNumSucursal.getText()));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Empleado");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            table.setItems(empleadodao.findAll());
                            agregando = false;
                            actions.setVisible(false);
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Empleado");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();
                        
                    }
                }
                else{
                    btnModificar.setDisable(true);
                    btnBorrar.setDisable(true);
                    txtNombre.setText("");
                    txtTelefono.setText("");
                    txtNumSucursal.setText("");
                    actions.setVisible(true);
                    agregando = true;
                }
            }
        });
        btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Empleado g = table.getSelectionModel().getSelectedItem();
                if(txtNombre.getText().trim().length() > 0 && txtTelefono.getText().trim().length() > 0 && txtNumSucursal.getText().trim().length() > 0){
                    g.setNombreEmpleado(txtNombre.getText());
                    g.setTelefono(txtTelefono.getText());
                    g.setNumSucursal(txtNumSucursal.getText());
                    if(empleadodao.update(g)){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Empleado");
                        msg.setContentText("Empleado modificado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            table.setItems(empleadodao.findAll());
                            actions.setVisible(false);
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Empleado");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                }
                else{
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Empleado");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Empleado g = table.getSelectionModel().getSelectedItem();
                if(empleadodao.delete(g.getNumEmpleado())){
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Empleado");
                    msg.setContentText("Empleado borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if(respuesta.get() == ButtonType.OK){
                        table.setItems(empleadodao.findAll());
                        actions.setVisible(false);
                    }
                }
                else{
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Empleado");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
    }   // END Initialize        
} // END EmpleadoController
