package database.dao;
import banco.Transaccion;
import banco.TipoTransaccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class TipoTransaccionDAO {
    Connection conn;
    
    public TipoTransaccionDAO(Connection conn){
        this.conn = conn;
    }
    public boolean isDuplicated(String clave){
        boolean duplicated = false;
        try {
            String query = "SELECT claveTipoTransaccion FROM tipoTransaccion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                if(rs.getString("claveTipoTransaccion").equals(clave))
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
    public ObservableList<TipoTransaccion> findAll() {
        ObservableList<TipoTransaccion> elements = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM tipoTransaccion";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                elements.add(new TipoTransaccion(rs.getString("claveTipoTransaccion"),rs.getString("descripcion")));
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
            String query = "delete from tipoTransaccion where claveTipoTransaccion = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, clave);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Boolean insert(TipoTransaccion tipo) {
        try {
            String query = "insert into tipoTransaccion "
                    + " (claveTipoTransaccion,descripcion)"
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
    public Boolean update(TipoTransaccion tipo) {
        try {
            String query = "update tipoTransaccion "
                    + " set descripcion = ?"
                    + " where claveTipoTransaccion = ?";
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
