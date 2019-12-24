package com.lavandinas.controllers;

import com.lavandinas.exceptions.ResourceNotFound;
import com.lavandinas.models.*;
import com.lavandinas.repositories.CustomerRepository;
import com.lavandinas.repositories.EmployeeRespository;
import com.lavandinas.repositories.OrderRepository;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/administracion")
public class OrdersController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EmployeeRespository employeeRespository;

    private final ModelMapper modelMapper = new ModelMapper();

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @PostMapping("/crearsolicitud")
    public String addOrder(@Valid CustomerOrderDTO customerOrderDTO, BindingResult result, Model model) throws ParseException {
        if (result.hasErrors()) {
            return "crear-solicitud";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        customerOrderDTO.setDeliveryDate(new SimpleDateFormat("dd/MM/yyyy").parse(customerOrderDTO.getDeliveryDateAsString()));
        customerOrderDTO.setDeliveryTime(new SimpleDateFormat("HH:mm").parse(customerOrderDTO.getDeliveryTimeAsString()));
        if(customerOrderDTO.getPickupTimeAsString() != null)
            customerOrderDTO.setPickupTime(new SimpleDateFormat("HH:mm").parse(customerOrderDTO.getPickupTimeAsString()));
        customerOrderDTO.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").parse(formatter.format(new Date())));
        customerOrderDTO.setStatus(Status.ACEPTADA);
        try {
            customerOrderDTO.setPrice(Integer.parseInt(customerOrderDTO.getPriceAsString()));
        }catch(Exception e){
            FieldError fieldError = new FieldError("CustomerOrderDTO", "price", "Precio debe ser solo números");
            result.addError(fieldError);
            return "crear-solicitud";
        }

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
        return "redirect:/administracion";
    }

    @GetMapping
    public String getTodaysOrders(@RequestParam(name = "p", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "n", defaultValue = "10") Integer pageSize,
                                   /*@RequestParam(name = "s", defaultValue = "createdDate") String sortBy,*/
                                   Model model, Principal principal) throws ParseException {
        Pageable paging = PageRequest.of(pageNo-1, pageSize/*, Sort.by(sortBy)*/);

        Page<Order> orderPage = orderRepository.getOrdersByCreatedDate(paging, new SimpleDateFormat("dd/MM/yyyy").parse(formatter.format(new Date())));
        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("orderPage", orderPage);
        return "ver-solicitudes-hoy";
    }

    @GetMapping("/solicitudes")
    public String getAllOrders(@RequestParam(name = "p", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "n", defaultValue = "10") Integer pageSize,
                                /*@RequestParam(name = "s", defaultValue = "createdDate") String sortBy,*/
                               Model model, @RequestParam(name = "buscar", required = false) String search){
        Pageable paging = PageRequest.of(pageNo-1, pageSize/*, Sort.by(sortBy)*/);
        Page<Order> orderPage;
        if(search != null){
            orderPage = orderRepository.getOrdersByPhoneOrByName(paging, search);
        }
        else {
            orderPage = orderRepository.findAll(paging);
        }
        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("orderPage", orderPage);
        return "ver-solicitudes-todas";
    }

    @GetMapping("/crearsolicitudform")
    public String viewOrderForm(CustomerOrderDTO customerOrderDTO){
        return "crear-solicitud";
    }

    @GetMapping("/editarsolicitudform/{orderId}")
    public String viewUpdateOrderForm(@PathVariable("orderId") long orderId, Model model) throws ResourceNotFound {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound());
        CustomerOrderDTO customerOrderDTO = modelMapper.map(order, CustomerOrderDTO.class);
        customerOrderDTO.setDeliveryDateAsString(new SimpleDateFormat("dd/MM/yyyy").format(order.getDeliveryDate()));
        customerOrderDTO.setDeliveryTimeAsString(new SimpleDateFormat("HH:mm").format(order.getDeliveryTime()));
        if(order.getPickupTime() != null)
            customerOrderDTO.setPickupTimeAsString(new SimpleDateFormat("HH:mm").format(order.getPickupTime()));
        customerOrderDTO.setPriceAsString(Optional.ofNullable(order.getPrice()).orElse(0).toString());
        Iterable<Employee> employees = employeeRespository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("customerOrderDTO", customerOrderDTO);
        return "editar-solicitud";
    }

    @PostMapping("/editarsolicitud/{orderId}")
    public String updateOrder(@PathVariable("orderId") long orderId, @Valid CustomerOrderDTO customerOrderDTO, BindingResult result, Model model) throws ParseException, ResourceNotFound {
        if (result.hasErrors()) {
            customerOrderDTO.setOrderId(orderId);
            return "editar-solicitud";
        }
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFound());
        customerOrderDTO.setDeliveryDate(new SimpleDateFormat("dd/MM/yyyy").parse(customerOrderDTO.getDeliveryDateAsString()));
        customerOrderDTO.setDeliveryTime(new SimpleDateFormat("HH:mm").parse(customerOrderDTO.getDeliveryTimeAsString()));
        if(customerOrderDTO.getPickupTimeAsString() != null)
            customerOrderDTO.setPickupTime(new SimpleDateFormat("HH:mm").parse(customerOrderDTO.getPickupTimeAsString()));
        customerOrderDTO.setCreatedDate(order.getCreatedDate());
        customerOrderDTO.setIsOvernight(Optional.ofNullable(customerOrderDTO.getIsOvernight()).orElse("No"));
        try {
            customerOrderDTO.setPrice(Integer.parseInt(customerOrderDTO.getPriceAsString()));
        }catch(Exception e){
            FieldError fieldError = new FieldError("CustomerOrderDTO", "price", "Precio debe ser solo números");
            result.addError(fieldError);
            return "editar-solicitud";
        }
        order = modelMapper.map(customerOrderDTO, Order.class);
        orderRepository.save(order);
        model.addAttribute("order", order);
        return "redirect:/administracion";
    }

}
