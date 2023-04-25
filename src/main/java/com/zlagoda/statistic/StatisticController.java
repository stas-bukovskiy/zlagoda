package com.zlagoda.statistic;

import com.zlagoda.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticRepository statisticRepository;


    @GetMapping("/home")
    private String home(Model model) {
        model.addAttribute("bestSellingProducts", statisticRepository.countMostSalableProducts());
        model.addAttribute("cashiersWithoutSales", statisticRepository.findCashiersWithoutSales());
        return "home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

}
