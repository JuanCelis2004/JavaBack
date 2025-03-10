let svr = new XMLHttpRequest();
svr.open('GET', '/svUsuarios', true); // URL del servlet
svr.onload = function () {
    if (svr.status === 200) {
        console.log(svr.responseText); // Mostrar la respuesta en la consola
    } else {
        console.error('Error:', svr.statusText);
    }
};
svr.onerror = function () {
    console.error('Error en la solicitud');
};
svr.send();

$(document).ready(function () {
    // Activate tooltip
    $('[data-toggle="tooltip"]').tooltip();

    // Select/Deselect checkboxes
    var checkbox = $('table tbody input[type="checkbox"]');
    $("#selectAll").click(function () {
        if (this.checked) {
            checkbox.each(function () {
                this.checked = true;
            });
        } else {
            checkbox.each(function () {
                this.checked = false;
            });
        }
    });
    checkbox.click(function () {
        if (!this.checked) {
            $("#selectAll").prop("checked", false);
        }
    });
});

$(document).ready(function () {
    $(".editU").click(function () {
        let id = $(this).data("id");
        let dni = $(this).data("dni");
        let nombre = $(this).data("nombre");
        let apellido = $(this).data("apellido");
        let telefono = $(this).data("telefono");
        $("#editEmployeeModal input[name='id_usuarioEditar']").val(id);
        $("#editEmployeeModal input[name='dniEdit']").val(dni);
        $("#editEmployeeModal input[name='nombreEdit']").val(nombre);
        $("#editEmployeeModal input[name='apellidoEdit']").val(apellido);
        $("#editEmployeeModal input[name='telefonoEdit']").val(telefono);
    });
});

$(document).ready(function () {
    $(".deleteU").click(function () {
        let id = $(this).data("id");
        $("#deleteEmployeeModal input[name='id_usuarioEliminar']").val(id);
    });
});