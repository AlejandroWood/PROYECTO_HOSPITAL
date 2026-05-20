package com.hospital.panels;


import com.hospital.App;
import com.hospital.model.DoctorDAO;
import com.hospital.model.DoctorDO;
import com.hospital.model.SalarioDO;
import com.hospital.model.SalarioDAO;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class MostrarListado extends GridPane{
    public TextArea taListaDoctores;
    private ScrollPane ScrollLista;
    public Button btnRecargar;

    public MostrarListado() {
        taListaDoctores = new TextArea();
        ScrollLista = new ScrollPane();
        btnRecargar = new Button("Recargar");

        taListaDoctores.setEditable(false);

        this.ScrollLista.setContent(taListaDoctores);

        this.setHgap(10);
        this.setVgap(8);
        this.setPadding(new Insets(20));
        ScrollLista.setPrefHeight(400);
        taListaDoctores.setPrefHeight(400);

        this.add(ScrollLista,0,0);
        this.add(btnRecargar,0,1);

        btnRecargar.setOnAction(e -> {
            cargarListaDoctores();
        });
    }

    private void cargarListaDoctores() {
        taListaDoctores.clear();

        try (DoctorDAO dDao = new DoctorDAO();
            SalarioDAO sDao = new SalarioDAO()) {
         
            App.listaDoctores = dDao.listar();
            App.listaSalarios = sDao.listar();

            for (DoctorDO doctor : App.listaDoctores) {
            
                SalarioDO salarioCorrespondiente = null;
                for (SalarioDO salario : App.listaSalarios) {
                   if (salario.getId_salario() == doctor.getSalario_id_salario()) {
                        salarioCorrespondiente = salario;
                        break;
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("*****************************\n");
                sb.append("Id Doctor: ").append(doctor.getSalario_id_salario()).append("\n"); // O el getter de su ID propio si lo tienes
                sb.append("Telefono: ").append(doctor.getTelefono()).append("\n");
                sb.append("Nombre: ").append(doctor.getNombre()).append("\n");
                sb.append("Dirección: ").append(doctor.getDireccion()).append("\n");
                sb.append("Correo Electrónico: ").append(doctor.getCorreo_electronico()).append("\n");
                sb.append("Especialidad: ").append(doctor.getEspecialidad()).append("\n");

                // Si encontramos su salario, mostramos los datos. Si no, avisamos.
                if (salarioCorrespondiente != null) {
                    sb.append("Id Salario: ").append(salarioCorrespondiente.getId_salario()).append("\n");
                    sb.append("Monto: ").append(salarioCorrespondiente.getMonto()).append("\n");
                    sb.append("Fecha Pago: ").append(salarioCorrespondiente.getFecha_pago()).append("\n");
                    sb.append("Tipo Salario: ").append(salarioCorrespondiente.getTipo_salario()).append("\n");
                } else {
                    sb.append("Salario: No asignado o no encontrado\n");
                }
            
                sb.append("*****************************\n\n");

                taListaDoctores.appendText(sb.toString());
            }

        } catch (Exception e) {
            System.out.println("Error al cargar la lista de doctores en la interfaz");
            e.printStackTrace();
        }
    }

}