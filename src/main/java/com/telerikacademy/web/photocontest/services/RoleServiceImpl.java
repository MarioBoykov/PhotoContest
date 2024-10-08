package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Role;
import com.telerikacademy.web.photocontest.repositories.RoleRepository;
import com.telerikacademy.web.photocontest.services.contracts.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleById(UUID id) {
        return roleRepository.getReferenceById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
