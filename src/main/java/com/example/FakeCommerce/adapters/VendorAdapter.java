package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetVendorResponseDto;
import com.example.FakeCommerce.schema.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VendorAdapter {

    public GetVendorResponseDto mapToGetVendorResponseDto(Vendor vendor) {
        if (vendor == null) {
            return null;
        }

        return GetVendorResponseDto.builder()
                .id(vendor.getId())
                .storeName(vendor.getStoreName())
                .storeDescription(vendor.getDescription())         // Mapped from entity's description
                .storePhoneNumber(vendor.getContactPhone())     // Mapped from entity's contactPhone
                .status(vendor.getStatus())
                .userId(vendor.getUser() != null ? vendor.getUser().getId() : null)
                .ownerFullName(vendor.getUser() != null ? vendor.getUser().getFullName() : null)
                .ownerEmail(vendor.getUser() != null ? vendor.getUser().getEmail() : null)
                .build();
    }

    public List<GetVendorResponseDto> mapToGetVendorResponseDtoList(List<Vendor> vendors) {
        if (vendors == null) {
            return List.of();
        }

        return vendors.stream()
                .map(this::mapToGetVendorResponseDto)
                .collect(Collectors.toList());
    }
}