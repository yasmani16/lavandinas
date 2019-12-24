package com.lavandinas.repositories;

import com.lavandinas.models.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {
}
