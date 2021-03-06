/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Cuenta;
import banco.TipoTransaccion;
import banco.Transaccion;
import database.MySQL;
import database.dao.TipoTransaccionDAO;
import database.dao.TransaccionDAO;
import java.awt.BorderLayout;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * FXML Controller class
 *
 * @author ELÍAS MANCERA
 */
public class TransaccionController implements Initializable {

    @FXML
    Button btnAgregarTransacciones, btnModificarTransacciones, btnBorrarTransacciones, btnVerSucursal;
    @FXML
    GridPane actionsTransacciones;
    @FXML
    TextField txtCantidadTransacciones;
    @FXML
    //DD-MM-AAAA
    TextField txtFechaTransacciones;
    @FXML
    TableView<Transaccion> tableTransacciones;
    @FXML
    ComboBox<Cuenta> cmbCuentaTransacciones;
    @FXML
    ComboBox<TipoTransaccion> cmbTipoTransaccion;
    @FXML
    ComboBox<Cuenta> cmbCuentaTranspaso;
    @FXML
    Label lblCT;
    Boolean agregandoTransacciones = false;

    TransaccionDAO transaccion;
    List<Cuenta> cuentas;
    List<TipoTransaccion> tipoTransaccionList;
    TipoTransaccionDAO tipoTransaccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        TransaccionDAO transacciondao = new TransaccionDAO(db.getConnection());

        TableColumn numCuenta = new TableColumn("Número");
        numCuenta.setCellValueFactory(new PropertyValueFactory("numTran"));

        TableColumn saldo = new TableColumn("Cantidad");
        saldo.setCellValueFactory(new PropertyValueFactory("cantidad"));

        TableColumn fecha = new TableColumn("Fecha");
        fecha.setCellValueFactory(new PropertyValueFactory("fecha"));

        TableColumn numSucursal = new TableColumn("Núm. Cuenta");
        numSucursal.setCellValueFactory(new PropertyValueFactory("numCuenta"));

        TableColumn tipoTrans = new TableColumn("Tipo Transaccion");
        tipoTrans.setCellValueFactory(new PropertyValueFactory("claveTipoTransaccion"));

        transaccion = new TransaccionDAO(db.getConnection());
        cuentas = transaccion.findAllCuenta();
        cmbCuentaTransacciones.getItems().addAll(cuentas);
        cmbCuentaTranspaso.getItems().addAll(cuentas);
        tipoTransaccion = new TipoTransaccionDAO(db.getConnection());
        tipoTransaccionList = tipoTransaccion.findAll();
        cmbTipoTransaccion.getItems().addAll(tipoTransaccionList);
        cmbTipoTransaccion.valueProperty().addListener(new ChangeListener<TipoTransaccion>() {
            @Override
            public void changed(ObservableValue<? extends TipoTransaccion> observable, TipoTransaccion oldValue, TipoTransaccion newValue) {
                if (newValue != null) {
                    if (!newValue.getClave().equals("088")) {
                        lblCT.setDisable(true);
                        cmbCuentaTranspaso.setDisable(true);
                    } else {
                        lblCT.setDisable(false);
                        cmbCuentaTranspaso.setDisable(false);

                    }
                } else {
                    lblCT.setDisable(true);
                    cmbCuentaTranspaso.setDisable(true);
                }
            }

        });
        tableTransacciones.getColumns().addAll(numCuenta, saldo, fecha, numSucursal, tipoTrans);
        tableTransacciones.setItems(transacciondao.findAll());
        tableTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Transaccion g = tableTransacciones.getSelectionModel().getSelectedItem();
                if (g == null) {
                    return;
                }
                txtCantidadTransacciones.setText(g.getCantidad() + "");
                txtFechaTransacciones.setText(g.getFecha() + "");
                for (int i = 0; i < cuentas.size(); i++) {
                    if (g.getNumCuenta() == cuentas.get(i).getNumCuenta()) {
                        cmbCuentaTransacciones.getSelectionModel().clearAndSelect(i);
                    }
                }
                for (int i = 0; i < tipoTransaccionList.size(); i++) {
                    if (g.getTipoTransaccion().equals(tipoTransaccionList.get(i).getClave())) {
                        cmbTipoTransaccion.getSelectionModel().clearAndSelect(i);
                    }
                }
                cmbCuentaTranspaso.getSelectionModel().clearSelection();
                btnModificarTransacciones.setDisable(false);
                btnBorrarTransacciones.setDisable(false);
                btnVerSucursal.setDisable(false);
                actionsTransacciones.setVisible(true);
                agregandoTransacciones = false;
            }
        });
        btnAgregarTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (agregandoTransacciones) {
                    //new Transaccion(cantidad, fecha, numCuenta)
                    if (txtCantidadTransacciones.getText().trim().length() > 0 && txtFechaTransacciones.getText().trim().length() > 0 && cmbCuentaTransacciones.getSelectionModel().getSelectedItem() != null && cmbTipoTransaccion.getSelectionModel().getSelectedItem() != null) {
                        if (!valida(txtCantidadTransacciones.getText())) {
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Transacción");
                            msg.setContentText("Cantidad tiene que ser un número");
                            msg.show();
                            return;
                        }
                        if (cmbTipoTransaccion.getSelectionModel().getSelectedItem().getClave().equals("088")) {
                            if (transacciondao.insert(new Transaccion(Integer.parseInt(txtCantidadTransacciones.getText()), txtFechaTransacciones.getText(), Integer.parseInt(cmbCuentaTransacciones.getSelectionModel().getSelectedItem().toString()), Integer.parseInt(cmbCuentaTranspaso.getSelectionModel().getSelectedItem().toString()), cmbTipoTransaccion.getSelectionModel().getSelectedItem().getClave()))) {
                                Alert msg = new Alert(Alert.AlertType.INFORMATION);
                                msg.setTitle("Guardar");
                                msg.setHeaderText("Transacción");
                                msg.setContentText("Información guardada correctamente");
                                Optional<ButtonType> respuesta = msg.showAndWait();
                                if (respuesta.get() == ButtonType.OK) {
                                    tableTransacciones.setItems(transacciondao.findAll());
                                    agregandoTransacciones = false;
                                    actionsTransacciones.setVisible(false);
                                }
                            }
                        } else {
                            if (transacciondao.insert(new Transaccion(Integer.parseInt(txtCantidadTransacciones.getText()), txtFechaTransacciones.getText(), Integer.parseInt(cmbCuentaTransacciones.getSelectionModel().getSelectedItem().toString()), cmbTipoTransaccion.getSelectionModel().getSelectedItem().getClave()))) {
                                //transacciondao.insert(new Transaccion(Integer.parseInt(txtCantidadTransacciones.getText()), txtFechaTransacciones.getText(), Integer.parseInt(cmbCuentaTransacciones.getSelectionModel().getSelectedItem().toString())));
                                Alert msg = new Alert(Alert.AlertType.INFORMATION);
                                msg.setTitle("Guardar");
                                msg.setHeaderText("Transacción");
                                msg.setContentText("Información guardada correctamente");
                                Optional<ButtonType> respuesta = msg.showAndWait();
                                if (respuesta.get() == ButtonType.OK) {
                                    tableTransacciones.setItems(transacciondao.findAll());
                                    agregandoTransacciones = false;
                                    actionsTransacciones.setVisible(false);
                                }
                            }
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();

                    }
                } else {
                    btnModificarTransacciones.setDisable(true);
                    btnBorrarTransacciones.setDisable(true);
                    btnVerSucursal.setDisable(true);
                    txtCantidadTransacciones.setText("");
                    txtFechaTransacciones.setText("");
                    cmbCuentaTransacciones.getSelectionModel().clearSelection();
                    cmbCuentaTranspaso.getSelectionModel().clearSelection();
                    cmbTipoTransaccion.getSelectionModel().clearSelection();
                    actionsTransacciones.setVisible(true);
                    agregandoTransacciones = true;
                }
            }
        });
        btnModificarTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Transaccion g = tableTransacciones.getSelectionModel().getSelectedItem();
                if (txtCantidadTransacciones.getText().trim().length() > 0 && txtFechaTransacciones.getText().trim().length() > 0 && cmbCuentaTransacciones.getSelectionModel().getSelectedItem() != null && cmbTipoTransaccion.getSelectionModel().getSelectedItem() != null) {
                    if (!valida(txtCantidadTransacciones.getText())) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Cantidad tiene que ser un número");
                        msg.show();
                        return;
                    }
                    g.setCantidad(Integer.parseInt(txtCantidadTransacciones.getText()));
                    g.setFecha(txtFechaTransacciones.getText());
                    g.setNumCuenta(Integer.parseInt(cmbCuentaTransacciones.getSelectionModel().getSelectedItem().toString()));
                    g.setTipoTransaccion(cmbTipoTransaccion.getSelectionModel().getSelectedItem().toString());
                    if (transacciondao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Transacción");
                        msg.setContentText("Transacción modificada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            tableTransacciones.setItems(transacciondao.findAll());
                            actionsTransacciones.setVisible(false);
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
        btnBorrarTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Transaccion g = tableTransacciones.getSelectionModel().getSelectedItem();
                if (transacciondao.delete(g.getNumTran())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Transacción");
                    msg.setContentText("Transacción borrada correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        tableTransacciones.setItems(transacciondao.findAll());
                        actionsTransacciones.setVisible(false);
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
        btnVerSucursal.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int g = tableTransacciones.getSelectionModel().getSelectedItem().getNumCuenta();

                JFrame mainFrame = new JFrame();
                //Container container;
                mainFrame.setTitle("Sucursal");
                mainFrame.setSize(600, 370);
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                mainFrame.setLayout(new BorderLayout());
                //container = mainFrame.getContentPane();
                JTable mytable = new JTable(transacciondao.findSucursalJT(g));
                JScrollPane scrollPane = new JScrollPane(mytable);
                mytable.setFillsViewportHeight(true);
                mainFrame.add(scrollPane);
//mainFrame.add(mytable);
//initComponents();
                mainFrame.setVisible(true);
                mainFrame.setLocationRelativeTo(null);
            }
        });
    }

    Boolean valida(String toValidate) {
        for (int i = 0; i < toValidate.length(); i++) {
            if (!(toValidate.charAt(i) >= '0' && toValidate.charAt(i) <= '9')) {
                return false;
            }
        }
        return true;
    }
}
