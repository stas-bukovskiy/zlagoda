package com.zlagoda.sale;

import com.zlagoda.exception.EntityCreationException;
import com.zlagoda.store.product.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final SaleConverter converter;
    private final StoreProductService storeProductService;

    @Override
    public List<SaleDto> getAll() {
        return repository.findAll()
                .stream()
                .map(converter::convertToDto)
                .toList();
    }

    @Override
    public SaleDto getById(Pair<String, String> upcAndCheckNumber) {
        return repository.findById(upcAndCheckNumber)
                .map(converter::convertToDto)
                .orElseThrow(SaleNotFoundException::new);
    }

    @Override
    public SaleDto create(SaleDto saleDto) {
        Sale saleToCreate = converter.convertToEntity(saleDto);
        saleToCreate.setSellingPrice(storeProductService.getPriceByUpc(saleDto.getStoreProductUpc()));

        int availableAmount = storeProductService.getAmountByUpc(saleDto.getStoreProductUpc());
        if (saleDto.getProductNumber() > availableAmount)
            throw new EntityCreationException("Not enough amount of store products with id = " +
                                              saleDto.getStoreProductUpc() + " to create check");

        storeProductService.subtractAmountByUpc(saleDto.getStoreProductUpc(), saleDto.getProductNumber());

        Sale createdSale = repository.save(saleToCreate);
        return converter.convertToDto(createdSale);
    }

    @Override
    public SaleDto update(Pair<String, String> upcAndCheckNumber, SaleDto saleDto) {
        if (!repository.existsById(upcAndCheckNumber))
            throw new SaleNotFoundException();
        Sale saleToUpdate = converter.convertToEntity(saleDto);
        Sale updatedSale = repository.update(upcAndCheckNumber, saleToUpdate);
        return converter.convertToDto(updatedSale);
    }

    @Override
    public SaleDto deleteById(Pair<String, String> upcAndCheckNumber) {
        return repository.deleteById(upcAndCheckNumber)
                .map(converter::convertToDto)
                .orElseThrow(SaleNotFoundException::new);
    }

}
