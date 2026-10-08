<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listado de Productos</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 30px; }
        h1 { color: #2c3e50; }
        table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        th, td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; }
        th { background-color: #2c3e50; color: #fff; }
        a.btn { text-decoration: none; padding: 5px 10px; border-radius: 4px; margin-right: 5px; }
        a.editar { background-color: #2980b9; color: #fff; }
        a.eliminar { background-color: #c0392b; color: #fff; }
        a.nuevo { display: inline-block; margin-bottom: 15px; background-color: #27ae60; color: #fff; padding: 8px 15px; border-radius: 4px; text-decoration: none; }
    </style>
</head>
<body>
    <h1>Listado de Productos</h1>
    <a class="nuevo" href="formulario.html">+ Nuevo producto</a>
    <table>
        <tr>
            <th>Código</th>
            <th>Nombre</th>
            <th>Precio compra</th>
            <th>Precio venta</th>
            <th>Stock actual</th>
            <th>Acciones</th>
        </tr>
        <c:forEach var="p" items="${productos}">
            <tr>
                <td>${p.codigo}</td>
                <td>${p.nombre}</td>
                <td>$${p.precioCompra}</td>
                <td>$${p.precioVenta}</td>
                <td>${p.stockActual}</td>
                <td>
                    <a class="btn editar" href="ProductoServlet?accion=editar&id=${p.idProducto}">Editar</a>
                    <a class="btn eliminar" href="ProductoServlet?accion=eliminar&id=${p.idProducto}"
                       onclick="return confirm('¿Seguro que deseas eliminar este producto?');">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>