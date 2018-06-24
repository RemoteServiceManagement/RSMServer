package com.rsm.customer;


import com.rsm.device.Device;
import com.rsm.user.service.BaseUserService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseUserService<Customer> {
    List<Device> findCustomersDevices(Long customerId);

    Optional<Customer> getCurrent();
}
