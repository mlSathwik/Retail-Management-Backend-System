package com.retail.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entity representing a retail store location.
 */
@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Address line 1 is required")
    @Column(nullable = false)
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column
    private String addressLine3;
}
