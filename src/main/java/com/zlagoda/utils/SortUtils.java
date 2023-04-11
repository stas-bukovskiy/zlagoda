package com.zlagoda.utils;

import org.springframework.data.domain.Sort;

public final class SortUtils {

    public static String sortToString(Sort sort) {
        return sort.toString().replaceAll(":", " ");
    }

}
