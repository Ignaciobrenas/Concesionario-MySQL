/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Vehiculo;

/**
 *
 * @author ignac
 */
public class DAOSQL implements IDAO {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306";
    private final String JDBC_OPTS = "?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String JDBC_USER = "root";
    private final String JDBC_PASSWORD = "";

    private final String nombreBBDD = "concesionario";
    private final String nombreTABLA = "vehiculos";
    private final String JDBC_DDBB_TABLE = nombreBBDD + "." + nombreTABLA;

    private final String SeleccionarTODO = "SELECT * FROM " + JDBC_DDBB_TABLE + ";";
    private final String seleccionarPorMatricula = "SELECT * FROM " + JDBC_DDBB_TABLE + " WHERE matricula = ?;";
    private final String seleccionarPorTipo = "SELECT * FROM " + JDBC_DDBB_TABLE + " WHERE tipo = ?;";
    
    private final String SQL_INSERT = "INSERT INTO " + JDBC_DDBB_TABLE + " (matricula, marca, modelo, precio, tipo) VALUES (?, ?, ?, ?, ?);";
    private final String SQL_UPDATE = "UPDATE " + JDBC_DDBB_TABLE + " SET marca = ?, modelo = ?, precio = ?, tipo = ? WHERE matricula = ?;";
    private final String SQL_DELETE = "DELETE FROM " + JDBC_DDBB_TABLE + " WHERE matricula = ?;";
    private final String SQL_DELETE_ALL = "DELETE FROM " + JDBC_DDBB_TABLE + ";";

    private final String SQL_CREATE_DB = "CREATE DATABASE IF NOT EXISTS " + nombreBBDD + ";";
    private final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + JDBC_DDBB_TABLE + " ("
            + "matricula VARCHAR(10) PRIMARY KEY, "
            + "marca VARCHAR(50) NOT NULL, "
            + "modelo VARCHAR(50) NOT NULL, "
            + "precio DOUBLE NOT NULL, "
            + "tipo VARCHAR(30) NOT NULL);";

    @Override
    public ArrayList<Vehiculo> readAll() {
        ArrayList<Vehiculo> lista = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(SeleccionarTODO);
            while (rs.next()) {
                lista.add(new Vehiculo(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDouble("precio"),
                        rs.getString("tipo")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error en readAll: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public Vehiculo read(String matricula) {
        Vehiculo vehiculo = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(seleccionarPorMatricula);
            ps.setString(1, matricula);
            rs = ps.executeQuery();
            if (rs.next()) {
                vehiculo = new Vehiculo(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDouble("precio"),
                        rs.getString("tipo")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error en read: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return vehiculo;
    }

    @Override
    public ArrayList<Vehiculo> readByTipo(String tipo) {
        ArrayList<Vehiculo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(seleccionarPorTipo);
            ps.setString(1, tipo);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Vehiculo(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDouble("precio"),
                        rs.getString("tipo")
                ));
            }
        } catch (SQLException ex) {
            System.err.println("Error en readByTipo: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return null;
    }

    @Override
    public  int insert(Vehiculo v) {
        int filas = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(SQL_INSERT);
            ps.setString(1, v.getMatricula());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setDouble(4, v.getPrecio());
            ps.setString(5, v.getTipo());
            filas = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error en insert: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return filas;
    }

    @Override
    public int update(Vehiculo v) {
        int filas = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModelo());
            ps.setDouble(3, v.getPrecio());
            ps.setString(4, v.getTipo());
            ps.setString(5, v.getMatricula());
            filas = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error en update: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return filas;
    }

    @Override
    public int delete(String matricula) {
        int filas = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(SQL_DELETE);
            ps.setString(1, matricula);
            filas = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error en delete: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return filas;
    }

    @Override
    public int deleteAll() {
        int filas = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL + JDBC_OPTS, JDBC_USER, JDBC_PASSWORD);
            conn.createStatement().executeUpdate(SQL_CREATE_DB);
            conn.createStatement().executeUpdate(SQL_CREATE_TABLE);

            ps = conn.prepareStatement(SQL_DELETE_ALL);
            filas = ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error en deleteAll: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return filas;
    }
}
