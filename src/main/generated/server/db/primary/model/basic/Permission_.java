package server.db.primary.model.basic;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Permission.class)
public abstract class Permission_ {

	public static volatile SingularAttribute<Permission, String> name;
	public static volatile SingularAttribute<Permission, String> permission;
	public static volatile SingularAttribute<Permission, Integer> id;

	public static final String NAME = "name";
	public static final String PERMISSION = "permission";
	public static final String ID = "id";

}

