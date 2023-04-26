package com.zlagoda.check;

import com.zlagoda.card.CustomerCardDto;
import com.zlagoda.card.CustomerCardService;
import com.zlagoda.employee.EmployeeDto;
import com.zlagoda.employee.EmployeeService;
import com.zlagoda.product.ProductService;
import com.zlagoda.store.product.StoreProductDto;
import com.zlagoda.store.product.StoreProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zlagoda.utils.DateConverter.convertToTimestamp;


@Controller
@RequestMapping("/check")
@RequiredArgsConstructor
public class CheckController {

    private final CheckService checkService;
    private final ProductService productService;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CheckProperties checkProperties;


    // List all checks
    @GetMapping
    public String listChecks(Model model) {
        model.addAttribute("checks", checkService.getAll());
        model.addAttribute("cashiers", employeeService.getAllCashiers().stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity())));
        addDefaultAttributes(model);
        return "check/list";
    }


    @GetMapping("/number-search")
    public String createSearchForm(@RequestParam(value = "number", required = false, defaultValue = "null") String number,
                                   Model model) {
        if (number.equals("null"))
            return "check/number-search";
        else {
            return "redirect:/check/view/" + number;
        }
    }

    @GetMapping("/period-search")
    private String getPeriodForm(Model model) {
        model.addAttribute("period", new CountData<String>());
        return "check/period-search";
    }

    @PostMapping("/period-search")
    private String listCheckForPeriodOfTime(@ModelAttribute @Valid CountData<String> period,
                                            Model model, BindingResult bindingResult) {
        model.addAttribute("period", period);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "check/period-search";
        }

        model.addAttribute("checks", checkService.getAllByPrintDateBetween(
                convertToTimestamp(period.getFrom()),
                convertToTimestamp(period.getTo())
        ));
        model.addAttribute("cashiers", employeeService.getAllCashiers().stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity())));
        addDefaultAttributes(model);
        return "check/list";
    }

    @GetMapping("/period-search/today")
    public String listTodayChecksOfCurrentCashier(Model model) {
        model.addAttribute("checks", checkService.getTodayChecksOfCurrentUser());
        model.addAttribute("cashiers", employeeService.getAllCashiers().stream()
                .collect(Collectors.toMap(EmployeeDto::getId, Function.identity())));
        addDefaultAttributes(model);
        return "check/list";
    }

    @GetMapping("/count/sum")
    public String countSumForm(Model model) {
        model.addAttribute("employees", employeeService.getAllCashiers());
        model.addAttribute("countData", new CountData<String>());
        return "check/count-sum";
    }

    @PostMapping("/count/sum")
    public String countSum(@ModelAttribute @Valid CountData<String> countData,
                           Model model, BindingResult bindingResult) {

        model.addAttribute("employees", employeeService.getAllCashiers());
        model.addAttribute("countData", countData);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "check/count-sum";
        }

        model.addAttribute("sum", !countData.getId().equals("") ?
                checkService.countTotalSumByEmployeeId(countData.getId(), convertToTimestamp(countData.getFrom()), convertToTimestamp(countData.getTo())) :
                checkService.countTotalSum(convertToTimestamp(countData.getFrom()), convertToTimestamp(countData.getTo())));
        return "check/count-sum";
    }

    @GetMapping("/count/amount")
    public String countAmountForm(Model model) {
        model.addAttribute("products", productService.getAll());
        model.addAttribute("countData", new CountData<Long>());
        return "check/count-sum";
    }

    @PostMapping("/count/amount")
    public String countAmount(@ModelAttribute @Valid CountData<Long> countData,
                              Model model, BindingResult bindingResult) {

        model.addAttribute("products", productService.getAll());
        model.addAttribute("countData", countData);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "check/count-amount";
        }

        model.addAttribute("amount", checkService.countTotalAmountSoldByProductId(
                countData.getId(),
                convertToTimestamp(countData.getFrom()),
                convertToTimestamp(countData.getTo())
        ));
        return "check/count-amount";
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
        if (checkDto.getSales() != null)
            checkService.checkAvailability(checkDto).forEach(bindingResult::addError);
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
                customerCardService.getAll().stream()
                        .collect(Collectors.toMap(CustomerCardDto::getCardNumber, Function.identity())));
        model.addAttribute("storeProducts",
                storeProductService.getAll().stream()
                        .collect(Collectors.toMap(StoreProductDto::getUpc, Function.identity())));
        model.addAttribute("vatCoefficient", checkProperties.getVat());
        model.addAttribute("formatter", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}


