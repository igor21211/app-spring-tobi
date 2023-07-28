package com.tobi.CustomerJDBC;

import com.tobi.CustomerRowMapper.CustomerRowMapper;
import com.tobi.Dao.CustomerDao;
import com.tobi.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
@AllArgsConstructor
public class CustomerJDBC implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    private final CustomerRowMapper customerRowMapper;



    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name, email, age 
                FROM customer   
                """;
        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                SELECT id, name, email, age 
                FROM customer WHERE id=?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name,email,age)
                VALUES(?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("JdbcTemplate.update = "+ result);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,email);
        return count != null && count >0;
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id=?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,customerId);
        return count != null && count >0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE 
                FROM customer WHERE id=?
                """;
        int result = jdbcTemplate.update(sql,customerId);
        System.out.println("delete Customer "+ result );
    }

    @Override
    public void updateCustomer(Customer update) {
        if(update.getName() != null){
            String sql = "UPDATE customer SET name=? WHERE id=?";
            int result = jdbcTemplate.update(sql,update.getName(), update.getId());
            System.out.println("Update customer name result = "+ result);
        }

        if(update.getEmail() != null){
            String sql = "UPDATE customer SET email=? WHERE id=?";
            int result = jdbcTemplate.update(sql,update.getEmail(), update.getId());
            System.out.println("Update customer email result = "+ result);
        }

        if(update.getAge() != null){
            String sql = "UPDATE customer SET age=? WHERE id=?";
            int result = jdbcTemplate.update(sql,update.getAge(), update.getId());
            System.out.println("Update customer age result = "+ result);
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = """
                SELECT id, name, email, age 
                FROM customer WHERE email=?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream()
                .findFirst();
    }
}
