package com.zlagoda.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.zlagoda.category.CategoryServiceImpl.DEFAULT_SORT;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    // List all categories
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAll(DEFAULT_SORT));
        return "category/list";
    }

    // Create a new category
    @GetMapping("/new")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        return "category/new";
    }

    @PostMapping("/new")
    public String createCategory(@ModelAttribute @Valid CategoryDto categoryDto,
                                 BindingResult bindingResult, Model model) {
        if (!categoryService.isNameUniqueToCreate(categoryDto.getName())) {
            bindingResult.rejectValue("name", "", "category name is not unique");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDto);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "category/new";
        }

        categoryService.create(categoryDto);
        return "redirect:/category";
    }

    // Update an existing category
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDto categoryDto = categoryService.getById(id);
        model.addAttribute("category", categoryDto);
        return "category/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute @Valid CategoryDto categoryDto,
                                 BindingResult bindingResult, Model model) {
        if (!categoryService.isNameUniqueToUpdate(categoryDto.getId(), categoryDto.getName())) {
            bindingResult.rejectValue("name", "", "category name is not unique");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryDto);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "category/edit";
        }
        categoryService.update(id, categoryDto);
        return "redirect:/category";
    }

    // Delete a category
    @GetMapping("/delete/{id}")
    public String deleteCategoryForm(@PathVariable Long id, Model model) {
        model.addAttribute("confirmation", categoryService.createDeleteConfirmation(id));
        model.addAttribute("uriPrefix", "/category");
        return "confirmation/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/category";
    }

}


