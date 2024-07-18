package net.javaguides.spring_boot_testing.service;

import net.javaguides.spring_boot_testing.exception.ResourceNotFoundException;
import net.javaguides.spring_boot_testing.model.Employee;
import net.javaguides.spring_boot_testing.repository.EmployeeRepository;
import net.javaguides.spring_boot_testing.services.impl.EmployeeServiceImpl;
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
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //  employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
    }


    // Junit Test for SaveEmployee method
    @Test
    @DisplayName("Junit Test for SaveEmployee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        //Given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);
        //When - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        //Then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
        System.out.println(employeeService);
        System.out.println(employeeRepository);
    }

    @Test
    @DisplayName("Junit Test for SaveEmployee method")
    public void givenExisteEmail_whenSaveEmployee_thenThrowException() {
        //Given - precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //When - action or the behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //JUnit test for getAllEmployees method
    @Test
    @DisplayName("JUnit test for getAllEmployees method")
    public void givenEmployeesList_whenGetAllEmployee_thenReturnEmployeeList() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Ariel")
                .lastName("Garcia")
                .email("garcia@test.com")
                .build();
        //Given - precondition or setup
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));
        //When - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //Then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isGreaterThan(1);
    }

    //JUnit test for
    @Test
    @DisplayName("JUnit test for Empity list")
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Ariel")
                .lastName("Garcia")
                .email("garcia@test.com")
                .build();
        //Given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());
        //When - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();
        //Then - verify the output
        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isLessThan(1);
    }

    //JUnit test for  get Employee by ID
    @Test
    @DisplayName("JUnit test for  get Employee by ID")
    public void givenEmployeeID_whenGetEmployeeByID_thenReturnEmployeeObject() {
        //Given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        //When - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
        //Then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        Assertions.assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    @DisplayName("JUnit test for  get Employee by ID")
    public void givenEmployeeID_whenUpdatedEmployeeByID_thenReturnEmployeeObject() {
        //Given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Ariel");
        employee.setLastName("Garcia");
        employee.setEmail("garcia@test.com");
        //When - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        //Then - verify the output
        Assertions.assertThat(updatedEmployee).isNotNull();
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Ariel");
        Assertions.assertThat(updatedEmployee.getLastName()).isEqualTo("Garcia");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("garcia@test.com");
    }

    //JUnit test for deleteEmployee method
    @Test
    @DisplayName("JUnit test for deleteEmployee method")
    public void givenEmployeeID_whenDeleteEmployee_thenReturnNothing() {
        //Given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(employee.getId());
        //When - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employee.getId());
        //Then - verify the output
        verify(employeeRepository, times(1)).deleteById(employee.getId());

    }
}
