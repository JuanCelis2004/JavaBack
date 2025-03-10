/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import Logica.claseUsuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author HOGAR
 */
public class claseUsuarioJpaController implements Serializable {

    public claseUsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public claseUsuarioJpaController() {
        emf=Persistence.createEntityManagerFactory("ejemploJavaWeb");
    }

    public void create(claseUsuario claseUsuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(claseUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(claseUsuario claseUsuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            claseUsuario = em.merge(claseUsuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = claseUsuario.getId();
                if (findclaseUsuario(id) == null) {
                    throw new NonexistentEntityException("The claseUsuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            claseUsuario claseUsuario;
            try {
                claseUsuario = em.getReference(claseUsuario.class, id);
                claseUsuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The claseUsuario with id " + id + " no longer exists.", enfe);
            }
            em.remove(claseUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<claseUsuario> findclaseUsuarioEntities() {
        return findclaseUsuarioEntities(true, -1, -1);
    }

    public List<claseUsuario> findclaseUsuarioEntities(int maxResults, int firstResult) {
        return findclaseUsuarioEntities(false, maxResults, firstResult);
    }

    private List<claseUsuario> findclaseUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(claseUsuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public claseUsuario findclaseUsuario(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(claseUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getclaseUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<claseUsuario> rt = cq.from(claseUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
