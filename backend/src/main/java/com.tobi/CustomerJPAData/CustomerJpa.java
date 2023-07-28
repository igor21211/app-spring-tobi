package com.tobi.CustomerJPAData;

import com.tobi.Dao.CustomerDao;
import com.tobi.exceptions.NotFoundCustomerException;
import com.tobi.model.Customer;
import com.tobi.repostitory.CustomersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jpa")
public class CustomerJpa implements CustomerDao {

    private final CustomersRepository repository;

    public CustomerJpa(CustomersRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return repository.findById(customerId);
    }

    @Override
    public void insertCustomer(Customer customer) {
        repository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return repository.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        return repository.existsCustomerById(customerId);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        repository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(Customer update) {
        repository.save(update);
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        return repository.findCustomerByEmail(email);
    }

}
