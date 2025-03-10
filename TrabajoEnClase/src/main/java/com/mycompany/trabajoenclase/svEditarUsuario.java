/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.trabajoenclase;

import Logica.claseUsuario;
import Logica.controladora;
import com.google.protobuf.Int32Value;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HOGAR
 */
@WebServlet(name = "svEditarUsuario", urlPatterns = {"/svEditarUsuario"})
public class svEditarUsuario extends HttpServlet {

    controladora control = new controladora();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id_editar = Integer.parseInt(request.getParameter("id_usuarioEditar"));
        claseUsuario usu = control.traerUsuario(id_editar);
        HttpSession misesion = request.getSession();
        misesion.setAttribute("usuEditar", usu);

        response.sendRedirect("index.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dni = request.getParameter("dniEdit");
        String nombre = request.getParameter("nombreEdit");
        String apellido = request.getParameter("apellidoEdit");
        String telefono = request.getParameter("telefonoEdit");

        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id_usuarioEditar"));
        } catch (NumberFormatException e) {
            System.out.println("Error en conversión de ID: " + e.getMessage());
        }

        claseUsuario usu = new claseUsuario();
        usu.setId(id);
        usu.setDni(dni);
        usu.setNombre(nombre);
        usu.setApellido(apellido);
        usu.setTelefono(telefono);

        // Llamar al método de actualización
        control.editarUsuario(usu);
        
        svUsuarios servlet  = new svUsuarios();
        servlet.doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
