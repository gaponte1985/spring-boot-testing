package net.javaguides.spring_boot_testing.repository;

import net.javaguides.spring_boot_testing.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
      employee = Employee.builder()
                .firstName("Gerardo")
                .lastName("Aponte")
                .email("aponte@test.com")
                .build();
    }


    //JUnit test for save employee operation

    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee() {

        //When
        Employee saveEmployee = employeeRepository.save(employee);
        //Then
        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getId()).isGreaterThan(0);
        assertThat(saveEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(saveEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(saveEmployee.getEmail()).isEqualTo(employee.getEmail());

    }

    @Test
    @DisplayName("JUnit test for find all  employee operation")
    public void givenEmployeeList_whenFindAll_theReturnEmployeeList() {
        //Given - precondition or setup
        Employee employee2 = Employee.builder()
                .firstName("Ariel")
                .lastName("Garcia")
                .email("ariel@test.com")
                .build();
        Employee saveEmployee = employeeRepository.save(employee);
        Employee saveEmployee2 = employeeRepository.save(employee2);
        //When - action or the behaviour that we are going to test
        List<Employee> employeesList = employeeRepository.findAll();
        //Then - verify the output
        assertThat(employeesList).isNotNull();
        assertThat(employeesList).hasSize(2);
        assertThat(employeesList.size()).isEqualTo(2);
    }

    // JUNIt test for get employee by ID operation
    @Test
    @DisplayName(" JUNIt test for get employee by ID")
    public void givenEmployeeObject_whenFindByID_thenReturnEmployeeObject() {
        //Given - precondition or setup
        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();
        //Then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // JUNIt test for get employee by email operation
    @Test
    @DisplayName("JUNIt test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();
        //Then - verify the output
        assertThat(employeeDB).isNotNull();
    }

    @Test
    @DisplayName("JUNIt for Updated Employee")
    public void givenEmployeeObject_whenUpdatedEmployee_thenReturnUpdatedEmployee() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findById(employee.getId()).get();
        saveEmployee.setEmail("gaponte@test.com");
        saveEmployee.setFirstName("Gerardito");
        employeeRepository.save(saveEmployee);
        //Then - verify the output
        assertThat(saveEmployee.getFirstName()).isEqualTo("Gerardito");
        assertThat(saveEmployee.getLastName()).isEqualTo("Aponte");
        assertThat(saveEmployee.getEmail()).isEqualTo("gaponte@test.com");
    }

    @Test
    @DisplayName("JUnit test for delete employee operation")
    public void givenEmployeeObject_whenDelete_theRemoveEmployee() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        //When - action or the behaviour that we are going to test
        employeeRepository.delete(employee);
        //  employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
        //Then - verify the output
        assertThat(employeeOptional.isPresent()).isFalse();
        assertThat(employeeOptional).isEmpty();
    }

    @Test
    @DisplayName("JUnit test for custom query using JPQL with index")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Gerardo";
        String lastName = "Aponte";
        //When - action or the behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findByJPQL(firstName, lastName);
        //Then - verify the output
        assertThat(saveEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(saveEmployee.getLastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("JUnit test for custom query using Name Params with index")
    public void givenFirstNameAndLastName_whenFindByNameParams_thenReturnEmployeeObject() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Gerardo";
        String lastName = "Aponte";
        //When - action or the behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findByJPQLNameParams(firstName, lastName);
        //Then - verify the output
        assertThat(saveEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(saveEmployee.getLastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("JUnit test for custom query using Custom SQL with index")
    public void givenFirstNameAndLastName_whenFindByCustomSQL_thenReturnEmployeeObject() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Gerardo";
        String lastName = "Aponte";
        //When - action or the behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findByNativeSQL(firstName, lastName);
        //Then - verify the output
        assertThat(saveEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(saveEmployee.getLastName()).isEqualTo(lastName);
    }

    @Test
    @DisplayName("JUnit test for custom query using Custom SQL and Name Params with index")
    public void givenFirstNameAndLastName_whenFindByCNameParamsCustomSQL_thenReturnEmployeeObject() {
        //Given - precondition or setup

        employeeRepository.save(employee);
        String firstName = "Gerardo";
        String lastName = "Aponte";
        //When - action or the behaviour that we are going to test
        Employee saveEmployee = employeeRepository.findByNativeSQL(firstName, lastName);
        //Then - verify the output
        assertThat(saveEmployee.getFirstName()).isEqualTo(firstName);
        assertThat(saveEmployee.getLastName()).isEqualTo(lastName);
    }
}
