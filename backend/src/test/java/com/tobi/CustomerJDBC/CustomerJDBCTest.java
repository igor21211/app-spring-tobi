package com.tobi.CustomerJDBC;

import com.tobi.AbstractUnitTestContainerUnitTest;
import com.tobi.CustomerRowMapper.CustomerRowMapper;
import com.tobi.model.Customer;
import com.tobi.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCTest extends AbstractUnitTestContainerUnitTest {

    private CustomerJDBC underTest;
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBC(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID(),
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        List<Customer> customers = underTest.selectAllCustomers();

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        List<Customer> customers = underTest.selectAllCustomers();

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        int id = -1;

        var actual = underTest.selectCustomerById(id);

        assertThat(actual).isEmpty();

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
        List<Customer> customers = underTest.selectAllCustomers();

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsCustomerWithEmailReturnTrue() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        boolean actual = underTest.existsCustomerWithEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithEmailReturnFalse() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        boolean actual = underTest.existsCustomerWithEmail(email);
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerByIdReturnTrue() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsCustomerById(id);
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdReturnFalse() {
        int id = -1;
        boolean actual = underTest.existsCustomerById(id);
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {

        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        underTest.deleteCustomerById(id);
        var actual =  underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerByName() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newName = "test";
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var emailNew = "test@gmail.com";
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(emailNew);
        underTest.updateCustomer(update);

        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(emailNew);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerByAge() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newAge = 90;
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateCustomerAllFields() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 90;
        var emailNew = "test@gmail.com";
        var newName = "test";
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        update.setEmail(emailNew);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        var actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getEmail()).isEqualTo(emailNew);
            assertThat(c.getGender()).isEqualTo(Gender.MALE);
        });
    }

    @Test
    void selectUserByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String name =  FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                "password", 20,
                Gender.MALE);
        underTest.insertCustomer(customer);

        var actual = underTest.selectUserByEmail(email);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}