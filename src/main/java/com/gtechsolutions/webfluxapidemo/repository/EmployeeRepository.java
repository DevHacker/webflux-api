package com.gtechsolutions.webfluxapidemo.repository;

import com.gtechsolutions.webfluxapidemo.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
