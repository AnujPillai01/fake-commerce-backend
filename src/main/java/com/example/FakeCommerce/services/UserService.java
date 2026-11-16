package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.UserAdapter;
import com.example.FakeCommerce.dtos.GetUserResponseDto;
import com.example.FakeCommerce.dtos.RegisterUserRequestDto;
import com.example.FakeCommerce.dtos.UpdateUserRequestDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.UserRepository;
import com.example.FakeCommerce.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAdapter userAdapter;

    @Transactional
    public GetUserResponseDto registerUser(RegisterUserRequestDto requestDto){
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("Email is already registered!");
        }

        User user = User
                .builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .fullName(requestDto.getFullName())
                .build();

        userRepository.save(user);

        return userAdapter.mapToGetUserResponseDto(user);
    }

    public GetUserResponseDto getUserProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        return userAdapter.mapToGetUserResponseDto(user);
    }

    public GetUserResponseDto getUserProfileByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        return userAdapter.mapToGetUserResponseDto(user);
    }

    @Transactional
    public GetUserResponseDto updateUserProfile(Long userId, UpdateUserRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(requestDto.getName()!=null && !requestDto.getName().isBlank()){
            user.setFullName(requestDto.getName());
        }

        if(requestDto.getContact()!=null && !requestDto.getContact().isBlank()){
            user.setPhoneNumber(requestDto.getContact());
        }

        userRepository.save(user);

        return userAdapter.mapToGetUserResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
