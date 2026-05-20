package com.hospital.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hospital.utils.Db;

public class SalarioDAO implements AutoCloseable {
    
    private Connection con;

    public SalarioDAO() {
        this.con = Db.conectar();
    }

    public int insertar(double monto, Date fecha_pago, String tipo_salario) {
        int idGenerado = -1;
        try {
            String query = "insert into salario (monto, fecha_pago, tipo_salario) values (?, ?, ?)";

            PreparedStatement stmt = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setDouble(1, monto);
            stmt.setDate(2, fecha_pago);
            stmt.setString(3, tipo_salario);

            int filas = stmt.executeUpdate();
        
            if (filas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Hubo un problema con la BD al crear salario");
            e.printStackTrace();
        }
        return idGenerado;
    }

    public ArrayList<SalarioDO> listar() {
        ArrayList<SalarioDO> listarSalarios = new ArrayList<SalarioDO>();
        String query = "select * from salario";

        try (Statement stmt = this.con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                SalarioDO salDO = new SalarioDO(0, null, query);
            
                salDO.setId_salario(rs.getInt("id_salario"));
                salDO.setMonto(rs.getDouble("monto"));
                salDO.setFecha_pago(rs.getDate("fecha_pago"));
                salDO.setTipo_salario(rs.getString("tipo_salario"));
            
                listarSalarios.add(salDO);
            }

        } catch (SQLException e) {
            System.out.println("Hubo un problema con la BD al listar salarios");
            e.printStackTrace();
        }

        return listarSalarios;
    }

    @Override
    public void close() throws Exception {
        this.con.close();
    }
}