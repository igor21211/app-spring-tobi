package com.tobi.customerService;

import com.tobi.Dao.CustomerDao;

import com.tobi.exceptions.DuplicateResourceException;
import com.tobi.exceptions.NotFoundCustomerException;
import com.tobi.exceptions.RequestValidationException;
import com.tobi.model.Customer;
import com.tobi.model.CustomerRegistrationRequest;
import com.tobi.model.CustomerUpdateRequest;
import com.tobi.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomServiceTest {

    private CustomService underTest;
    @Mock
    private  CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomService(customerDao);
    }


    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 10;
        Customer customer = new Customer(
          id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomer() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> underTest.getCustomer(id))
                .isInstanceOf(NotFoundCustomerException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        String email = "alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19 ,Gender.MALE
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getId()).isNull();
        assertThat(captureCustomer.getName()).isEqualTo(request.name());
        assertThat(captureCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captureCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsAddCustomer() {
        String email = "alex@gmail.com";
        when(customerDao.existsCustomerWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19, Gender.MALE
        );

        assertThatThrownBy(()-> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage( "email already taken");

        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        int id = 10;
        when(customerDao.existsCustomerById(id)).thenReturn(true);
        underTest.deleteCustomerById(id);
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThorExceptionNotExistIdDeleteCustomerById() {
        int id = 10;
        when(customerDao.existsCustomerById(id)).thenReturn(false);
        assertThatThrownBy(()-> underTest.deleteCustomerById(id))
                .isInstanceOf(NotFoundCustomerException.class)
                        .hasMessage( "customer with id [%s] not found".formatted(id));
        verify(customerDao, never()).deleteCustomerById(any());
    }

    @Test
    void canUpdateAllCustomerFields() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Alexandro", "alexandro@gmail.com", 23
        );

        when(customerDao.existsCustomerWithEmail("alexandro@gmail.com")).thenReturn(false);

        underTest.updateCustomer(id,request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getName()).isEqualTo(request.name());
        assertThat(captureCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captureCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void canUpdateAllCustomerOnlyNameField() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Alexandro", null, null
        );


        underTest.updateCustomer(id,request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getName()).isEqualTo(request.name());
        assertThat(captureCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captureCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateAllCustomerOnlyEmailField() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandresf@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, newEmail, null
        );
        when(customerDao.existsCustomerWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id,request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captureCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(captureCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateAllCustomerOnlyAgeField() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));


        CustomerUpdateRequest request = new CustomerUpdateRequest(
                null, null, 20
        );

        underTest.updateCustomer(id,request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer captureCustomer = customerArgumentCaptor.getValue();
        assertThat(captureCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captureCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captureCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowExceptionWhenTryUpdateCustomerEmailWhenAlreadyTaken() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                "Alexandro", "alexandro@gmail.com", 23
        );

        when(customerDao.existsCustomerWithEmail("alexandro@gmail.com")).thenReturn(true);

        assertThatThrownBy(()-> underTest.updateCustomer(id,request))
                .isInstanceOf(DuplicateResourceException.class)
                        .hasMessage("email already taken");

        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowExceptionWhenCustomerUpdateNoChanges() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        assertThatThrownBy(()-> underTest.updateCustomer(id,request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowExceptionWhenDontFindCustomerId() {
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        CustomerUpdateRequest request = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        assertThatThrownBy(()-> underTest.updateCustomer(id,request))
                .isInstanceOf(NotFoundCustomerException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
        verify(customerDao, never()).updateCustomer(any());
    }
}