package com.authservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.authservice.model.Usuario;
import com.authservice.model.UsuarioDAO;

/**
 * Servicio web (Servlet) encargado del registro de nuevos usuarios.
 * Metodo soportado: POST
 * Parametros esperados (form-urlencoded o query params): usuario, contrasena, correo
 * Respuesta: JSON con el resultado de la operacion.
 *
 * Endpoint: /RegistroServlet
 *
 * @author Steven Diaz Morales
 */
@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se configura la respuesta como JSON en UTF-8
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Se obtienen los parametros enviados desde el cliente
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String correo = request.getParameter("correo");

        PrintWriter out = response.getWriter();

        // Validacion basica de campos obligatorios
        if (usuario == null || usuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"exito\": false, \"mensaje\": \"Usuario y contrasena son obligatorios\"}");
            out.flush();
            return;
        }

        // Se intenta registrar el nuevo usuario
        Usuario nuevoUsuario = new Usuario(usuario, contrasena, correo);
        boolean registrado = usuarioDAO.registrar(nuevoUsuario);

        if (registrado) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.print("{\"exito\": true, \"mensaje\": \"Usuario registrado correctamente\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("{\"exito\": false, \"mensaje\": \"El usuario ya existe\"}");
        }
        out.flush();
    }
}
