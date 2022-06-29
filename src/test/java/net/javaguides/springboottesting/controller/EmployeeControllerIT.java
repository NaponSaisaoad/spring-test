package net.javaguides.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Testcontainers
public class EmployeeControllerIT {

    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("ems");

    @DynamicPropertySource
    public static void  dynamicPropertySource(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {


        System.out.println(mySQLContainer.getUsername());
        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

       ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

       response.andDo(print())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Napon").lastName("Saisaoad").email("NaponSaisaoad@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Bunphot").lastName("Saisaoad").email("BunphotSaisaoad@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeesId_thenReturnEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();
        employeeRepository.save(employee);

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    @Test
    public void givenInvalidEmployee_whenGetEmployeesId_thenReturnEmpty() throws Exception {
        long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void givenUpdateEmployee_whenGetUpdateEmployees_thenReturnUpdateEmployeeObject() throws Exception {
        long employeeId = 1L;
        Employee saveEmployee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Bunphot")
                .lastName("Saisaoad")
                .email("BunphotSaisaoad@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(saveEmployee));

        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(updateEmployee.getEmail())));
    }

    @Test
    public void givenUpdateEmployee_whenGetUpdateEmployees_thenReturn404() throws Exception {
        long employeeId = 1L;
        Employee saveEmployee = Employee.builder()
                .firstName("Napon")
                .lastName("Saisaoad")
                .email("NaponSaisaoad@gmail.com")
                .build();

        Employee updateEmployee = Employee.builder()
                .firstName("Bunphot")
                .lastName("Saisaoad")
                .email("BunphotSaisaoad@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        response.andDo(print())
                .andExpect(status().isOk());
    }
}
