package com.lavandinas.models;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "delivery_time")
    @Temporal(TemporalType.TIME)
    private Date deliveryTime;

    @Column(name = "pickup_time")
    @Temporal(TemporalType.TIME)
    private Date pickupTime;

    @Column(name = "price")
    private Integer price;

    @NotNull
    @ManyToOne
    @JoinColumn(name="phone_number")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.INICIADA;

    @Column(name = "description")
    private String orderDescription;

    @NotBlank(message = "direccion es obligatoria")
    @Column(name = "address")
    private String orderAddress;

    @Column(name = "is_overnight")
    private String isOvernight = "No";

    @Temporal(TemporalType.DATE)
    @Column(name = "created")
    private Date createdDate;

    @Column(name = "size")
    private String size;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", deliveryDate=" + deliveryDate +
                ", deliveryTime=" + deliveryTime +
                ", price=" + price +
                ", status=" + status +
                ", orderDescription='" + orderDescription + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", isOverNight=" + isOvernight +
                ", createdDate=" + createdDate +
                '}';
    }
}
