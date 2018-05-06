package com.rsm.device;

import com.rsm.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Dawid on 27.04.2018 at 11:18.
 */
@Entity
@Table(name="DEVICE")
@Getter
@Setter
public class Device extends BaseEntity {
    @Column(name="BRAND")
    private String brand;
    @Column(name="MODEL")
    private String model;
    @Column(name="SERIAL_NUMBER")
    private String serialNumber;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="EXTERNAL_ID")
    private String externalId;
}
