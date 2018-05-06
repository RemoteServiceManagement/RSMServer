package com.rsm.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findRoleByRoleName(String roleName) {
        return roleRepository.findRoleByRoleName(roleName);
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role saveAndReturn(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
