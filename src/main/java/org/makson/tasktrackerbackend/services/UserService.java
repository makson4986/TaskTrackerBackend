package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.UserDto;
import org.makson.tasktrackerbackend.exceptions.UserNotFoundException;
import org.makson.tasktrackerbackend.mappers.UserMapper;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = repository.findByEmail(email);
        return checkExistUser(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        List<User> users = repository.findAll();
        return mapper.toListDto(users);
    }

    @Transactional(readOnly = true)
    public User getById(Integer id) {
        Optional<User> user = repository.findById(id);
        return checkExistUser(user);
    }

    private User checkExistUser(Optional<User> user) {
        if (user.isEmpty()) {
            throw new UserNotFoundException("User isn't found");
        }

        return user.get();
    }

}
