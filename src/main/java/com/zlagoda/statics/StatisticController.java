package com.zlagoda.statics;

import com.zlagoda.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticRepository statisticRepository;
    private final CategoryService categoryService;


    @GetMapping("/home")
    private String home(@RequestParam(name = "category_id", required = false, defaultValue = "-1") Long categoryId,
                        Model model) {
        model.addAttribute("averageChecks", statisticRepository.countAvgCheckForTop5Categories());
        model.addAttribute("categories", categoryService.getAll());
        if (categoryId != -1) {
            model.addAttribute("unsuccessfulCashiers", statisticRepository.findCashiersThatNotSoldProductsWithCategory(categoryId));
            model.addAttribute("category", categoryService.getById(categoryId));
        }
        return "home";
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/home";
    }

}
