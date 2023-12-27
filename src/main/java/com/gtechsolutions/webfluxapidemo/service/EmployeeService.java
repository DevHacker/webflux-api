package com.gtechsolutions.webfluxapidemo.service;

import com.gtechsolutions.webfluxapidemo.dtos.EmployeeDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Mono<ResponseEntity<EmployeeDTO>> createEmployee(EmployeeDTO employeeDTO) throws IllegalAccessException;
    Mono<ResponseEntity<EmployeeDTO>> updateEmployee(EmployeeDTO employeeDTO, String employeeId);
    Mono<ResponseEntity<EmployeeDTO>> retrieveEmployeeById(String employeeId);
    Flux<ResponseEntity<EmployeeDTO>> retrieveAllEmployees();
    Mono<ResponseEntity<Void>> deleteEmployee(String employeeId);

}
