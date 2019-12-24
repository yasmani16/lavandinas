package com.lavandinas.controllers;

import com.lavandinas.exceptions.ResourceNotFound;
import com.lavandinas.models.Employee;
import com.lavandinas.repositories.EmployeeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/administracion")
public class EmployeesController {

    @Autowired
    private EmployeeRespository employeeRespository;

    @GetMapping("/empleados")
    public String getAllEmployees(Model model){
        model.addAttribute("employees", employeeRespository.findAll());
        return "ver-empleados";
    }

    @GetMapping("/crearempleadoform")
    public String addEmployeeForm(Employee employee){
        return "crear-empleado";
    }

    @GetMapping("/editarsempleadoform/{employeeId}")
    public String editEmployeeForm(@PathVariable("employeeId") long employeeId, Model model) throws ResourceNotFound {
        Employee employee = employeeRespository.findById(employeeId).orElseThrow(() -> new ResourceNotFound());
        model.addAttribute("employee", employee);
        return "editar-empleado";
    }

    @PostMapping("/editarempleado/{employeeId}")
    public String editEmployee(@PathVariable("employeeId") long employeeId, @Valid Employee employee, BindingResult result, Model model){
        if (result.hasErrors()) {
            employee.setEmployeeId(employeeId);
            return "editar-empleado";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        employee.setPassword(encoder.encode(employee.getPassword()));
        employeeRespository.save(employee);
        return "redirect:/administracion/empleados";
    }


    @PostMapping("/crearempleado")
    public String addEmployee(@Valid Employee employee, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "crear-empleado";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        employee.setPassword(encoder.encode(employee.getPassword()));
        employeeRespository.save(employee);
        return "redirect:/administracion/empleados";
    }

}
