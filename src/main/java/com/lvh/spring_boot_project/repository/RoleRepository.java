package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(String name);

}
