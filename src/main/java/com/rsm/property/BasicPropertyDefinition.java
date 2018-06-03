package com.rsm.property;

import com.rsm.common.BaseEntity;
import com.rsm.report.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Dawid on 04.05.2018 at 00:23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BasicPropertyDefinition extends BaseEntity {
    private String name;
    private String code;

    @ManyToOne
    private Report report;

    public BasicPropertyDefinition(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
