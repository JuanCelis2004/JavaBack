package Model;

import Model.Author;
import Model.Category;
import Model.Loan;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-03-10T00:23:50")
@StaticMetamodel(Book.class)
public class Book_ { 

    public static volatile SetAttribute<Book, Loan> loan;
    public static volatile SingularAttribute<Book, Author> author;
    public static volatile SingularAttribute<Book, String> name;
    public static volatile SingularAttribute<Book, Long> id;
    public static volatile SetAttribute<Book, Category> category;

}