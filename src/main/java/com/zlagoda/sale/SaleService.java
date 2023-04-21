package com.zlagoda.sale;

import com.zlagoda.helpers.Service;
import org.springframework.data.util.Pair;

public interface SaleService extends Service<Pair<String, String>, SaleDto> {

}
