package com.demo.service;


import com.demo.entity.RoleEntity;

public interface IRoleService {

    RoleEntity findByRoleName(String roleName);
}
