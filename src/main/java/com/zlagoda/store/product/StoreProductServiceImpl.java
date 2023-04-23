package com.zlagoda.store.product;

import com.zlagoda.confiramtion.DeleteConfirmation;
import com.zlagoda.product.Product;
import com.zlagoda.sale.SaleDeleteConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.zlagoda.utils.RandomUtils.randomUPC;

@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements StoreProductService {

    public final static Sort DEFAULT_SORT = Sort.by("sp.upc");

    private final StoreProductRepository repository;
    private final StoreProductConverter converter;
    private final StoreProductProperties properties;
    private final SaleDeleteConfirmationService saleDeleteConfirmationService;


    @Override
    public List<StoreProductDto> getAll(Sort sort) {
        return repository.findAll(sort)
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
    public void makeStoreProductPromotional(String upc) {
        StoreProduct productToGetPromotional = repository.findById(upc)
                .orElseThrow(StoreProductNotFoundException::new);

        if (productToGetPromotional.getPromStoreProduct() != null)
            throw new RuntimeException("product already has promo");
        // TODO: 22.04.2023 make exceptions class

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
    public void unmakeStoreProductPromotional(String upc) {
        StoreProduct productToLosePromotional = repository.findById(upc)
                .orElseThrow(StoreProductNotFoundException::new);

        if (productToLosePromotional.getPromStoreProduct() == null)
            throw new RuntimeException("product does not have a promo");
        // TODO: 22.04.2023 make exceptions class


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


}
