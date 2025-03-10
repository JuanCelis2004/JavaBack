<%@page import="com.mycompany.trabajoenclase.svUsuarios"%>
<%@page import="Logica.claseUsuario"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario de pruebas</title>
        <link rel="stylesheet" href="styles/css/index.css"/>
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
        <script src="styles/js/index.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
                    <a class="navbar-brand" href="#">I'm programming this</a>
                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarText">
                        <ul class="navbar-nav mr-auto">
                            <li class="nav-item active">
                                <a class="nav-link" href="#">Inicio <span class="sr-only">(current)</span></a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">About us</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#">Setting</a>
                            </li>
                        </ul>
                        <span class="navbar-text">
                            Holi
                        </span>
                    </div>
                </nav>
        <div class="main-container">
            <div class="container-xl">
                <div class="table-responsive">
                    <div class="table-wrapper">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Lista <b>Usuarios</b></h2>
                                </div>
                                <div class="col-sm-6">
                                    <a href="#addEmployeeModal" class="btn btn-success" data-toggle="modal" style="justify-content: center;"><i class="material-icons">&#xE147;</i><span>Añadir nuevo usuario</span></a>
                                    <!-- <a href="#deleteEmployeeModal" class="btn btn-danger" data-toggle="modal"><i class="material-icons">&#xE15C;</i> <span>Delete</span></a> -->						
                                </div>
                            </div>
                        </div>
                        <table class="table table-striped table-hover" >
                            <thead>
                                <tr>
                                    <th>No.</th>
                                    <th>Dni</th>
                                    <th>Nombre</th>
                                    <th>Apellido</th>
                                    <th>Telefono</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    // Llamar al método para cargar los usuarios
                                    svUsuarios serv = new svUsuarios();
                                    serv.cargarUsuarios(request);
                                    // Recuperar la lista de usuarios de la sesión
                                    List<claseUsuario> listaUsuarios = (List<claseUsuario>) session.getAttribute("listaUsuarios");
                                    int cont = 1;
                                    if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                                        for (claseUsuario usu : listaUsuarios) {
                                %>
                                <tr>
                                    <td><%=cont%></td>
                                    <td><%=usu.getDni()%></td>
                                    <td><%=usu.getNombre()%></td>
                                    <td><%=usu.getApellido()%></td>
                                    <td><%=usu.getTelefono()%></td>
                                    <td>
                                        <a href="#" class="editU" data-toggle="modal" data-target="#editEmployeeModal"
                                           data-id="<%=usu.getId()%>"
                                           data-dni="<%=usu.getDni()%>"
                                           data-nombre="<%=usu.getNombre()%>"
                                           data-apellido="<%=usu.getApellido()%>"
                                           data-telefono="<%=usu.getTelefono()%>">
                                            <i class="material-icons" data-toggle="tooltip" title="Edit" style="color: orange;">&#xE254;</i>
                                        </a>
                                        <a href="#deleteEmployeeModal" class="deleteU" data-toggle="modal" data-id="<%=usu.getId()%>">
                                            <i class="material-icons" data-toggle="tooltip" title="Delete" style="color: red;">&#xE872;</i>
                                        </a>
                                    </td>
                                </tr>
                                <%
                                            cont++;
                                        }
                                    } else {
                                    }
                                %>

                            </tbody>
                        </table>
                        <div class="clearfix">                           
                            <ul class="pagination">
                                <li class="page-item disabled"><a href="#">Previous</a></li>
                                <li class="page-item"><a href="#" class="page-link">1</a></li>
                                <li class="page-item"><a href="#" class="page-link">2</a></li>
                                <li class="page-item"><a href="#" class="page-link">3</a></li>
                                <li class="page-item"><a href="#" class="page-link">4</a></li>
                                <li class="page-item"><a href="#" class="page-link">5</a></li>
                                <li class="page-item"><a href="#" class="page-link">Next</a></li>
                            </ul>
                        </div>
                    </div>
                </div>        
            </div>
        </div>
        <!-- Add Modal HTML -->
        <div id="addEmployeeModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="svUsuarios" method="POST">
                        <div class="modal-header">						
                            <h4 class="modal-title">Agregar Usuario</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label>DNI</label>
                                <input type="text" class="form-control" name="dniAdd" required>
                            </div>
                            <div class="form-group">
                                <label>Nombre</label>
                                <input type="text" class="form-control" name="nombreAdd" required>
                            </div>
                            <div class="form-group">
                                <label>Apellido</label>
                                <input type="text" class="form-control" name="apellidoAdd" required>
                            </div>
                            <div class="form-group">
                                <label>Telefono</label>
                                <input type="number" class="form-control" name="telefonoAdd" required>
                            </div>				
                        </div>
                        <div class="modal-footer">

                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel">
                            <input type="submit" class="btn btn-success" value="Add">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Edit Modal HTML -->
        <div id="editEmployeeModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="svEditarUsuario" method="POST">
                        <div class="modal-header">						
                            <h4 class="modal-title">Editar Usuario</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        </div>
                        <div class="modal-body">					
                            <input type="hidden" id="editId" name="id_usuarioEditar">
                            <div class="form-group">
                                <label>DNI</label>
                                <input type="text" class="form-control" id="editDni" name="dniEdit" required>
                            </div>
                            <div class="form-group">
                                <label>Nombre</label>
                                <input type="text" class="form-control" id="editNombre" name="nombreEdit" required>
                            </div>
                            <div class="form-group">
                                <label>Apellido</label>
                                <input type="text" class="form-control" id="editApellido" name="apellidoEdit" required>
                            </div>
                            <div class="form-group">
                                <label>Teléfono</label>
                                <input type="text" class="form-control" id="editTelefono" name="telefonoEdit" required>
                            </div>					
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-info" >Editar</button>
                        </div>
                    </form>

                </div>
            </div>
        </div>
        <!-- Delete Modal HTML -->
        <div id="deleteEmployeeModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="svEliminarUsuario" method="POST">
                        <div class="modal-header">						
                            <h4 class="modal-title">Eliminar Usuario</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        </div>
                        <input type="hidden" id="editId" name="id_usuarioEliminar">
                        <div class="modal-body">					
                            <p>¿Esta seguro de eliminar este dato?</p>
                            <p class="text-warning"><small>!Esta acción no se puede deshacer¡.</small></p>
                        </div>
                        <div class="modal-footer">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel">
                            <input type="submit" class="btn btn-danger" value="Delete">
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </body>

</html>
