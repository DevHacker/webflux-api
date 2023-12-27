package com.gtechsolutions.webfluxapidemo.controller;

import com.gtechsolutions.webfluxapidemo.dtos.EmployeeDTO;
import com.gtechsolutions.webfluxapidemo.repository.EmployeeRepository;
import com.gtechsolutions.webfluxapidemo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    static final String EMPLOYEE_URI = "/api/v1/employees";

    @BeforeEach
    void setUp() {
        var employees = List.of(new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com"),
                new EmployeeDTO(null, "DONVIDE", "DIABATE Segana Rolande", "s.diro@gmail.com"));

    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll().block();
    }

    @Test
    void createEmployee() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");

        //EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        webTestClient.post()
                .uri(EMPLOYEE_URI + "/create-employee")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDTO), EmployeeDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDTO.class)
                .consumeWith(employeeDTOEntityExchangeResult -> {
                    var result = employeeDTOEntityExchangeResult.getResponseBody();
                    assert result != null;
                    assert result.getId() != null;

                });

        StepVerifier.create(employeeService.createEmployee(employeeDTO)).expectNextCount(1);

    }

    @Test
    void updateEmployee() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");
        EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        var employeeToUpdate = new EmployeeDTO(null, "DONVIDE", "Fidele", "toussaint.donvide@gmail.com");

        assert savedEmployee != null;
        webTestClient.put()
                .uri(EMPLOYEE_URI + "/update-employee/"+savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(Mono.just(employeeToUpdate), EmployeeDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .consumeWith(employeeDTOEntityExchangeResult -> {
                    var result = employeeDTOEntityExchangeResult.getResponseBody();
                    assert(result != null);
                   assertEquals(result.getFirstName(), employeeToUpdate.getFirstName());
                   assertEquals(result.getEmail(), employeeToUpdate.getEmail());
                });
    }

    @Test
    void retrieveEmployee() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");
        EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        webTestClient.get()
                .uri(EMPLOYEE_URI +"/"+savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDTO.class)
                .consumeWith(employeeDTOEntityExchangeResult -> {
                    var result = employeeDTOEntityExchangeResult.getResponseBody();
                    assert(result != null);
                    assertEquals(result.getFirstName(), savedEmployee.getFirstName());
                    assertEquals(result.getEmail(), savedEmployee.getEmail());
                    assertEquals(result.getLastName(), savedEmployee.getLastName());
                });

    }

    @Test
    void retrieveAllEmployees() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");
        EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        var employeeDTO1 = new EmployeeDTO(null, "DONVIDE", "Anne Marie Danielle", "a.danielle@gmail.com");
        EmployeeDTO savedEmployee1 = Objects.requireNonNull(employeeService.createEmployee(employeeDTO1).block()).getBody();


        webTestClient.get()
                .uri(EMPLOYEE_URI +"/")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDTO.class)
                .hasSize(2);
    }

    @Test
    void deleteAllEmployee() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");
        EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        webTestClient.delete()
                .uri(EMPLOYEE_URI +"/"+savedEmployee.getId())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteAllEmployee_withError() throws IllegalAccessException {
        var employeeDTO = new EmployeeDTO(null, "DONVIDE", "Nouhousse Toussaint Fidele", "f.donvide@gmail.com");
        EmployeeDTO savedEmployee = Objects.requireNonNull(employeeService.createEmployee(employeeDTO).block()).getBody();

        webTestClient.delete()
                .uri(EMPLOYEE_URI +"/123")
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().isNotFound();
    }
}