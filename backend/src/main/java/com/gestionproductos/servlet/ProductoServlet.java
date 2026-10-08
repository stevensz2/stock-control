package com.gestionproductos.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gestionproductos.dao.ProductoDAO;
import com.gestionproductos.model.Producto;

/**
 * Servlet encargado de gestionar las operaciones CRUD
 * (Crear, Listar, Editar, Eliminar) sobre la entidad Producto.
 */
@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// DAO reutilizado para todas las operaciones de acceso a datos
	private final ProductoDAO productoDAO = new ProductoDAO();

	public ProductoServlet() {
		super();
	}

	/**
	 * Maneja las peticiones GET. Según el parámetro "accion" decide
	 * si listar todos los productos, mostrar el formulario de edición
	 * o eliminar un producto.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String accion = request.getParameter("accion");
		if (accion == null) {
			accion = "listar"; // acción por defecto
		}

		switch (accion) {
			case "eliminar":
				eliminarProducto(request, response);
				break;
			case "editar":
				mostrarFormularioEdicion(request, response);
				break;
			case "listar":
			default:
				listarProductos(request, response);
				break;
		}
	}

	/**
	 * Maneja las peticiones POST: creación de un nuevo producto
	 * o actualización de uno existente, según venga o no el idProducto.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Leer y validar los datos comunes del formulario.
		// Se valida cada número por separado para poder mostrar un mensaje
		// de error claro en lugar de una excepción genérica del servidor.
		String codigo = request.getParameter("codigo");
		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");

		double precioCompra, precioVenta;
		int stockActual, stockMinimo;
		Integer idCategoria;
		try {
			precioCompra = Double.parseDouble(request.getParameter("precioCompra"));
			precioVenta = Double.parseDouble(request.getParameter("precioVenta"));
			stockActual = Integer.parseInt(request.getParameter("stockActual"));

			String stockMinimoStr = request.getParameter("stockMinimo");
			stockMinimo = (stockMinimoStr == null || stockMinimoStr.isEmpty())
					? 5 : Integer.parseInt(stockMinimoStr);

			String idCategoriaStr = request.getParameter("idCategoria");
			idCategoria = (idCategoriaStr == null || idCategoriaStr.isEmpty())
					? null : Integer.valueOf(idCategoriaStr);
		} catch (NumberFormatException e) {
			// Alguno de los campos numéricos venía vacío o con texto no numérico
			mostrarError(response, "Los campos de precio, stock y categoría deben ser numéricos.");
			return;
		}

		// Validaciones básicas de negocio antes de tocar la base de datos
		if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank()) {
			mostrarError(response, "El código y el nombre son obligatorios.");
			return;
		}
		if (precioCompra < 0 || precioVenta < 0 || stockActual < 0 || stockMinimo < 0) {
			mostrarError(response, "Los valores numéricos no pueden ser negativos.");
			return;
		}

		// Si viene idProducto, es una actualización; si no, es un producto nuevo
		String idProductoStr = request.getParameter("idProducto");
		boolean operacionExitosa;

		if (idProductoStr != null && !idProductoStr.isEmpty()) {
			// --- ACTUALIZAR ---
			Producto producto = productoDAO.buscarPorId(Integer.parseInt(idProductoStr));
			if (producto == null) {
				// El id no existe en la base de datos: evita el NullPointerException
				response.sendError(HttpServletResponse.SC_NOT_FOUND,
						"El producto que intenta editar ya no existe.");
				return;
			}
			producto.setCodigo(codigo);
			producto.setNombre(nombre);
			producto.setDescripcion(descripcion);
			producto.setPrecioCompra(precioCompra);
			producto.setPrecioVenta(precioVenta);
			producto.setStockActual(stockActual);
			producto.setStockMinimo(stockMinimo);
			producto.setIdCategoria(idCategoria);

			operacionExitosa = productoDAO.actualizar(producto);
		} else {
			// --- CREAR ---
			Producto producto = new Producto(codigo, nombre, descripcion,
					precioCompra, precioVenta, stockActual, stockMinimo, idCategoria);
			operacionExitosa = productoDAO.guardar(producto);
		}

		if (!operacionExitosa) {
			// El guardado falló (por ejemplo, código duplicado): se informa al
			// usuario en lugar de redirigir como si todo hubiera salido bien.
			mostrarError(response, "No se pudo guardar el producto. Verifique que el código no esté repetido.");
			return;
		}

		// Después de guardar o actualizar con éxito, redirige al listado
		response.sendRedirect("ProductoServlet?accion=listar");
	}

	/**
	 * Muestra una página sencilla de error con el mensaje indicado y un
	 * enlace para volver al formulario, en lugar de dejar que la
	 * aplicación falle silenciosamente o redirija como si todo hubiera
	 * salido bien.
	 */
	private void mostrarError(HttpServletResponse response, String mensaje) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().printf(
				"<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
				"<title>Error</title></head><body>" +
				"<h2 style='color:#c0392b;'>%s</h2>" +
				"<a href='formulario.html'>&larr; Volver al formulario</a>" +
				"</body></html>", mensaje);
	}

	/**
	 * Obtiene todos los productos y los envía a listaProductos.jsp
	 */
	private void listarProductos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Producto> productos = productoDAO.listarTodos();
		request.setAttribute("productos", productos);
		request.getRequestDispatcher("listaProductos.jsp").forward(request, response);
	}

	/**
	 * Busca un producto por id y lo envía a formularioEditar.jsp
	 * para precargar los campos.
	 */
	private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Producto producto = productoDAO.buscarPorId(id);
		request.setAttribute("producto", producto);
		request.getRequestDispatcher("formularioEditar.jsp").forward(request, response);
	}

	/**
	 * Elimina un producto por id y redirige de vuelta al listado.
	 */
	private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		productoDAO.eliminar(id);
		response.sendRedirect("ProductoServlet?accion=listar");
	}
}