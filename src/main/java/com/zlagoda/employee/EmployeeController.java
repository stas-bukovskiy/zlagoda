package com.zlagoda.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    // List all employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees",
                employeeService.getAll(Sort.by("empl_name")));
        return "employees/list";
    }

    // Create a new employee
    @GetMapping("/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        return "employees/new";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute EmployeeDto employeeDto) {
        employeeService.create(employeeDto);
        return "redirect:/employees";
    }

    // Update an existing employee
    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable String id, Model model) {
        Employee employee = employeeService.getById(id);
        model.addAttribute("employee", employee);
        return "employees/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable String id, @ModelAttribute EmployeeDto employeeDTO) {
        employeeService.update(id, employeeDTO);
        return "redirect:/employees";
    }

    // Delete an employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }

}


