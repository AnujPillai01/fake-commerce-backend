package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.GetVendorResponseDto;
import com.example.FakeCommerce.dtos.RegisterVendorRequestDto;
import com.example.FakeCommerce.dtos.UpdateVendorRequestDto;
import com.example.FakeCommerce.schema.Vendor;
import com.example.FakeCommerce.schema.VendorStatus;
import com.example.FakeCommerce.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vendors")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping("/register/{userId}")
    public ResponseEntity<ApiResponseDto<GetVendorResponseDto>> registerVendor(@PathVariable Long userId, @RequestBody RegisterVendorRequestDto requestDto){
        GetVendorResponseDto registeredVendor = vendorService.registerVendor(userId,requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Vendor registered successfully",registeredVendor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetVendorResponseDto>> getVendorProfile(@PathVariable Long id){
        GetVendorResponseDto vendor = vendorService.getVendorByProfile(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Vendor fetched successfully",vendor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetVendorResponseDto>> updateVendorProfile(@PathVariable Long id, @RequestBody UpdateVendorRequestDto requestDto){
        GetVendorResponseDto updatedVendor = vendorService.updateVendorProfile(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Vendor updated successfully",updatedVendor));
    }

    // 4. Admin: Approve Vendor Status
    @PatchMapping("/{id}/approve")
    public ResponseEntity<ApiResponseDto<GetVendorResponseDto>> approveVendor(@PathVariable Long id) {
        GetVendorResponseDto approvedVendor = vendorService.approveVendor(id);
        return ResponseEntity.ok(ApiResponseDto.success("Vendor approved successfully", approvedVendor));
    }

    // 5. Admin: Get Vendors by Status (e.g. ?status=PENDING)
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<GetVendorResponseDto>>> getVendorsByStatus(@RequestParam VendorStatus status) {
        List<GetVendorResponseDto> vendors = vendorService.getVendorsByStatus(status);
        return ResponseEntity.ok(ApiResponseDto.success("Vendors fetched successfully", vendors));
    }
}
