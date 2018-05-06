package com.rsm.common;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Dawid on 27.04.2018 at 11:16.
 */
@MappedSuperclass
@Getter
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
