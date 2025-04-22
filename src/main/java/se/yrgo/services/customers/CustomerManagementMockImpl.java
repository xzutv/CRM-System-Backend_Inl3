package se.yrgo.services.customers;

import java.util.*;

import se.yrgo.domain.Call;
import se.yrgo.domain.Customer;

public class CustomerManagementMockImpl implements CustomerManagementService {

	private HashMap<String, Customer> customerMap;

	public CustomerManagementMockImpl() {
		customerMap = new HashMap<String, Customer>();
		customerMap.put("OB74", new Customer("OB74", "Fargo Ltd", "some notes"));
		customerMap.put("NV10", new Customer("NV10", "North Ltd", "some other notes"));
		customerMap.put("RM210", new Customer("RM210", "River Ltd", "some more notes"));
	}

	@Override
	public void newCustomer(Customer newCustomer) {
		customerMap.put(newCustomer.getCustomerId(), newCustomer);

	}

	@Override
	public void updateCustomer(Customer changedCustomer) {
		Customer customer = customerMap.get(changedCustomer.getCustomerId());
		if (customer != null) {
			customer.setEmail(changedCustomer.getEmail());
			customer.setCompanyName(changedCustomer.getEmail());
			customer.setCustomerId(changedCustomer.getCustomerId());
			customer.setTelephone(changedCustomer.getTelephone());
			customer.setNotes(changedCustomer.getNotes());
			customer.setCalls(changedCustomer.getCalls());
		} else {
			System.out.println("Trying to find customer.. ");
			System.out.println("Could not find customer");
		}
	}

	@Override
	public void deleteCustomer(Customer oldCustomer) {
		if (oldCustomer != null) {
			customerMap.remove(oldCustomer.getCustomerId(), oldCustomer);
		} else {
			System.out.println("Trying to remove customer.. ");
			System.out.println("Could not find customer.");
		}
	}

	@Override
	public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
		return this.customerMap.get(customerId);
	}

	@Override
	public List<Customer> findCustomersByName(String name) {
		List<Customer> result = new ArrayList<>();

		for (Customer customer : customerMap.values()) {
			if (customer.getCompanyName().equalsIgnoreCase(name)) {
				result.add(customer);
			}
		}
		return result;
	}

	@Override
	public List<Customer> getAllCustomers() {
		return new ArrayList<Customer>(customerMap.values());
	}

	@Override
	public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
		Customer c = customerMap.get(customerId);
		if (c == null) {
			throw new CustomerNotFoundException();
		} else {

			return c;
		}
	}

	@Override
	public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
		// First find the customer

		Customer c = customerMap.get(customerId);

		if (c == null) {
			throw new CustomerNotFoundException();
		}

		// Call the addCall on the customer

		c.addCall(callDetails);

	}

}
