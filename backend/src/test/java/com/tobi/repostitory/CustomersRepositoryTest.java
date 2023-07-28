package com.tobi.repostitory;

import com.tobi.AbstractUnitTestContainerUnitTest;
import com.tobi.model.Customer;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomersRepositoryTest extends AbstractUnitTestContainerUnitTest  {

    @Autowired
    private CustomersRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmailWillReturnTrue() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        List<Customer> customers = underTest.findAll();

        var actual = underTest.existsCustomerByEmail(email);
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByEmailWillReturnFalse() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        List<Customer> customers = underTest.findAll();

        var actual = underTest.existsCustomerByEmail(email);
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerByIdWillReturnTrue() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);
        List<Customer> customers = underTest.findAll();

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsCustomerById(id);
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByIdWillReturnFalse() {
        int id = -1;

        var actual = underTest.existsCustomerById(id);
        assertThat(actual).isFalse();
    }

    @Test
    void findCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        var actual = underTest.findCustomerByEmail(email);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}