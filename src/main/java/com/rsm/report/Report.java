package com.rsm.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rsm.common.BaseEntity;
import com.rsm.customer.Customer;
import com.rsm.device.Device;
import com.rsm.employee.Employee;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="REPORT")
@Getter
@Setter
public class Report extends BaseEntity {
    @Column(name="TITLE")
    private String title;
    @Column(name="DESCRIPTION")
    private String description;

    @JsonIgnore
    @Column(name="REPORT_PHOTO")
    @Lob
    @Type(type="binary")
    private byte[] reportPhoto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="DEVICE_ID")
    private Device device;

    @Column(name="REPORT_DATE")
    private LocalDate reportDate;

    @Column(name="REPORT_STATUS")
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;


    @ManyToOne
    @JoinColumn(name="EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="CUSTOMER_ID")
    private Customer customer;

}
