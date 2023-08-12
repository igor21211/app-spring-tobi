package com.tobi.auth;

import com.tobi.dto.CustomerDTO;

public record AuthenticationResponse(String token,CustomerDTO customerDTO) {
}
