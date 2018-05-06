package com.rsm.role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();
    Optional<Role> findRoleByRoleName(String roleName);
    void save(Role role);
    Role saveAndReturn(Role role);
    void delete(Role role);
    void deleteById(Long id);
}
