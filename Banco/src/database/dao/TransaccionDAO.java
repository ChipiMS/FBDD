/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dao;

import banco.Cliente;
import banco.Cuenta;
import banco.Transaccion;
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
public class TransaccionDAO {
    int nextId;
    Connection conn;
    
    public TransaccionDAO(Connection conn){
        this.conn = conn;
        nextId = 0;
        try {
            String query = "SELECT * FROM transaccion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                int aux = rs.getInt("numTran");
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
    
    public List<Cuenta> findAllCuenta() {
        List<Cuenta> cuentas = new ArrayList<Cuenta>();
        try {
            String query = "SELECT * FROM cuenta";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                cuentas.add(new Cuenta(rs.getInt("numCuenta"),rs.getDouble("saldo"), rs.getString("seguroSocial") , rs.getInt("numSucursal")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return cuentas;
    }
    
    public ObservableList<Transaccion> findAll() {
        ObservableList<Transaccion> transacciones = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM transaccion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                transacciones.add(new Transaccion(rs.getInt("numTran"), rs.getInt("cantidad"),rs.getString("fecha"),rs.getInt("numCuenta")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return transacciones;
    }
    public Boolean delete(int numTran) {
        try {
            String query = "delete from transaccion where numTran = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, numTran);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(Transaccion transaccion) {
        try {
            String query = "insert into transaccion "
                    + " (numTran,cantidad,fecha,numCuenta)"
                    + " values (?,?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, nextId);
            st.setDouble(2, transaccion.getCantidad());
            st.setString(3, transaccion.getFecha());
            st.setInt(4, transaccion.getNumCuenta());
            nextId++;
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    public Boolean update(Transaccion transaccion) {
        try {
            String query = "update transaccion "
                    + " set cantidad = ?,fecha = ?,numCuenta = ? "
                    + " where numTran = ?";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setDouble(1, transaccion.getCantidad());
            st.setString(2, transaccion.getFecha());
            st.setInt(3, transaccion.getNumCuenta());
            st.setInt(4, transaccion.getNumTran());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
