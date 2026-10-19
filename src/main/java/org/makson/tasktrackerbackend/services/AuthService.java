package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.makson.tasktrackerbackend.dto.AuthDto;
import org.makson.tasktrackerbackend.exceptions.UserAlreadyExistException;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final GreetingService greetingService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Transactional
    public String register(AuthDto authDto) {
        if (userRepository.findByEmail(authDto.email()).isPresent()) {
            throw new UserAlreadyExistException("Email '" + authDto.email() + "' is already taken");
        }

        User user = User.builder()
                .email(authDto.email())
                .password(passwordEncoder.encode(authDto.password()))
                .build();

        userRepository.save(user);
        log.info("The user has successfully registered with email: {}", user.getEmail());

        greetingService.sendGreeting(user.getEmail());
        return jwtService.generateJwt(user);
    }

    @Transactional(readOnly = true)
    public String login(AuthDto authDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password()));
        UserDetails principal = (UserDetails) authenticate.getPrincipal();
        log.info("The user has successfully logged with email: {}", authDto.email());

        return jwtService.generateJwt(principal);
    }
}
