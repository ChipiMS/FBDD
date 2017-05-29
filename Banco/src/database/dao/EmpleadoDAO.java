/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.dao;

import banco.Empleado;
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
public class EmpleadoDAO {

    int nextId;
    Connection conn;

    public EmpleadoDAO(Connection conn) {
        this.conn = conn;
        nextId = 0;
        try {
            String query = "SELECT * FROM empleado";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int aux = rs.getInt("numEmpleado");
                if (aux > nextId) {
                    nextId = aux;
                }
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        nextId++;
    }

    public ObservableList<Empleado> findAll() {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM empleado";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                empleados.add(new Empleado(rs.getInt("numEmpleado"), rs.getString("nombreEmpleado"), rs.getString("telefono"), rs.getString("numSucursal")));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return empleados;
    }

    public Boolean delete(int numEmpleado) {
        try {
            String query = "delete from empleado where numEmpleado = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, numEmpleado);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Empleado empleado) {
        try {
            String query = "insert into empleado "
                    + " (numEmpleado,nombreEmpleado,telefono,numSucursal)"
                    + " values (?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, nextId);
            st.setString(2, empleado.getNombreEmpleado());
            st.setLong(3, Long.parseLong(empleado.getTelefono()));
            System.out.println("Parseo Correcto " + Long.parseLong(empleado.getTelefono()));
            st.setString(4, empleado.getNumSucursal());
            nextId++;
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Empleado empleado) {
        try {
            String query = "update empleado "
                    + " set nombreEmpleado = ?,telefono = ?,numSucursal = ? "
                    + " where numEmpleado = ?";
            PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, empleado.getNombreEmpleado());
            st.setString(2, empleado.getTelefono());
            st.setString(3, empleado.getNumSucursal());
            st.setInt(4, empleado.getNumEmpleado());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
} // END EmpleadoDAO
