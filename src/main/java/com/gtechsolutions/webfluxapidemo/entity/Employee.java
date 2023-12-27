package com.gtechsolutions.webfluxapidemo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "employees")
public class Employee {
    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
}
