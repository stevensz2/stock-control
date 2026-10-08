package com.gestionproductos.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gestionproductos.model.Producto;
import com.gestionproductos.model.ProductoDAO;

/**
 * Servicio web REST para la gestion de productos del proyecto Stock Control.
 * Implementa las operaciones basicas: consultar, crear, actualizar y eliminar (CRUD).
 *
 * Endpoints:
 *   GET    /api/productos          -> Lista todos los productos
 *   GET    /api/productos/{id}     -> Consulta un producto por id
 *   POST   /api/productos          -> Crea un nuevo producto (parametros: nombre, descripcion, precio, cantidad)
 *   PUT    /api/productos/{id}     -> Actualiza un producto existente
 *   DELETE /api/productos/{id}     -> Elimina un producto
 *
 * Esta evidencia corresponde a GA7-220501096-AA5-EV03 (Diseno y desarrollo
 * de servicios web - proyecto). El testing con Postman de estos endpoints
 * corresponde a la evidencia AA5-EV04.
 *
 * @author Steven Diaz Morales
 */
@WebServlet("/api/productos/*")
public class ProductoApiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepararRespuesta(request, response);
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo(); // ej: "/3" cuando piden un id especifico

        if (pathInfo == null || pathInfo.equals("/")) {
            // No se especifico id: se listan todos los productos
            Collection<Producto> lista = productoDAO.listarTodos();
            StringBuilder json = new StringBuilder("[");
            int i = 0;
            for (Producto p : lista) {
                if (i++ > 0) json.append(",");
                json.append(p.toJson());
            }
            json.append("]");
            out.print(json.toString());
        } else {
            // Se especifico un id: se busca el producto puntual
            int id = extraerId(pathInfo);
            Producto p = productoDAO.buscarPorId(id);
            if (p == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"mensaje\": \"Producto no encontrado\"}");
            } else {
                out.print(p.toJson());
            }
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepararRespuesta(request, response);
        PrintWriter out = response.getWriter();

        Producto nuevo = leerProductoDeParametros(request);

        if (nuevo.getNombre() == null || nuevo.getNombre().trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"mensaje\": \"El nombre del producto es obligatorio\"}");
            out.flush();
            return;
        }

        Producto creado = productoDAO.crear(nuevo);
        response.setStatus(HttpServletResponse.SC_CREATED);
        out.print(creado.toJson());
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepararRespuesta(request, response);
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"mensaje\": \"Debe indicar el id del producto a actualizar\"}");
            out.flush();
            return;
        }

        int id = extraerId(pathInfo);
        Producto datosNuevos = leerProductoDeParametros(request);

        if (datosNuevos.getNombre() == null || datosNuevos.getNombre().trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"mensaje\": \"El nombre del producto es obligatorio\"}");
            out.flush();
            return;
        }

        boolean actualizado = productoDAO.actualizar(id, datosNuevos);

        if (actualizado) {
            out.print(productoDAO.buscarPorId(id).toJson());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"mensaje\": \"Producto no encontrado\"}");
        }
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        prepararRespuesta(request, response);
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"mensaje\": \"Debe indicar el id del producto a eliminar\"}");
            out.flush();
            return;
        }

        int id = extraerId(pathInfo);
        boolean eliminado = productoDAO.eliminar(id);

        if (eliminado) {
            out.print("{\"mensaje\": \"Producto eliminado correctamente\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"mensaje\": \"Producto no encontrado\"}");
        }
        out.flush();
    }

    /** Configura la codificacion de la peticion y el tipo de contenido de la respuesta. */
    private void prepararRespuesta(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    /** Extrae el id numerico desde el pathInfo, ej: "/3" -> 3 */
    private int extraerId(String pathInfo) {
        try {
            return Integer.parseInt(pathInfo.replace("/", ""));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Construye un objeto Producto a partir de los parametros enviados en la peticion.
     * Tomcat solo lee el cuerpo de formularios en peticiones POST, por eso en PUT
     * el cuerpo (nombre=...&precio=...) se lee manualmente.
     */
    private Producto leerProductoDeParametros(HttpServletRequest request) throws IOException {
        Map<String, String> params = leerParametros(request);
        String nombre = params.get("nombre");
        String descripcion = params.get("descripcion");
        double precio = parsearDouble(params.get("precio"));
        int cantidad = parsearInt(params.get("cantidad"));
        return new Producto(0, nombre, descripcion, precio, cantidad);
    }

    /** Reune los parametros de la URL, del formulario POST y del cuerpo de un PUT. */
    private Map<String, String> leerParametros(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<String, String>();

        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (e.getValue().length > 0) {
                params.put(e.getKey(), e.getValue()[0]);
            }
        }

        String contentType = request.getContentType();
        if ("PUT".equalsIgnoreCase(request.getMethod()) && contentType != null
                && contentType.toLowerCase().startsWith("application/x-www-form-urlencoded")) {
            StringBuilder cuerpo = new StringBuilder();
            BufferedReader reader = request.getReader();
            String linea;
            while ((linea = reader.readLine()) != null) {
                cuerpo.append(linea);
            }
            for (String par : cuerpo.toString().split("&")) {
                if (par.isEmpty()) continue;
                int igual = par.indexOf('=');
                String clave = igual >= 0 ? par.substring(0, igual) : par;
                String valor = igual >= 0 ? par.substring(igual + 1) : "";
                params.put(URLDecoder.decode(clave, "UTF-8"), URLDecoder.decode(valor, "UTF-8"));
            }
        }
        return params;
    }

    private double parsearDouble(String valor) {
        try {
            return valor == null ? 0 : Double.parseDouble(valor.replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int parsearInt(String valor) {
        try {
            return valor == null ? 0 : Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}