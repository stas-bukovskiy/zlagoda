package com.zlagoda.store.product;

import com.zlagoda.product.ProductDto;
import com.zlagoda.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/store/product")
@RequiredArgsConstructor
public class StoreProductController {

    private final StoreProductService storeProductService;
    private final ProductService productService;


    // List all storeProducts
    @GetMapping
    public String listStoreProducts(@RequestParam(value = "upc", required = false, defaultValue = "null") String upc,
                                    Model model) {
        model.addAttribute("storeProducts",
                (upc.equals("null")) ? storeProductService.getAll() : List.of(storeProductService.getById(upc)));
        addDefaultAttributes(model);
        return "store/product/list";
    }

    @GetMapping("/promotional")
    public String listPromotionalStoreProducts(@RequestParam(value = "sort", required = false, defaultValue = "product_name") String sort,
                                               Model model) {
        model.addAttribute("storeProducts", storeProductService.getAllPromotional(Sort.by(sort)));
        addDefaultAttributes(model);
        return "store/product/list";
    }

    @GetMapping("/not-promotional")
    public String listNotPromotionalStoreProducts(@RequestParam(value = "sort", required = false, defaultValue = "product_name") String sort,
                                                  Model model) {
        model.addAttribute("storeProducts", storeProductService.getAllNotPromotional(Sort.by(sort)));
        addDefaultAttributes(model);
        return "store/product/list";
    }


    @GetMapping("/upc-search")
    public String createSearchForm() {
        return "store/product/upc-search";
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
        storeProductService.checkProductIdToCreate(storeProductDto).forEach(bindingResult::addError);
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
        storeProductService.checkProductIdToUpdate(storeProductDto).forEach(bindingResult::addError);
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


    @GetMapping("/make/prom/{upc}")
    public String makeProm(@PathVariable String upc) {
        storeProductService.makeStoreProductPromotional(upc);
        return "redirect:/store/product";
    }

    @GetMapping("/unmake/prom/{upc}")
    public String unmakeProm(@PathVariable String upc) {
        storeProductService.unmakeStoreProductPromotional(upc);
        return "redirect:/store/product";
    }

    // Delete an storeProduct
    @GetMapping("/delete/{upc}")
    public String deleteStoreProductForm(@PathVariable String upc, Model model) {
        model.addAttribute("confirmation", storeProductService.createDeleteConfirmation(upc));
        model.addAttribute("uriPrefix", "/store/product");
        return "confirmation/delete";
    }

    @PostMapping("/delete/{upc}")
    public String deleteStoreProduct(@PathVariable String upc) {
        storeProductService.deleteById(upc);
        return "redirect:/store/product";
    }

    private void addDefaultAttributes(Model model) {
        model.addAttribute("products", productService.getAll().stream()
                .collect(Collectors.toMap(ProductDto::getId, Function.identity())));
    }
}


