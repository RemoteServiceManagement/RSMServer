package com.rsm.customer;

import com.rsm.device.Device;
import com.rsm.device.DeviceService;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;
    private final DeviceService deviceService;

    @GetMapping("/devices")
    public String getCustomerDevices(Model model) {
        model.addAttribute("devices", customerService.findCustomersDevices(userService.getCurrentUser().getId()));
        return "customer/devices";
    }

    @GetMapping("/addDeviceForm")
    public String reportForm(Model model) {
        Device device = new Device();
        model.addAttribute(device);
        return "customer/addDeviceForm";
    }

    @PostMapping("/addDeviceForm")
    public String reportFormPost(@ModelAttribute Device device, BindingResult result, Model model,
                                 Principal principal) {
        Optional<Customer> optionalCustomer = customerService.getCurrent();
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            device.setCustomer(customer);
            deviceService.save(device);
        }
        return "redirect:/customer/devices";
    }


    @ExceptionHandler(CustomerDoesNotExistException.class)
    public String handleNonCustomerAccess(Model model) {
        return "error";
    }
}
