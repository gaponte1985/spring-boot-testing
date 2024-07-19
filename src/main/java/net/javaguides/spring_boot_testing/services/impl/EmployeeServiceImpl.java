package net.javaguides.spring_boot_testing.services.impl;

import net.javaguides.spring_boot_testing.exception.ResourceNotFoundException;
import net.javaguides.spring_boot_testing.model.Employee;
import net.javaguides.spring_boot_testing.repository.EmployeeRepository;
import net.javaguides.spring_boot_testing.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> saveEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (saveEmployee.isPresent()) {
            throw  new ResourceNotFoundException("Employee " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updateEmployee) {
        return employeeRepository.save(updateEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
