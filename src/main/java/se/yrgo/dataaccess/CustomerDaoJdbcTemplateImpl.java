package se.yrgo.dataaccess;

import java.sql.*;
import java.util.*;
import java.util.Date;

import org.springframework.dao.*;
import org.springframework.jdbc.core.*;

import se.yrgo.domain.*;

public class CustomerDaoJdbcTemplateImpl implements CustomerDao {

    private static final String CREATE_TBL_CALL = "CREATE TABLE IF NOT EXISTS TBL_CALL (TIMEANDDATE TIMESTAMP, NOTES VARCHAR(255), CUSTOMERID VARCHAR(100))";
    private static final String CREATE_CUSTOMER_TBL = "CREATE TABLE IF NOT EXISTS CUSTOMER (CUSTOMERID VARCHAR(100), COMPANYNAME VARCHAR(100), EMAIL VARCHAR(30), TELEPHONE VARCHAR(12), NOTES VARCHAR(255))";

    private static final String INSERT_SQL = "INSERT INTO CUSTOMER (CUSTOMERID, COMPANYNAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)";
    private static final String DELETE_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMERID=?";
    private static final String UPDATE_SQL = "UPDATE CUSTOMER SET COMPANYNAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMERID=?";
    private static final String GET_ALL_CUSTOMERS = "SELECT CUSTOMERID, COMPANYNAME, EMAIL, TELEPHONE, NOTES FROM CUSTOMER";
    private static final String GET_CUSTOMER_BY_NAME = "SELECT * FROM CUSTOMER WHERE COMPANYNAME=?";
    private static final String GET_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE CUSTOMERID=?";
    private static final String GET_CUSTOMER_CALLS = "SELECT * FROM TBL_CALL WHERE CUSTOMERID=?";
    private static final String INSERT_NEW_CALL = "INSERT INTO TBL_CALL (TIMEANDDATE, NOTES, CUSTOMERID) VALUES (?,?,?)";

    private JdbcTemplate template;

    public CustomerDaoJdbcTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void createTables() throws DataAccessException {
        try {
            this.template.update(CREATE_TBL_CALL);
            this.template.update(CREATE_CUSTOMER_TBL);
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("Assuming the table exists");
        } catch (Exception e) {
            System.out.println("Unexpected error while creating tables: " + e.getMessage());
        }
    }

    @Override
    public void create(Customer customer) {
        template.update(INSERT_SQL, customer.getCustomerId(), customer.getCompanyName(), customer.getEmail(), customer.getTelephone(),
                customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        try {
            
            return template.queryForObject(GET_CUSTOMER_BY_ID, new CustomerRowMapper(), customerId);
        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public List<Customer> getByName(String name) {
        return this.template.query(GET_CUSTOMER_BY_NAME, new CustomerRowMapper());
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        this.template.update(UPDATE_SQL, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(),
                customerToUpdate.getTelephone(), customerToUpdate.getNotes(), customerToUpdate.getCustomerId());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        this.template.update(DELETE_SQL, oldCustomer.getCustomerId());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return this.template.query(GET_ALL_CUSTOMERS, new CustomerRowMapper());
    }

    @Override // DDENNA METOD ÄR INTE RÄTT, DU SKA ÄVEN HÄMTA CALLS RELATERADE TILL CUSTOMER
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        try {
            

            Customer customer = this.template.queryForObject(GET_CUSTOMER_BY_ID, new CustomerRowMapper(), customerId);

            List<Call> calls = this.template.query(GET_CUSTOMER_CALLS, new CallRowMapper(), customerId);

            customer.setCalls(calls);

            return customer;

        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        if (newCall == null) {
            throw new IllegalArgumentException("Call object cannot be null.");
        }
        try {
            
            Customer customer = this.template.queryForObject(GET_CUSTOMER_BY_ID, new CustomerRowMapper(), customerId);

            this.template.update(INSERT_NEW_CALL, newCall.getTimeAndDate(), newCall.getNotes(),
                    customer.getCustomerId());

        } catch (EmptyResultDataAccessException e) {
            throw new RecordNotFoundException();
        }
    }

}

class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        String CUSTOMERID = rs.getString(1);
        String COMPANYNAME = rs.getString(2);
        String EMAIL = rs.getString(3);
        String TELEPHONE = rs.getString(4);
        String NOTES = rs.getString(5);

        return new Customer("" + CUSTOMERID, COMPANYNAME, EMAIL, TELEPHONE, NOTES);
    }
}

class CallRowMapper implements RowMapper<Call> {

    @Override
    public Call mapRow(ResultSet rs, int rowNum) throws SQLException {

        Date timeStamp = rs.getTimestamp(2);
        String notes = rs.getString(3);
        return new Call(timeStamp, notes);
    }
}
