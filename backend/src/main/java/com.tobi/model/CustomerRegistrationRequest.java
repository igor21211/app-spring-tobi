package com.tobi.model;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
