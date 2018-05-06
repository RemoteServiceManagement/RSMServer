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

    private void attachReportsToEmployees(){
        Optional<User> employeeUserOptional=userService.findByUsername("adam_adamowicz");
        Optional<User> customerUserOptional=userService.findByUsername("adam_klient");
        if(employeeUserOptional.isPresent()){
            Optional<Employee> employeeOptional=employeeService.findByUser(employeeUserOptional.get());
            Optional<Customer> customerOptional=customerService.findByUser(customerUserOptional.get());

            List<Report> reports=reportService.findAll();
            Employee employee=employeeOptional.get();
            employee.setReports(reports);
            employeeService.save(employee);

            Customer customer=customerOptional.get();
            customer.setReports(reports);
            customerService.save(customer);



            Optional<Device> optionalDevice1=deviceService.findById(1L);
            Optional<Device> optionalDevice2=deviceService.findById(2L);
            Optional<Device> optionalDevice3=deviceService.findById(3L);
            Report report1=new Report();
            Report report2=new Report();
            Report report3=new Report();
            if(optionalDevice1.isPresent() &&
                    optionalDevice2.isPresent() && optionalDevice3.isPresent()){



                report1.setTitle("AWARIA PRALKI");
                report1.setDevice(optionalDevice1.get());
                report1.setDescription("Nie reaguje na programowanie polecenia");
                report1.setEmployee(employee);
                report1.setCustomer(customer);

                report2.setTitle("AWARIA ZMYWARKI");
                report2.setDevice(optionalDevice2.get());
                report2.setDescription("Nie wykonuje cyklu mycia");
                report2.setEmployee(employee);
                report2.setCustomer(customer);

                report3.setTitle("AWARIA LODÓWKI");
                report3.setDevice(optionalDevice3.get());
                report3.setDescription("Z wnętrza wydobywają się dziwne dźwięki");
                report3.setEmployee(employee);
                report3.setCustomer(customer);

                reportService.save(report1);
                reportService.save(report2);
                reportService.save(report3);
            }
        }
    }
}
