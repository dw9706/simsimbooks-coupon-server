package org.simsimbooks.couponserver.category.dto;

import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private Long parent;
}
