package com.demo.repositories;

import com.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    UserEntity findByUsername(String username);

    boolean existsByUsername(String username);

    UserEntity findByUsernameAndPassword(String username,String password);


}
