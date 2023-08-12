package com.tobi.CustomerJPAData;

import com.github.javafaker.Faker;
import com.tobi.model.Customer;
import com.tobi.model.Gender;
import com.tobi.repostitory.CustomersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;


import static org.mockito.Mockito.verify;

class CustomerJpaTest {

    private CustomerJpa underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomersRepository repository;

    private final Faker FAKER = new Faker();


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpa(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();
        verify(repository).findAll();
    }

    @Test
    void selectCustomerById() {
        int id = 1;
        underTest.selectCustomerById(id);
        verify(repository).findById(id);
    }

    @Test
    void insertCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        verify(repository).save(customer);
    }

    @Test
    void existsCustomerWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);
        underTest.existsCustomerWithEmail(email);
        verify(repository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerById() {
        int id = 1;
        underTest.existsCustomerById(id);
        verify(repository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        int id = 1;
        underTest.deleteCustomerById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);
        underTest.updateCustomer(customer);
        verify(repository).save(customer);
    }
    @Test
    void selectUserByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        underTest.selectUserByEmail(email);
        verify(repository).findCustomerByEmail(email);
    }
}