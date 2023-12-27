package com.gtechsolutions.webfluxapidemo.mapper;

import com.gtechsolutions.webfluxapidemo.dtos.EmployeeDTO;
import com.gtechsolutions.webfluxapidemo.entity.Employee;
import org.springframework.context.annotation.Configuration;

public class EmployeeMapper {
    public static EmployeeDTO fromEmployee(Employee employee){
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

    public static Employee fromEmployeeDTO(EmployeeDTO employeeDTO){
        return new Employee(
                employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail()
        );

            }
}
