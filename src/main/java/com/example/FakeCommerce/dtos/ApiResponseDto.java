package com.example.FakeCommerce.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {
    private Boolean success;
    private String message;
    private Object error;
    private T data;

    public static <T> ApiResponseDto<T> success(String message, T data){
       return ApiResponseDto.<T>builder()
               .success(true)
               .message(message)
               .error(null)
               .data(data)
               .build();
    }

    public static <T> ApiResponseDto<T> error(String message, Object err){
        return ApiResponseDto.<T>builder()
                .success(false)
                .message(message)
                .error(err)
                .data(null)
                .build();
    }
}
