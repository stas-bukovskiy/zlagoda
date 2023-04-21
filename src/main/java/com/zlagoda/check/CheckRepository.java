package com.zlagoda.check;

import com.zlagoda.helpers.Repository;

public interface CheckRepository extends Repository<Check, String> {

    boolean existsById(String checkNumber);
}
