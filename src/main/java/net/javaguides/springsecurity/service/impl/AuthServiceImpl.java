package net.javaguides.springsecurity.service.impl;

import net.javaguides.springsecurity.dto.RegisterDto;
import net.javaguides.springsecurity.entity.Role;
import net.javaguides.springsecurity.entity.User;
import net.javaguides.springsecurity.exception.EmailAlreadyExistsException;
import net.javaguides.springsecurity.repository.RoleRepository;
import net.javaguides.springsecurity.repository.UserRepository;
import net.javaguides.springsecurity.service.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;        // From Spring Security Framework

//    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(RegisterDto registerDto) {       // <=> Sign in

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new UsernameNotFoundException("Username is already taken!");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));    // Obs! Password must be encrypted for security to work!

        Set<Role> roles = new HashSet<>();
        for (String roleName : registerDto.getRoles()) {
            roles.add(roleRepository.findByName(roleName));
        }
        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully!";
    }
}
