package com.demo.repositories;

import com.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {

    RoleEntity findByName(String roleName);

}
