package Model;

import Model.Loan;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-03-10T00:23:50")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SetAttribute<User, Loan> loan;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, Long> id;

}