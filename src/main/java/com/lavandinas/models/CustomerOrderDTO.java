package com.lavandinas.models;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class CustomerOrderDTO {

    @NotBlank(message = "Se requiere su nombre")
    @Size(min = 2, max = 40, message = "Nombre debe ser entre 2 y 40 letras")
    private String customerName;
    @Pattern(regexp = "\\d{7,10}", message = "Número de teléfono debe ser de 7 a 10 dígitos")
    private String customerPhone;
    @Size(max = 40, message = "Su correo es demasiado largo")
    private String customerEmail;
    @NotBlank(message = "Se requiere su direccíon")
    @Size(max = 140, message = "Su direccíon es demasiado larga")
    private String orderAddress;
    @Size(max = 140, message = "Su mensaje debe ser de 140 letras")
    private String orderDescription;
    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{4}", message = "Fecha inválida")
    private String deliveryDateAsString;
    @Pattern(regexp = "\\d{1,2}:\\d{1,2}", message = "Hora inválida")
    private String deliveryTimeAsString;
    @Pattern(regexp = "\\d{1,2}:\\d{1,2}", message = "Hora inválida")
    private String pickupTimeAsString;
    private Status status;
    private String priceAsString;
    private Date deliveryDate;
    private Date deliveryTime;
    private Date pickupTime;
    private Date createdDate;
    private Integer price;
    private long orderId;
    private Integer employeeId;
    private String size;
    private String isOvernight;
}
