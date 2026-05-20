package com.hospital.panels;

import com.hospital.model.DoctorDAO;
import com.hospital.model.SalarioDAO;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class FormBuscarInternet extends GridPane {

    private TextField tfBuscador;
    private Button btnBuscar;
    private TextArea taResultado;
    private Label lblTitulo;
    private Label lblRating;
    private Button btnGuardar;

    private String apiKey = "";
    private String[] datosDoctor = new String[7]; 

    public FormBuscarInternet() {
        try {
            Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\daw\\Documents\\VISUAL_STUDIO\\PROYECTO_HOSPITAL\\.env").load();
            this.apiKey = dotenv.get("OMDB_API_KEY");
        } catch (Exception e) {
            System.err.println("No se pudo cargar el archivo .env: " + e.getMessage());
        }

        this.setHgap(10);
        this.setVgap(8);
        this.setPadding(new Insets(20));

        tfBuscador = new TextField();
        tfBuscador.setPromptText("Introduce el nombre del doctor...");
        
        btnBuscar = new Button("Buscar Doctor");
        
        taResultado = new TextArea();
        taResultado.setEditable(false);
        taResultado.setPrefSize(380, 220);

        lblTitulo = new Label("Doctor seleccionado: -");
        lblRating = new Label("Salario Propuesto: -");

        btnGuardar = new Button("Guardar Doctor y Salario en BD");
        btnGuardar.setDisable(true);

        this.add(new Label("Buscador de Doctores (Internet):"), 0, 0);
        this.add(tfBuscador, 1, 0);
        this.add(btnBuscar, 2, 0);
        this.add(taResultado, 0, 1, 3, 1);
        this.add(lblTitulo, 0, 2, 3, 1);
        this.add(lblRating, 0, 3, 3, 1);
        this.add(btnGuardar, 0, 4, 3, 1);

        btnBuscar.setOnAction(e -> ejecutarPeticionHTTP());
        btnGuardar.setOnAction(e -> guardarEnBaseDatos());
    }

    private void ejecutarPeticionHTTP() {
        String busqueda = tfBuscador.getText().trim();
        if (busqueda.isEmpty()) return;

        if (this.apiKey == null || this.apiKey.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Configuración", "Falta la clave OMDB_API_KEY en tu .env");
            return;
        }

        String textoFormateado = busqueda.replace(" ", "+");
        String urlCompleta = "https://www.omdbapi.com/?t=" + textoFormateado + "&apikey=" + this.apiKey;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlCompleta))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            if (jsonResponse.contains("\"Response\":\"False\"")) {
                taResultado.setText("entidad no encontrada");
                lblTitulo.setText("Doctor seleccionado: -");
                lblRating.setText("Salario Propuesto: -");
                btnGuardar.setDisable(true);
            } else {
                String title = extraerCampoJson(jsonResponse, "Title");
                String director = extraerCampoJson(jsonResponse, "Director");
                String year = extraerCampoJson(jsonResponse, "Year");
                String rating = extraerCampoJson(jsonResponse, "imdbRating");

                datosDoctor[0] = "7001"; 
                datosDoctor[1] = "Dr. " + title;
                datosDoctor[2] = "Calle Med / " + director;
                datosDoctor[3] = title.toLowerCase().replace(" ", "") + "@hosp.com";
                
                int anioInt = 2000;
                try {
                    anioInt = Integer.parseInt(year.substring(0, 4));
                } catch(Exception e){}

                if (anioInt % 3 == 0) {
                    datosDoctor[4] = "Cardiología";
                } else if (anioInt % 3 == 1) {
                    datosDoctor[4] = "Neurología";
                } else {
                    datosDoctor[4] = "Medicina interna";
                }

                // Cálculo controlado del monto salarial con decimales
                double salarioCalculado = 2100.00;
                try {
                    salarioCalculado = Double.parseDouble(rating) * 285.35; 
                } catch (Exception e) {}
                
                datosDoctor[5] = String.format(java.util.Locale.US, "%.2f", salarioCalculado);
                
                datosDoctor[6] = "Salario Normal";

                Date fechaActual = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = sdf.format(fechaActual);

                StringBuilder sb = new StringBuilder();
                sb.append("*****************************\n");
                sb.append("Telefono: ").append(datosDoctor[0]).append("\n");
                sb.append("Nombre: ").append(datosDoctor[1]).append("\n");
                sb.append("Dirección: ").append(datosDoctor[2]).append("\n");
                sb.append("Correo Electrónico: ").append(datosDoctor[3]).append("\n");
                sb.append("Especialidad: ").append(datosDoctor[4]).append("\n");
                sb.append("Monto: ").append(datosDoctor[5]).append("\n");
                sb.append("Fecha Pago: ").append(fechaFormateada).append("\n");
                sb.append("Tipo Salario: ").append(datosDoctor[6]).append("\n");
                sb.append("*****************************");

                taResultado.setText(sb.toString());

                lblTitulo.setText("Doctor seleccionado: " + datosDoctor[1]);
                lblRating.setText("Salario Propuesto: " + datosDoctor[5] + " €");

                btnGuardar.setDisable(false); 
            }

        } catch (Exception ex) {
            mostrarAlerta(AlertType.ERROR, "Error de Red", "No se pudo conectar al servidor: " + ex.getMessage());
        }
    }

    private void guardarEnBaseDatos() {
        if (datosDoctor[1] == null) return;

        double monto = Double.parseDouble(datosDoctor[5]);
        Date fechaActual = new Date(System.currentTimeMillis());

        try (SalarioDAO sDao = new SalarioDAO();
             DoctorDAO dDao = new DoctorDAO()) {

            int idSalarioGenerado = sDao.insertar(monto, fechaActual, datosDoctor[6]);

            if (idSalarioGenerado != -1) {
                int columnasDoctor = dDao.insertar(
                        datosDoctor[0],
                        datosDoctor[1],
                        datosDoctor[2],
                        datosDoctor[3],
                        datosDoctor[4],
                        idSalarioGenerado
                );

                if (columnasDoctor > 0) {
                    mostrarAlerta(AlertType.INFORMATION, "Inserción Exitosa", "¡Registro completado con éxito en la base de datos!");
                    btnGuardar.setDisable(true); 
                } else {
                    mostrarAlerta(AlertType.ERROR, "Error JDBC", "No se pudo registrar la ficha del doctor.");
                }
            } else {
                mostrarAlerta(AlertType.ERROR, "Error JDBC", "La restricción de tipo_salario o monto rechazó la inserción.");
            }

        } catch (Exception ex) {
            mostrarAlerta(AlertType.ERROR, "Error de Base de Datos", "Problema de restricción en la transacción: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String extraerCampoJson(String json, String campo) {
        String clave = "\"" + campo + "\":\"";
        int inicio = json.indexOf(clave);
        if (inicio == -1) return "No disponible";
        inicio += clave.length();
        int fin = json.indexOf("\"", inicio);
        return json.substring(inicio, fin);
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}