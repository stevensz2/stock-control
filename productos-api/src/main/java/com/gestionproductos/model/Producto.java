package com.gestionproductos.model;

import java.util.Locale;

/**
 * Clase modelo que representa un producto del sistema Stock Control.
 * Se usa como objeto de transferencia entre el servicio web y el cliente.
 *
 * @author Steven Diaz Morales
 */
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidad;

    public Producto() {
    }

    public Producto(int id, String nombre, String descripcion, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Convierte el producto a formato JSON de forma manual,
     * evitando dependencias externas como Gson.
     * Se usa Locale.US para que el decimal lleve punto (85000.00) y el JSON sea valido
     * sin importar la configuracion regional del sistema operativo.
     */
    public String toJson() {
        return String.format(
            Locale.US,
            "{\"id\": %d, \"nombre\": \"%s\", \"descripcion\": \"%s\", \"precio\": %.2f, \"cantidad\": %d}",
            id, escapar(nombre), escapar(descripcion), precio, cantidad
        );
    }

    /** Escapa los caracteres que invalidan un texto JSON. */
    private String escapar(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}