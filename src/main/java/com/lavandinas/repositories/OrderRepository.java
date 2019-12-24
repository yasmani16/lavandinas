package com.lavandinas.repositories;

import com.lavandinas.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
@Transactional
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    public Page<Order> getOrdersByCreatedDate(Pageable pageable, Date created);

    @Query("select o from Order o where o.customer.customerPhone LIKE %:item% or o.customer.customerName LIKE %:item%")
    public Page<Order> getOrdersByPhoneOrByName(Pageable pageable, String item);
}
