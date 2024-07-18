package net.javaguides.spring_boot_testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.spring_boot_testing.model.Employee;
import net.javaguides.spring_boot_testing.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("JUnitCreate A New Employee Using Post using MySQL Database")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //Given - precondition or setup using MySQL Database
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("GerardoDB")
                .lastName("AponteDB")
                .email("aponteDB@test.com")
                .build();
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

    //JUnit test for GetAll Employee API using MySQL Database
    @Test
    @DisplayName("Unit test for GetAll Employee API using MySQL Database")
    public void givenListOfEmployee_whenGetAllEmployee_thenReturnListOfEmployee() throws Exception {
        //Given - precondition or setup
        List<Employee> listOfEmployee = new ArrayList<>();
        listOfEmployee.add(Employee.builder().firstName("Gerardo").lastName("Aponte").email("gerardo@test.com").build());
        listOfEmployee.add(Employee.builder().firstName("Ariel").lastName("Garcia").email("ariel@test.com").build());
        listOfEmployee.add(Employee.builder().firstName("agua").lastName("bottton").email("peluche@test.com").build());
        employeeRepository.saveAll(listOfEmployee);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));
        //Then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployee.size())));
    }
    //JUnit test for Get Employee by ID Rest API using MySQL Database
    @Test
    @DisplayName("JUnit test for Get Employee by Id Rest API using MySQL Database")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //Given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }


    //using MySQL Database
    @Test
    @DisplayName("JUnit test for Get Employee by Id Negative Scenario using MySQL Database")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmpity() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    //JUnit test for updated employee REST API using MySQL Database
    @Test
    @DisplayName("JUnit test for updated employee REST API using MySQL Database ")
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //Given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Ariel")
                .lastName("Garcia")
                .email("Garcia@test.com")
                .build();
        //When - action or the behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    // using MySQL Database
    @Test
    @DisplayName("JUnit test for updated employee REST API Negative Scenario using MySQL Database")
    public void givenUpdatedEmployee_whenUpdatedEmployeeNegative_thenReturnUpdateEmployeeObject() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        employeeRepository.save(savedEmployee);
        Employee updatedEmployee = Employee.builder()
                .firstName("Ariel")
                .lastName("Garcia")
                .email("Garcia@test.com")
                .build();
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        //Then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit test for deleted employee REST API using MySQL Database
    @Test
    @DisplayName("Unit test for deleted employee REST API using MySQL Database")
    public void givenEmployeeId_whenDeleteEmployee_thenReturnEmployeeObjectDelete() throws Exception {
        //Given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
        employeeRepository.save(savedEmployee);
        //When - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
        //Then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
