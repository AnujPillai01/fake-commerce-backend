package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.schema.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserRole role;
}