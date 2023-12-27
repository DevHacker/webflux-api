package com.gtechsolutions.webfluxapidemo.controller;

import com.gtechsolutions.webfluxapidemo.dtos.EmployeeDTO;
import com.gtechsolutions.webfluxapidemo.service.impl.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;

    @PostMapping("/create-employee")
    public Mono<ResponseEntity<EmployeeDTO>> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws IllegalAccessException {
        return employeeService.createEmployee(employeeDTO);
    }

    @PutMapping("/update-employee/{employeeId}")
    public Mono<ResponseEntity<EmployeeDTO>> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable("employeeId") String employeeId) throws IllegalAccessException {
        return employeeService.updateEmployee(employeeDTO, employeeId);
    }

    @GetMapping("/{employeeId}")
    public Mono<ResponseEntity<EmployeeDTO>> retrieveEmployee(@PathVariable("employeeId") String employeeId) throws IllegalAccessException {
        return employeeService.retrieveEmployeeById(employeeId);
    }

    @GetMapping("/")
    public Flux<ResponseEntity<EmployeeDTO>> RetrieveAllEmployees() {
        return employeeService.retrieveAllEmployees();
    }

    @DeleteMapping("/{employeeId}")
    public Mono<ResponseEntity<Void>> deleteAllEmployee(@PathVariable("employeeId") String employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }
}
