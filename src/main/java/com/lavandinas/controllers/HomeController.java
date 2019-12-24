package com.lavandinas.controllers;

import com.lavandinas.models.Customer;
import com.lavandinas.models.CustomerOrderDTO;
import com.lavandinas.models.Order;
import com.lavandinas.models.Status;
import com.lavandinas.repositories.CustomerRepository;
import com.lavandinas.repositories.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping()
    public String home(CustomerOrderDTO customerOrderDTO){
        return "index";
    }

    @PostMapping("/agregarsolicitud")
    public String addOrder(@Valid CustomerOrderDTO customerOrderDTO, BindingResult result, Model model) throws ParseException {
        if (result.hasErrors()) {
            return "solicitud";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        customerOrderDTO.setDeliveryDate(new SimpleDateFormat("dd/MM/yyyy").parse(customerOrderDTO.getDeliveryDateAsString()));
        customerOrderDTO.setDeliveryTime(new SimpleDateFormat("HH:mm").parse(customerOrderDTO.getDeliveryTimeAsString()));
        customerOrderDTO.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").parse(formatter.format(new Date())));
        customerOrderDTO.setStatus(Status.INICIADA);
        Customer customer = modelMapper.map(customerOrderDTO, Customer.class);
        Order order = modelMapper.map(customerOrderDTO, Order.class);


        Optional<Customer> optionalCustomer = customerRepository.findById(customerOrderDTO.getCustomerPhone());
        if(!optionalCustomer.isPresent()) {
            customer.setCustomerPhone(customerOrderDTO.getCustomerPhone());
            customer.setOrders(Arrays.asList(order));
            order.setCustomer(customer);
            customerRepository.save(customer);
        }
        else {
            customer = optionalCustomer.get();
            order.setCustomer(customer);
            order = orderRepository.save(order);
        }
        model.addAttribute("order", order);
        return "gracias";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "loginPage";
    }

    @GetMapping("/403")
    public String accessDenied(Model model) {
        return "403Page";
    }


}
