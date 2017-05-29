/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dao;

import banco.Cliente;
import banco.Cuenta;
import banco.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ELÍAS MANCERA
 */
public class CuentaDAO {
    int nextId;
    Connection conn;
    
    public CuentaDAO(Connection conn){
        this.conn = conn;
        nextId = 0;
        try {
            String query = "SELECT * FROM cuenta";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                int aux = rs.getInt("numCuenta");
                if(aux > nextId)
                    nextId = aux;
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        nextId++;
    }
    
    public List<Sucursal> findAllSuc() {
        List<Sucursal> sucursales = new ArrayList<Sucursal>();
        try {
            String query = "SELECT * FROM sucursal";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                sucursales.add(new Sucursal(rs.getInt("numSucursal"),rs.getString("nombreSucursal"), rs.getString("ciudad") , rs.getString("direccion")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return sucursales;
    }
    
    public List<Cliente> findAllCliente() {
        List<Cliente> clientes = new ArrayList<Cliente>();
        try {
            String query = "SELECT * FROM cliente";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            
            while(rs.next()) {
                clientes.add(new Cliente(rs.getInt("seguroSocial"),rs.getString("nombreCliente"), rs.getString("ciudad") , rs.getString("calle")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return clientes;
    }
    
    
    public ObservableList<Cuenta> findAll() {
        ObservableList<Cuenta> cuentas = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM cuenta";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                cuentas.add(new Cuenta(rs.getInt("numCuenta"), rs.getDouble("saldo"),rs.getString("seguroSocial"),rs.getInt("numSucursal")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return cuentas;
    }
    public Boolean delete(int numCuenta) {
        try {
            String query = "delete from cuenta where numCuenta = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, numCuenta);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(Cuenta cuenta) {
        try {
            String query = "insert into cuenta "
                    + " (numCuenta,saldo,seguroSocial,numSucursal)"
                    + " values (?,?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, nextId);
            st.setDouble(2, cuenta.getSaldo());
            st.setString(3, cuenta.getSeguroSocial());
            st.setInt(4, cuenta.getNumSucursal());
            nextId++;
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    public Boolean update(Cuenta cuenta) {
        try {
            String query = "update cuenta "
                    + " set saldo = ?,seguroSocial = ?,numSucursal = ? "
                    + " where numCuenta = ?";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setDouble(1, cuenta.getSaldo());
            st.setString(2, cuenta.getSeguroSocial());
            st.setInt(3, cuenta.getNumSucursal());
            st.setInt(4, cuenta.getNumCuenta());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
