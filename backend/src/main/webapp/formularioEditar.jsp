<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Producto</title>
</head>
<body>
    <h1>Editar Producto</h1>
    <form action="ProductoServlet" method="post">
        <input type="hidden" name="idProducto" value="${producto.idProducto}">

        <label for="codigo">Código:</label><br>
        <input type="text" id="codigo" name="codigo" value="${producto.codigo}" required><br><br>

        <label for="nombre">Nombre del producto:</label><br>
        <input type="text" id="nombre" name="nombre" value="${producto.nombre}" required><br><br>

        <label for="descripcion">Descripción:</label><br>
        <input type="text" id="descripcion" name="descripcion" value="${producto.descripcion}"><br><br>

        <label for="precioCompra">Precio de compra:</label><br>
        <input type="number" id="precioCompra" name="precioCompra" step="0.01" value="${producto.precioCompra}" required><br><br>

        <label for="precioVenta">Precio de venta:</label><br>
        <input type="number" id="precioVenta" name="precioVenta" step="0.01" value="${producto.precioVenta}" required><br><br>

        <label for="stockActual">Stock actual:</label><br>
        <input type="number" id="stockActual" name="stockActual" value="${producto.stockActual}" required><br><br>

        <label for="stockMinimo">Stock mínimo:</label><br>
        <input type="number" id="stockMinimo" name="stockMinimo" value="${producto.stockMinimo}"><br><br>

        <label for="idCategoria">ID Categoría (opcional):</label><br>
        <input type="number" id="idCategoria" name="idCategoria" value="${producto.idCategoria}"><br><br>

        <button type="submit">Guardar cambios</button>
    </form>
</body>
</html>