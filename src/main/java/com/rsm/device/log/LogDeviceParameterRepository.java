package com.rsm.device.log;

import com.rsm.device.DeviceLogDto;
import com.rsm.elesticsearch.ESFacade;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by Dawid on 03.05.2018 at 20:48.
 */
@Repository
@RequiredArgsConstructor
public class LogDeviceParameterRepository {
    public static final String INDEX = "device";
    public static final String TYPE = "log";
    private final ESFacade esFacade;

    public void save(DeviceLogDto deviceLogDto, Long reportId) {
        esFacade.putIfMappingDoesNotExist(INDEX, TYPE, getMapping());
        esFacade.putIndex(INDEX, TYPE, deviceLogDto, reportId.toString());
    }

    private XContentBuilder getMapping() {
        try {
            return XContentFactory.jsonBuilder()
                    .startObject().startObject("properties").startObject("logs").field("type", "nested")
                    .endObject().endObject().endObject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
