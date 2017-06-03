package banco;

public class Sucursal {

    int numSucursal;
    String nombreSucursal, ciudad, direccion;

    public Sucursal(int numSucursal, String nombreSucursal, String ciudad, String direccion) {
        this.numSucursal = numSucursal;
        this.nombreSucursal = nombreSucursal;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public Sucursal(String nombreSucursal, String ciudad, String direccion) {
        this.nombreSucursal = nombreSucursal;
        this.ciudad = ciudad;
        this.direccion = direccion;
    }

    public int getNumSucursal() {
        return numSucursal;
    }

    public void setNumSucursal(int numSucursal) {
        this.numSucursal = numSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return nombreSucursal;
    }

}
