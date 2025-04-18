package org.simsimbooks.couponserver.category.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private Long parent;
    private List<Long> children;

}
