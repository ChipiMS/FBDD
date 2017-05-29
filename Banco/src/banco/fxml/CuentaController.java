package banco.fxml;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import banco.Cliente;
import banco.Cuenta;
import banco.Sucursal;
import database.MySQL;
import database.dao.CuentaDAO;
import java.net.URL;
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
public class CuentaController implements Initializable {

    @FXML
    Button btnAgregar, btnModificar, btnBorrar, btnClientes, btnSucursales;
    @FXML
    GridPane actions;
    @FXML
    TextField txtSaldo;
    @FXML
    TableView<Cuenta> table;
    @FXML
    ComboBox<Sucursal> cmbSucursal;
    @FXML
    ComboBox<Cliente> cmbCliente;
    Boolean agregando = false, show = true;

    CuentaDAO cuenta;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        CuentaDAO cuentadao = new CuentaDAO(db.getConnection());

        TableColumn numCuenta = new TableColumn("Número");
        numCuenta.setCellValueFactory(new PropertyValueFactory("numCuenta"));

        TableColumn saldo = new TableColumn("Saldo");
        saldo.setCellValueFactory(new PropertyValueFactory("saldo"));

        TableColumn seguroSocial = new TableColumn("Seguro Social");
        seguroSocial.setCellValueFactory(new PropertyValueFactory("seguroSocial"));

        TableColumn numSucursal = new TableColumn("Núm. Sucursal");
        numSucursal.setCellValueFactory(new PropertyValueFactory("numSucursal"));
        
        cuenta = new CuentaDAO(db.getConnection());
        cmbCliente.getItems().addAll(cuenta.findAllCliente());
        cmbSucursal.getItems().addAll(cuenta.findAllSuc());
        table.getColumns().addAll(numCuenta, saldo, seguroSocial, numSucursal);
        table.setItems(cuentadao.findAll());
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                show = true;
                try {
                    Cuenta g = table.getSelectionModel().getSelectedItem();
                    txtSaldo.setText(g.getSaldo() + "");
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
                    if (txtSaldo.getText().trim().length() > 0) {
                        cuentadao.insert(new Cuenta(Double.parseDouble(txtSaldo.getText()), cmbCliente.getSelectionModel().getSelectedItem().toString() + "", Integer.parseInt(cmbSucursal.getSelectionModel().getSelectedItem().toString())));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            table.setItems(cuentadao.findAll());
                            agregando = false;
                            actions.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();

                    }
                } else {
                    btnModificar.setDisable(true);
                    btnBorrar.setDisable(true);
                    txtSaldo.setText("");
                    actions.setVisible(true);
                    agregando = true;
                }
            }
        });
        btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cuenta g = table.getSelectionModel().getSelectedItem();
                if (txtSaldo.getText().trim().length() > 0) {
                    g.setSaldo(Double.parseDouble(txtSaldo.getText()));
                    g.setSeguroSocial(cmbCliente.getSelectionModel().getSelectedItem().toString());
                    g.setNumSucursal(Integer.parseInt(cmbSucursal.getSelectionModel().getSelectedItem().toString()));
                    if (cuentadao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Cuenta modificado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            table.setItems(cuentadao.findAll());
                            actions.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Cuenta");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cuenta g = table.getSelectionModel().getSelectedItem();
                if (cuentadao.delete(g.getNumCuenta())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Cuenta");
                    msg.setContentText("Cuenta borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        table.setItems(cuentadao.findAll());
                        actions.setVisible(false);
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Cuenta");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
        
        btnClientes.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                FXMLLoader loader = new FXMLLoader();
                Parent rootConsultaPersonas = null;
                try {
                    rootConsultaPersonas = loader.load(getClass().getResource("Cliente.fxml"));
                    Scene scene = new Scene(rootConsultaPersonas);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        });
        
        btnSucursales.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
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
            
        });
    }

}
