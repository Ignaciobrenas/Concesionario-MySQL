/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Vehiculo;

/**
 *
 * @author ignac
 */
public class Fichero {

     public static String nombreArchivo = "archivo.txt";
    
    public Fichero(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        //crea el archivo si no existe
        
        File archivo = new File(nombreArchivo);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                System.out.println("archivo  creado correctamente.");
            } catch (IOException e) {
                System.out.println("Error al crear el archivo " + e.getMessage());
                return;
            }
        }
    }

    
    public ArrayList<Vehiculo> cargar() throws IOException {
        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        
        BufferedReader leerTXT = new BufferedReader(new FileReader(nombreArchivo));
        String linea;
        
        while ((linea = leerTXT.readLine()) != null) {
            String[] datos = linea.split(";");
            
            //cada posicion del array es un atributo
            //convierto el precio a double 
            Vehiculo v = new Vehiculo(datos[0], datos[1], datos[2], Double.parseDouble(datos[3]), datos[4]);
            vehiculos.add(v);
        }
        
        leerTXT.close();
        return vehiculos;
    }
    
    public  void guardar(ArrayList<Vehiculo> vehiculos) throws IOException {
        BufferedWriter escribirTXT = new BufferedWriter(new FileWriter(nombreArchivo));
        
        for (Vehiculo v : vehiculos) {
            escribirTXT.write(v.getMatricula() + ";" + v.getMarca() + ";" + v.getModelo() + ";" + v.getPrecio() + ";" + v.getTipo());
        }
        
        escribirTXT.close();
    }

}
