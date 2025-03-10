package Model;

import Model.Book;
import Model.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-03-10T00:23:50")
@StaticMetamodel(Loan.class)
public class Loan_ { 

    public static volatile SingularAttribute<Loan, Book> book;
    public static volatile SingularAttribute<Loan, String> name;
    public static volatile SingularAttribute<Loan, Long> id;
    public static volatile SingularAttribute<Loan, User> user;

}