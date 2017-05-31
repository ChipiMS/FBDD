/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Cuenta;
import banco.Transaccion;
import database.MySQL;
import database.dao.TransaccionDAO;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ELÍAS MANCERA
 */
public class TransaccionController implements Initializable {

    @FXML
    Button btnAgregar, btnModificar, btnBorrar, btnCuentas;
    @FXML
    GridPane actions;
    @FXML
    TextField txtCantidad;
    @FXML
    //DD-MM-AAAA
    TextField txtFecha;
    @FXML
    TableView<Transaccion> table;
    @FXML
    ComboBox<Cuenta> cmbCuenta;
    Boolean agregando = false, show = true;

    TransaccionDAO transaccion;
    List<Cuenta> cuentas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        TransaccionDAO transacciondao = new TransaccionDAO(db.getConnection());

        TableColumn numCuenta = new TableColumn("Número");
        numCuenta.setCellValueFactory(new PropertyValueFactory("numTran"));

        TableColumn saldo = new TableColumn("Cantidad");
        saldo.setCellValueFactory(new PropertyValueFactory("cantidad"));

        TableColumn seguroSocial = new TableColumn("Fecha");
        seguroSocial.setCellValueFactory(new PropertyValueFactory("fecha"));

        TableColumn numSucursal = new TableColumn("Núm. Cuenta");
        numSucursal.setCellValueFactory(new PropertyValueFactory("numCuenta"));
        
        transaccion = new TransaccionDAO(db.getConnection());
        cuentas = transaccion.findAllCuenta();
        cmbCuenta.getItems().addAll(cuentas);
        table.getColumns().addAll(numCuenta, saldo, seguroSocial, numSucursal);
        table.setItems(transacciondao.findAll());
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                show = true;
                try {
                    Transaccion g = table.getSelectionModel().getSelectedItem();
                    txtCantidad.setText(g.getCantidad()+ "");
                    txtFecha.setText(g.getFecha() + "");
                    for(int i=0;i<cuentas.size();i++){
                        if(g.getNumCuenta() == cuentas.get(i).getNumCuenta()){
                            cmbCuenta.getSelectionModel().clearAndSelect(i);
                        }
                    }
                } catch (Exception e) {
                    show = false;
                }
                if (show){
                    btnModificar.setDisable(false);
                    btnBorrar.setDisable(false);
                    actions.setVisible(true);
                    agregando = false;
                }
            }
        });
        btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (agregando) {
                    //new Transaccion(cantidad, fecha, numCuenta)
                    if (txtCantidad.getText().trim().length() > 0 && txtFecha.getText().trim().length() > 0 && cmbCuenta.getSelectionModel().getSelectedItem() != null) {
                        if(!valida(txtCantidad.getText())){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Transacción");
                            msg.setContentText("Cantidad tiene que ser un número");
                            msg.show();
                            return;
                        }
                        transacciondao.insert(new Transaccion(Integer.parseInt(txtCantidad.getText()), txtFecha.getText(), Integer.parseInt(cmbCuenta.getSelectionModel().getSelectedItem().toString())));
                        //transacciondao.insert(new Transaccion(Integer.parseInt(txtCantidad.getText()), txtFecha.getText(), Integer.parseInt(cmbCuenta.getSelectionModel().getSelectedItem().toString())));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            table.setItems(transacciondao.findAll());
                            agregando = false;
                            actions.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();

                    }
                } else {
                    btnModificar.setDisable(true);
                    btnBorrar.setDisable(true);
                    txtCantidad.setText("");
                    txtFecha.setText("");
                    cmbCuenta.getSelectionModel().clearSelection();
                    actions.setVisible(true);
                    agregando = true;
                }
            }
        });
        btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Transaccion g = table.getSelectionModel().getSelectedItem();
                if (txtCantidad.getText().trim().length() > 0 && txtFecha.getText().trim().length() > 0 && cmbCuenta.getSelectionModel().getSelectedItem() != null) {
                    if(!valida(txtCantidad.getText())){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Cantidad tiene que ser un número");
                        msg.show();
                        return;
                    }
                    g.setCantidad(Integer.parseInt(txtCantidad.getText()));
                    g.setFecha(txtFecha.getText());
                    g.setNumCuenta(Integer.parseInt(cmbCuenta.getSelectionModel().getSelectedItem().toString()));
                    if (transacciondao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Transacción modificada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            table.setItems(transacciondao.findAll());
                            actions.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Transacción");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Transaccion g = table.getSelectionModel().getSelectedItem();
                if (transacciondao.delete(g.getNumTran())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Transacción");
                    msg.setContentText("Transacción borrada correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        table.setItems(transacciondao.findAll());
                        actions.setVisible(false);
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Transacción");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
        btnCuentas.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource("Cuenta.fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });
    }
    Boolean valida(String toValidate){
        for(int i=0;i<toValidate.length();i++){
            if(!(toValidate.charAt(i)>='0'&&toValidate.charAt(i)<='9')){
                return false;
            }
        }
        return true;
    }
}
