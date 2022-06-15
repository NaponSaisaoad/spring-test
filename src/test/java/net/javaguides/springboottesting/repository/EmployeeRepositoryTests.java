package net.javaguides.springboottesting.repository;

import net.javaguides.springboottesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        Employee employeeSaved = employeeRepository.save(employee);

        assertThat(employeeSaved).isNotNull();
        assertThat(employeeSaved.getId()).isGreaterThan(0);
    }

    @DisplayName("JUnit test for get All employee operation")
    @Test
    public void giveEmployeeList_whenFindAll_thenEmployeeList() {

        Employee employee_1 = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();
        Employee employee_2 = Employee.builder()
                .firstName("Bunphot")
                .lastName("Saisaoad")
                .email("BunphotSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee_1);
        employeeRepository.save(employee_2);

        List<Employee> employeeList = employeeRepository.findAll();

        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get by id employee operation")
    @Test
    public void giveEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee);

        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        assertThat(employeeDB).isNotNull();
    }


    @DisplayName("JUnit test for get by email employee operation")
    @Test
    public void giveEmployeeObject_whenFindByEmail_thenReturnEmployeeObject() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee);

        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("JUnit test for update employee operation")
    @Test
    public void giveEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee);

        Employee savaEmployee = employeeRepository.findById(employee.getId()).get();
        savaEmployee.setFirstName("Naja");
        savaEmployee.setEmail("Naja@gmail.com");
        Employee updateEmployee = employeeRepository.save(savaEmployee);

        assertThat(updateEmployee.getFirstName()).isEqualTo("Naja");
        assertThat(updateEmployee.getEmail()).isEqualTo("Naja@gmail.com");
    }

    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void giveEmployeeObject_whenDeleteEmployee_thenReturnEmployeeObject() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee);

        employeeRepository.delete(employee);

        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        assertThat(employeeOptional).isEmpty();

    }

    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    public void giveFirstNameAndLastName_wheFindByJPQL_thenReturnEmployeeObject() {

        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        employeeRepository.save(employee);
        String firstName = "Napon";
        String lastName = "Saisaoad";


        Employee saveEmployee = employeeRepository.findByJPQL(firstName, lastName);

        assertThat(saveEmployee).isNotNull();

    }


}
