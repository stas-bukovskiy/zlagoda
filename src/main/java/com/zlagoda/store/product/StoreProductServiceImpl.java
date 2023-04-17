package com.zlagoda.store.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zlagoda.utils.RandomUtils.randomUPC;

@Service
@RequiredArgsConstructor
public class StoreProductServiceImpl implements StoreProductService {

    public final static Sort DEFAULT_SORT = Sort.by("upc");

    private final StoreProductRepository repository;
    private final StoreProductConverter converter;
    private final StoreProductProperties properties;


    @Override
    public List<StoreProductDto> getAll(Sort sort) {
        return repository.findAll(sort)
                .stream()
                .map(this::makePromIfNeeds)
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
        StoreProduct createdStoreProduct = repository.save(productToCreate);
        return converter.convertToDto(createdStoreProduct);
    }

    @Override
    public StoreProductDto update(String upc, StoreProductDto storeProductDto) {
        if (!repository.existsById(upc))
            throw new StoreProductNotFoundException();
        StoreProduct productToUpdate = converter.convertToEntity(storeProductDto);
        StoreProduct updatedStoreProduct = repository.update(upc, productToUpdate);
        return converter.convertToDto(updatedStoreProduct);
    }

    @Override
    public StoreProductDto deleteById(String upc) {
        return repository.deleteById(upc)
                .map(converter::convertToDto)
                .orElseThrow(StoreProductNotFoundException::new);
    }

    @Override
    public boolean isUniqueToCreate(String upc) {
        return !repository.existsById(upc);
    }

    @Override
    public boolean isUniqueToUpdate(String oldUpc, String newUpc) {
        return oldUpc.equals(newUpc) || isUniqueToCreate(newUpc);
    }

    private StoreProduct makePromIfNeeds(StoreProduct storeProduct) {
        if (storeProduct.isPromotional()) return storeProduct;
        if (isProductNeedDiscount(storeProduct)) {
            storeProduct.setPromotional(true);
            StoreProduct promStoreProduct = new StoreProduct(
                    randomUPC(),
                    storeProduct.getSellingPrice().multiply(properties.getPromotionalDiscountCoefficient()),
                    storeProduct.getProductsNumber(),
                    storeProduct.isPromotional(),
                    storeProduct.getProduct(),
                    null
            );
            storeProduct.setPromStoreProduct(repository.save(promStoreProduct));
            repository.update(storeProduct.getUpc(), storeProduct);
        }
        return storeProduct;
    }

    private boolean isProductNeedDiscount(StoreProduct storeProduct) {
//        if (storeProduct.getProduct().getExpirationDate() == null) return false;
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, (int) properties.getNumberOfDaysToBePromotional().get(ChronoUnit.DAYS));
//        Date date = calendar.getTime();
//        Date expirationDate = storeProduct.getProduct().getExpirationDate();
//        return storeProduct.getProductsNumber() >= properties.getNumberOfProductsToBePromotional()
//                && date.after(expirationDate);

        return storeProduct.getProductsNumber() >= properties.getNumberOfProductsToBePromotional();
    }
}
