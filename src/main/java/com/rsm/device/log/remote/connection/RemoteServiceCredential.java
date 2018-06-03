package com.rsm.device.log.remote.connection;

import com.rsm.common.BaseEntity;
import com.rsm.customer.Customer;
import com.rsm.device.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Dawid on 03.05.2018 at 14:46.
 */

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class RemoteServiceCredential extends BaseEntity {
    @ManyToOne
    private Customer customer;
    private String name;
    private int port;
    @OneToMany(mappedBy = "remoteServiceCredential")
    private List<Device> device;
}
