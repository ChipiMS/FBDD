/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Cliente;
import database.MySQL;
import database.dao.ClienteDAO;
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
public class ClienteController implements Initializable {

@FXML
        Button btnAgregar,btnModificar,btnBorrar;
        @FXML
        GridPane actions;
        @FXML
        TextField txtNombre,txtCiudad,txtCalle;
        @FXML
        TableView<Cliente> table;
        Boolean agregando = false;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            MySQL db = new MySQL();
            db.Connect();
            ClienteDAO clientedao = new ClienteDAO(db.getConnection());

            TableColumn nombres = new TableColumn("Seguro Social");
            nombres.setCellValueFactory(new PropertyValueFactory("seguroSocial"));

            TableColumn apellidos = new TableColumn("Nombre");
            apellidos.setCellValueFactory(new PropertyValueFactory("nombreCliente"));

            TableColumn ciudad = new TableColumn("Ciudad");
            ciudad.setCellValueFactory(new PropertyValueFactory("ciudad"));

            TableColumn domicilio = new TableColumn("Calle");
            domicilio.setCellValueFactory(new PropertyValueFactory("calle"));

            table.getColumns().addAll(nombres,apellidos,ciudad,domicilio);
            table.setItems(clientedao.findAll());
            table.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Cliente g = table.getSelectionModel().getSelectedItem();
                    if(g == null)
                        return;
                    btnModificar.setDisable(false);
                    btnBorrar.setDisable(false);
                    txtNombre.setText(g.getNombreCliente());
                    txtCiudad.setText(g.getCiudad());
                    txtCalle.setText(g.getCalle());
                    actions.setVisible(true);
                    agregando = false;
                }
            });
            btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(agregando){
                        if(txtNombre.getText().trim().length() > 0 && txtCiudad.getText().trim().length() > 0 && txtCalle.getText().trim().length() > 0){
                            clientedao.insert(new Cliente(txtNombre.getText(),txtCiudad.getText(),txtCalle.getText()));
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Cliente");
                            msg.setContentText("Información guardada correctamente");
                            Optional<ButtonType> respuesta = msg.showAndWait();
                            if(respuesta.get() == ButtonType.OK){
                                table.setItems(clientedao.findAll());
                                agregando = false;
                                actions.setVisible(false);
                            }
                        }
                        else{
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Cliente");
                            msg.setContentText("Hay que poner bien la información");
                            msg.show();

                        }
                    }
                    else{
                        btnModificar.setDisable(true);
                        btnBorrar.setDisable(true);
                        txtNombre.setText("");
                        txtCiudad.setText("");
                        txtCalle.setText("");
                        actions.setVisible(true);
                        agregando = true;
                    }
                }
            });
            btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Cliente g = table.getSelectionModel().getSelectedItem();
                    if(txtNombre.getText().trim().length() > 0 && txtCiudad.getText().trim().length() > 0 && txtCalle.getText().trim().length() > 0){
                        g.setNombreCliente(txtNombre.getText());
                        g.setCiudad(txtCiudad.getText());
                        g.setCalle(txtCalle.getText());
                        if(clientedao.update(g)){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Borrar");
                            msg.setHeaderText("Cliente");
                            msg.setContentText("Cliente modificado correctamente");
                            Optional<ButtonType> respuesta = msg.showAndWait();
                            if(respuesta.get() == ButtonType.OK){
                                table.setItems(clientedao.findAll());
                                actions.setVisible(false);
                            }
                        }
                        else{
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Modificar");
                            msg.setHeaderText("Cliente");
                            msg.setContentText("No se puede modificar");
                            msg.show();
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();
                    }
                }
            });
            btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Cliente g = table.getSelectionModel().getSelectedItem();
                    if(clientedao.delete(g.getSeguroSocial())){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("Cliente borrado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            table.setItems(clientedao.findAll());
                            actions.setVisible(false);
                        }
                    }
                    else{
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("No se puede borrar");
                        msg.show();
                    }
                }
            });
        }   // END Initialize 
    
}
