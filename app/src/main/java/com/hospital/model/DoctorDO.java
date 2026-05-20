package com.hospital.model;

public class DoctorDO {
    
    // SE DECLARAN LAS VARIABLES

    private int id_doctor;
    private String telefono;
    private String nombre;
    private String direccion;
    private String correo_electronico;
    private String especialidad;

    private int salario_id_salario;

    public DoctorDO(String telefono, String nombre, String direccion,
        String correo_electronico, String especialidad, int salario_id_salario) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.direccion = direccion;
        this.correo_electronico = correo_electronico;
        this.especialidad = especialidad;
        this.salario_id_salario = salario_id_salario;
    }

    // SE HACEN LOS GETTERS Y SETTERS

    public int getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(int id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public int getSalario_id_salario() {
        return salario_id_salario;
    }

    public void setSalario_id_salario(int salario_id_salario) {
        this.salario_id_salario = salario_id_salario;
    }
}