package com.rsm.device.log.remote.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Dawid on 03.05.2018 at 14:46.
 */

@Data
@AllArgsConstructor
public class RemoteServiceCredential {
    private String name;
    private int port;
}
