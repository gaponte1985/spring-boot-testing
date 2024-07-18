package net.javaguides.spring_boot_testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.spring_boot_testing.model.Employee;
import net.javaguides.spring_boot_testing.services.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    //JUnit test for
    @Test
    @DisplayName("JUnitCreate A New Employee Using Post")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //Given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));
    }

    //JUnit test for GetAll Employee API
    @Test
    @DisplayName("Unit test for GetAll Employee API")
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception {
        //Given - precondition or setup
        List<Employee> listOfEmployee = new ArrayList<>();
        listOfEmployee.add(Employee.builder().firstName("Gerardo").lastName("Aponte").email("gerardo@test.com").build());
        listOfEmployee.add(Employee.builder().firstName("Ariel").lastName("Garcia").email("ariel@test.com").build());
        listOfEmployee.add(Employee.builder().firstName("agua").lastName("bottton").email("peluche@test.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployee);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //Then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployee.size())));
    }

    //JUnit test for Get Employee by ID Rest API
    @Test
    @DisplayName("JUnit test for Get Employee by Id Rest API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    @DisplayName("JUnit test for Get Employee by Id Negative Scenario")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpity() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit test for updated employee REST API
    @Test
    @DisplayName("JUnit test for updated employee REST API ")
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .firstName("Ariel")
                .lastName("Garcia")
                .email("Garcia@test.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //When - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    //JUnit test for
    @Test
    @DisplayName("JUnit test for updated employee REST API Negative Scenario")
    public void givenUpdatedEmployee_whenUpdatedEmployeeNegative_thenReturnUpdateEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstName("Ariel")
                .lastName("Garcia")
                .email("Garcia@test.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //When - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit test for deleted employee REST API
    @Test
    @DisplayName("Unit test for deleted employee REST API")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnEmployeeObjectDelete() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
