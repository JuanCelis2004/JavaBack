<%@page import="Logica.claseUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar usuario</title>
    </head>
    <body>
        <% 
            claseUsuario usu = (claseUsuario) request.getSession().getAttribute("usuEditar");
        %>
        
        <h1>Datos del usuario</h1>
        <form action="svEditarUsuario" method="POST">
            <p><label>Dni</label><input type="text" name="dni" value="<%=usu.getDni()%>"></p>
            <p><label>Nombre</label><input type="text" name="nombre" value="<%=usu.getNombre()%>"></p>
            <p><label>Apellidos</label><input type="text" name="apellido" value="<%=usu.getApellido()%>"></p>
            <p><label>Teléfono</label><input type="text" name="telefono" value="<%=usu.getTelefono()%>"></p>
            <p><button type="submit">Actualizar</button> </p>
        </form>
    </body>
</html>
