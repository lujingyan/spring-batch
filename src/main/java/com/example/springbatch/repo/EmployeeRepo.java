package com.example.springbatch.repo;

import com.example.springbatch.entity.Employee;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("local")
public interface EmployeeRepo extends JpaRepository<Employee , Integer> {
}
