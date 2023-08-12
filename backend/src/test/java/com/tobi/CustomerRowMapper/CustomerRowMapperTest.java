package com.tobi.CustomerRowMapper;

import com.tobi.model.Customer;
import com.tobi.model.Gender;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {



    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Kox");
        when(resultSet.getString("email")).thenReturn("kox@gmail.com");
        when(resultSet.getString("gender")).thenReturn("FEMALE");


        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        Customer expected = new Customer(
                1, "Kox", "kox@gmail.com", "password", 19,
                Gender.FEMALE);

        assertThat(actual).isEqualTo(expected);
    }
}