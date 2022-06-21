package net.javaguides.springboottesting.controller;

import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId) {
        return employeeService.getEmployeeById(employeeId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee) {
        return employeeService.getEmployeeById(employeeId).map(saveEmployee -> {
            saveEmployee.setFirstName(employee.getFirstName());
            saveEmployee.setLastName(employee.getLastName());
            saveEmployee.setEmail(employee.getEmail());

            Employee updateEmployee = employeeService.updateEmployee(saveEmployee);
            return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
