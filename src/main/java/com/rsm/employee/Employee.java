package com.rsm.employee;


import com.rsm.common.BaseEntity;
import com.rsm.common.BaseUserEntity;
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
public class Employee extends BaseUserEntity {

    @OneToMany(mappedBy = "employee")
    private List<Report> reports;
}
