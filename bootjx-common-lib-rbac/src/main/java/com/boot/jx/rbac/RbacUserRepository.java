package com.boot.jx.rbac;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boot.jx.rbac.docs.RbacUser;

public interface RbacUserRepository extends MongoRepository<RbacUser, String> {
    RbacUser findByUsername(String username);
}
