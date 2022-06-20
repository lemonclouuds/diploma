package com.company.financeApp.service;

import com.company.financeApp.domain.dto.UserDto;
import com.company.financeApp.domain.user.User;
import com.company.financeApp.exception.EntityAlreadyExistsException;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final ModelMapper modelMapper = new ModelMapper();

    private final String USER_WITH_ID_NOT_FOUND = "User[%d] not found";

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return MapperHelper.mapList(userRepository.findAll(), UserDto.class);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, userId)));
    }

    public UserDto findDtoById(Long userId) {
        return modelMapper.map(findById(userId), UserDto.class);
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (!userRepository.existsById(userDto.getId())) {
            user.setTransactions(new ArrayList<>());
            user.setGoals(new ArrayList<>());
            user.setCategories(new ArrayList<>());
            userRepository.save(user);
        } else {
            throw new EntityAlreadyExistsException(String.format("User with id[%d] already exists",
                    userDto.getId()));
        }
        return userDto;
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setId(userId);
        if (userRepository.existsById(userId)) {
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException(String.format(USER_WITH_ID_NOT_FOUND, userId));
        }
        return userDto;
    }

    @Transactional
    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteByIdIn(Collection<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with email[%s] not found", username))
        );
    }
}
