package com.toptaltest.toptaltest.controller;

import com.toptaltest.toptaltest.entity.Employee;
import com.toptaltest.toptaltest.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/toptal_test")
public class RestAPIController {
    EmployeeService employeeService;

    public RestAPIController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/read_csv")
    public ResponseEntity<List<Employee>> readCsv() {
        return ResponseEntity.ok().body(employeeService.getEmployeesList());
    }
}
