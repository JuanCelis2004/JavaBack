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
import Model.Author;
import Model.Book;
import Model.Category;
import java.util.HashSet;
import java.util.Set;
import Model.Loan;
import Persistence.exceptions.NonexistentEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author HOGAR
 */
public class BookJpaController implements Serializable {

    public BookJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public BookJpaController(){
        emf=Persistence.createEntityManagerFactory("StoreLibPU");
    }

    public void create(Book book) {
        if (book.getCategory() == null) {
            book.setCategory(new HashSet<Category>());
        }
        if (book.getLoan() == null) {
            book.setLoan(new HashSet<Loan>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Author author = book.getAuthor();
            if (author != null) {
                author = em.getReference(author.getClass(), author.getId());
                book.setAuthor(author);
            }
            Set<Category> attachedCategory = new HashSet<Category>();
            for (Category categoryCategoryToAttach : book.getCategory()) {
                categoryCategoryToAttach = em.getReference(categoryCategoryToAttach.getClass(), categoryCategoryToAttach.getId());
                attachedCategory.add(categoryCategoryToAttach);
            }
            book.setCategory(attachedCategory);
            Set<Loan> attachedLoan = new HashSet<Loan>();
            for (Loan loanLoanToAttach : book.getLoan()) {
                loanLoanToAttach = em.getReference(loanLoanToAttach.getClass(), loanLoanToAttach.getId());
                attachedLoan.add(loanLoanToAttach);
            }
            book.setLoan(attachedLoan);
            em.persist(book);
            if (author != null) {
                author.getBook().add(book);
                author = em.merge(author);
            }
            for (Category categoryCategory : book.getCategory()) {
                categoryCategory.getBook().add(book);
                categoryCategory = em.merge(categoryCategory);
            }
            for (Loan loanLoan : book.getLoan()) {
                Book oldBookOfLoanLoan = loanLoan.getBook();
                loanLoan.setBook(book);
                loanLoan = em.merge(loanLoan);
                if (oldBookOfLoanLoan != null) {
                    oldBookOfLoanLoan.getLoan().remove(loanLoan);
                    oldBookOfLoanLoan = em.merge(oldBookOfLoanLoan);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Book book) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Book persistentBook = em.find(Book.class, book.getId());
            Author authorOld = persistentBook.getAuthor();
            Author authorNew = book.getAuthor();
            Set<Category> categoryOld = persistentBook.getCategory();
            Set<Category> categoryNew = book.getCategory();
            Set<Loan> loanOld = persistentBook.getLoan();
            Set<Loan> loanNew = book.getLoan();
            if (authorNew != null) {
                authorNew = em.getReference(authorNew.getClass(), authorNew.getId());
                book.setAuthor(authorNew);
            }
            Set<Category> attachedCategoryNew = new HashSet<Category>();
            for (Category categoryNewCategoryToAttach : categoryNew) {
                categoryNewCategoryToAttach = em.getReference(categoryNewCategoryToAttach.getClass(), categoryNewCategoryToAttach.getId());
                attachedCategoryNew.add(categoryNewCategoryToAttach);
            }
            categoryNew = attachedCategoryNew;
            book.setCategory(categoryNew);
            Set<Loan> attachedLoanNew = new HashSet<Loan>();
            for (Loan loanNewLoanToAttach : loanNew) {
                loanNewLoanToAttach = em.getReference(loanNewLoanToAttach.getClass(), loanNewLoanToAttach.getId());
                attachedLoanNew.add(loanNewLoanToAttach);
            }
            loanNew = attachedLoanNew;
            book.setLoan(loanNew);
            book = em.merge(book);
            if (authorOld != null && !authorOld.equals(authorNew)) {
                authorOld.getBook().remove(book);
                authorOld = em.merge(authorOld);
            }
            if (authorNew != null && !authorNew.equals(authorOld)) {
                authorNew.getBook().add(book);
                authorNew = em.merge(authorNew);
            }
            for (Category categoryOldCategory : categoryOld) {
                if (!categoryNew.contains(categoryOldCategory)) {
                    categoryOldCategory.getBook().remove(book);
                    categoryOldCategory = em.merge(categoryOldCategory);
                }
            }
            for (Category categoryNewCategory : categoryNew) {
                if (!categoryOld.contains(categoryNewCategory)) {
                    categoryNewCategory.getBook().add(book);
                    categoryNewCategory = em.merge(categoryNewCategory);
                }
            }
            for (Loan loanOldLoan : loanOld) {
                if (!loanNew.contains(loanOldLoan)) {
                    loanOldLoan.setBook(null);
                    loanOldLoan = em.merge(loanOldLoan);
                }
            }
            for (Loan loanNewLoan : loanNew) {
                if (!loanOld.contains(loanNewLoan)) {
                    Book oldBookOfLoanNewLoan = loanNewLoan.getBook();
                    loanNewLoan.setBook(book);
                    loanNewLoan = em.merge(loanNewLoan);
                    if (oldBookOfLoanNewLoan != null && !oldBookOfLoanNewLoan.equals(book)) {
                        oldBookOfLoanNewLoan.getLoan().remove(loanNewLoan);
                        oldBookOfLoanNewLoan = em.merge(oldBookOfLoanNewLoan);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = book.getId();
                if (findBook(id) == null) {
                    throw new NonexistentEntityException("The book with id " + id + " no longer exists.");
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
            Book book;
            try {
                book = em.getReference(Book.class, id);
                book.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The book with id " + id + " no longer exists.", enfe);
            }
            Author author = book.getAuthor();
            if (author != null) {
                author.getBook().remove(book);
                author = em.merge(author);
            }
            Set<Category> category = book.getCategory();
            for (Category categoryCategory : category) {
                categoryCategory.getBook().remove(book);
                categoryCategory = em.merge(categoryCategory);
            }
            Set<Loan> loan = book.getLoan();
            for (Loan loanLoan : loan) {
                loanLoan.setBook(null);
                loanLoan = em.merge(loanLoan);
            }
            em.remove(book);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Book> findBookEntities() {
        return findBookEntities(true, -1, -1);
    }

    public List<Book> findBookEntities(int maxResults, int firstResult) {
        return findBookEntities(false, maxResults, firstResult);
    }

    private List<Book> findBookEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Book.class));
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

    public Book findBook(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Book.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Book> rt = cq.from(Book.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
