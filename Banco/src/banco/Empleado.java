/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

/**
 *
 * @author aldoea
 */
public class Empleado {
    int numEmpleado;
    String nombreEmpleado,telefono,numSucursal;

    public Empleado(int numEmpleado, String nombreEmpleado, String telefono, String numSucursal) {
        this.numEmpleado = numEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.telefono = telefono;
        this.numSucursal = numSucursal;
    }

    public Empleado(String nombreEmpleado, String telefono, String numSucursal) {
        this.nombreEmpleado = nombreEmpleado;
        this.telefono = telefono;
        this.numSucursal = numSucursal;
    }

    public int getNumEmpleado() {
        return numEmpleado;
    }

    public void setNumEmpleado(int numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumSucursal() {
        return numSucursal;
    }

    public void setNumSucursal(String numSucursal) {
        this.numSucursal = numSucursal;
    }

}
