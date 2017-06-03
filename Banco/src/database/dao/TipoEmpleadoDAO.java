package database.dao;
import banco.Empleado;
import banco.TipoEmpleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class TipoEmpleadoDAO {
    Connection conn;
    
    public TipoEmpleadoDAO(Connection conn){
        this.conn = conn;
    }
    public boolean isDuplicated(String clave){
        boolean duplicated = false;
        try {
            String query = "SELECT claveTipoEmpleado FROM tipoEmpleado";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                if(rs.getString("claveTipoEmpleado").equals(clave))
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
    public ObservableList<TipoEmpleado> findAll() {
        ObservableList<TipoEmpleado> elements = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM tipoEmpleado";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                elements.add(new TipoEmpleado(rs.getString("claveTipoEmpleado"),rs.getString("descripcion")));
            }
            rs.close();
            st.close();
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return elements;
    }
    public Boolean delete(String clave) {
        try {
            String query = "delete from tipoEmpleado where claveTipoEmpleado = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, clave);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(TipoEmpleado tipo) {
        try {
            String query = "insert into tipoEmpleado "
                    + " (claveTipoEmpleado,descripcion)"
                    + " values (?,?)";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, tipo.getClave());
            st.setString(2, tipo.getDescripcion());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    public Boolean update(TipoEmpleado tipo) {
        try {
            String query = "update tipoEmpleado "
                    + " set descripcion = ?"
                    + " where claveTipoEmpleado = ?";
            PreparedStatement st =  conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, tipo.getDescripcion());
            st.setString(2, tipo.getClave());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
