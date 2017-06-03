/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Empleado;
import banco.Sucursal;
import database.MySQL;
import database.dao.CuentaDAO;
import database.dao.EmpleadoDAO;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
        Button btnAgregarEmpleados,btnModificarEmpleados,btnBorrarEmpleados;
        @FXML
        GridPane actionsEmpleados;
        @FXML
        TextField txtNombreEmpleados,txtTelefonoEmpleados;
        @FXML
        ComboBox<Sucursal> cmbSucursalEmpleados;
        @FXML
        TableView<Empleado> tableEmpleados;
        Boolean agregandoEmpleados = false;
        List<Sucursal> sucursales;
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            MySQL db = new MySQL();
            db.Connect();
            EmpleadoDAO empleadodao = new EmpleadoDAO(db.getConnection());

            TableColumn nombresEmpleados = new TableColumn("Número");
            nombresEmpleados.setCellValueFactory(new PropertyValueFactory("numEmpleado"));

            TableColumn apellidosEmpleados = new TableColumn("Nombre");
            apellidosEmpleados.setCellValueFactory(new PropertyValueFactory("nombreEmpleado"));

            TableColumn telefonoEmpleados = new TableColumn("Telefono");
            telefonoEmpleados.setCellValueFactory(new PropertyValueFactory("telefono"));

            TableColumn domicilioEmpleados = new TableColumn("Sucursal");
            domicilioEmpleados.setCellValueFactory(new PropertyValueFactory("numSucursal"));
            
            tableEmpleados.getColumns().addAll(nombresEmpleados,apellidosEmpleados,telefonoEmpleados,domicilioEmpleados);
            tableEmpleados.setItems(empleadodao.findAll());
            
            CuentaDAO cuenta = new CuentaDAO(db.getConnection());
            sucursales = cuenta.findAllSuc();
            cmbSucursalEmpleados.getItems().addAll(sucursales);
            
            tableEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Empleado g = tableEmpleados.getSelectionModel().getSelectedItem();
                    if(g == null)
                        return;
                    btnModificarEmpleados.setDisable(false);
                    btnBorrarEmpleados.setDisable(false);
                    txtNombreEmpleados.setText(g.getNombreEmpleado());
                    txtTelefonoEmpleados.setText(g.getTelefono());
                    for(int i=0;i<sucursales.size();i++){
                        if(g.getNumSucursal().equals(sucursales.get(i).getNumSucursal()+"")){
                            cmbSucursalEmpleados.getSelectionModel().clearAndSelect(i);
                        }
                    }
                    actionsEmpleados.setVisible(true);
                    agregandoEmpleados = false;
                }
            });
            btnAgregarEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(agregandoEmpleados){
                        if(txtNombreEmpleados.getText().trim().length() > 0 && txtTelefonoEmpleados.getText().trim().length() > 0 && cmbSucursalEmpleados.getSelectionModel().getSelectedItem() != null){
                            empleadodao.insert(new Empleado(txtNombreEmpleados.getText(),txtTelefonoEmpleados.getText(),cmbSucursalEmpleados.getSelectionModel().getSelectedItem().getNumSucursal()+""));
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Empleado");
                            msg.setContentText("Información guardada correctamente");
                            Optional<ButtonType> respuesta = msg.showAndWait();
                            if(respuesta.get() == ButtonType.OK){
                                tableEmpleados.setItems(empleadodao.findAll());
                                agregandoEmpleados = false;
                                actionsEmpleados.setVisible(false);
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
                        btnModificarEmpleados.setDisable(true);
                        btnBorrarEmpleados.setDisable(true);
                        txtNombreEmpleados.setText("");
                        txtTelefonoEmpleados.setText("");
                        cmbSucursalEmpleados.getSelectionModel().clearSelection();
                        actionsEmpleados.setVisible(true);
                        agregandoEmpleados = true;
                    }
                }
            });
            btnModificarEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Empleado g = tableEmpleados.getSelectionModel().getSelectedItem();
                    if(txtNombreEmpleados.getText().trim().length() > 0 && txtTelefonoEmpleados.getText().trim().length() > 0 && cmbSucursalEmpleados.getSelectionModel().getSelectedItem() != null){
                        g.setNombreEmpleado(txtNombreEmpleados.getText());
                        g.setTelefono(txtTelefonoEmpleados.getText());
                        g.setNumSucursal(cmbSucursalEmpleados.getSelectionModel().getSelectedItem().getNumSucursal()+"");
                        if(empleadodao.update(g)){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Borrar");
                            msg.setHeaderText("Empleado");
                            msg.setContentText("Empleado modificado correctamente");
                            Optional<ButtonType> respuesta = msg.showAndWait();
                            if(respuesta.get() == ButtonType.OK){
                                tableEmpleados.setItems(empleadodao.findAll());
                                actionsEmpleados.setVisible(false);
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
            btnBorrarEmpleados.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Empleado g = tableEmpleados.getSelectionModel().getSelectedItem();
                    if(empleadodao.delete(g.getNumEmpleado())){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Empleado");
                        msg.setContentText("Empleado borrado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            tableEmpleados.setItems(empleadodao.findAll());
                            actionsEmpleados.setVisible(false);
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
