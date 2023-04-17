package com.zlagoda.store.product;

import com.zlagoda.product.ProductDto;
import com.zlagoda.product.ProductService;
import com.zlagoda.product.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.stream.Collectors;

import static com.zlagoda.store.product.StoreProductServiceImpl.DEFAULT_SORT;


@Controller
@RequestMapping("/store/product")
@RequiredArgsConstructor
public class StoreProductController {

    private final StoreProductService storeProductService;
    private final ProductService productService;


    // List all storeProducts
    @GetMapping
    public String listStoreProducts(Model model) {
        model.addAttribute("storeProducts", storeProductService.getAll(DEFAULT_SORT));
        addDefaultAttributes(model);
        return "store/product/list";
    }

    // Create a new storeProduct
    @GetMapping("/new")
    public String createStoreProductForm(Model model) {
        model.addAttribute("storeProduct", new StoreProductDto());
        addDefaultAttributes(model);
        return "store/product/new";
    }

    @PostMapping("/new")
    public String createStoreProduct(@ModelAttribute @Valid StoreProductDto storeProductDto,
                                     BindingResult bindingResult, Model model) {
        if (!storeProductService.isUniqueToCreate(storeProductDto.getUpc()))
            bindingResult.rejectValue("upc", "", "product upc is not unique");
        if (bindingResult.hasErrors()) {
            model.addAttribute("storeProduct", storeProductDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "store/product/new";
        }

        storeProductService.create(storeProductDto);
        return "redirect:/store/product";
    }

    // Update an existing storeProduct
    @GetMapping("/edit/{upc}")
    public String editStoreProductForm(@PathVariable String upc, Model model) {
        StoreProductDto storeProductDto = storeProductService.getById(upc);
        model.addAttribute("storeProduct", storeProductDto);
        addDefaultAttributes(model);

        return "store/product/edit";
    }

    @PostMapping("/edit/{upc}")
    public String updateStoreProduct(@PathVariable String upc,
                                     @ModelAttribute @Valid StoreProductDto storeProductDto,
                                     BindingResult bindingResult, Model model) {
        if (!storeProductService.isUniqueToUpdate(upc, storeProductDto.getUpc()))
            bindingResult.rejectValue("upc", "", "product upc is not unique");
        if (bindingResult.hasErrors()) {
            model.addAttribute("storeProduct", storeProductDto);
            addDefaultAttributes(model);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "store/product/edit";
        }
        storeProductService.update(upc, storeProductDto);
        return "redirect:/store/product";
    }

    // Delete an storeProduct
    @GetMapping("/delete/{upc}")
    public String deleteStoreProduct(@PathVariable String upc) {
        storeProductService.deleteById(upc);
        return "redirect:/store/product";
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("products", productService.getAll(ProductServiceImpl.DEFAULT_SORT).stream()
                .collect(Collectors.toMap(ProductDto::getId, Function.identity())));
    }
}


