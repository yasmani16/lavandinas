package com.lavandinas.services;

import com.lavandinas.models.Employee;
import com.lavandinas.repositories.EmployeeRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRespository employeeRespository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeRespository.findByLogin(s);
        return employee.map(e -> {
            List<String> roles = Arrays.asList(e.getRole());
            List<GrantedAuthority> grantedAuthorityList = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return new User(e.getLogin(), e.getPassword(), grantedAuthorityList);
        }).orElseThrow(() -> new UsernameNotFoundException("Empleado no existe."));
    }

}
