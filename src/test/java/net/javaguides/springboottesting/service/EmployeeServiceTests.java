package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.exception.ResourceNotFoundException;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.Impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();
    }

    @DisplayName("JUnit test for for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        Employee saveEmployee = employeeService.saveEmployee(employee);

        Assertions.assertThat(saveEmployee).isNotNull();
    }

    @DisplayName("JUnit test for for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @DisplayName("JUnit test for for getAllEmployees method")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList() {

        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Bunphot")
                .lastName("Saisaoad")
                .email("BunphotSaisaoad@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        List<Employee> employeeList = employeeService.getAllEmployees();

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        List<Employee> employeeList = employeeService.getAllEmployees();

        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for for getEmployeeById method")
    @Test
    public void givenGetByIdEmployee_whenGetAllEmployees_thenReturnGetByIdEmployee() {

        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        Employee saveEmployee = employeeService.getEmployeeById(employee.getId()).get();

        Assertions.assertThat(saveEmployee).isNotNull();
    }

    @DisplayName("JUnit test for for updateEmployee method")
    @Test
    public void givenUpdateEmployee_whenGetAllEmployees_thenReturnUpdateEmployee() {

        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Napon");
        employee.setEmail("NaponSaisaoad@gmail.com");

        Employee updateEmployee = employeeService.updateEmployee(employee);

        Assertions.assertThat(updateEmployee.getEmail()).isEqualTo("NaponSaisaoad@gmail.com");
    }

    @DisplayName("JUnit test for for deleteEmployee method")
    @Test
    public void givenDeleteEmployee_whenGetAllEmployees_thenReturnDeleteEmployee() {

        long employeeId = 1L;

        willDoNothing().given(employeeRepository).deleteById(employeeId);

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
}
