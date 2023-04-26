package com.boot.jx.rbac.docs;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RBAC_ROLE")
public class RbacRole {

    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)

    private String role;

    @DBRef
    private Set<RbacPrivilege> privileges;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public Set<RbacPrivilege> getPrivileges() {
	return privileges;
    }

    public void setPrivileges(Set<RbacPrivilege> privileges) {
	this.privileges = privileges;
    }

}
