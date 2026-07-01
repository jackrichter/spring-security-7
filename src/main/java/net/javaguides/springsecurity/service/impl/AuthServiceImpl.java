package net.javaguides.springsecurity.service.impl;

import net.javaguides.springsecurity.dto.LoginDto;
import net.javaguides.springsecurity.dto.RegisterDto;
import net.javaguides.springsecurity.entity.Role;
import net.javaguides.springsecurity.entity.User;
import net.javaguides.springsecurity.exception.EmailAlreadyExistsException;
import net.javaguides.springsecurity.repository.RoleRepository;
import net.javaguides.springsecurity.repository.UserRepository;
import net.javaguides.springsecurity.security.JwtTokenProvider;
import net.javaguides.springsecurity.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

//    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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

    /**
     * Provides the client with a JWT token to be used for authentication
     * @param loginDto
     * @return
     */
    @Override
    public String login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );

        // The authentication object has to be set in the 'Security Context'
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token to be returned to the client
        String jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;

//        return "User has logged in successfully"; // Ideally, we should return a JWT token here!!!
    }
}
