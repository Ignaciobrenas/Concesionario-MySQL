/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.DAOSQL;
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
    public static DAOSQL dao = new DAOSQL();

    public static void insertarVehiculo(Vehiculo v) throws IllegalArgumentException {

//        for (Vehiculo existente : vehiculos) {
//            if (existente.getMatricula().equalsIgnoreCase(v.getMatricula())) {
//                throw new IllegalArgumentException(
//                        "ya existe en el registro"
//                );
//    }
//        }
//
//        //lo añadimos al arraylist
//        vehiculos.add(v);
        if (dao.read(v.getMatricula()) != null) {
            throw new IllegalArgumentException("ya existe en el registro");
        }
        dao.insert(v);

        //lo añadimos al arraylist
        vehiculos.add(v);
        
    }

    public static Vehiculo buscarVehiculo(String matricula) {
//        for (Vehiculo v : vehiculos) {
//            if (v.getMatricula().equals(matricula)) {
//                return v;
//    }
//        }
//        return null;
        return dao.read(matricula);

    }
 
    public static ArrayList<Vehiculo> listarVehiculos() {
        return (ArrayList<Vehiculo>) dao.readAll();
    }

    public static void eliminarVehiculo(String matricula) throws IllegalArgumentException {
//        Vehiculo encontrado = buscarVehiculo(matricula);
//
//        if (encontrado == null) {
//            throw new IllegalArgumentException(
//                    "no existe en el registro"
//            );
//}
//        vehiculos.remove(encontrado);
//    }
        if (dao.read(matricula) == null) {
            throw new IllegalArgumentException("no existe en el registro");
        }
        dao.delete(matricula);
    }
}
