package database.dao;
import banco.Sucursal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class SucursalDAO {
    int nextId;
    Connection conn;
    public SucursalDAO(Connection conn){
        this.conn = conn;
        nextId = 0;
        try {
            String query = "SELECT * FROM sucursal";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                int aux = rs.getInt("numSucursal");
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
    public ObservableList<Sucursal> findAll() {
        ObservableList<Sucursal> sucursales = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM sucursal";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                sucursales.add(new Sucursal(rs.getInt("numSucursal"), rs.getString("nombreSucursal"),rs.getString("ciudad"),rs.getString("direccion")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return sucursales;
    }
    public Boolean delete(int numSucursal) {
        try {
            String query = "delete from sucursal where numSucursal = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, numSucursal);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(Sucursal sucursal) {
        try {
            String query = "insert into sucursal "
                    + " (numSucursal,nombreSucursal,ciudad,direccion)"
                    + " values (?,?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, nextId);
            st.setString(2, sucursal.getNombreSucursal());
            st.setString(3, sucursal.getCiudad());
            st.setString(4, sucursal.getDireccion());
            nextId++;
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    public Boolean update(Sucursal sucursal) {
        try {
            String query = "update sucursal "
                    + " set nombreSucursal = ?,ciudad = ?,direccion = ? "
                    + " where numSucursal = ?";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, sucursal.getNombreSucursal());
            st.setString(2, sucursal.getCiudad());
            st.setString(3, sucursal.getDireccion());
            st.setInt(4, sucursal.getNumSucursal());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
