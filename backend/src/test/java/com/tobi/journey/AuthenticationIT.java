package com.tobi.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.tobi.auth.AuthenticationRequest;
import com.tobi.auth.AuthenticationResponse;
import com.tobi.dto.CustomerDTO;
import com.tobi.jwt.JWTUtil;
import com.tobi.model.CustomerRegistrationRequest;
import com.tobi.model.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {

    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    @Autowired
    private  JWTUtil jwtUtil;
    private static final String CUSTOMER_AUTHENTICATION = "api/v1/auth";
    private static final String CUSTOMER_URI = "api/v1/customers";



    @Test
    void canLogin(){
        Faker faker = new Faker();
        String password = "password";
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@fooobaarrraa.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, password, age, gender
        );
        AuthenticationRequest authenticationRequest =  new AuthenticationRequest(
                email,
                password
        );

        webTestClient.post()
                .uri(CUSTOMER_AUTHENTICATION+"/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

       EntityExchangeResult<AuthenticationResponse> result =  webTestClient.post()
                .uri(CUSTOMER_AUTHENTICATION+"/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();
        String token = result.getResponseHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        AuthenticationResponse authenticationResponse = result.getResponseBody();
        CustomerDTO customerDTO = authenticationResponse.customerDTO();
        assertThat(jwtUtil.isTokenValid(token, authenticationResponse.customerDTO().username()));

        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));


    }


}
