package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.VendorAdapter;
import com.example.FakeCommerce.dtos.GetVendorResponseDto;
import com.example.FakeCommerce.dtos.RegisterVendorRequestDto;
import com.example.FakeCommerce.dtos.UpdateVendorRequestDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.UserRepository;
import com.example.FakeCommerce.repositories.VendorRepository;
import com.example.FakeCommerce.schema.UserRole;
import com.example.FakeCommerce.schema.User;
import com.example.FakeCommerce.schema.Vendor;
import com.example.FakeCommerce.schema.VendorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VendorService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final VendorAdapter vendorAdapter;

    @Transactional
    public GetVendorResponseDto registerVendor(Long userId, RegisterVendorRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with this id"));

        if(vendorRepository.findByUserId(userId).isPresent()){
            throw new RuntimeException("User is already registered as a vendor!");
        }

        Vendor vendor = Vendor
                .builder()
                .storeName(requestDto.getStoreName())
                .description(requestDto.getStoreDescription())
                .user(user)
                .contactPhone(requestDto.getStorePhoneNumber())
                .build();

        vendorRepository.save(vendor);

        return vendorAdapter.mapToGetVendorResponseDto(vendor);
    }

    public GetVendorResponseDto getVendorByProfile(Long vendorId){
        Vendor vendor = vendorRepository.findByIdWithUser(vendorId)
                .orElseThrow(()-> new ResourceNotFoundException("Vendor not found with ID: " + vendorId));

        return vendorAdapter.mapToGetVendorResponseDto(vendor);
    }

    @Transactional
    public GetVendorResponseDto approveVendor(Long vendorId){
        Vendor vendor = vendorRepository.findByIdWithUser(vendorId)
                .orElseThrow(()-> new ResourceNotFoundException("Vendor not found with ID: " + vendorId));
        vendor.setStatus(VendorStatus.APPROVED);

        // Update the underlying user's role to VENDOR upon approval
        User user = vendor.getUser();
        user.setRole(UserRole.VENDOR);
        userRepository.save(user);

        vendorRepository.save(vendor);

        return vendorAdapter.mapToGetVendorResponseDto(vendor);
    }

    public List<GetVendorResponseDto> getVendorsByStatus(VendorStatus status){
        List<Vendor> vendors = vendorRepository.findByStatusWithUser(status);
        return vendorAdapter.mapToGetVendorResponseDtoList(vendors);
    }

    @Transactional
    public GetVendorResponseDto updateVendorProfile(Long vendorId, UpdateVendorRequestDto requestDto){
        Vendor vendor = vendorRepository.findByIdWithUser(vendorId)
                .orElseThrow(()-> new ResourceNotFoundException("Vendor not found with ID: " + vendorId));;

        if(requestDto.getStoreName() != null && !requestDto.getStoreName().isBlank()){
            vendor.setStoreName(requestDto.getStoreName());
        }

        if(requestDto.getStoreDescription()!=null && !requestDto.getStoreDescription().isBlank()){
            vendor.setDescription(requestDto.getStoreDescription());
        }

        if(requestDto.getStorePhoneNumber()!=null && !requestDto.getStorePhoneNumber().isBlank()){
            vendor.setContactPhone(requestDto.getStorePhoneNumber());
        }

        vendorRepository.save(vendor);

        return vendorAdapter.mapToGetVendorResponseDto(vendor);
    }
}
