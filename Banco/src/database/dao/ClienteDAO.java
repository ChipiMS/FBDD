/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dao;

import banco.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author aldoea
 */
public class ClienteDAO {
    Connection conn;
    public ClienteDAO(Connection conn){
        this.conn = conn;
    }
    public boolean isDuplicated(String seguroSocial){
        boolean duplicated = false;
        try {
            String query = "SELECT seguroSocial FROM cliente";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                if(rs.getString("seguroSocial").equals(seguroSocial))
                    duplicated = true;
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return duplicated;
    }
    public ObservableList<Cliente> findAll() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM cliente";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                clientes.add(new Cliente(rs.getString("seguroSocial"), rs.getString("nombreCliente"),rs.getString("ciudad"),rs.getString("calle")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return clientes;
    }
    public Boolean delete(String seguroSocial) {
        try {
            String query = "delete from cliente where seguroSocial = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, seguroSocial);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(Cliente cliente) {
        try {
            String query = "insert into cliente "
                    + " (seguroSocial,nombreCliente,ciudad,calle)"
                    + " values (?,?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, cliente.getSeguroSocial());
            st.setString(2, cliente.getNombreCliente());
            st.setString(3, cliente.getCiudad());
            st.setString(4, cliente.getCalle());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    public Boolean update(String seguroSocial,Cliente cliente) {
        try {
            String query = "update cliente "
                    + " set nombreCliente = ?,ciudad = ?,calle = ?,set seguroSocial = ?"
                    + " where seguroSocial = ?";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, cliente.getNombreCliente());
            st.setString(2, cliente.getCiudad());
            st.setString(3, cliente.getCalle());
            st.setString(4, cliente.getSeguroSocial());
            st.setString(5, seguroSocial);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    } 
} // END ClienteDAO
