package com.hospital;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.hospital.model.DoctorDO;
import com.hospital.model.SalarioDO;
import com.hospital.panels.FormEntidad;
import com.hospital.panels.MostrarListado;
import com.hospital.panels.FormBuscarInternet;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    public static ArrayList<DoctorDO> listaDoctores = new ArrayList<>();
    public static ArrayList<SalarioDO> listaSalarios = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        FormEntidad fDoctor = new FormEntidad();
        MostrarListado fListado = new MostrarListado();
        FormBuscarInternet fBuscarAPI = new FormBuscarInternet();
        
        TabPane tPane = new TabPane();
        MenuBar mbPrincipal = new MenuBar();
        
        Tab tDoctor = new Tab("Crear doctor");
        Tab tListado = new Tab("Mostrar listado");
        Tab tBuscarAPI = new Tab("Buscar en internet");
        
        Menu mArchivo = new Menu("Archivo");
        Menu mVer = new Menu("Ver");
        Menu mAyuda = new Menu("Ayuda");
        MenuItem miNuevoDoctor = new MenuItem("Nuevo doctor..");
        MenuItem miExportarListado = new MenuItem("Exportar listado...");
        MenuItem miImportarListado = new MenuItem("Importar listado...");
        MenuItem miCerrar = new MenuItem("Cerrar..");
        MenuItem miVer = new MenuItem("Listado...");
        MenuItem miAyuda = new MenuItem("Acerca de...");
        SeparatorMenuItem separador = new SeparatorMenuItem();
        
        mArchivo.getItems().addAll(miNuevoDoctor, miExportarListado, miImportarListado, separador, miCerrar);
        mVer.getItems().add(miVer);
        mAyuda.getItems().add(miAyuda);
        mbPrincipal.getMenus().addAll(mArchivo, mVer, mAyuda);
        
        tDoctor.setClosable(false);
        tListado.setClosable(false);
        tBuscarAPI.setClosable(false);
        
        // Añadimos las tres pestañas al contenedor
        tPane.getTabs().addAll(tDoctor, tListado, tBuscarAPI);
        
        tDoctor.setContent(fDoctor);
        tListado.setContent(fListado);
        tBuscarAPI.setContent(fBuscarAPI);

        // EVENTOS
        miExportarListado.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exportar");
            fileChooser.setInitialFileName("documento.txt");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Texto", "*.txt")
            );

            File fichero = fileChooser.showSaveDialog(stage);

            if (fichero != null) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero))){
                    String textoAExportar = fListado.taListaDoctores.getText();

                    bw.write(textoAExportar);

                    String[] bloques = textoAExportar.split("\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*");
                    int contadorEntidades = (bloques.length -1) /2;

                    if (textoAExportar.trim().isEmpty()) {
                        contadorEntidades = 0;
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportación Exitosa");
                    alert.setHeaderText(null);
                    alert.setContentText("Exportado: " + contadorEntidades + " entidades.");
                    alert.showAndWait();

                } catch (IOException ex) {
                    System.err.println("Error al guardar: " + ex.getMessage());
                }
            }
        });

        miImportarListado.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importar");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Texto", "*.txt")
            );

            File fichero = fileChooser.showOpenDialog(stage);

            if (fichero != null) {
                StringBuilder sb = new StringBuilder();
                int contadorEntidades = 0;

                try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        sb.append(linea).append("\n");

                        if (linea.equals("*****************************")) {
                            contadorEntidades++;
                        }
                    }

                    contadorEntidades = contadorEntidades / 2;

                    fListado.taListaDoctores.setText(sb.toString());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Importación Exitosa");
                    alert.setHeaderText(null);
                    alert.setContentText("Importado: " + contadorEntidades + " entidades.");
                    alert.showAndWait();
            
                } catch (IOException ex) {
                    fListado.taListaDoctores.setText("Error: " + ex.getMessage());
                }
            }
        });

        miCerrar.setOnAction(e -> {
            stage.close();
        });
        miNuevoDoctor.setOnAction(e -> {
            tPane.getSelectionModel().select(tDoctor);
            fDoctor.reset();
        });
        miVer.setOnAction(e -> {
            tPane.getSelectionModel().select(tListado);
        });
        miAyuda.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alejandro Wood");
            alert.setHeaderText(null);
            alert.setContentText("Aplicacion Hospital.");
            alert.showAndWait();
        });

        BorderPane panelPrincipal = new BorderPane();
        panelPrincipal.setTop(mbPrincipal);
        panelPrincipal.setCenter(tPane);

        var scene = new Scene(panelPrincipal, 480, 460);
        stage.setScene(scene);
        stage.setTitle("Gestión del Hospital");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}