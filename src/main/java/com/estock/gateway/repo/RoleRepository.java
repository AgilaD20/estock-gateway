package com.estock.gateway.repo;

import com.estock.gateway.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query
    public Role findByRoleName(String rolename);

}