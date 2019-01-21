package server.db.primary.model.basic;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RolePermission.class)
public abstract class RolePermission_ {

	public static volatile SingularAttribute<RolePermission, Integer> permissionId;
	public static volatile SingularAttribute<RolePermission, Integer> roleId;
	public static volatile SingularAttribute<RolePermission, Integer> id;

	public static final String PERMISSION_ID = "permissionId";
	public static final String ROLE_ID = "roleId";
	public static final String ID = "id";

}

