package com.hospital.panels;

import java.time.LocalDate;

import com.hospital.model.DoctorDAO;
import com.hospital.model.SalarioDAO;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class FormEntidad extends GridPane{
    // VARIABLE MIEMBRO DEL GRIDPANE
    private ToggleGroup grupoTipoSalario;
    private Label lblTelefono;
    private Label lblNombre;
    private Label lblDireccion;
    private Label lblCorreo;
    private Label lblEspecialidad;
    private Label lblMonto;
    private Label lblFechaPago;
    private Label lblTipoSalario;
    private double valorExacto;

    public TextField txtTelefono;
    public TextField txtNombre;
    public TextField txtDireccion;
    public TextField txtCorreo;
    public ComboBox<String> cmbEspecialidad;
    public Slider sldMonto;
    public DatePicker dpFechaPago;
    public RadioButton rbTipoSalario1;
    public RadioButton rbTipoSalario2;
    public RadioButton rbTipoSalario3;
    public RadioButton rbTipoSalarioSeleccionado;
    public Button btnReset;
    public Button btnSave;

    public FormEntidad() {
        // CREACIÓN DE LOS CONTROLES
        lblTelefono = new Label("Teléfono");
        lblNombre = new Label("Nombre");
        lblDireccion = new Label("Dirección");
        lblCorreo = new Label("Correo Electrónico");
        lblEspecialidad = new Label("Especialidad");
        
        lblFechaPago = new Label("Fecha del Pago");
        lblTipoSalario = new Label("Tipo de Salario");

        txtTelefono = new TextField();
        txtNombre = new TextField();
        txtDireccion = new TextField();
        txtCorreo = new TextField();
        cmbEspecialidad = new ComboBox<String>();
        sldMonto = new Slider(1221, 3000, 1221);
        dpFechaPago = new DatePicker(LocalDate.now());
        rbTipoSalario1 = new RadioButton("Salario Normal");
        rbTipoSalario2 = new RadioButton("Dietas");
        rbTipoSalario3 = new RadioButton("Paga Extraordinaria");
        btnReset = new Button("Limpiar");
        btnSave = new Button("Guardar");

        sldMonto.setMajorTickUnit(0.01);
        sldMonto.setMinorTickCount(0);
        sldMonto.setSnapToTicks(true);
        sldMonto.setBlockIncrement(0.01);

        sldMonto.valueProperty().addListener((obs, oldVal, newVal) -> {
            // Esto limpia cualquier "ruido" decimal de Java
            valorExacto = Math.round(newVal.doubleValue() * 100.0) / 100.0;
    
            // Ahora usa 'valorExacto' para lo que necesites
            lblMonto.setText("Monto: " + valorExacto);
        });


        // ORGANIZACIÓN DE ESPACIOS
        this.setHgap(10);
        this.setVgap(8);
        this.setPadding(new Insets(20));
        sldMonto.setMaxWidth(400);

        // AÑADIMOS DATOS AL COMBOBOX
        cmbEspecialidad.getItems().addAll("Medicina interna", "Cardiología", 
            "Neumología", "Neurología", "Pediatría", "Traumatología", "Cirugía general"
        );

        // CONFIGURAMOS EL RADIOBUTTON
        grupoTipoSalario = new ToggleGroup();

        rbTipoSalario1.setToggleGroup(grupoTipoSalario);
        rbTipoSalario2.setToggleGroup(grupoTipoSalario);
        rbTipoSalario3.setToggleGroup(grupoTipoSalario);

        rbTipoSalario1.setSelected(true);
        this.cmbEspecialidad.getSelectionModel().selectFirst();

        lblMonto = new Label("Monto: " + (double) sldMonto.getValue());

        // AÑADIMOS LOS ELEMENTOS AL GRIDPANE
        this.add(lblTelefono, 0, 0);
        this.add(txtTelefono, 1, 0);
        this.add(lblNombre, 0, 1);
        this.add(txtNombre, 1, 1);
        this.add(lblDireccion, 0, 2);
        this.add(txtDireccion, 1, 2);
        this.add(lblCorreo, 0, 3);
        this.add(txtCorreo, 1, 3);
        this.add(lblEspecialidad, 0, 4);
        this.add(cmbEspecialidad, 1, 4);
        this.add(lblMonto, 0, 5);
        this.add(sldMonto, 0, 6, 2, 1);
        this.add(lblFechaPago, 0, 7);
        this.add(dpFechaPago, 1, 7);
        this.add(lblTipoSalario, 0,8);
        this.add(rbTipoSalario1, 0, 9, 2, 1);
        this.add(rbTipoSalario2, 0, 10, 2, 1);
        this.add(rbTipoSalario3, 0, 11, 2, 1);
        this.add(btnSave, 0, 12);
        this.add(btnReset, 1, 12);

        // EVENTOS
        sldMonto.setOnMouseDragged(e -> {
            sldMonto.valueProperty().addListener((obs, oldVal, newVal) -> {
                double valorExacto = Math.round(newVal.doubleValue() * 100.0) / 100.0;
    
                lblMonto.setText("Monto: " + valorExacto);
            });
        });

        btnReset.setOnAction(e -> {
            reset();
        });

        btnSave.setOnAction(e -> {
            int resultado = guardar();

            if (resultado == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error En la Operacion");
                alert.setHeaderText(null); // sin cabecera
                alert.setContentText("El registro no se ha guardado correctamente.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Operacion completada");
                alert.setHeaderText(null); // sin cabecera
                alert.setContentText("El registro se ha guardado correctamente.");
                alert.showAndWait();
            }
        });
    }

    // SE RESETEA EL FORMULARIO
    public void reset() {
        this.txtTelefono.clear();
        this.txtNombre.clear();
        this.txtDireccion.clear();
        this.txtCorreo.clear();
        this.cmbEspecialidad.getSelectionModel().selectFirst();
        sldMonto.setValue(1221);
        dpFechaPago.setValue(LocalDate.now());
        rbTipoSalario1.setSelected(true);
    }

    private String obtenerTipoSalario() {
        RadioButton rbSeleccionado = (RadioButton) grupoTipoSalario.getSelectedToggle();
        return rbSeleccionado.getText();
    }

    // SE GUARDA EL FORMULARIO
    /**
     * * @return
     */
    private int guardar() {
        int resultado = -1;
        
        try (SalarioDAO salarioDAO = new SalarioDAO();
             DoctorDAO doctorDAO = new DoctorDAO()) {
            
            int idSalarioGenerado = salarioDAO.insertar(
                valorExacto,
                java.sql.Date.valueOf(dpFechaPago.getValue()),
                obtenerTipoSalario()
            );

            if (idSalarioGenerado != -1) {
                resultado = doctorDAO.insertar(
                    txtTelefono.getText(),
                    txtNombre.getText(),
                    txtDireccion.getText(),
                    txtCorreo.getText(),
                    cmbEspecialidad.getSelectionModel().getSelectedItem(),
                    idSalarioGenerado
                );
            } else {
                System.out.println("Error: No se pudo generar el salario, abortando creación del doctor.");
            }

        } catch (Exception e) {
            System.out.println("Error en el proceso de guardado:");
            e.printStackTrace();
        }
        
        return resultado;
    }
}