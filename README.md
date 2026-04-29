# 🚗 Concesionario de Vehículos – RA9 (MP0485)

> **Módulo:** MP0485 – Acceso a Datos  
> **Unidad:** RA9 – Persistencia con Base de Datos Relacional  
> **Centro:** Stucom · Grado Superior DAW · 2026

---

## 📋 Descripción

Aplicación de escritorio en Java para gestionar el catálogo de un concesionario de vehículos.  
Evolución de la práctica **RA5**, donde la persistencia en fichero de texto (`vehiculos.txt`) es **sustituida completamente por una base de datos MySQL** gestionada mediante **JDBC**.

La arquitectura sigue el patrón **MVC** (Modelo – Vista – Controlador) con una capa **DAO** dedicada al acceso a datos y un paquete de **excepciones personalizadas**.

---

## 🔄 Diferencias respecto a RA5

| Aspecto | RA5 – Fichero `.txt` | RA9 – Base de datos MySQL |
|---|---|---|
| Persistencia | `FicheroVehiculos.java` | `DAOSQL.java` (JDBC) |
| Formato de datos | `matricula;marca;modelo;precio;tipo` | Tabla SQL `vehiculos` |
| Integridad de datos | Validación manual en memoria | `PRIMARY KEY` en la BD |
| Carga al iniciar | Leer fichero → `List<Vehiculo>` | Consulta directa `SELECT *` |
| Guardado al cerrar | Escribir todo el fichero | No necesario (cada operación persiste al instante) |
| Búsqueda por tipo | Bucle `for` sobre la lista | `SELECT ... WHERE tipo = ?` |
| Control de errores | Excepciones genéricas | **Excepciones personalizadas** por tipo de operación |
| Operaciones extra | — | `update`, `deleteAll`, `contar` |

---

## 🗂️ Estructura real del proyecto

```
Concesionario/
│
└── Source Packages/
    │
    ├── controller/
    │   └── Concesionario.java          ← Lógica de negocio, usa IDAO
    │
    ├── dao/
    │   ├── DAOSQL.java                 ← Implementación JDBC (sustituye FicheroVehiculos)
    │   └── IDAO.java                   ← Interfaz con el contrato CRUD
    │
    ├── exception/
    │   ├── DAO_Excep.java              ← Excepción base del DAO
    │   ├── Read_SQL_DAO_Excep.java     ← Error en operaciones de lectura
    │   ├── Vehicle_Excep.java          ← Error de validación del vehículo
    │   └── Write_SQL_DAO_Excep.java    ← Error en operaciones de escritura
    │
    ├── main/
    │   └── main.java                   ← Punto de entrada de la aplicación
    │
    ├── model/
    │   └── Vehiculo.java               ← Clase modelo (sin cambios respecto a RA5)
    │
    ├── persistencia/                   ← Paquete heredado de RA5 (referencia histórica)
    │
    └── view/
        ├── VentanaPrincipal.java       ← Ventana principal con menú (JFrame)
        ├── buscarView.java             ← Búsqueda por matrícula (JDialog)
        ├── contarVehiculosView.java    ← Muestra el total de vehículos (JDialog)
        ├── eliminarView.java           ← Eliminar vehículo por matrícula (JDialog)
        └── insertarView.java           ← Formulario de alta de vehículo (JDialog)
```

---

## 🧩 Capa DAO

### `IDAO.java` – Interfaz

Define el contrato que debe cumplir cualquier implementación de persistencia:

```java
public interface IDAO {
    ArrayList<Vehiculo> readAll();
    Vehiculo            read(String matricula);
    ArrayList<Vehiculo> readByTipo(String tipo);
    int                 insert(Vehiculo v);
    int                 update(Vehiculo v);
    int                 delete(String matricula);
    int                 deleteAll();
    int                 contar();
}
```

### `DAOSQL.java` – Implementación JDBC

| Método | SQL ejecutado | Descripción |
|---|---|---|
| `readAll()` | `SELECT * FROM vehiculos` | Lista todos los vehículos |
| `read(matricula)` | `SELECT * FROM vehiculos WHERE matricula = ?` | Busca por matrícula |
| `readByTipo(tipo)` | `SELECT * FROM vehiculos WHERE tipo = ?` | Filtra por tipo |
| `insert(v)` | `INSERT INTO vehiculos VALUES (?, ?, ?, ?, ?)` | Añade un vehículo |
| `update(v)` | `UPDATE vehiculos SET ... WHERE matricula = ?` | Modifica un vehículo |
| `delete(matricula)` | `DELETE FROM vehiculos WHERE matricula = ?` | Elimina por matrícula |
| `deleteAll()` | `DELETE FROM vehiculos` | Elimina todos los registros |
| `contar()` | `SELECT COUNT(*) FROM vehiculos` | Cuenta el total de vehículos |

Cada método **crea la BD y la tabla automáticamente** si no existen, antes de ejecutar su operación.

---

## ⚠️ Excepciones personalizadas

El paquete `exception` introduce un sistema de errores propio organizado por tipo de operación:

```
DAO_Excep  (base)
├── Read_SQL_DAO_Excep    ← Se lanza en readAll(), read(), readByTipo(), contar()
├── Write_SQL_DAO_Excep   ← Se lanza en insert(), update(), delete(), deleteAll()
└── Vehicle_Excep         ← Se lanza al validar los datos del modelo Vehiculo
```

**Ventaja respecto a RA5:** permite capturar errores de forma granular en la vista, mostrando mensajes distintos según si el problema fue de lectura, escritura o validación del vehículo.

---

## 🗃️ Base de datos

### Creación automática

No es necesario ejecutar ningún script SQL previo. `DAOSQL` se encarga:

```sql
CREATE DATABASE IF NOT EXISTS concesionario;

CREATE TABLE IF NOT EXISTS concesionario.vehiculos (
    matricula VARCHAR(10)  PRIMARY KEY,
    marca     VARCHAR(50)  NOT NULL,
    modelo    VARCHAR(50)  NOT NULL,
    precio    DOUBLE       NOT NULL,
    tipo      VARCHAR(30)  NOT NULL
);
```

### Ejemplo de datos

```
matricula  | marca   | modelo  | precio  | tipo
-----------+---------+---------+---------+-------
1234ABC    | Toyota  | Corolla | 15000.0 | coche
5678DEF    | Yamaha  | R6      |  9000.0 | moto
```

---

## ⚙️ Configuración de la conexión

Definida como constantes en `DAOSQL.java`:

```java
private final String JDBC_URL      = "jdbc:mysql://localhost:3306";
private final String JDBC_OPTS     = "?useSSL=false&useTimezone=true"
                                   + "&serverTimezone=UTC"
                                   + "&allowPublicKeyRetrieval=true";
private final String JDBC_USER     = "root";
private final String JDBC_PASSWORD = "";   // ← Ajusta si tienes contraseña
```

> ⚠️ MySQL debe estar en ejecución en `localhost:3306` antes de arrancar la app.

---

## 🖥️ Interfaz gráfica (Swing)

| Clase | Tipo | Función |
|---|---|---|
| `VentanaPrincipal` | `JFrame` | Menú principal con botones de acción |
| `insertarView` | `JDialog` | Formulario para dar de alta un vehículo |
| `buscarView` | `JDialog` | Búsqueda de vehículo por matrícula |
| `eliminarView` | `JDialog` | Eliminar vehículo por matrícula |
| `contarVehiculosView` | `JDialog` | Muestra el número total de vehículos |

Los errores se presentan al usuario mediante `JOptionPane.showMessageDialog(...)`.

---

## 🏗️ Arquitectura MVC

```
┌──────────────────────────────────────────────────────────────┐
│                          VISTA                               │
│  VentanaPrincipal · insertarView · buscarView                │
│  eliminarView · contarVehiculosView                          │
└──────────────────────────┬───────────────────────────────────┘
                           │ llama a
┌──────────────────────────▼───────────────────────────────────┐
│                      CONTROLADOR                             │
│                   Concesionario.java                         │
└──────────────────────────┬───────────────────────────────────┘
                           │ usa
┌──────────────────────────▼───────────────────────────────────┐
│                    CAPA DAO                                  │
│           IDAO (interfaz) + DAOSQL (JDBC)                    │
└──────────────────────────┬───────────────────────────────────┘
                           │ lanza
┌──────────────────────────▼───────────────────────────────────┐
│               EXCEPCIONES PERSONALIZADAS                     │
│  DAO_Excep · Read_SQL_DAO_Excep · Write_SQL_DAO_Excep        │
│  Vehicle_Excep                                               │
└──────────────────────────────────────────────────────────────┘
                           │ persiste en
┌──────────────────────────▼───────────────────────────────────┐
│                  BASE DE DATOS MySQL                         │
│               concesionario.vehiculos                        │
└──────────────────────────────────────────────────────────────┘

               ┌──────────────────────┐
               │        MODELO        │
               │    Vehiculo.java     │
               └──────────────────────┘
             (usado por todas las capas)
```

---

## ✅ Funcionalidades implementadas

- [x] Insertar vehículo (matrícula única garantizada por `PRIMARY KEY`)
- [x] Buscar vehículo por matrícula
- [x] Filtrar vehículos por tipo
- [x] Listar todos los vehículos
- [x] Actualizar datos de un vehículo existente
- [x] Eliminar vehículo por matrícula
- [x] Eliminar todos los vehículos
- [x] Contar el número total de vehículos
- [x] Creación automática de BD y tabla al primer arranque
- [x] Excepciones personalizadas por tipo de operación (lectura / escritura / validación)
- [x] Errores mostrados mediante diálogos Swing

---

## 🔧 Requisitos previos

- **Java** 11 o superior
- **MySQL** 8.x en ejecución en `localhost:3306`
- Driver **mysql-connector-j** añadido a las librerías del proyecto en NetBeans
- NetBeans IDE (recomendado) o cualquier IDE compatible con Java

---

## 🚀 Cómo ejecutar

1. Clona o descarga el proyecto.
2. En NetBeans: botón derecho sobre el proyecto → *Properties* → *Libraries* → añade `mysql-connector-j-x.x.x.jar`.
3. Asegúrate de que MySQL está activo (`root` sin contraseña por defecto).
4. Ejecuta `main.java`.
5. La base de datos `concesionario` y la tabla `vehiculos` se crean automáticamente.

---

## 👤 Autor

**Ignac** · Stucom · DAW · MP0485 – Acceso a Datos · 2026
