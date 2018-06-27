package com.rsm.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rsm.common.BaseEntity;
import com.rsm.customer.Customer;
import com.rsm.device.Device;
import com.rsm.device.log.remote.connection.RemoteServiceCredential;
import com.rsm.device.property.BasicPropertyDefinition;
import com.rsm.employee.Employee;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Base64Utils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deviceDataFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deviceDataTo;

    @ManyToOne
    @JoinColumn(name="EMPLOYEE_ID")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name="CUSTOMER_ID")
    private Customer customer;

    @Column(name="DIAGNOSIS")
    private String diagnosis;

    @Column(name="PRICING")
    private Float pricing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<BasicPropertyDefinition> chosenProperty;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "report")
    private List<ReportCopy> reportCopies;

    private Instant chosenDateFrom;

    private Instant chosenDateTo;

    public RemoteServiceCredential getReportedDeviceServiceCredential() {
        return getDevice().getRemoteServiceCredential();
    }

    public String getReportedDeviceExternalId() {
        return getDevice().getExternalId();
    }

    public List<String> getChosenPropertyCode() {
        return getChosenProperty().stream().map(BasicPropertyDefinition::getCode).collect(Collectors.toList());
    }
    public String getImageEncoded(){
        return Base64Utils.encodeToString(this.reportPhoto);
    }
}
