package com.zlagoda.card;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.zlagoda.card.CustomerCardServiceImpl.DEFAULT_SORT;


@Controller
@RequestMapping("/customer/card")
@RequiredArgsConstructor
public class CustomerCardController {

    private final CustomerCardService service;


    // List all customer cards
    @GetMapping
    public String listCustomerCards(Model model) {
        model.addAttribute("cards", service.getAll(DEFAULT_SORT));
        return "card/list";
    }

    // Create a new customer card
    @GetMapping("/new")
    public String createCustomerCardForm(Model model) {
        model.addAttribute("card", new CustomerCardDto());
        addDefaultAttributes(model);
        return "card/new";
    }

    @PostMapping("/new")
    public String createCustomerCard(@ModelAttribute @Valid CustomerCardDto cardDto,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("card", cardDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "card/new";
        }

        service.create(cardDto);
        return "redirect:/customer/card";
    }

    // Update an existing customer card
    @GetMapping("/edit/{id}")
    public String editCustomerCardForm(@PathVariable String id, Model model) {
        CustomerCardDto cardDto = service.getById(id);
        model.addAttribute("card", cardDto);
        addDefaultAttributes(model);
        return "card/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCustomerCard(@PathVariable String id, @ModelAttribute @Valid CustomerCardDto cardDto,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("card", cardDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "card/edit";
        }
        service.update(id, cardDto);
        return "redirect:/customer/card";
    }

    // Delete a customer card
    @GetMapping("/delete/{id}")
    public String deleteCustomerCard(@PathVariable String id) {
        service.deleteById(id);
        return "redirect:/customer/card";
    }


    private void addDefaultAttributes(Model model) {
        model.addAttribute("cities", service.getCities());
        model.addAttribute("streets", service.getStreets());
        model.addAttribute("zipCodes", service.getZipCodes());
    }


}


