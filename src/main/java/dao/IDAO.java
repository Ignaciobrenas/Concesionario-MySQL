/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import model.Vehiculo;

/**
 *
 * @author ignac
 */
public interface IDAO {

    public abstract ArrayList<Vehiculo> readAll();

    public abstract Vehiculo read(String matricula);

    public abstract ArrayList<Vehiculo> readByTipo(String tipo);

    public abstract int insert(Vehiculo vehiculo);

    public abstract int update(Vehiculo vehiculo);

    public abstract int delete(String matricula);

    public abstract int deleteAll();

    public abstract int contar();
}
