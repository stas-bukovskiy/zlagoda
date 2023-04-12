package com.zlagoda.product;

import com.zlagoda.category.CategoryDto;
import com.zlagoda.category.CategoryService;
import com.zlagoda.category.CategoryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zlagoda.product.ProductServiceImpl.DEFAULT_SORT;


@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    // List all products
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAll(DEFAULT_SORT));
        addDefaultAttributes(model);
        return "product/list";
    }

    // Create a new product
    @GetMapping("/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        addDefaultAttributes(model);
        return "product/new";
    }

    @PostMapping("/new")
    public String createProduct(@ModelAttribute @Valid ProductDto productDto,
                                BindingResult bindingResult, Model model) {
        if (!productService.isNameUniqueToCreate(productDto.getName()))
            bindingResult.rejectValue("name", "", "product name is not unique");
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/new";
        }

        productService.create(productDto);
        return "redirect:/product";
    }

    // Update an existing product
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        ProductDto productDto = productService.getById(id);
        model.addAttribute("product", productDto);
        addDefaultAttributes(model);
        return "product/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute @Valid ProductDto productDto,
                                BindingResult bindingResult, Model model) {
        if (!productService.isNameUniqueToUpdate(productDto.getId(), productDto.getName()))
            bindingResult.rejectValue("name", "", "product name is not unique");
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product/edit";
        }
        productService.update(id, productDto);
        return "redirect:/product";
    }

    // Delete an product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/product";
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("categories",
                categoryService.getAll(CategoryServiceImpl.DEFAULT_SORT).stream()
                        .collect(Collectors.toMap(CategoryDto::getId, Function.identity())));
    }
}


