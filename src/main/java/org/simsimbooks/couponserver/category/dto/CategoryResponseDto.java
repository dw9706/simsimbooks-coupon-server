package org.simsimbooks.couponserver.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class CategoryResponseDto {
    private Long id;
    private String name;
    private Long parent;
    private List<Long> children;

}
