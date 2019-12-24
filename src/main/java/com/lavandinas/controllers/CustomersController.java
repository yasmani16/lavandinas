package com.lavandinas.controllers;


import com.lavandinas.models.Customer;
import com.lavandinas.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/administracion")
public class CustomersController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/clientes")
    public String getAllclients(@RequestParam(name = "p", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "n", defaultValue = "10") Integer pageSize,
            /*@RequestParam(name = "s", defaultValue = "createdDate") String sortBy,*/
                               Model model){
        Pageable paging = PageRequest.of(pageNo-1, pageSize/*, Sort.by(sortBy)*/);
        Page<Customer> customerPage = customerRepository.findAll(paging);

        int totalPages = customerPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("customerPage", customerPage);
        return "ver-clientes";
    }

}
