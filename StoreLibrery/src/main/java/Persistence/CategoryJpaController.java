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
import Model.Category;
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
public class CategoryJpaController implements Serializable {

    public CategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public CategoryJpaController(){
        emf=Persistence.createEntityManagerFactory("StoreLibPU");
    }

    public void create(Category category) {
        if (category.getBook() == null) {
            category.setBook(new HashSet<Book>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Book> attachedBook = new HashSet<Book>();
            for (Book bookBookToAttach : category.getBook()) {
                bookBookToAttach = em.getReference(bookBookToAttach.getClass(), bookBookToAttach.getId());
                attachedBook.add(bookBookToAttach);
            }
            category.setBook(attachedBook);
            em.persist(category);
            for (Book bookBook : category.getBook()) {
                bookBook.getCategory().add(category);
                bookBook = em.merge(bookBook);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getId());
            Set<Book> bookOld = persistentCategory.getBook();
            Set<Book> bookNew = category.getBook();
            Set<Book> attachedBookNew = new HashSet<Book>();
            for (Book bookNewBookToAttach : bookNew) {
                bookNewBookToAttach = em.getReference(bookNewBookToAttach.getClass(), bookNewBookToAttach.getId());
                attachedBookNew.add(bookNewBookToAttach);
            }
            bookNew = attachedBookNew;
            category.setBook(bookNew);
            category = em.merge(category);
            for (Book bookOldBook : bookOld) {
                if (!bookNew.contains(bookOldBook)) {
                    bookOldBook.getCategory().remove(category);
                    bookOldBook = em.merge(bookOldBook);
                }
            }
            for (Book bookNewBook : bookNew) {
                if (!bookOld.contains(bookNewBook)) {
                    bookNewBook.getCategory().add(category);
                    bookNewBook = em.merge(bookNewBook);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = category.getId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            Set<Book> book = category.getBook();
            for (Book bookBook : book) {
                bookBook.getCategory().remove(category);
                bookBook = em.merge(bookBook);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
