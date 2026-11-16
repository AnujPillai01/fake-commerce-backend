package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.schema.VendorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetVendorResponseDto {
    private Long id;
    private String storeName;
    private String storeDescription;
    private String storePhoneNumber;
    private VendorStatus status;
    private Long userId;
    private String ownerFullName;
    private String ownerEmail;
}