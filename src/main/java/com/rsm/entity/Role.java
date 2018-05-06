package com.rsm.entity;

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
public class Role {
    @Id
    @Column(name="ROLE_ID")
    private Long roleId;

    @Column(name="ROLE_NAME")
    private String roleName;

    @Column(name="ROLE_DESCRIPTION")
    private String roleDescription;
}
