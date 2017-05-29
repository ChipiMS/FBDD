/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banco;

/**
 *
 * @author ELÍAS MANCERA
 */
public class Transaccion {
    int numTran, cantidad;
    String fecha;
    int numCuenta;

    public Transaccion(int numTran, int cantidad, String fecha, int numCuenta) {
        this.numTran = numTran;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.numCuenta = numCuenta;
    }
    
    public Transaccion(int cantidad, String fecha, int numCuenta) {
        this.numTran = numTran;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.numCuenta = numCuenta;
    }

    public int getNumTran() {
        return numTran;
    }

    public void setNumTran(int numTran) {
        this.numTran = numTran;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }
    
}
