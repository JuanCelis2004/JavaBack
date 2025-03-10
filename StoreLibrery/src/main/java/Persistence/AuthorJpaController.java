/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistence;

import Model.Author;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Book;
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
public class AuthorJpaController implements Serializable {

    public AuthorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public AuthorJpaController(){
        emf=Persistence.createEntityManagerFactory("StoreLibPU");
    }

    public void create(Author author) {
        if (author.getBook() == null) {
            author.setBook(new HashSet<Book>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Set<Book> attachedBook = new HashSet<Book>();
            for (Book bookBookToAttach : author.getBook()) {
                bookBookToAttach = em.getReference(bookBookToAttach.getClass(), bookBookToAttach.getId());
                attachedBook.add(bookBookToAttach);
            }
            author.setBook(attachedBook);
            em.persist(author);
            for (Book bookBook : author.getBook()) {
                Author oldAuthorOfBookBook = bookBook.getAuthor();
                bookBook.setAuthor(author);
                bookBook = em.merge(bookBook);
                if (oldAuthorOfBookBook != null) {
                    oldAuthorOfBookBook.getBook().remove(bookBook);
                    oldAuthorOfBookBook = em.merge(oldAuthorOfBookBook);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Author author) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Author persistentAuthor = em.find(Author.class, author.getId());
            Set<Book> bookOld = persistentAuthor.getBook();
            Set<Book> bookNew = author.getBook();
            Set<Book> attachedBookNew = new HashSet<Book>();
            for (Book bookNewBookToAttach : bookNew) {
                bookNewBookToAttach = em.getReference(bookNewBookToAttach.getClass(), bookNewBookToAttach.getId());
                attachedBookNew.add(bookNewBookToAttach);
            }
            bookNew = attachedBookNew;
            author.setBook(bookNew);
            author = em.merge(author);
            for (Book bookOldBook : bookOld) {
                if (!bookNew.contains(bookOldBook)) {
                    bookOldBook.setAuthor(null);
                    bookOldBook = em.merge(bookOldBook);
                }
            }
            for (Book bookNewBook : bookNew) {
                if (!bookOld.contains(bookNewBook)) {
                    Author oldAuthorOfBookNewBook = bookNewBook.getAuthor();
                    bookNewBook.setAuthor(author);
                    bookNewBook = em.merge(bookNewBook);
                    if (oldAuthorOfBookNewBook != null && !oldAuthorOfBookNewBook.equals(author)) {
                        oldAuthorOfBookNewBook.getBook().remove(bookNewBook);
                        oldAuthorOfBookNewBook = em.merge(oldAuthorOfBookNewBook);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = author.getId();
                if (findAuthor(id) == null) {
                    throw new NonexistentEntityException("The author with id " + id + " no longer exists.");
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
            Author author;
            try {
                author = em.getReference(Author.class, id);
                author.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The author with id " + id + " no longer exists.", enfe);
            }
            Set<Book> book = author.getBook();
            for (Book bookBook : book) {
                bookBook.setAuthor(null);
                bookBook = em.merge(bookBook);
            }
            em.remove(author);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Author> findAuthorEntities() {
        return findAuthorEntities(true, -1, -1);
    }

    public List<Author> findAuthorEntities(int maxResults, int firstResult) {
        return findAuthorEntities(false, maxResults, firstResult);
    }

    private List<Author> findAuthorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Author.class));
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

    public Author findAuthor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Author.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Author> rt = cq.from(Author.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
