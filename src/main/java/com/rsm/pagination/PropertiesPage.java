package com.rsm.pagination;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * Created by Dawid on 04.06.2018 at 21:24.
 */
public interface PropertiesPage<V, H> {
    public static int PAGE_BUTTONS_COUNT = 10;

    int getTotalPages();

    int getNumber();

    int pageSize();

    int nextPageNumber();

    int previousPageNumber();

    List<V> getContent();

    List<H> getHeader();

    default List<Integer> getPageRange() {
        return range(1, getTotalPages() + 1)
                .boxed()
                .filter(number -> number >= getNumber() - PAGE_BUTTONS_COUNT / 2 + 2)
                .limit(PAGE_BUTTONS_COUNT)
                .collect(toList());
    }
}
