package com.tobi.Controller;



import com.tobi.customerService.CustomService;
import com.tobi.dto.CustomerDTO;
import com.tobi.jwt.JWTUtil;
import com.tobi.model.Customer;
import com.tobi.model.CustomerRegistrationRequest;
import com.tobi.model.CustomerUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/customers")
public class Controller {

    private final CustomService service;
    private final JWTUtil jwt;

    @PostMapping()
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest request){
         service.addCustomer(request);
        String jwtToken = jwt.issueToken(request.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }
    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("{customerId}")
    public CustomerDTO getCustomer(
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
