package com.example.bookstore.service.user.impl;

import com.example.bookstore.dto.user.UserRegistrationRequestDto;
import com.example.bookstore.dto.user.UserResponseDto;
import com.example.bookstore.exception.RegistrationException;
import com.example.bookstore.mapper.UserMapper;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.role.RoleRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import com.example.bookstore.service.user.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Unable to complete registration.");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        if (requestDto.getEmail().contains("admin")) {
            roles.add(roleRepository.findByName(Role.RoleName.ROLE_ADMIN));
        }
        roles.add(defaultRole);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        shoppingCartService.registerNewShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
