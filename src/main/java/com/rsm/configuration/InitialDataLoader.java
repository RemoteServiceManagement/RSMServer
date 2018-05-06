package com.rsm.configuration;

import com.rsm.customer.Customer;
import com.rsm.customer.CustomerService;
import com.rsm.device.Device;
import com.rsm.device.DeviceService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.report.Report;
import com.rsm.report.ReportService;
import com.rsm.user.User;
import com.rsm.user.UserRepository;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final ReportService reportService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //temporary: execute script data.sql and login on adam_adamowicz instead of admin
 /*       if (!userRepository.findByUsername("admin").isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEnabled(true);
            userRepository.save(user);
        }*/

        attachReportsToEmployees();
    }

    private void attachReportsToEmployees() {
        Optional<User> employeeUserOptional = userService.findByUsername("adam_adamowicz");
        Optional<User> customerUserOptional = userService.findByUsername("adam_klient");
        if (employeeUserOptional.isPresent()) {
            Optional<Employee> employeeOptional = employeeService.findByUser(employeeUserOptional.get());
            Optional<Customer> customerOptional = customerService.findByUser(customerUserOptional.get());

            List<Report> reports = reportService.findAll();
            Employee employee = employeeOptional.get();
            employee.setReports(reports);
            employeeService.save(employee);

            Customer customer = customerOptional.get();
            customer.setReports(reports);
            customerService.save(customer);
            Device device1 = new Device();
            device1.setBrand("SAMSUNG");
            device1.setModel("ABC");
            device1.setSerialNumber("0001234HUAHUA1234");
            device1.setDescription("Desc 1");
            device1.setExternalId("#EXTERNALID9999345");

            Device device2 = new Device();
            device2.setBrand("LOGITECH");
            device2.setModel("CCC");
            device2.setSerialNumber("0007777LLAA5555");
            device2.setDescription("Desc 2");
            device2.setExternalId("#EXTERNALID1114447");

            Device device3 = new Device();
            device3.setBrand("ASUS");
            device3.setModel("DDDD");
            device3.setSerialNumber("7424PINGPONG3787");
            device3.setDescription("Desc 3");
            device3.setExternalId("#EXTERNALID777788");

            Report report1 = new Report();
            Report report2 = new Report();
            Report report3 = new Report();


            report1.setTitle("AWARIA PRALKI");
            report1.setDevice(device1);
            report1.setDescription("Nie reaguje na programowanie polecenia");
            report1.setEmployee(employee);
            report1.setCustomer(customer);

            report2.setTitle("AWARIA ZMYWARKI");
            report2.setDevice(device2);
            report2.setDescription("Nie wykonuje cyklu mycia");
            report2.setEmployee(employee);
            report2.setCustomer(customer);

            report3.setTitle("AWARIA LODÓWKI");
            report3.setDevice(device3);
            report3.setDescription("Z wnętrza wydobywają się dziwne dźwięki");
            report3.setEmployee(employee);
            report3.setCustomer(customer);

            reportService.save(report1);
            reportService.save(report2);
            reportService.save(report3);
        }
    }
}
