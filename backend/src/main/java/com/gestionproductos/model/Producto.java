package com.gestionproductos.model;

import javax.persistence.*;

/**
 * Entidad JPA/Hibernate que representa un producto del módulo de
 * Gestión de Productos (Stock Control).
 * Se mapea a la tabla "producto" de la base de datos MySQL.
 */
@Entity
@Table(name = "producto")
public class Producto {

    // Identificador único autogenerado por la base de datos (AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int idProducto;

    // Código único del producto (no se permiten códigos repetidos)
    @Column(name = "codigo", unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String codigo;

    // Nombre comercial del producto
    @Column(name = "nombre", nullable = false, columnDefinition = "VARCHAR(150)")
    private String nombre;

    // Descripción libre del producto (opcional)
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Precio al que se compra el producto al proveedor
    @Column(name = "precio_compra", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double precioCompra;

    // Precio al que se vende el producto al cliente
    @Column(name = "precio_venta", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double precioVenta;

    // Cantidad actual disponible en inventario
    @Column(name = "stock_actual", nullable = false)
    private int stockActual;

    // Cantidad mínima antes de generar alerta de reabastecimiento (por defecto 5)
    @Column(name = "stock_minimo", nullable = false)
    private int stockMinimo = 5;

    // Referencia opcional a la categoría del producto
    @Column(name = "id_categoria")
    private Integer idCategoria;

    /** Constructor vacío requerido por Hibernate/JPA. */
    public Producto() {}

    /** Constructor usado al registrar un producto nuevo (sin id, aún no persistido). */
    public Producto(String codigo, String nombre, String descripcion,
                     double precioCompra, double precioVenta,
                     int stockActual, int stockMinimo, Integer idCategoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.idCategoria = idCategoria;
    }

    public int getIdProducto() { return idProducto; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
}