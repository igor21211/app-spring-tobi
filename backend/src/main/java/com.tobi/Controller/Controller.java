package com.tobi.Controller;



import com.tobi.customerService.CustomService;
import com.tobi.model.Customer;
import com.tobi.model.CustomerRegistrationRequest;
import com.tobi.model.CustomerUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class Controller {

    private final CustomService service;

    @PostMapping()
    public void addCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest){
         service.addCustomer(customerRegistrationRequest);
    }
    @GetMapping
    public List<Customer> getCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId) {
        return service.getCustomer(customerId);
    }


    @DeleteMapping("{customerId}")
    public void deleteCustomer(
            @PathVariable("customerId") Integer customerId) {
        service.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerUpdateRequest updateRequest) {
        service.updateCustomer(customerId, updateRequest);
    }

}
