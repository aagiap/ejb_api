package com.example.ws_cert.repository;

import com.example.ws_cert.constant.UserRole;
import com.example.ws_cert.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(UserRole name);
}
