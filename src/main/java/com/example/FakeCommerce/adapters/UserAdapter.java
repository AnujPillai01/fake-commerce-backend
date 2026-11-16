package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetUserResponseDto;
import com.example.FakeCommerce.schema.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAdapter {

    public GetUserResponseDto mapToGetUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return GetUserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    public List<GetUserResponseDto> mapToGetUserResponseDtoList(List<User> users) {
        if (users == null) {
            return List.of();
        }

        return users.stream()
                .map(this::mapToGetUserResponseDto)
                .collect(Collectors.toList());
    }
}