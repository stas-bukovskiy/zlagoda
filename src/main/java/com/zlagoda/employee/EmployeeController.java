package com.zlagoda.employee;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.zlagoda.employee.EmployeeServiceImpl.DEFAULT_SORT;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    // List all employees
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getAll(DEFAULT_SORT));
        return "employee/list";
    }

    // Create a new employee
    @GetMapping("/new")
    public String createEmployeeForm(Model model) {
        model.addAttribute("employee", new EmployeeDto());
        addDefaultAttributes(model);
        return "employee/new";
    }

    @PostMapping("/new")
    public String createEmployee(@ModelAttribute @Valid EmployeeDto employeeDto,
                                 BindingResult bindingResult, Model model) {
        if (!employeeService.isUsernameUniqueToCreate(employeeDto.getUsername())) {
            bindingResult.rejectValue("username", "", "username is not unique");
        }
        if (employeeDto.getPassword() == null || employeeDto.getPassword().length() < 6)
            bindingResult.rejectValue("password", "", "password must be between 6 and 50 characters");
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "employee/new";
        }

        employeeService.create(employeeDto);
        return "redirect:/employee";
    }

    // Update an existing employee
    @GetMapping("/edit/{id}")
    public String editEmployeeForm(@PathVariable String id, Model model) {
        EmployeeDto employeeDto = employeeService.getById(id);
        model.addAttribute("employee", employeeDto);
        addDefaultAttributes(model);
        return "employee/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable String id, @ModelAttribute @Valid EmployeeDto employeeDto,
                                 BindingResult bindingResult, Model model) {
        if (!employeeService.isUsernameUniqueToUpdate(employeeDto.getId(), employeeDto.getUsername())) {
            bindingResult.rejectValue("username", "", "username is not unique");
        }
        if (!isPasswordValid(employeeDto.getPassword()))
            bindingResult.rejectValue("password", "", "password must be between 6 and 50 characters");
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "employee/edit";
        }
        employeeService.update(id, employeeDto);
        return "redirect:/employee";
    }

    private boolean isPasswordValid(String password) {
        return password == null || password.equals("") || password.length() >= 6;
    }

    // Delete an employee
    @GetMapping("/delete/{id}")
    public String deleteEmployeeForm(@PathVariable String id, Model model) {
        model.addAttribute("confirmation", employeeService.createDeleteConfirmation(id));
        model.addAttribute("uriPrefix", "/employee");
        return "confirmation/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id) {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }


    private void addDefaultAttributes(Model model) {
        model.addAttribute("roles", employeeService.getRoles());
        model.addAttribute("cities", employeeService.getCities());
        model.addAttribute("streets", employeeService.getStreets());
        model.addAttribute("zipCodes", employeeService.getZipCodes());
    }


}


