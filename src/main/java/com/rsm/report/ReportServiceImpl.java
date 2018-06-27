package com.rsm.report;

import com.querydsl.core.BooleanBuilder;
import com.rsm.customer.Customer;
import com.rsm.customer.CustomerDoesNotExistException;
import com.rsm.customer.CustomerService;
import com.rsm.employee.Employee;
import com.rsm.employee.EmployeeService;
import com.rsm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final UserService userService;
    private final static Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public List<Report> findAll() {
        return reportRepository.findAllByOrderByReportDateDesc();
    }

    @Override
    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    public Report saveAndReturn(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void delete(Report report) {
        reportRepository.save(report);
    }

    @Override
    public void deleteById(Long id) {
        reportRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void attachReportToCustomer(Report report, String username) {
        Optional<Customer> optionalCustomer = customerService.findByUsername(username);
        Customer customer = optionalCustomer.orElseThrow(CustomerDoesNotExistException::new);
        report.setCustomer(customer);
        reportRepository.save(report);

    }

    @Override
    @Transactional
    public void attachReportToRandomEmployee(Report report) {
        List<Employee> employees = employeeService.findAll();
        int randomIndex = new Random().nextInt(employees.size());
        LOG.info("randomEmployeeIndex: " + randomIndex);
        Employee employee = employees.get(randomIndex);
        List<Report> employeeReports = employee.getReports();
        if (employeeReports == null) {
            employeeReports = new ArrayList<>();
        }
        employeeReports.add(report);
        employee.setReports(employeeReports);
        report.setEmployee(employee);
        reportRepository.save(report);
        employeeService.save(employee);
    }

    @Override
    public List<Report> findUnassignedAndNotFinished() {
        List<Report> reports = this.findAll();
        List<Report> unassigned = new ArrayList<>();
        for (Report report : reports) {
            if (report.getEmployee() == null && !report.getReportStatus().equals(ReportStatus.FINISHED))
                unassigned.add(report);
        }
        return unassigned;
    }

    @Override
    public List<Report> findByDeviceId(Long deviceId) {
        return reportRepository.findByDevice_IdOrderByReportDateDesc(deviceId);
    }

    @Override
    public Iterable<Report> findByQuery(SearchReportParam searchReportParam) {
        BooleanBuilder employeePredicate = new BooleanBuilder();
        String[] employeeTokens = StringUtils.split(searchReportParam.getEmployeeIdentify(), ' ');
        if (employeeTokens.length != 2) {
            for (String token : employeeTokens) {
                employeePredicate.or(QReport.report.employee.username.containsIgnoreCase(token));
                employeePredicate.or(QReport.report.employee.details.firstName.containsIgnoreCase(token));
                employeePredicate.or(QReport.report.employee.details.lastName.containsIgnoreCase(token));
            }
        } else {
            for (String token : employeeTokens) {
                employeePredicate.or(QReport.report.employee.username.containsIgnoreCase(token));
            }
            employeePredicate.or(QReport.report.employee.details.firstName.containsIgnoreCase(employeeTokens[0]));
            employeePredicate.or(QReport.report.employee.details.lastName.containsIgnoreCase(employeeTokens[1]));
        }

        BooleanBuilder customerPredicate = new BooleanBuilder();
        String[] customerTokens = StringUtils.split(searchReportParam.getCustomerIdentify(), ' ');
        if (customerTokens.length != 2) {
            for (String token : customerTokens) {
                customerPredicate.or(QReport.report.customer.username.containsIgnoreCase(token));
                customerPredicate.or(QReport.report.customer.details.firstName.containsIgnoreCase(token));
                customerPredicate.or(QReport.report.customer.details.lastName.containsIgnoreCase(token));
            }
        } else {
            for (String token : customerTokens) {
                customerPredicate.or(QReport.report.customer.username.containsIgnoreCase(token));
            }
            customerPredicate.or(QReport.report.customer.details.firstName.containsIgnoreCase(customerTokens[0]));
            customerPredicate.or(QReport.report.customer.details.lastName.containsIgnoreCase(customerTokens[1]));
        }

        BooleanBuilder result = new BooleanBuilder();
        result.and(customerPredicate).and(employeePredicate);

        if (searchReportParam.getReportStatus() != null) {
            result.and(QReport.report.reportStatus.eq(searchReportParam.getReportStatus()));
        }

        return reportRepository.findAll(result);
    }
}
