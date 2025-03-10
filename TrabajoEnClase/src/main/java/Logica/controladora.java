/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.List;
import persistencia.controladoraPersistencia;

/**
 *
 * @author HOGAR
 */
public class controladora {
    controladoraPersistencia controlPersis = new controladoraPersistencia();
    
    public void crearUsuario (claseUsuario usu){
        controlPersis.crearUsuario(usu);
    }
    
    public List<claseUsuario> traerUsuarios(){
        return controlPersis.traerUsuarios();
    }
    
    public void borrarUsuario(int id_eliminar){
        controlPersis.borrarUsuario(id_eliminar);
    }
    
    public claseUsuario traerUsuario(int id_editar){
        return controlPersis.traerUsuario(id_editar);
    }
    
    public void editarUsuario(claseUsuario usu){
        controlPersis.editarUsuario(usu);
    }
    
}
