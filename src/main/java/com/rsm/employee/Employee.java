package com.rsm.employee;


import com.rsm.report.Report;
import com.rsm.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="EMPLOYEE")
@Getter
@Setter
public class Employee{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="EMPLOYEE_ID")
    private Long employeeId;
    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="LAST_NAME")
    private String lastName;
    @Column(name="EMPLOYEE_PHONE")
    private String employeePhone;
    @Column(name="EMPLOYEE_EMAIL")
    private String employeeEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @Valid
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "employee")
    private List<Report> reports;
}
