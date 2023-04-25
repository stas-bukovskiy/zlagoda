package com.zlagoda.store.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.exception.EntityCreationException;
import com.zlagoda.product.Product;
import com.zlagoda.sale.SaleDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.zlagoda.utils.RandomUtils.randomUPC;

@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements StoreProductService {

    private final StoreProductRepository repository;
    private final StoreProductConverter converter;
    private final StoreProductProperties properties;
    private final SaleDeleteConfirmationService saleDeleteConfirmationService;


    @Override
    public List<StoreProductDto> getAll() {
        return repository.findAll()
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public StoreProductDto getById(String upc) {
        return repository.findById(upc)
                .map(converter::convertToDto)
                .orElseThrow(StoreProductNotFoundException::new);
    }

    @Override
    public StoreProductDto create(StoreProductDto storeProductDto) {
        StoreProduct productToCreate = converter.convertToEntity(storeProductDto);
        productToCreate.setPromotional(false);
        StoreProduct createdStoreProduct = repository.save(productToCreate);
        return converter.convertToDto(createdStoreProduct);
    }

    @Override
    public StoreProductDto update(String upc, StoreProductDto storeProductDto) {
        StoreProduct productToUpdate = repository.findById(upc)
                .orElseThrow(StoreProductNotFoundException::new);

        productToUpdate.setProductsNumber(storeProductDto.getProductsNumber());
        if (!productToUpdate.isPromotional()) {
            productToUpdate.setSellingPrice(storeProductDto.getSellingPrice());
            Product newProduct = Product.builder().id(storeProductDto.getProduct().getId()).build();
            productToUpdate.setProduct(newProduct);

            StoreProduct promStoreProduct = productToUpdate.getPromStoreProduct();
            if (promStoreProduct != null) {
                promStoreProduct.setProduct(newProduct);
                promStoreProduct.setSellingPrice(storeProductDto.getSellingPrice()
                        .multiply(properties.getPromotionalDiscountCoefficient()));
                repository.update(promStoreProduct.getUpc(), promStoreProduct);
            }
        }
        StoreProduct updatedStoreProduct = repository.update(upc, productToUpdate);
        return converter.convertToDto(updatedStoreProduct);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public StoreProductDto deleteById(String upc) {
        StoreProductDto storeProductToDelete = getById(upc);
        saleDeleteConfirmationService.confirmDeletion(upc);
        repository.deleteById(upc);
        return storeProductToDelete;
    }

    @Override
    public List<StoreProductDto> getAllPromotional(Sort sort) {
        return repository.findAllPromotional(sort).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public List<StoreProductDto> getAllNotPromotional(Sort sort) {
        return repository.findAllNotPromotional(sort).stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public boolean isUniqueToCreate(String upc) {
        return !repository.existsById(upc);
    }

    @Override
    public boolean isUniqueToUpdate(String oldUpc, String newUpc) {
        return oldUpc.equals(newUpc) || isUniqueToCreate(newUpc);
    }

    @Override
    public BigDecimal getPriceByUpc(String upc) {
        return repository.findPriceById(upc)
                .orElseThrow(StoreProductNotFoundException::new);
    }

    @Override
    public int getAmountByUpc(String upc) {
        return repository.findProductsNumberById(upc)
                .orElseThrow(StoreProductNotFoundException::new);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = EntityCreationException.class)
    public void makeStoreProductPromotional(String upc) {
        StoreProduct productToGetPromotional = repository.findById(upc)
                .orElseThrow(StoreProductNotFoundException::new);

        if (productToGetPromotional.getPromStoreProduct() != null)
            throw new EntityCreationException("Store product with UPC = " + upc +
                                              " already has promotional product with upc = " +
                                              productToGetPromotional.getPromStoreProduct().getUpc());

        StoreProduct promStoreProduct = new StoreProduct(
                randomUPC(),
                productToGetPromotional.getSellingPrice().multiply(properties.getPromotionalDiscountCoefficient()),
                productToGetPromotional.getProductsNumber(),
                true,
                productToGetPromotional.getProduct(),
                null
        );
        productToGetPromotional.setProductsNumber(0);
        productToGetPromotional.setPromStoreProduct(repository.save(promStoreProduct));
        repository.update(productToGetPromotional.getUpc(), productToGetPromotional);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = EntityCreationException.class)
    public void unmakeStoreProductPromotional(String upc) {
        StoreProduct productToLosePromotional = repository.findById(upc)
                .orElseThrow(StoreProductNotFoundException::new);

        if (productToLosePromotional.getPromStoreProduct() == null)
            throw new EntityCreationException("Store product with UPC = " + upc + " does not have promotional product");


        int productsNumber = productToLosePromotional.getPromStoreProduct().getProductsNumber();
        productToLosePromotional.setProductsNumber(productToLosePromotional.getProductsNumber() + productsNumber);

        repository.deleteById(productToLosePromotional.getPromStoreProduct().getUpc());
        productToLosePromotional.setPromStoreProduct(null);
        repository.update(productToLosePromotional.getUpc(), productToLosePromotional);
    }

    @Override
    public void subtractAmountByUpc(String storeProductUpc, int productNumber) {
        repository.subtractAmountByUpc(storeProductUpc, productNumber);
    }

    @Override
    public DeleteConfirmation createDeleteConfirmation(String upc) {
        DeleteConfirmation confirmation = new DeleteConfirmation();
        confirmation.setId(upc);
        confirmation.setObjectName("Store product");
        confirmation.setChildRemovals(saleDeleteConfirmationService.createChildDeleteConfirmation(upc));
        return confirmation;
    }

    @Override
    public List<FieldError> checkProductIdToCreate(StoreProductDto storeProductDto) {
        List<FieldError> errors = new ArrayList<>();
        if (storeProductDto.getProduct() == null || storeProductDto.getProduct().getId() == null)
            errors.add(new FieldError("Store product", "product", "Product can not be null"));
        if (repository.existsByProductId(storeProductDto.getProduct().getId()))
            errors.add(new FieldError("Store product", "product", "Store product with such product already created"));
        return errors;
    }

    @Override
    public List<FieldError> checkProductIdToUpdate(StoreProductDto storeProductDto) {
        List<FieldError> errors = new ArrayList<>();
        if (storeProductDto.getProduct() == null || storeProductDto.getProduct().getId() == null)
            errors.add(new FieldError("Store product", "product", "Product can not be null"));
        if (repository.existsByProductIdAndUpcIsNot(storeProductDto.getProduct().getId(), storeProductDto.getUpc()))
            errors.add(new FieldError("Store product", "product", "Store product with such product already created"));
        return errors;
    }


}
