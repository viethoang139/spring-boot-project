package com.lvh.spring_boot_project.repository;

import com.lvh.spring_boot_project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
