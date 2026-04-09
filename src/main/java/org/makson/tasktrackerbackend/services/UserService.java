package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.exceptions.UserNotFoundException;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email '" + email + "' not found");
        }

        return user.get();
    }
}
