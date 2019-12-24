package com.lavandinas.repositories;

import com.lavandinas.models.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface EmployeeRespository extends PagingAndSortingRepository<Employee, Long> {
    public Optional<Employee> findByLogin(String login);
}
