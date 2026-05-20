package com.hospital.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hospital.utils.Db;

public class DoctorDAO implements AutoCloseable {
    
    private Connection con;

    public DoctorDAO() {
        this.con = Db.conectar();
    }


    public int insertar(String telefono, String nombre, String direccion, String correo_electronico, String especialidad, int idSalario) {
        int columnasModificadas = -1;
        try {
            String query = "insert into doctor (telefono, nombre, direccion, correo_electronico, especialidad, salario_id_salario) values (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = this.con.prepareStatement(query);

            stmt.setString(1, telefono);
            stmt.setString(2, nombre);
            stmt.setString(3, direccion);
            stmt.setString(4, correo_electronico);
            stmt.setString(5, especialidad);
            stmt.setInt(6, idSalario);

            columnasModificadas = stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Hubo un problema con la BD al crear doctor");
            e.printStackTrace();
        }
        return columnasModificadas;
    }

    public ArrayList<DoctorDO> listar() {
        ArrayList<DoctorDO> listarDoctores = new ArrayList<DoctorDO>();
        String query = "select * from doctor";

        try (Statement stmt = this.con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Recorremos el resultset
            while (rs.next()) {
                DoctorDO docDO = new DoctorDO(query, query, query, query, query, 0);
            
                docDO.setId_doctor(rs.getInt("id_doctor"));
                docDO.setTelefono(rs.getString("telefono"));
                docDO.setNombre(rs.getString("nombre"));
                docDO.setDireccion(rs.getString("direccion"));
                docDO.setCorreo_electronico(rs.getString("correo_electronico"));
                docDO.setEspecialidad(rs.getString("especialidad"));
                docDO.setSalario_id_salario(rs.getInt("salario_id_salario"));
            
                listarDoctores.add(docDO);
            }

        } catch (SQLException e) {
            System.out.println("Hubo un problema con la BD al listar doctores");
            e.printStackTrace();
        }

        return listarDoctores;
    }

    @Override
    public void close() throws Exception {
        this.con.close();
    }
}