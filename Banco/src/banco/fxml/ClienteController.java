/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco.fxml;

import banco.Cliente;
import database.MySQL;
import database.dao.ClienteDAO;
import java.awt.BorderLayout;
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

/**
 * FXML Controller class
 *
 * @author aldoea
 */
public class ClienteController implements Initializable {

    @FXML
    Button btnAgregarCliente,btnModificarCliente,btnBorrarCliente,btnVerTransacciones;
    @FXML
    GridPane actionsCliente;
    @FXML
    TextField txtSeguroSocialCliente,txtNombreCliente,txtCiudadCliente,txtCalleCliente;
    @FXML
    TableView<Cliente> tableCliente;
    Boolean agregandoCliente = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQL db = new MySQL();
        db.Connect();
        ClienteDAO clientedao = new ClienteDAO(db.getConnection());

        TableColumn nombresClientes = new TableColumn("Seguro Social");
        nombresClientes.setCellValueFactory(new PropertyValueFactory("seguroSocial"));

        TableColumn apellidosClientes = new TableColumn("Nombre");
        apellidosClientes.setCellValueFactory(new PropertyValueFactory("nombreCliente"));

        TableColumn ciudadClientes = new TableColumn("Ciudad");
        ciudadClientes.setCellValueFactory(new PropertyValueFactory("ciudad"));

        TableColumn domicilioClientes = new TableColumn("Calle");
        domicilioClientes.setCellValueFactory(new PropertyValueFactory("calle"));

        tableCliente.getColumns().addAll(nombresClientes,apellidosClientes,ciudadClientes,domicilioClientes);
        tableCliente.setItems(clientedao.findAll());
        tableCliente.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Cliente g = tableCliente.getSelectionModel().getSelectedItem();
                if(g == null)
                    return;
                btnModificarCliente.setDisable(false);
                btnBorrarCliente.setDisable(false);
                btnVerTransacciones.setDisable(false);
                txtSeguroSocialCliente.setText(g.getSeguroSocial());
                txtNombreCliente.setText(g.getNombreCliente());
                txtCiudadCliente.setText(g.getCiudad());
                txtCalleCliente.setText(g.getCalle());
                actionsCliente.setVisible(true);
                agregandoCliente = false;
            }
        });
        btnAgregarCliente.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(agregandoCliente){
                    if(txtSeguroSocialCliente.getText().trim().length() > 0 && txtNombreCliente.getText().trim().length() > 0 && txtCiudadCliente.getText().trim().length() > 0 && txtCalleCliente.getText().trim().length() > 0){
                        if(clientedao.isDuplicated(txtSeguroSocialCliente.getText())){
                            Alert msg = new Alert(Alert.AlertType.INFORMATION);
                            msg.setTitle("Modificar");
                            msg.setHeaderText("Cliente");
                            msg.setContentText("Seguro social ya existente");
                            msg.show();
                            return;
                        }
                        clientedao.insert(new Cliente(txtSeguroSocialCliente.getText(),txtNombreCliente.getText(),txtCiudadCliente.getText(),txtCalleCliente.getText()));
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Guardar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("Información guardada correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            tableCliente.setItems(clientedao.findAll());
                            agregandoCliente = false;
                            actionsCliente.setVisible(false);
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
                    btnModificarCliente.setDisable(true);
                    btnBorrarCliente.setDisable(true);
                    btnVerTransacciones.setDisable(true);
                    txtNombreCliente.setText("");
                    txtCiudadCliente.setText("");
                    txtCalleCliente.setText("");
                    actionsCliente.setVisible(true);
                    agregandoCliente = true;
                }
            }
        });
        btnModificarCliente.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Cliente g = tableCliente.getSelectionModel().getSelectedItem();
                if(txtSeguroSocialCliente.getText().trim().length() > 0 && txtNombreCliente.getText().trim().length() > 0 && txtCiudadCliente.getText().trim().length() > 0 && txtCalleCliente.getText().trim().length() > 0){
                    String seguroSocial = g.getSeguroSocial();
                    if(!seguroSocial.equals(txtSeguroSocialCliente.getText()) && clientedao.isDuplicated(txtSeguroSocialCliente.getText())){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Modificar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("Seguro social ya existente");
                        msg.show();
                        return;
                    }
                    g.setSeguroSocial(txtSeguroSocialCliente.getText());
                    g.setNombreCliente(txtNombreCliente.getText());
                    g.setCiudad(txtCiudadCliente.getText());
                    g.setCalle(txtCalleCliente.getText());
                    if(clientedao.update(seguroSocial,g)){
                        Alert msg = new Alert(Alert.AlertType.INFORMATION);
                        msg.setTitle("Borrar");
                        msg.setHeaderText("Cliente");
                        msg.setContentText("Cliente modificado correctamente");
                        Optional<ButtonType> respuesta = msg.showAndWait();
                        if(respuesta.get() == ButtonType.OK){
                            tableCliente.setItems(clientedao.findAll());
                            actionsCliente.setVisible(false);
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
        btnBorrarCliente.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                Cliente g = tableCliente.getSelectionModel().getSelectedItem();
                if(clientedao.delete(g.getSeguroSocial())){
                    Alert msg = new Alert(Alert.AlertType.INFORMATION);
                    msg.setTitle("Borrar");
                    msg.setHeaderText("Cliente");
                    msg.setContentText("Cliente borrado correctamente");
                    Optional<ButtonType> respuesta = msg.showAndWait();
                    if(respuesta.get() == ButtonType.OK){
                        tableCliente.setItems(clientedao.findAll());
                        actionsCliente.setVisible(false);
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
        btnVerTransacciones.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String g = tableCliente.getSelectionModel().getSelectedItem().getSeguroSocial();

                JFrame mainFrame = new JFrame();
                //Container container;
                mainFrame.setTitle("Transacciones");
                mainFrame.setSize(600, 370);
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                mainFrame.setLayout(new BorderLayout());
                //container = mainFrame.getContentPane();
                JTable mytable = new JTable(clientedao.findTransaccionJT(g));
                JScrollPane scrollPane = new JScrollPane(mytable);
                mytable.setFillsViewportHeight(true);
                mainFrame.add(scrollPane);
//mainFrame.add(mytable);
//initComponents();
                mainFrame.setVisible(true);
                mainFrame.setLocationRelativeTo(null);
            }
        });
    }   // END Initialize 
    
}
