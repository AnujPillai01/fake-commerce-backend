package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.GetUserResponseDto;
import com.example.FakeCommerce.dtos.RegisterUserRequestDto;
import com.example.FakeCommerce.dtos.UpdateUserRequestDto;
import com.example.FakeCommerce.schema.User;
import com.example.FakeCommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> registerUser(@RequestBody RegisterUserRequestDto requestDto){
        GetUserResponseDto savedUser = userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("User registered successfully",savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> getUserProfile(@PathVariable Long id){
        GetUserResponseDto user = userService.getUserProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("User profile fetched successfully",user));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> getUserProfileByEmail(@RequestParam String email){
        GetUserResponseDto user = userService.getUserProfileByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("User profile fetched successfully",user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetUserResponseDto>> updateUserProfile(@PathVariable Long id , @RequestBody UpdateUserRequestDto requestDto){
        GetUserResponseDto updatedUser = userService.updateUserProfile(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("User updated successfully",updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("User deleted successfully",null));
    }
}
