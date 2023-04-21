package com.zlagoda.check;

import com.zlagoda.card.CustomerCardDto;
import com.zlagoda.card.CustomerCardService;
import com.zlagoda.card.CustomerCardServiceImpl;
import com.zlagoda.employee.Employee;
import com.zlagoda.employee.EmployeeService;
import com.zlagoda.store.product.StoreProductDto;
import com.zlagoda.store.product.StoreProductService;
import com.zlagoda.store.product.StoreProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zlagoda.check.CheckServiceImpl.DEFAULT_SORT;


@Controller
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CheckProperties checkProperties;


    // List all checks
    @GetMapping
    public String listChecks(Model model) {
        model.addAttribute("checks", checkService.getAll(DEFAULT_SORT));
        model.addAttribute("cashiers", employeeService.getAllCashiers().stream()
                .collect(Collectors.toMap(Employee::getId, Function.identity())));
        addDefaultAttributes(model);
        return "check/list";
    }

    // Create a new check
    @GetMapping("/new")
    public String createCheckForm(Model model) {
        model.addAttribute("check", CheckDto.builder().sales(new ArrayList<>()).build());

        addDefaultAttributes(model);
        return "check/new";
    }

    @PostMapping("/new")
    public String createCheck(@ModelAttribute @Valid CheckDto checkDto,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("check", checkDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "check/new";
        }

        checkService.create(checkDto);
        return "redirect:/check";
    }

    // View an existing check
    @GetMapping("/view/{id}")
    public String editCheckForm(@PathVariable String id, Model model) {
        CheckDto checkDto = checkService.getById(id);

        model.addAttribute("check", checkDto);
        addDefaultAttributes(model);

        return "check/view";
    }

    // Delete acheck
    @GetMapping("/delete/{id}")
    public String deleteCheck(@PathVariable String id) {
        checkService.deleteById(id);
        return "redirect:/check";
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("customerCards",
                customerCardService.getAll(CustomerCardServiceImpl.DEFAULT_SORT).stream()
                        .collect(Collectors.toMap(CustomerCardDto::getCardNumber, Function.identity())));
        model.addAttribute("storeProducts",
                storeProductService.getAll(StoreProductServiceImpl.DEFAULT_SORT).stream()
                        .collect(Collectors.toMap(StoreProductDto::getUpc, Function.identity())));
        model.addAttribute("vatCoefficient", checkProperties.getVat());
    }
}


