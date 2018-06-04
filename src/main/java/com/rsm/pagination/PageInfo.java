package com.rsm.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dawid on 04.06.2018 at 23:40.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> {
    private T content;
    private int totalPage;
}
