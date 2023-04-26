package com.boot.jx.rbac.docs;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "RBAC_PRIVILEGE")
public class RbacPrivilege {
    @Id
    private Integer id;
    private String privilegeName;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getPrivilegeName() {
	return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
	this.privilegeName = privilegeName;
    }
}
