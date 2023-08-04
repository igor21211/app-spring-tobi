package com.tobi.customerService;

import com.tobi.Dao.CustomerDao;
import com.tobi.exceptions.DuplicateResourceException;
import com.tobi.exceptions.NotFoundCustomerException;
import com.tobi.exceptions.RequestValidationException;
import com.tobi.model.Customer;
import com.tobi.model.CustomerRegistrationRequest;
import com.tobi.model.CustomerUpdateRequest;
import com.tobi.model.Gender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomService {

    private final CustomerDao customerDao;

    public CustomService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new NotFoundCustomerException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // check if email exists
        String email = customerRegistrationRequest.email();
        if (customerDao.existsCustomerWithEmail(email)) {
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        // add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender());

        customerDao.insertCustomer(customer);
    }

    public void deleteCustomerById(Integer customerId) {
        checkIfCustomerExistsOrThrow(customerId);
        customerDao.deleteCustomerById(customerId);
    }

    private void checkIfCustomerExistsOrThrow(Integer customerId) {
        if (!customerDao.existsCustomerById(customerId)) {
            throw new NotFoundCustomerException(
                    "customer with id [%s] not found".formatted(customerId)
            );
        }
    }

    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Customer customer = customerDao.selectCustomerById(customerId)
                .orElseThrow(() -> new NotFoundCustomerException(
                        "customer with id [%s] not found".formatted(customerId)
                ));

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);
    }
}
