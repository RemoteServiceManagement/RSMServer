package com.rsm.entity;

import com.rsm.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="CUSTOMER")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CUSTOMER_ID")
    private Long customerId;
    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="LAST_NAME")
    private String lastName;
    @Column(name="CUSTOMER_PHONE")
    private String customerPhone;
    @Column(name="CUSTOMER_EMAIL")
    private String customerEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @Valid
    @JoinColumn(name = "id")
    private User user;
}
