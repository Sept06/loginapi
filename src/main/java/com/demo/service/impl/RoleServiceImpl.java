package com.demo.service.impl;

import com.demo.entity.RoleEntity;
import com.demo.repositories.RoleRepository;
import com.demo.service.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoleEntity findByRoleName(String roleName) {
        return repository.findByName(roleName);
    }
}
