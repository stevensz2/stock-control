package com.authservice.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.authservice.model.UsuarioDAO;

/**
 * Servicio web (Servlet) encargado del inicio de sesion (login).
 * Metodo soportado: POST
 * Parametros esperados: usuario, contrasena
 * Respuesta: JSON indicando si la autenticacion fue satisfactoria o no.
 *
 * Endpoint: /LoginServlet
 *
 * Caso de uso (segun evidencia AA5-EV01):
 * El servicio recibe un usuario y una contrasena; si la autenticacion
 * es correcta responde con un mensaje de autenticacion satisfactoria,
 * en caso contrario responde con un mensaje de error en la autenticacion.
 *
 * @author Steven Diaz Morales
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        PrintWriter out = response.getWriter();

        // Validacion de campos obligatorios
        if (usuario == null || usuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"exito\": false, \"mensaje\": \"Debe enviar usuario y contrasena\"}");
            out.flush();
            return;
        }

        // Se valida la autenticacion contra el DAO
        boolean autenticado = usuarioDAO.autenticar(usuario, contrasena);

        if (autenticado) {
            response.setStatus(HttpServletResponse.SC_OK);
            out.print("{\"exito\": true, \"mensaje\": \"Autenticacion satisfactoria\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"exito\": false, \"mensaje\": \"Error en la autenticacion\"}");
        }
        out.flush();
    }
}
