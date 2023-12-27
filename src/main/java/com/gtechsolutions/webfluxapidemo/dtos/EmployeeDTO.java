package com.gtechsolutions.webfluxapidemo.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private String id;
    private String lastName;
    private String firstName;
    private String email;

}
