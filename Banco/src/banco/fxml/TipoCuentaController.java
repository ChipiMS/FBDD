package banco.fxml;
import banco.Cliente;
import banco.TipoCuenta;
import banco.Sucursal;
import database.MySQL;
import database.dao.TipoCuentaDAO;
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
public class TipoCuentaController implements Initializable {
    @FXML
    Button btnAgregar, btnModificar, btnBorrar;
    @FXML
    GridPane actions;
    @FXML
    TextField txtClave,txtDescripcion;
    @FXML
    TableView<TipoCuenta> table;
    Boolean agregando = false;
    TipoCuentaDAO dao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        dao = new TipoCuentaDAO(db.getConnection());

        TableColumn num = new TableColumn("Clave");
        num.setCellValueFactory(new PropertyValueFactory("clave"));

        TableColumn saldo = new TableColumn("Descripción");
        saldo.setCellValueFactory(new PropertyValueFactory("descripcion"));
        
        dao = new TipoCuentaDAO(db.getConnection());
        table.getColumns().addAll(num, saldo);
        table.setItems(dao.findAll());
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TipoCuenta g = table.getSelectionModel().getSelectedItem();
                if(g == null){
                    actions.setVisible(false);
                    return;
                }
                txtClave.setText(g.getClave());
                txtDescripcion.setText(g.getDescripcion());
                txtClave.setDisable(true);
                btnModificar.setDisable(false);
                btnBorrar.setDisable(false);
                actions.setVisible(true);
                agregando = false;
            }
        });
        btnAgregar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (agregando) {
                    if (txtClave.getText().trim().length() > 0 && txtDescripcion.getText().trim().length() > 0) {
                        if(dao.isDuplicated(txtClave.getText())){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Tipo de Cuenta");
                            msg.setContentText("Clave ya existente");
                            msg.show();
                        }
                        else{
                            dao.insert(new TipoCuenta(txtClave.getText(),txtDescripcion.getText()));
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Guardar");
                            msg.setHeaderText("Tipo de Cuenta");
                            msg.setContentText("Información guardada correctamente");
                            Optional<ButtonType> respuesta = msg.showAndWait();
                            if (respuesta.get() == ButtonType.OK) {
                                table.setItems(dao.findAll());
                                agregando = false;
                                actions.setVisible(false);
                            }
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Tipo de Cuenta");
                        msg.setContentText("Hay que poner bien la información");
                        msg.show();
                    }
                } else {
                    btnModificar.setDisable(true);
                    btnBorrar.setDisable(true);
                    txtClave.setText("");
                    txtDescripcion.setText("");
                    txtClave.setDisable(false);
                    actions.setVisible(true);
                    agregando = true;
                }
            }
        });
        btnModificar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TipoCuenta g = table.getSelectionModel().getSelectedItem();
                if (txtDescripcion.getText().trim().length() > 0) {
                    g.setDescripcion(txtDescripcion.getText());
                    if (dao.update(g)) {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Tipo de Cuenta");
                        msg.setContentText(" modificada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if (respuesta.get() == ButtonType.OK) {
                            table.setItems(dao.findAll());
                            actions.setVisible(false);
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Tipo de Cuenta");
                        msg.setContentText("No se puede modificar");
                        msg.show();
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Modificar");
                    msg.setHeaderText("Tipo de Cuenta");
                    msg.setContentText("Hay que poner bien la información");
                    msg.show();
                }
            }
        });
        btnBorrar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TipoCuenta g = table.getSelectionModel().getSelectedItem();
                if (dao.delete(g.getClave())) {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Tipo de Cuenta");
                    msg.setContentText(" borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        table.setItems(dao.findAll());
                        actions.setVisible(false);
                    }
                } else {
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Tipo de Cuenta");
                    msg.setContentText("No se puede borrar");
                    msg.show();
                }
            }
        });
    }
}
