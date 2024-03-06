package org.samydevup.blogrestapi.service;

import org.samydevup.blogrestapi.payload.LoginDto;
import org.samydevup.blogrestapi.payload.RegisterDto;

public interface AuthService {
    String Login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
