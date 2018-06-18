package com.rsm.pagination;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by Dawid on 04.06.2018 at 21:40.
 */

@RequiredArgsConstructor
public class PropertiesPageImpl<V, H> implements PropertiesPage<V, H> {
    private final int totalPages;
    private final int number;
    private final int pageSize;
    private final List<V> content;
    private final List<H> header;


    @Override
    public int getTotalPages() {
        return totalPages;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int pageSize() {
        return pageSize;
    }

    @Override
    public int nextPageNumber() {
        return number + 1;
    }

    @Override
    public int previousPageNumber() {
        return number - 1;
    }

    @Override
    public List<V> getContent() {
        return content;
    }

    @Override
    public List<H> getHeader() {
        return header;
    }
}
