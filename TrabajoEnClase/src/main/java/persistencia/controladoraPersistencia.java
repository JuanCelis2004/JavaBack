
package persistencia;

import Logica.claseUsuario;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import persistencia.exceptions.NonexistentEntityException;


public class controladoraPersistencia {

    claseUsuarioJpaController usuJpa = new claseUsuarioJpaController();
    
    
    //create
    public void crearUsuario(claseUsuario usu){
        usuJpa.create(usu);
    }
    
    //read
    public List<claseUsuario> traerUsuarios(){
        return usuJpa.findclaseUsuarioEntities();
    }
    
    //Delete
    public void borrarUsuario(int id_eliminar){
        try {
            usuJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(controladoraPersistencia.class.getName()).log( Level.SEVERE, null, ex);
        }
    }
    
    public claseUsuario traerUsuario(int id_editar){
        return usuJpa.findclaseUsuario(id_editar);
    }
    
    public void editarUsuario(claseUsuario usu){
        try {
            usuJpa.edit(usu);
        } catch (Exception ex) {
            Logger.getLogger(controladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
