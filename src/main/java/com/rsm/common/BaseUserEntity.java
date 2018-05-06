package com.rsm.common;

import com.rsm.report.Report;
import com.rsm.user.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@MappedSuperclass
public class BaseUserEntity extends BaseEntity {

    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="LAST_NAME")
    private String lastName;
    @Column(name="PHONE")
    private String phone;
    @Column(name="EMAIL")
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @Valid
    @JoinColumn(name = "USER_ID")
    private User user;
}
