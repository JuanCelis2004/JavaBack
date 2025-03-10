<%-- 
    Document   : index
    Created on : 23/02/2025, 2:20:34 p. m.
    Author     : HOGAR
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario de pruebas</title>
    </head>
    <body>
        
        
        
        <h1>Datos del cliente</h1>
        <form action="svUsuarios" method="POST">
            <p><label>Dni</label><input type="text" name="dni"></p>
            <p><label>Nombre</label><input type="text" name="nombre"></p>
            <p><label>Apellidos</label><input type="text" name="apellido"></p>
            <p><label>Teléfono</label><input type="text" name="telefono"></p>
            <p><button type="submit">Enviar</button> </p>
        </form>
        
        <h1>Ver lista de usuarios</h1>
        <p>Para ver los datos cargardos haga click en el siguiente botón</p>
        <form action="svUsuarios" method="GET">
            <p><button type="submit">Mostrar usuarios</button> </p>
        </form>
        
        <h1>Eliminar usuario</h1>
        <p>Ingrese el Id del usuario a Eliminar</p>
        <form action="svEliminarUsuario" method="POST">
            <p><label>Id: </label><input type="text" name="id_usuario"></p>
            <p><button type="submit">Eliminar usuarios</button> </p>
        </form>
        
        <h1>Editar usuario</h1>
        <p>Ingrese el Id del usuario a editar</p>
        <form action="svEditarUsuario" method="GET">
            <p><label>Id: </label><input type="text" name="id_usuarioEditar"></p>
            <p><button type="submit">Editar usuarios</button> </p>
        </form>
    </body>
</html>
