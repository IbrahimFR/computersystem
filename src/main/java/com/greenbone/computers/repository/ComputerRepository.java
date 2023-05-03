package com.greenbone.computers.repository;

import com.greenbone.computers.domain.Computer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Computer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {}
