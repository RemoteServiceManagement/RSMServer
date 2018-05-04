package com.rsm.device.log.remote.connection;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by Dawid on 03.05.2018 at 14:55.
 */

@Component
public class RemoteServiceFactory {

    public ManagedChannel create(RemoteServiceCredential remoteServiceCredential) {
        return ManagedChannelBuilder.forAddress(remoteServiceCredential.getName(), remoteServiceCredential.getPort())
                .usePlaintext()
                .build();
    }
}
