package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code,Long> {

    Optional<Code> findByCode(String code);
}
