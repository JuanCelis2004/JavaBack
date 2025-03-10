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
import Model.Loan;
import Model.User;
import Persistence.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Persistence;

/**
 *
 * @author HOGAR
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public UserJpaController(){
        emf=Persistence.createEntityManagerFactory("StoreLibPU");
    }

    public void create(User user) {
        if (user.getLoan() == null) {
            user.setLoan(new HashSet<Loan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Loan> attachedLoan = new HashSet<Loan>();
            for (Loan loanLoanToAttach : user.getLoan()) {
                loanLoanToAttach = em.getReference(loanLoanToAttach.getClass(), loanLoanToAttach.getId());
                attachedLoan.add(loanLoanToAttach);
            }
            user.setLoan(attachedLoan);
            em.persist(user);
            for (Loan loanLoan : user.getLoan()) {
                User oldUserOfLoanLoan = loanLoan.getUser();
                loanLoan.setUser(user);
                loanLoan = em.merge(loanLoan);
                if (oldUserOfLoanLoan != null) {
                    oldUserOfLoanLoan.getLoan().remove(loanLoan);
                    oldUserOfLoanLoan = em.merge(oldUserOfLoanLoan);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Set<Loan> loanOld = persistentUser.getLoan();
            Set<Loan> loanNew = user.getLoan();
            Set<Loan> attachedLoanNew = new HashSet<Loan>();
            for (Loan loanNewLoanToAttach : loanNew) {
                loanNewLoanToAttach = em.getReference(loanNewLoanToAttach.getClass(), loanNewLoanToAttach.getId());
                attachedLoanNew.add(loanNewLoanToAttach);
            }
            loanNew = attachedLoanNew;
            user.setLoan(loanNew);
            user = em.merge(user);
            for (Loan loanOldLoan : loanOld) {
                if (!loanNew.contains(loanOldLoan)) {
                    loanOldLoan.setUser(null);
                    loanOldLoan = em.merge(loanOldLoan);
                }
            }
            for (Loan loanNewLoan : loanNew) {
                if (!loanOld.contains(loanNewLoan)) {
                    User oldUserOfLoanNewLoan = loanNewLoan.getUser();
                    loanNewLoan.setUser(user);
                    loanNewLoan = em.merge(loanNewLoan);
                    if (oldUserOfLoanNewLoan != null && !oldUserOfLoanNewLoan.equals(user)) {
                        oldUserOfLoanNewLoan.getLoan().remove(loanNewLoan);
                        oldUserOfLoanNewLoan = em.merge(oldUserOfLoanNewLoan);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Set<Loan> loan = user.getLoan();
            for (Loan loanLoan : loan) {
                loanLoan.setUser(null);
                loanLoan = em.merge(loanLoan);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
