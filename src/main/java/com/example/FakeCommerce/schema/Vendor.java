package com.example.FakeCommerce.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vendors")
@SQLDelete(sql="UPDATE vendors SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Vendor extends BaseEntity{
    @Column(nullable = false)
    private String storeName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String contactPhone;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status = VendorStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
