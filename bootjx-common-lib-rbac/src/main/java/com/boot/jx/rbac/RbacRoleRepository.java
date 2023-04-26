package com.boot.jx.rbac;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boot.jx.rbac.docs.RbacRole;

public interface RbacRoleRepository extends MongoRepository<RbacRole, String> {
    RbacRole findByRole(String role);
}
