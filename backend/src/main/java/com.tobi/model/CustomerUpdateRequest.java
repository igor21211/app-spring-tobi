package com.tobi.model;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
