package banco.fxml;

import banco.Sucursal;
import database.MySQL;
import database.dao.SucursalDAO;
import java.awt.BorderLayout;
import java.awt.Container;
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
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SucursalesController implements Initializable {

    @FXML
    Button btnAgregarSucursales, btnModificarSucursales, btnBorrarSucursales, btnVerTransacciones;
    @FXML
    GridPane actionsSucursales;
    @FXML
    TextField txtNombreSucursales, txtCiudadSucursales, txtDireccionSucursales;
    @FXML
    TableView<Sucursal> tableSucursales;
    Boolean agregandoSucursales = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        SucursalDAO sucursaldao = new SucursalDAO(db.getConnection());

        TableColumn nombresSucursales = new TableColumn("Número");
        nombresSucursales.setCellValueFactory(new PropertyValueFactory("numSucursal"));

        TableColumn apellidosSucursales = new TableColumn("Nombre");
        apellidosSucursales.setCellValueFactory(new PropertyValueFactory("nombreSucursal"));

        TableColumn telefonoSucursales = new TableColumn("Ciudad");
        telefonoSucursales.setCellValueFactory(new PropertyValueFactory("ciudad"));

        TableColumn domicilioSucursales = new TableColumn("Dirección");
        domicilioSucursales.setCellValueFactory(new PropertyValueFactory("direccion"));

        tableSucursales.getColumns().addAll(nombresSucursales, apellidosSucursales, telefonoSucursales, domicilioSucursales);
        tableSucursales.setItems(sucursaldao.findAll());
        tableSucursales.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = tableSucursales.getSelectionModel().getSelectedItem();
                if (g == null) {
                    return;
                }
                btnModificarSucursales.setDisable(false);
                btnBorrarSucursales.setDisable(false);
                btnVerTransacciones.setDisable(false);
                txtNombreSucursales.setText(g.getNombreSucursal());
                txtCiudadSucursales.setText(g.getCiudad());
                txtDireccionSucursales.setText(g.getDireccion());
                actionsSucursales.setVisible(true);
                agregandoSucursales = false;
            }
        });
        btnAgregarSucursales.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (agregandoSucursales) {
                    if (txtNombreSucursales.getText().trim().length() > 0 && txtCiudadSucursales.getText().trim().length() > 0 && txtDireccionSucursales.getText().trim().length() > 0) {
                        sucursaldao.insert(new Sucursal(txtNombreSucursales.getText(), txtCiudadSucursales.getText(), txtDireccionSucursales.getText()));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Crear");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            tableSucursales.setItems(sucursaldao.findAll());
                            agregandoSucursales = false;
                            actionsSucursales.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Crear");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();

                    }
                } else {
                    btnModificarSucursales.setDisable(true);
                    btnBorrarSucursales.setDisable(true);
                    txtNombreSucursales.setText("");
                    txtCiudadSucursales.setText("");
                    txtDireccionSucursales.setText("");
                    actionsSucursales.setVisible(true);
                    agregandoSucursales = true;
                }
            }
        });
        btnModificarSucursales.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = tableSucursales.getSelectionModel().getSelectedItem();
                if (txtNombreSucursales.getText().trim().length() > 0 && txtCiudadSucursales.getText().trim().length() > 0 && txtDireccionSucursales.getText().trim().length() > 0) {
                    g.setNombreSucursal(txtNombreSucursales.getText());
                    g.setCiudad(txtCiudadSucursales.getText());
                    g.setDireccion(txtDireccionSucursales.getText());
                    if (sucursaldao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("Sucursal modificado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            tableSucursales.setItems(sucursaldao.findAll());
                            actionsSucursales.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Sucursal");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrarSucursales.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Sucursal g = tableSucursales.getSelectionModel().getSelectedItem();
                if (sucursaldao.delete(g.getNumSucursal())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("Sucursal borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        tableSucursales.setItems(sucursaldao.findAll());
                        actionsSucursales.setVisible(false);
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Sucursal");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
        btnVerTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int g = tableSucursales.getSelectionModel().getSelectedItem().getNumSucursal();

                JFrame mainFrame = new JFrame();
                //Container container;
                mainFrame.setTitle("Transacciones");
                mainFrame.setSize(600, 370);
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                mainFrame.setLayout(new BorderLayout());
                //container = mainFrame.getContentPane();
                JTable mytable = new JTable(sucursaldao.findTransaccionJT(g));
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
}
