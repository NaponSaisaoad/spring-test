package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.Impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class EmployeeServiceTests {

    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    public void setup(){
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }
}
