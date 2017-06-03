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
public class CuentaController implements Initializable {

    @FXML
    Button btnAgregarCuenta, btnModificarCuenta, btnBorrarCuenta;
    @FXML
    GridPane actionsCuenta;
    @FXML
    TextField txtSaldoCuenta;
    @FXML
    TableView<Cuenta> tableCuenta;
    @FXML
    ComboBox<Sucursal> cmbSucursalCuenta;
    @FXML
    ComboBox<Cliente> cmbClienteCuenta;
    Boolean agregandoCuenta = false;

    CuentaDAO cuenta;
    List<Sucursal> sucursales;
    List<Cliente> clientes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        CuentaDAO cuentadao = new CuentaDAO(db.getConnection());

        TableColumn numCuentaCuenta = new TableColumn("Número");
        numCuentaCuenta.setCellValueFactory(new PropertyValueFactory("numCuenta"));

        TableColumn saldoCuenta = new TableColumn("Saldo");
        saldoCuenta.setCellValueFactory(new PropertyValueFactory("saldo"));

        TableColumn seguroSocialCuenta = new TableColumn("Seguro Social");
        seguroSocialCuenta.setCellValueFactory(new PropertyValueFactory("seguroSocial"));

        TableColumn numSucursalCuenta = new TableColumn("Núm. Sucursal");
        numSucursalCuenta.setCellValueFactory(new PropertyValueFactory("numSucursal"));
        
        cuenta = new CuentaDAO(db.getConnection());
        clientes = cuenta.findAllCliente();
        cmbClienteCuenta.getItems().addAll(clientes);
        sucursales = cuenta.findAllSuc();
        cmbSucursalCuenta.getItems().addAll(sucursales);
        tableCuenta.getColumns().addAll(numCuentaCuenta, saldoCuenta, seguroSocialCuenta, numSucursalCuenta);
        tableCuenta.setItems(cuentadao.findAll());
        tableCuenta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cuenta g = tableCuenta.getSelectionModel().getSelectedItem();
                if(g == null){
                    return;
                }
                txtSaldoCuenta.setText(g.getSaldo() + "");
                for(int i=0;i<sucursales.size();i++){
                    if(sucursales.get(i).getNumSucursal() == g.getNumSucursal()){
                        cmbSucursalCuenta.getSelectionModel().clearAndSelect(i);
                    }
                }
                for(int i=0;i<clientes.size();i++){
                    if(g.getSeguroSocial().equals(clientes.get(i).getSeguroSocial()+"")){
                        cmbClienteCuenta.getSelectionModel().clearAndSelect(i);
                    }
                }
                btnModificarCuenta.setDisable(false);
                btnBorrarCuenta.setDisable(false);
                actionsCuenta.setVisible(true);
                agregandoCuenta = false;
            }
        });
        btnAgregarCuenta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (agregandoCuenta) {
                    if (txtSaldoCuenta.getText().trim().length() > 0 && cmbClienteCuenta.getSelectionModel().getSelectedItem() != null && cmbSucursalCuenta.getSelectionModel().getSelectedItem() != null) {
                        if(!valida(txtSaldoCuenta.getText())){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Cuenta");
                            msg.setContentText("Saldo tiene que ser un número");
                            msg.show();
                            return;
                        }
                        cuentadao.insert(new Cuenta(Double.parseDouble(txtSaldoCuenta.getText()), cmbClienteCuenta.getSelectionModel().getSelectedItem().getSeguroSocial(),cmbSucursalCuenta.getSelectionModel().getSelectedItem().getNumSucursal()));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            tableCuenta.setItems(cuentadao.findAll());
                            agregandoCuenta = false;
                            actionsCuenta.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();

                    }
                } else {
                    btnModificarCuenta.setDisable(true);
                    btnBorrarCuenta.setDisable(true);
                    txtSaldoCuenta.setText("");
                    cmbClienteCuenta.getSelectionModel().clearSelection();
                    cmbSucursalCuenta.getSelectionModel().clearSelection();
                    actionsCuenta.setVisible(true);
                    agregandoCuenta = true;
                }
            }
        });
        btnModificarCuenta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cuenta g = tableCuenta.getSelectionModel().getSelectedItem();
                if (txtSaldoCuenta.getText().trim().length() > 0 && cmbClienteCuenta.getSelectionModel().getSelectedItem() != null && cmbSucursalCuenta.getSelectionModel().getSelectedItem() != null) {
                    if(!valida(txtSaldoCuenta.getText())){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Saldo tiene que ser un número");
                        msg.show();
                        return;
                    }
                    g.setSaldo(Double.parseDouble(txtSaldoCuenta.getText()));
                    g.setSeguroSocial(cmbClienteCuenta.getSelectionModel().getSelectedItem().toString());
                    g.setNumSucursal(Integer.parseInt(cmbSucursalCuenta.getSelectionModel().getSelectedItem().toString()));
                    if (cuentadao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Cuenta");
                        msg.setContentText("Cuenta modificada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            tableCuenta.setItems(cuentadao.findAll());
                            actionsCuenta.setVisible(false);
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
        btnBorrarCuenta.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Cuenta g = tableCuenta.getSelectionModel().getSelectedItem();
                if (cuentadao.delete(g.getNumCuenta())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Cuenta");
                    msg.setContentText("Cuenta borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        tableCuenta.setItems(cuentadao.findAll());
                        actionsCuenta.setVisible(false);
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
    }
    Boolean valida(String porValidar){
        Boolean valido=true;
        int puntos=0;
        if(porValidar.length()==0)
            valido=false;
        else{
            if(porValidar.charAt(0)!='-'&&!(porValidar.charAt(0)>='0'&&porValidar.charAt(0)<='9'))
                if(porValidar.charAt(0)!='.')
                    valido=false;
                else
                    puntos++;
            for(int i=1;i<porValidar.length();i++){
                if(!(porValidar.charAt(i)>='0'&&porValidar.charAt(i)<='9'))
                    if(porValidar.charAt(i)!='.')
                        valido=false;
                    else
                        puntos++;
            }
        }
        if(puntos>1)
            valido=false;
        return valido;
    }

}
