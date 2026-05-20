package com.hospital.model;

import java.sql.Date;

public class SalarioDO {

    private int id_salario;
    private double monto;
    private Date fecha_pago;
    private String tipo_salario;

    public SalarioDO(double monto, Date fecha_pago, String tipo_salario) {
        this.monto = monto;
        this.fecha_pago = fecha_pago;
        this.tipo_salario = tipo_salario;
    }

    // SE HACEN LOS GETTERS Y SETTERS

    public int getId_salario() {
        return id_salario;
    }

    public void setId_salario(int id_salario) {
        this.id_salario = id_salario;
    }

   public double getMonto() {
       return monto;
   }

   public void setMonto(double monto) {
       this.monto = monto;
   }

   public Date getFecha_pago() {
       return fecha_pago;
   }

   public void setFecha_pago(Date fecha_pago) {
       this.fecha_pago = fecha_pago;
   }

   public String getTipo_salario() {
       return tipo_salario;
   }

   public void setTipo_salario(String tipo_salario) {
       this.tipo_salario = tipo_salario;
   }
}