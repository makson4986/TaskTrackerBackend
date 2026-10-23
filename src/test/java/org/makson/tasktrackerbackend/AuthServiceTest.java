package org.makson.tasktrackerbackend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.makson.tasktrackerbackend.dto.AuthDto;
import org.makson.tasktrackerbackend.exceptions.UserAlreadyExistException;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.UserRepository;
import org.makson.tasktrackerbackend.services.AuthService;
import org.makson.tasktrackerbackend.services.GreetingService;
import org.makson.tasktrackerbackend.services.JwtService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private GreetingService greetingService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should register user, save to DB, send greeting and return JWT")
    void shouldRegisterUserSuccessfully() {
        AuthDto authDto = new AuthDto("test@test.test", "password");

        doReturn(Optional.empty())
                .when(userRepository)
                .findByEmail(anyString());
        doReturn("hashPassword")
                .when(passwordEncoder)
                .encode(anyString());
        doReturn("token")
                .when(jwtService)
                .generateJwt(any(User.class));

        String token = authService.register(authDto);

        assertEquals("token", token);
        verify(userRepository).save(any(User.class));
        verify(greetingService).sendGreeting(anyString());
        verify(jwtService).generateJwt(any(User.class));
    }

    @Test
    @DisplayName("Registering a user with a non-unique login results in an exception")
    void ifUserExistsThenException() {
        AuthDto authDto = new AuthDto("test@test.test", "password");

        doReturn(Optional.of(new User()))
                .when(userRepository)
                .findByEmail(anyString());

        assertThrows(UserAlreadyExistException.class, () -> authService.register(authDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login user and return JWT")
    void shouldLoginUserSuccessfully() {
        AuthDto authDto = new AuthDto("test@test.test", "password");
        UserDetails userDetails = User.builder()
                .email("test@test.test")
                .password("password")
                .build();
        Authentication authentication = mock(Authentication.class);

        doReturn(authentication)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        doReturn(userDetails)
                .when(authentication)
                .getPrincipal();
        doReturn("token")
                .when(jwtService)
                .generateJwt(any(User.class));

        String token = authService.login(authDto);

        assertEquals("token", token);
        verify(jwtService).generateJwt(any(User.class));
    }
}
