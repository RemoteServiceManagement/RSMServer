package com.rsm.role;

import com.rsm.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Table(name = "ROLE")
public class Role extends BaseEntity {

    @Column(name="ROLE_NAME")
    private String roleName;

    @Column(name="ROLE_DESCRIPTION")
    private String roleDescription;
}
