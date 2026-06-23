package net.javaguides.springsecurity.service;

import net.javaguides.springsecurity.dto.RegisterDto;

public interface AuthService {

    String register(RegisterDto registerDto);
}
