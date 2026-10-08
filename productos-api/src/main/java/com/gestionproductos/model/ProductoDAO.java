package com.gestionproductos.model;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase de acceso a datos (DAO) para el manejo de productos.
 * Para efectos de esta evidencia se usa almacenamiento en memoria.
 * NOTA: si ya tienes tu DAO real con JDBC del modulo GestionProductos
 * (evidencia AA2-EV02), puedes reemplazar la logica interna de estos
 * metodos por las consultas SQL correspondientes, manteniendo las
 * mismas firmas de metodo para no romper los servlets que la usan.
 *
 * @author Steven Diaz Morales
 */
public class ProductoDAO {

    private static final Map<Integer, Producto> productos = new ConcurrentHashMap<>();
    private static final AtomicInteger contador = new AtomicInteger(1);

    // Datos de ejemplo precargados para poder probar la API de inmediato
    static {
        agregar(new Producto(0, "Teclado mecanico", "Teclado USB retroiluminado", 85000, 20));
        agregar(new Producto(0, "Mouse inalambrico", "Mouse optico 2.4GHz", 45000, 35));
    }

    /**
     * Metodo auxiliar estatico: asigna un id autoincremental y guarda el producto.
     * Lo usan el bloque estatico (datos de ejemplo) y el metodo crear().
     */
    private static Producto agregar(Producto producto) {
        int nuevoId = contador.getAndIncrement();
        producto.setId(nuevoId);
        productos.put(nuevoId, producto);
        return producto;
    }

    /** Crea un nuevo producto y le asigna un id autoincremental. */
    public Producto crear(Producto producto) {
        return agregar(producto);
    }

    /** Retorna todos los productos registrados. */
    public Collection<Producto> listarTodos() {
        return new ArrayList<>(productos.values());
    }

    /** Busca un producto por su id. Retorna null si no existe. */
    public Producto buscarPorId(int id) {
        return productos.get(id);
    }

    /** Actualiza un producto existente. Retorna true si se pudo actualizar. */
    public boolean actualizar(int id, Producto datosNuevos) {
        if (!productos.containsKey(id)) {
            return false;
        }
        datosNuevos.setId(id);
        productos.put(id, datosNuevos);
        return true;
    }

    /** Elimina un producto por su id. Retorna true si existia y fue eliminado. */
    public boolean eliminar(int id) {
        return productos.remove(id) != null;
    }
}