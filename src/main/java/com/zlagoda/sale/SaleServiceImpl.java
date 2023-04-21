package com.zlagoda.sale;

import com.zlagoda.store.product.StoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    public final static Sort DEFAULT_SORT = Sort.by("upc", "check_number");

    private final SaleRepository repository;
    private final SaleConverter converter;
    private final StoreProductService storeProductService;

    @Override
    public List<SaleDto> getAll(Sort sort) {
        return repository.findAll(sort)
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

    // TODO: 18.04.2023 check for uniqueness 

}
