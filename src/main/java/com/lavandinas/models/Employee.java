package com.lavandinas.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id")
    private long employeeId;

    @Column(name = "employee_name")
    @NotBlank(message = "Nombre es obligatorio")
    private String employeeName;

    @OneToMany(mappedBy = "employee")
    private List<Order> orders;

    @Column(name = "user_name")
    @NotBlank(message = "Nombre de usuario es obligatorio")
    private String login;

    @Column(name = "password")
    @NotBlank(message = "Contraseña es obligatorio")
    private String password;

    @Column(name = "role")
    @NotNull
    private String role;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", userName='" + login + '\'' +
                ", password='" + password + '\'' +
                ", position='" + role + '\'' +
                '}';
    }
}
