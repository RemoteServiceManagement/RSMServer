package com.rsm.device;

import com.rsm.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by Dawid on 27.04.2018 at 11:18.
 */
@Entity
@Getter
@Setter
public class Device extends BaseEntity {
    private String brand;
    private String model;
    private String serialNumber;
    private String description;
    private String externalId;
}
