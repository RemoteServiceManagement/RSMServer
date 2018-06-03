package com.rsm.customer;

import com.rsm.device.log.remote.connection.RemoteServiceCredential;
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
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @OneToMany(mappedBy = "customer")
    private List<Report> reports;
    @OneToMany(mappedBy = "customer")
    private List<RemoteServiceCredential> remoteServiceCredentials;
}
