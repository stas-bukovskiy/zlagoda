package com.zlagoda.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;


    @GetMapping("/home")
    private String home(Model model) {
        model.addAttribute("totalSoldAndSales", statisticService.countTotalSoldAndSales());
        model.addAttribute("cashiersWithoutNotPromo", statisticService.findCashiersWithoutNotPromoProductSales());
        model.addAttribute("cashiersWithoutPromo", statisticService.findCashiersWithoutPromoProductSales());
        return "home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

}
