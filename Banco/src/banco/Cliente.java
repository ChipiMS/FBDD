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
public class Cliente {
    int seguroSocial;
    String nombreCliente,ciudad,calle;

    public Cliente(int seguroSocial, String nombreCliente, String ciudad, String calle) {
        this.seguroSocial = seguroSocial;
        this.nombreCliente = nombreCliente;
        this.ciudad = ciudad;
        this.calle = calle;
    }

    public Cliente(String nombreCliente, String ciudad, String calle) {
        this.nombreCliente = nombreCliente;
        this.ciudad = ciudad;
        this.calle = calle;
    }

    public int getSeguroSocial() {
        return seguroSocial;
    }

    public void setSeguroSocial(int seguroSocial) {
        this.seguroSocial = seguroSocial;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

} // END Cliente
