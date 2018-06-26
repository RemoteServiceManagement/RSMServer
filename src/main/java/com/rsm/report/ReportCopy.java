package com.rsm.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rsm.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="REPORT_COPY")
@Getter
@Setter
public class ReportCopy extends BaseEntity {

    @Column(name="DESCRIPTION")
    private String description;

    @JsonIgnore
    @Column(name="REPORT_PHOTO")
    @Lob
    @Type(type="binary")
    private byte[] reportPhoto;

    @Column(name="REPORT_DATE")
    private LocalDate reportDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="REPORT_ID")
    private Report report;

}
