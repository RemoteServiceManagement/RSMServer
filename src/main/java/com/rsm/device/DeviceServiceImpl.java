package com.rsm.device;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> findById(Long id) {
        return deviceRepository.findById(id);
    }

    @Override
    public void save(Device device) {
        deviceRepository.save(device);
    }

    @Override
    public Device saveAndReturn(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void delete(Device device) {
        deviceRepository.delete(device);
    }

    @Override
    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }
}
