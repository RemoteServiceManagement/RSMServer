package com.rsm.pagination;

import java.util.List;

/**
 * Created by Dawid on 04.06.2018 at 21:24.
 */
public interface PropertiesPage<V, H> {

    int totalPages();

    int number();

    int pageSize();

    int nextPageNumber();

    int previousPageNumber();

    List<V> content();

    H header();
}
