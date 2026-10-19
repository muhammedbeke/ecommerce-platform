package com.emin.e_commerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buying")
@SQLDelete(sql = "UPDATE buying SET deleted_datetime = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_datetime IS NULL")
@Getter
@Setter
@NoArgsConstructor
public class Buying extends BaseEntity {


    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;

    @OneToMany(mappedBy = "buying", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuyingDetail> buyingDetails = new ArrayList<>();
}
