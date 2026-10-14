package com.toptaltest.toptaltest.service;

import com.toptaltest.toptaltest.entity.Employee;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    public List<Employee> getEmployeesList() {
        List<Employee> employees = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("employees.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (line.isBlank()) continue;
                String[] data = line.split(",");
                Employee emp = new Employee();
                emp.setId(Integer.parseInt(data[0]));
                emp.setName(data[1]);
                emp.setDepartment(data[2]);
                emp.setSalary(Double.parseDouble(data[3]));
                employees.add(emp);
            }
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can not read csv file");
        }
        return employees;
    }
}
