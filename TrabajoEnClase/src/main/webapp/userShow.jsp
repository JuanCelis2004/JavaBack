<%-- 
    Document   : UserShow
    Created on : 23/02/2025, 2:13:18 p. m.
    Author     : HOGAR
--%>

<%@page import="Logica.claseUsuario"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mostrar Usuarios</title>
    </head>
    <body>
        <h1>Lista de usuarios registrados</h1>
        <% 
            List<claseUsuario> listaUsuarios = (List) request.getSession().getAttribute("listaUsuarios");
            int cont = 1;
            for( claseUsuario usu : listaUsuarios){
        %>
        
        <p><b>Usuario No. <%=cont %></b></p>
        <p>Id: <%=usu.getId()%></p>
        <p>Dni: <%=usu.getDni()%></p>
        <p>Nombre: <%=usu.getNombre()%></p>
        <p>Apellidos: <%=usu.getApellido()%></p>
        <p>Teelfono: <%=usu.getTelefono()%></p>
        <p>-----------------------------------</p>

        <% 
            cont ++;
            }
        %>
    </body>
</html>
