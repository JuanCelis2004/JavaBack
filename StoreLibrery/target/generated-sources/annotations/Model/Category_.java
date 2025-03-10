package Model;

import Model.Book;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-03-10T00:23:50")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile SetAttribute<Category, Book> book;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile SingularAttribute<Category, Long> id;

}