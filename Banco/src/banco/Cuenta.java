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
public class Cuenta {

    int numCuenta, numSucursal;
    double saldo;
    String seguroSocial;

    public Cuenta(int numCuenta, double saldo, String seguroSocial, int numSucursal) {
        this.numCuenta = numCuenta;
        this.numSucursal = numSucursal;
        this.saldo = saldo;
        this.seguroSocial = seguroSocial;
    }

    public Cuenta(double saldo, String seguroSocial, int numSucursal) {
        this.numSucursal = numSucursal;
        this.saldo = saldo;
        this.seguroSocial = seguroSocial;
    }

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }

    public int getNumSucursal() {
        return numSucursal;
    }

    public void setNumSucursal(int numSucursal) {
        this.numSucursal = numSucursal;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSeguroSocial() {
        return seguroSocial;
    }

    public void setSeguroSocial(String seguroSocial) {
        this.seguroSocial = seguroSocial;
    }

    @Override
    public String toString() {
        return this.numCuenta + "";
    }
}
