package com.retail.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

    private Long id;

    @NotBlank(message = "Store name is required")
    private String name;

    @NotBlank(message = "Address line 1 is required")
    private String addressLine1;

    private String addressLine2;

    private String addressLine3;
}
