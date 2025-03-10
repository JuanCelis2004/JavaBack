/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.trabajoenclase;



import Logica.claseUsuario;
import Logica.controladora;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
@WebServlet(name = "svUsuarios", urlPatterns = {"/svUsuarios"})
public class svUsuarios extends HttpServlet {
    
    
    //instancia de la clase controladora de manera global
    controladora control = new controladora();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<claseUsuario> listaUsuarios = new ArrayList<>();
        
        //Datos quemados
        /*listaUsuarios.add(new claseUsuario(1,"79","Jose","Perez","312"));
        listaUsuarios.add(new claseUsuario(2,"25","Martin","Alvarez","319"));
        listaUsuarios.add(new claseUsuario(3,"75","David","Ruiz","310"));*/
        
        //ya no vamos a poner datos quemados
        
        listaUsuarios = control.traerUsuarios();

        HttpSession misesion = request.getSession();
        misesion.setAttribute("listaUsuarios", listaUsuarios);
        
        response.sendRedirect("userShow.jsp");
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       String dni = request.getParameter("dni");
       String nombre = request.getParameter("nombre");
       String apellido = request.getParameter("apellido");
       String telefono = request.getParameter("telefono");

       claseUsuario usu = new claseUsuario();
       usu.setDni(dni);
       usu.setNombre(nombre);
       usu.setApellido(apellido);
       usu.setTelefono(telefono);
       
       control.crearUsuario(usu);
       
       response.sendRedirect("index.jsp");
       
       
        System.out.println("Dni es: " + dni);
        System.out.println("nombre es: " + nombre);
        System.out.println("apellido es: " + apellido);
        System.out.println("telefono es: " + telefono);

    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
