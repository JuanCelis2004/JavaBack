/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Book;
import Model.Loan;
import Model.User;
import Persistence.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author HOGAR
 */
public class LoanJpaController implements Serializable {

    public LoanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public LoanJpaController(){
        emf=Persistence.createEntityManagerFactory("StoreLibPU");
    }

    public void create(Loan loan) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Book book = loan.getBook();
            if (book != null) {
                book = em.getReference(book.getClass(), book.getId());
                loan.setBook(book);
            }
            User user = loan.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getId());
                loan.setUser(user);
            }
            em.persist(loan);
            if (book != null) {
                book.getLoan().add(loan);
                book = em.merge(book);
            }
            if (user != null) {
                user.getLoan().add(loan);
                user = em.merge(user);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Loan loan) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Loan persistentLoan = em.find(Loan.class, loan.getId());
            Book bookOld = persistentLoan.getBook();
            Book bookNew = loan.getBook();
            User userOld = persistentLoan.getUser();
            User userNew = loan.getUser();
            if (bookNew != null) {
                bookNew = em.getReference(bookNew.getClass(), bookNew.getId());
                loan.setBook(bookNew);
            }
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                loan.setUser(userNew);
            }
            loan = em.merge(loan);
            if (bookOld != null && !bookOld.equals(bookNew)) {
                bookOld.getLoan().remove(loan);
                bookOld = em.merge(bookOld);
            }
            if (bookNew != null && !bookNew.equals(bookOld)) {
                bookNew.getLoan().add(loan);
                bookNew = em.merge(bookNew);
            }
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.getLoan().remove(loan);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                userNew.getLoan().add(loan);
                userNew = em.merge(userNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loan.getId();
                if (findLoan(id) == null) {
                    throw new NonexistentEntityException("The loan with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Loan loan;
            try {
                loan = em.getReference(Loan.class, id);
                loan.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loan with id " + id + " no longer exists.", enfe);
            }
            Book book = loan.getBook();
            if (book != null) {
                book.getLoan().remove(loan);
                book = em.merge(book);
            }
            User user = loan.getUser();
            if (user != null) {
                user.getLoan().remove(loan);
                user = em.merge(user);
            }
            em.remove(loan);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Loan> findLoanEntities() {
        return findLoanEntities(true, -1, -1);
    }

    public List<Loan> findLoanEntities(int maxResults, int firstResult) {
        return findLoanEntities(false, maxResults, firstResult);
    }

    private List<Loan> findLoanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Loan.class));
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

    public Loan findLoan(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Loan.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Loan> rt = cq.from(Loan.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
