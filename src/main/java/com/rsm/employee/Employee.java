package com.rsm.employee;


import com.rsm.report.Report;
import com.rsm.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @OneToMany(mappedBy = "employee")
    private List<Report> reports;
}
