/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

import model.Vehiculo;

/**
 *
 * @author ignac
 */
public class Concesionario {

    public static ArrayList<Vehiculo> vehiculos = new ArrayList<>();

    public static void insertarVehiculo(Vehiculo v) throws IllegalArgumentException {

        for (Vehiculo existente : vehiculos) {
            if (existente.getMatricula().equalsIgnoreCase(v.getMatricula())) {
                throw new IllegalArgumentException(
                        "ya existe en el registro"
                );
            }
        }

        //lo añadimos al arraylist
        vehiculos.add(v);
        
    }

        public static Vehiculo buscarVehiculo(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v.getMatricula().equals(matricula)) {
                return v;
            }
        }
        return null;
        
    }
 
    public static ArrayList<Vehiculo> listarVehiculos() {
        return vehiculos;
        
        
    }
 
    public static void eliminarVehiculo(String matricula) throws IllegalArgumentException {
        Vehiculo encontrado = buscarVehiculo(matricula);
        
        if (encontrado == null) {
            throw new IllegalArgumentException(
                    "no existe en el registro"
            );
        }
        vehiculos.remove(encontrado);
    }
}