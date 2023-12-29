package com.gtechsolutions.webfluxapidemo.service.impl;

import com.gtechsolutions.webfluxapidemo.dtos.EmployeeDTO;
import com.gtechsolutions.webfluxapidemo.entity.Employee;
import com.gtechsolutions.webfluxapidemo.mapper.EmployeeMapper;
import com.gtechsolutions.webfluxapidemo.repository.EmployeeRepository;
import com.gtechsolutions.webfluxapidemo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.validation.Validator;

@Service
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    public static final String API_URL = "http://localhost:8081/api/v1/employees/";
    public static final String HEADER_NAME = "Location";
    private final EmployeeRepository employeeRepository;

    private Validator validator;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

    }


    @Override
    public Mono<ResponseEntity<EmployeeDTO>> createEmployee(EmployeeDTO employeeDTO) throws IllegalAccessException {

        Employee employee = EmployeeMapper.fromEmployeeDTO(employeeDTO);
        if (employeeDTO.getId() != null) {
            throw new IllegalArgumentException("Employee to be created must not have an id");
        }

        return employeeRepository.save(employee)
                .map(savedEmployee -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_NAME, API_URL + savedEmployee.getId())
                        .body(EmployeeMapper.fromEmployee(savedEmployee)))
                .doOnError(error -> log.error("Error occurred while saving employee", error))
                .onErrorReturn(ResponseEntity.badRequest().build())
                .log();
    }

    @Override
    public Mono<ResponseEntity<EmployeeDTO>> updateEmployee(EmployeeDTO employeeDTO, String employeeId) {
        Employee employee = EmployeeMapper.fromEmployeeDTO(employeeDTO);
        return employeeRepository.findById(employeeId)
                .flatMap(foundedEmployee -> {
                    foundedEmployee.setLastName(employee.getLastName());
                    foundedEmployee.setFirstName(employee.getFirstName());
                    foundedEmployee.setEmail(employee.getEmail());
                    return employeeRepository.save(foundedEmployee);
                })
                .map(savedEmployee -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_NAME, API_URL + savedEmployee.getId())
                        .body(EmployeeMapper.fromEmployee(savedEmployee)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @Override
    public Mono<ResponseEntity<EmployeeDTO>> retrieveEmployeeById(String employeeId) {
        log.info("retrieveEmployeeById id : {}", employeeId);

        return this.employeeRepository.findById(employeeId)
                .doOnNext(employee -> log.info("Found employee id is : {}", employee.getId()))
                .flatMap(foundedEmployee -> Mono.just(ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HEADER_NAME, API_URL + foundedEmployee.getId())
                        .body(EmployeeMapper.fromEmployee(foundedEmployee))))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @Override
    public Flux<ResponseEntity<EmployeeDTO>> retrieveAllEmployees() {
        Flux<Employee> employees = employeeRepository.findAll();
        return employees
                .map(EmployeeMapper::fromEmployee)
                .flatMap(mappedEmployee -> Mono.just(ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mappedEmployee)))
                .switchIfEmpty(Flux.empty())
                .log();
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteEmployee(String employeeId) {
        return this.employeeRepository.findById(employeeId)
                .flatMap(foundEmployee -> employeeRepository.deleteById(foundEmployee.getId())
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    /*private void validate(Employee employee) {
        var constraintViolations = validator.validate(employee);
        log.info("constraintViolations {}", constraintViolations);

        if (!constraintViolations.isEmpty()) {
            var errorMessage = constraintViolations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(", "));

           *//* Errors errors = new BeanPropertyBindingResult(review, "review");
            validator.validate(review);
            if (errors.hasErrors()) {
                throw new ServerWebInputException(errors.toString());
            }*//*
            throw new RuntimeException(errorMessage);
        }
    }*/
}
